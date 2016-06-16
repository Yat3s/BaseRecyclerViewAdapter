package com.yat3s.library.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

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
    private static final int VIEW_TYPE_HEADER = 0x0010;

    /**
     * Base config
     */
    private List<T> mData;
    private View mHeaderView;
    protected Context mContext;
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

    /**
     * Animation
     */
    private AnimationType mAnimationType;
    private int mAnimationDuration = 300;
    private boolean showItemAnimationEveryTime = false;
    private Interpolator mItemAnimationInterpolator;
    private CustomAnimator mCustomAnimator;
    private int mLastItemPosition = -1;

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
        if (VIEW_TYPE_HEADER == viewType) {
            baseViewHolder = new BaseViewHolder(mHeaderView, mContext);
            bindClickListenerToHeaderView(baseViewHolder);
        } else {
            baseViewHolder = new BaseViewHolder(mInflater.inflate(layoutIdMap.get(viewType),
                    parent, false), mContext);
            bindClickListenerToItemView(baseViewHolder);
        }
        return baseViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderViewCount()) {
            return VIEW_TYPE_HEADER;
        } else {
            int currentPosition = position - getHeaderViewCount();
            int currentLayoutId = getItemViewLayoutId(currentPosition, mData.get(currentPosition));
            if (null == viewTypeMap.get(currentLayoutId)) {
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
            case VIEW_TYPE_HEADER:
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
     * Base api
     */
    protected abstract void bindDataToItemView(BaseViewHolder holder, T data, int position);

    protected abstract int getItemViewLayoutId(int position, T data);

    public void addFirstDataSet(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addMoreDataSet(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public List getDataSource() {
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
        return mData.size() + getHeaderViewCount();
    }


    /**
     * Header api
     */
    public void addHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyDataSetChanged();
    }

    public void addHeaderViewResId(int layoutResId) {
        addHeaderView(mInflater.inflate(layoutResId, null));
    }

    private int getHeaderViewCount() {
        return null == mHeaderView ? 0 : 1;
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
     * Some interface
     */
    public interface OnHeaderClickListener {
        void onClick(View view);
    }

    public interface OnItemClickListener<T> {
        void onClick(View view, T data, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onLongClick(View view, T data, int position);
    }

    public interface CustomAnimator {
        Animator getAnimator(View itemView);
    }
}
