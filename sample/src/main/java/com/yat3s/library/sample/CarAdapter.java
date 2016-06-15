package com.yat3s.library.sample;

import android.content.Context;

import com.yat3s.library.adapter.BaseAdapter;
import com.yat3s.library.adapter.BaseViewHolder;

/**
 * Created by Yat3s on 6/14/16.
 * Email: yat3s@opentown.cn
 * Copyright (c) 2015 opentown. All rights reserved.
 */

public class CarAdapter extends BaseAdapter<CarModel> {
    public CarAdapter(Context context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder holder, CarModel data, int position) {
        holder.setText(R.id.name_tv, data.name)
                .setText(R.id.price_tv, String.valueOf(data.price));
    }

    @Override
    protected int getItemViewLayoutId(int position) {
        if (position % 3 == 0) {
            return R.layout.item_car;
        }
        return R.layout.item_house;
    }
}
