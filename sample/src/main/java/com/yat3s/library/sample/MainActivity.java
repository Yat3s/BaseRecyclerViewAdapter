package com.yat3s.library.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yat3s.library.adapter.AnimationType;
import com.yat3s.library.adapter.BaseAdapter;
import com.yat3s.library.adapter.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yat3s on 6/14/16.
 * Email: hawkoyates@gmail.com
 * GitHub: https://github.com/yat3s
 */

public class MainActivity extends AppCompatActivity {

    private MusicAdapter mMusicAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        initAdapter();
        loadData();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    private void initAdapter() {
        mMusicAdapter = new MusicAdapter(this);
        mMusicAdapter.addParallaxHeaderViewLayoutResId(R.layout.layout_header, mRecyclerView);
        mMusicAdapter.setItemAnimation(AnimationType.SLIDE_FROM_LEFT);
        mMusicAdapter.setShowItemAnimationEveryTime(false);

        /**
         * Config some click listener
         */
        mMusicAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<MusicModel>() {
            @Override
            public void onClick(View view, MusicModel car, int position) {
                Toast.makeText(MainActivity.this, "Click " + position + ":" + car.name, Toast
                        .LENGTH_SHORT).show();
            }
        });
        mMusicAdapter.setOnHeaderClickListener(new BaseAdapter.OnHeaderClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Header Click", Toast.LENGTH_SHORT).show();
            }
        });


        /**
         * Config loading view
         */
        View loadingView = getLayoutInflater().inflate(R.layout.layout_loading_view, (ViewGroup)
                mRecyclerView.getParent(), false);
        mMusicAdapter.setLoadingView(loadingView);


        mRecyclerView.setAdapter(mMusicAdapter);
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMusicAdapter.addFirstDataSet(generateMockDataSet());
            }
        }, 1000);
    }

    private List<MusicModel> generateMockDataSet() {
        List<MusicModel> mockData = new ArrayList<>();
        for (int idx = 0; idx < 100; idx++) {
            MusicModel car = new MusicModel("Music " + idx, (int) (Math.random() * 100));
            mockData.add(car);
        }
        return mockData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_alpha:
                mMusicAdapter.setItemAnimation(AnimationType.ALPHA);
                break;
            case R.id.action_scale:
                mMusicAdapter.setItemAnimation(AnimationType.SCALE);
                break;
            case R.id.action_slide_from_left:
                mMusicAdapter.setItemAnimation(AnimationType.SLIDE_FROM_LEFT);
                break;
            case R.id.action_slide_from_right:
                mMusicAdapter.setItemAnimation(AnimationType.SLIDE_FROM_RIGHT);
                break;
            case R.id.action_slide_from_bottom:
                mMusicAdapter.setItemAnimation(AnimationType.SLIDE_FROM_BOTTOM);
                break;
            case R.id.action_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Yat3s/BaseRecyclerViewAdapter"));
                startActivity(browserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
