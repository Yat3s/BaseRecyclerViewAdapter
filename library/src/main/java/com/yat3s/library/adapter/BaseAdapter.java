package com.yat3s.library.adapter;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yat3s on 6/13/16.
 * Email: hawkoyates@gmail.com
 * GitHub: https://github.com/yat3s
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "BaseAdapter";

    private static class VIEW_TYPE {
        static final int HEADER = 0x0010;
        static final int EMPTY_VIEW = 0x0011;
        static final int LOADING_VIEW = 0x0012;
    }

    /**
     * Base config
     */
    private List<T> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    /**
     * Listener
     */
    private OnHeaderClickListener mOnHeaderClickListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * View type
     */
    private Map<Integer, Integer> layoutIdMap, viewTypeMap;
    private int mCurrentViewTypeValue = 0x0107;
    private View mEmptyView, mLoadingView;
    private boolean hasAddData, showLoadingViewIgnoreHeader = true;

    /**
     * Animation
     */
    private AnimationType mAnimationType;
    private int mAnimationDuration = 300;
    private boolean showItemAnimationEveryTime = false;
    private Interpolator mItemAnimationInterpolator;
    private CustomAnimator mCustomAnimator;
    private int mLastItemPosition = -1;

    /**
     * Parallax
     */
    private CustomRelativeWrapper mHeaderView;
    private float mScrollMultiplier = 0.5f;
    private OnParallaxScrollListener mParallaxScrollListener;
    private RecyclerView mRecyclerView;
    private boolean mShouldClipView = true;

    public BaseAdapter(Context context) {
        this(context, null);
    }

    public BaseAdapter(Context context, List<T> data) {
        mData = null == data ? new ArrayList<T>() : data;
        layoutIdMap = new HashMap<>();
        viewTypeMap = new HashMap<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder;
        switch (viewType) {
            case VIEW_TYPE.HEADER:
                baseViewHolder = new BaseViewHolder(mHeaderView, mContext);
                bindClickListenerToHeaderView(baseViewHolder);
                break;
            case VIEW_TYPE.EMPTY_VIEW:
                baseViewHolder = new BaseViewHolder(mEmptyView, mContext);
                break;
            case VIEW_TYPE.LOADING_VIEW:
                baseViewHolder = new BaseViewHolder(mLoadingView, mContext);
                break;
            default:
                baseViewHolder = new BaseViewHolder(mInflater.inflate(layoutIdMap.get(viewType),
                        parent, false), mContext);
                bindClickListenerToItemView(baseViewHolder);
                break;
        }
        return baseViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (null != mLoadingView) {
            notifyItemChanged(showLoadingViewIgnoreHeader ? 0 : getHeaderViewCount());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderViewCount()) {
            if (!hasAddData && showLoadingViewIgnoreHeader && null != mLoadingView) {
                return VIEW_TYPE.LOADING_VIEW;
            }
            return VIEW_TYPE.HEADER;
        } else if (!hasAddData && null != mLoadingView && !showLoadingViewIgnoreHeader) {
            return VIEW_TYPE.LOADING_VIEW;
        } else if (mData.size() == 0 && null != mEmptyView) {
            return VIEW_TYPE.EMPTY_VIEW;
        } else {
            int currentPosition = position - getHeaderViewCount();
            int currentLayoutId = getItemViewLayoutId(currentPosition, mData.get(currentPosition));
            if (!viewTypeMap.containsKey(currentLayoutId)) {
                mCurrentViewTypeValue++;
                viewTypeMap.put(currentLayoutId, mCurrentViewTypeValue);
                layoutIdMap.put(viewTypeMap.get(currentLayoutId), currentLayoutId);
            }
            return viewTypeMap.get(currentLayoutId);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE.HEADER:
                // Do nothing
                break;
            case VIEW_TYPE.EMPTY_VIEW:
                // Do nothing
                break;
            case VIEW_TYPE.LOADING_VIEW:
                // Do nothing
                break;
            default:
                bindDataToItemView(holder, getItem(position - getHeaderViewCount()), position -
                        getHeaderViewCount());
                bindItemAnimationToItemView(holder);
                break;
        }
    }

    protected final void bindItemAnimationToItemView(final BaseViewHolder holder) {
        int currentPosition = holder.getAdapterPosition();
        if (null != mCustomAnimator) {
            mCustomAnimator.getAnimator(holder.itemView).setDuration(mAnimationDuration).start();
        } else if (null != mAnimationType) {
            if (showItemAnimationEveryTime || currentPosition > mLastItemPosition) {
                new AnimationUtil()
                        .setAnimationType(mAnimationType)
                        .setTargetView(holder.itemView)
                        .setDuration(mAnimationDuration)
                        .setInterpolator(mItemAnimationInterpolator)
                        .start();
                mLastItemPosition = currentPosition;
            }
        }
    }

    protected final void bindClickListenerToItemView(final BaseViewHolder holder) {
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = holder.getAdapterPosition() - getHeaderViewCount();
                    mOnItemClickListener.onClick(view, mData.get(position), position);
                }
            });
        }

        if (null != mOnItemLongClickListener) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int position = holder.getAdapterPosition() - getHeaderViewCount();
                    mOnItemLongClickListener.onLongClick(v, mData.get(position), position);
                    return true;
                }
            });
        }
    }

    protected final void bindClickListenerToHeaderView(BaseViewHolder holder) {
        if (null != mOnHeaderClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnHeaderClickListener.onClick(view);
                }
            });
        }
    }

    /**
     * Translates the adapter in Y
     *
     * @param of offset in px
     */
    private void translateHeader(float of) {
        float ofCalculated = of * mScrollMultiplier;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && of < mHeaderView.getHeight
                ()) {
            mHeaderView.setTranslationY(ofCalculated);
        } else if (of < mHeaderView.getHeight()) {
            TranslateAnimation anim = new TranslateAnimation(0, 0, ofCalculated, ofCalculated);
            anim.setFillAfter(true);
            anim.setDuration(0);
            mHeaderView.startAnimation(anim);
        }
        mHeaderView.setClipY(Math.round(ofCalculated));
        if (mParallaxScrollListener != null) {
            final RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition
                    (0);
            float left;
            if (holder != null) {
                left = Math.min(1, ((ofCalculated) / (mHeaderView.getHeight() *
                        mScrollMultiplier)));
            } else {
                left = 1;
            }
            mParallaxScrollListener.onParallaxScroll(left, of, mHeaderView);
        }
    }


    /**
     * Base api
     */
    protected abstract void bindDataToItemView(BaseViewHolder holder, T item, int position);

    protected abstract int getItemViewLayoutId(int position, T item);

    public void addFirstDataSet(List<T> data) {
        mData = data;
        notifyDataSetChanged();
        hasAddData = true;
    }

    public void addMoreDataSet(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public List<T> getDataSource() {
        return mData;
    }

    protected Context getContext() {
        return mContext;
    }

    protected T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        if (mData.size() == 0 && mEmptyView != null) {
            return getHeaderViewCount() + 1;
        } else {
            return mData.size() + getHeaderViewCount();
        }
    }


    /**
     * Listener api
     */

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        mOnHeaderClickListener = onHeaderClickListener;
    }


    /**
     * Animation api
     */
    public void setItemAnimation(AnimationType animationType) {
        mAnimationType = animationType;
    }

    public void setItemAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public void setItemAnimationInterpolator(Interpolator animationInterpolator) {
        mItemAnimationInterpolator = animationInterpolator;
    }

    public void setShowItemAnimationEveryTime(boolean showItemAnimationEveryTime) {
        this.showItemAnimationEveryTime = showItemAnimationEveryTime;
    }

    public void setCustomItemAnimator(CustomAnimator customAnimator) {
        mCustomAnimator = customAnimator;
    }


    /**
     * Header api
     */
    public void addHeaderView(View headerView) {
        mHeaderView = new CustomRelativeWrapper(headerView.getContext());
        mHeaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderView.addView(headerView);
        notifyItemChanged(0);
    }

    public void addHeaderViewResId(int layoutResId) {
        addHeaderView(mInflater.inflate(layoutResId, null));
    }

    public void addParallaxHeaderView(View headerView, final RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mHeaderView = new CustomRelativeWrapper(headerView.getContext(), mShouldClipView);
        mHeaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderView.addView(headerView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHeaderView != null) {
                    translateHeader(mRecyclerView.getLayoutManager().getChildAt(0) == mHeaderView ?
                            mRecyclerView.computeVerticalScrollOffset() : mHeaderView.getHeight());
                }
            }
        });
    }

    public void addParallaxHeaderViewLayoutResId(int layoutResId, final RecyclerView recyclerView) {
        addParallaxHeaderView(mInflater.inflate(layoutResId, null), recyclerView);
    }

    public boolean isShouldClipView() {
        return mShouldClipView;
    }

    public void setParallaxScrollListener(OnParallaxScrollListener parallaxScroll) {
        mParallaxScrollListener = parallaxScroll;
        mParallaxScrollListener.onParallaxScroll(0, 0, mHeaderView);
    }

    public void setScrollMultiplier(float mul) {
        if (mul < 0 || mul > 1f) {
            throw new IllegalArgumentException("Scroll multiplier must between 0 to 1f");
        }
        this.mScrollMultiplier = mul;
    }

    private int getHeaderViewCount() {
        return null == mHeaderView ? 0 : 1;
    }


    /**
     * Empty view /retry view / loading view api
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    /**
     * @param emptyViewResId
     * @param viewParent     because your need set your empty view match parent
     */
    public void setEmptyViewResId(int emptyViewResId, ViewGroup viewParent) {
        setEmptyView(mInflater.inflate(emptyViewResId, viewParent, false));
    }

    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
    }

    public void setShowLoadingViewIgnoreHeader(boolean showLoadingViewIgnoreHeader) {
        this.showLoadingViewIgnoreHeader = showLoadingViewIgnoreHeader;
    }


    /**
     * Some interface
     */
    public interface OnHeaderClickListener {
        void onClick(View view);
    }

    public interface OnItemClickListener<T> {
        void onClick(View view, T item, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onLongClick(View view, T item, int position);
    }

    public interface OnParallaxScrollListener {
        void onParallaxScroll(float percentage, float offset, View parallax);
    }

    public interface CustomAnimator {
        Animator getAnimator(View itemView);
    }

    /**
     * This is parallax header view wrapper class ,it aim to clip layout height on Y.
     */
    static class CustomRelativeWrapper extends RelativeLayout {

        private int mOffset;
        private boolean mShouldClip;

        public CustomRelativeWrapper(Context context) {
            super(context);
        }

        public CustomRelativeWrapper(Context context, boolean shouldClick) {
            super(context);
            mShouldClip = shouldClick;
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            if (mShouldClip) {
                canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
            }
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset) {
            mOffset = offset;
            invalidate();
        }
    }
}
