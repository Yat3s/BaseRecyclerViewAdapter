package com.yat3s.library.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mCarAdapter);
    }

    private void loadData() {
        List<CarModel> mockData = new ArrayList<>();
        CarModel car1 = new CarModel("Panamera", 1092321);
        CarModel car2 = new CarModel("Porsche", 4231942);

        mockData.add(car1);
        mockData.add(car2);
        mCarAdapter.addFirstDataSet(mockData);
    }

}
