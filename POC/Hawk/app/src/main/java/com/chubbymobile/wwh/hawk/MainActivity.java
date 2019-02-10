package com.chubbymobile.wwh.hawk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.chubbymobile.wwh.hawk.Adapters.ModelAdapter;
import com.chubbymobile.wwh.hawk.Bean.Vehicle;
import com.chubbymobile.wwh.hawk.Services.BroadcastService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements PullToRefreshListener, ModelAdapter.OnItemClickListener {

    private PullToRefreshRecyclerView recyclerView;
    private ModelAdapter adapter;
    int counter = 0;
    private List<Vehicle> vData;

    private Intent intent;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, BroadcastService.class);

        initVehicles();

        recyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recyclerView);

        View headView = View.inflate(this, R.layout.layout_head_view, null);
        recyclerView.addHeaderView(headView);

        View footerView = View.inflate(this, R.layout.layout_foot_view, null);
        recyclerView.addFooterView(footerView);

        View emptyView = View.inflate(this, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setEmptyView(emptyView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ModelAdapter(this, vData);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);

        //设置是否开启上拉加载
        recyclerView.setLoadingMoreEnabled(true);
        //设置是否开启下拉刷新
        recyclerView.setPullRefreshEnabled(true);
        //设置是否显示上次刷新的时间
        recyclerView.displayLastRefreshTime(true);
        //设置刷新回调
        recyclerView.setPullToRefreshListener(this);
        //主动触发下拉刷新操作
        recyclerView.onRefresh();

        recyclerView.setRefreshingResource(R.drawable.volvologo);
        recyclerView.setLoadMoreResource(R.drawable.volvologo);

//        recyclerView.addOnItemTouchListener(
//        new RecyclerItemClickListener(getApplicationContext(),
//                recyclerView,
//                new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                        Log.e(TAG, "sss"+position);
//                    }
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//                    }
//                }));
    }

    private void initVehicles() {
        vData = new ArrayList<Vehicle>();
        vData.add(new Vehicle(R.drawable.fe, "FE"));
        vData.add(new Vehicle(R.drawable.fh, "FH"));
        vData.add(new Vehicle(R.drawable.fm, "FM"));
        vData.add(new Vehicle(R.drawable.fmx, "FMX"));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(intent);
    }

    private void updateUI(Intent intent) {
        String counter = intent.getStringExtra("counter");
        String time = intent.getStringExtra("time");
        Log.d(TAG, counter);
        Log.d(TAG, time);

        TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
        if(txtDateTime != null && txtCounter!=null) {
            txtDateTime.setText(time);
            txtCounter.setText(counter);
        }
    }

    @Override
    public void onRefresh() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshComplete();
                //模拟没有数据的情况
                //data.clear();
                adapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                counter++;
                if(counter>=4){
                    recyclerView.setLoadMoreFail();
                    return;
                }
                recyclerView.setLoadMoreComplete();
                vData.add(new Vehicle(R.drawable.fe, "FE"+counter));
                vData.add(new Vehicle(R.drawable.fh, "FH"+counter));
                vData.add(new Vehicle(R.drawable.fm, "FM"+counter));
                vData.add(new Vehicle(R.drawable.fmx, "FMX"+counter));
                adapter.notifyDataSetChanged();
            }
        }, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setRefreshComplete();
        recyclerView.setLoadMoreComplete();
        recyclerView.loadMoreEnd();
    }

    @Override
    public void onItemClick(String str, int position) {
        Log.d(TAG, "--->" + str + position);
    }
}
