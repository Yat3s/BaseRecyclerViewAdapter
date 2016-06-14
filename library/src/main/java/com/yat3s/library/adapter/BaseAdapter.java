package com.yat3s.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yat3s on 6/13/16.
 * Email: yat3s@opentown.cn
 * Copyright (c) 2015 opentown. All rights reserved.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0x0010;
    private static final int VIEW_TYPE_ITEM = 0x0011;

    private List<T> mData;
    private View mHeaderView;
    private Context mContext;
    protected LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseAdapter(Context context) {
        this(context, null);
    }

    public BaseAdapter(Context context, List<T> data) {
        mData = null == data ? new ArrayList<T>() : data;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                baseViewHolder = new BaseViewHolder(mHeaderView);
                break;
            case VIEW_TYPE_ITEM:
                baseViewHolder = new BaseViewHolder(mInflater.inflate(getItemViewResId(viewType),
                        parent, false));
                break;
        }

        return baseViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderViewCount()) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

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

    public void addHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyDataSetChanged();
    }

    public void addHeaderViewResId(int layoutResId) {
        mHeaderView = mInflater.inflate(layoutResId, null);
        notifyDataSetChanged();
    }

    private int getHeaderViewCount() {
        return null == mHeaderView ? 0 : 1;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    protected abstract void bindDataToItemView(BaseViewHolder holder, T data, int position);

    protected abstract int getItemViewResId(int viewType);

    protected T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:

                break;
            case VIEW_TYPE_ITEM:
                bindDataToItemView(holder, getItem(position - getHeaderViewCount()), position -
                        getHeaderViewCount());
                bindClickListenerToItemView(holder, getItem(position - getHeaderViewCount()),
                        position - getHeaderViewCount());
                break;
        }
    }

    protected final void bindClickListenerToItemView(BaseViewHolder holder, final T
            data, final int position) {
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, data, position);
                }
            });
        }

        if (null != mOnItemLongClickListener) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onLongClick(v, data, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + getHeaderViewCount();
    }

    public interface OnItemClickListener<T> {
        void onClick(View view, T data, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onLongClick(View view, T data, int position);
    }
}
