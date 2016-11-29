package com.zylaoshi.project.refreshlayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mListView;
    private List<String> mList;
    private ArrayAdapter<String> adapter;
    private final int SUCCESS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.lv_view);
        mList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(adapter);

        //找到下拉控件
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshlayout_view);
        //设置下拉刷新过程中进度动画四种颜色变化
        swipeRefreshLayout.setColorSchemeResources(R.color.color_1,
                R.color.color_2,
                R.color.color_3,
                R.color.color_4);
        //进度圈的背景颜色
        swipeRefreshLayout.setProgressBackgroundColor(R.color.swipe_background_color);
        //progress的位置从100位置移动到200 单位是scale
        swipeRefreshLayout.setProgressViewOffset(true, 100, 200);
        //设置手指在屏幕下拉多少距离会触发下拉刷新
        swipeRefreshLayout.setDistanceToTriggerSync(50);
        //实现下拉滚动效果，150是下拉的位置
        swipeRefreshLayout.setProgressViewEndTarget(true, 150);
        //设置圆圈的大小，只有两个值：DEFAULT、LARGE
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //手势滑动监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //new一个县城实现刷新，下面内容改为实现的功能。例如网页获取数据的重新获取一遍网页你内容
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mList.clear();           //清空数据
                        //刷新添加的内容
                        for(int i=1;i<20;i++){
                            mList.add("刷新结果"+i);
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //刷新成功
                        mHandler.sendEmptyMessage(SUCCESS);
                    }
                }).start();
            }
        });
    }
    //new一个处理程序，接收传过来的数据并做处理
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //返回成功
                case 1:
                    swipeRefreshLayout.setRefreshing(false);        //关闭刷新
                    //将刷新得到的数据添加到适配器里
                    adapter.notifyDataSetChanged();

                    //swipeRefreshLayout.setEnabled(false);
                    break;
                default:
                    break;
            }
        }

    };
}
