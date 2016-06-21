package com.yat3s.library.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private CarAdapter mCarAdapter;
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
        mCarAdapter = new CarAdapter(this);
        mCarAdapter.addParallaxHeaderViewLayoutResId(R.layout.layout_header, mRecyclerView);
        mCarAdapter.setItemAnimation(AnimationType.SLIDE_FROM_LEFT);
        mCarAdapter.setShowItemAnimationEveryTime(false);

        /**
         * Config some click listener
         */
        mCarAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<CarModel>() {
            @Override
            public void onClick(View view, CarModel car, int position) {
                Toast.makeText(MainActivity.this, "Click " + position + ":" + car.name, Toast
                        .LENGTH_SHORT).show();
            }
        });
        mCarAdapter.setOnHeaderClickListener(new BaseAdapter.OnHeaderClickListener() {
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
        mCarAdapter.setLoadingView(loadingView);


        mRecyclerView.setAdapter(mCarAdapter);
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCarAdapter.addFirstDataSet(generateMockDataSet());
            }
        }, 2000);
    }

    private List<CarModel> generateMockDataSet() {
        List<CarModel> mockData = new ArrayList<>();
        for (int idx = 0; idx < 100; idx++) {
            CarModel car = new CarModel("Car " + idx, (int) (Math.random() * 10000));
            mockData.add(car);
        }
        return mockData;
    }
}
