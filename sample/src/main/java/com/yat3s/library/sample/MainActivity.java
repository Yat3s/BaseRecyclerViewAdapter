package com.yat3s.library.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yat3s.library.adapter.AnimationType;
import com.yat3s.library.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CarAdapter mCarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        loadData();
    }

    private void initView() {
        mCarAdapter = new CarAdapter(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mCarAdapter);
        mCarAdapter.addHeaderViewResId(R.layout.layout_header);
        mCarAdapter.setItemAnimation(AnimationType.SLIDE_FROM_RIGHT);
        mCarAdapter.setShowItemAnimationEveryTime(true);
        mCarAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<CarModel>() {
            @Override
            public void onClick(View view, CarModel car, int position) {
                Toast.makeText(MainActivity.this, position + ":" + car.name, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        mCarAdapter.setOnHeaderClickListener(new BaseAdapter.OnHeaderClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Header Click", Toast.LENGTH_SHORT)
                        .show();
                mCarAdapter.addMoreDataSet(generateMockDataSet());
            }
        });
    }

    private void loadData() {
        mCarAdapter.addFirstDataSet(generateMockDataSet());
    }

    private List<CarModel> generateMockDataSet() {
        List<CarModel> mockData = new ArrayList<>();
        for (int idx = 0; idx < 50; idx++) {
            CarModel car = new CarModel("Car " + idx, (int) (Math.random() * 10000));
            mockData.add(car);
        }
        return mockData;
    }
}
