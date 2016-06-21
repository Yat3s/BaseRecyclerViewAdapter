package com.yat3s.library.sample;

import android.content.Context;

import com.yat3s.library.adapter.BaseAdapter;
import com.yat3s.library.adapter.BaseViewHolder;

/**
 * Created by Yat3s on 6/14/16.
 * Email: hawkoyates@gmail.com
 * GitHub: https://github.com/yat3s
 */

public class MusicAdapter extends BaseAdapter<MusicModel> {
    public MusicAdapter(Context context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder holder, MusicModel data, int position) {
        holder.setText(R.id.name_tv, data.name)
                .setText(R.id.price_tv, "$ " + data.price);
    }

    @Override
    protected int getItemViewLayoutId(int position, MusicModel musicModel) {
        if (isLargeMusicItem(position, musicModel)) {
            return R.layout.item_music_large;
        }
        return R.layout.item_music_small;
    }

    private boolean isLargeMusicItem(int position, MusicModel musicModel) {
        return 0 == position % 3 && musicModel.price < 80;
    }

}
