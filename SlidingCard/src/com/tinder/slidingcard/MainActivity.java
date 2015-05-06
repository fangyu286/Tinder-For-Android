package com.tinder.slidingcard;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tinder.slidingcard.DiscoverContainerView.ContainerInterface;

public class MainActivity extends Activity {

	/**slidingCard的容器*/
	private DiscoverContainerView contentView;
	/**数据队列*/
	private ConcurrentLinkedQueue<Integer> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contentView = (DiscoverContainerView) findViewById(R.id.contentview);
		dataList = new ConcurrentLinkedQueue<Integer>(); // 初始化数据
		dataList.add(R.color.red);
		dataList.add(R.color.green);
		dataList.add(R.color.blue);
		dataList.add(R.color.white);
		dataList.add(R.color.red);
		dataList.add(R.color.green);
		dataList.add(R.color.blue);
		dataList.add(R.color.white);
		contentView.setDataList(dataList);
		// 设置监听，用于回调给界面用户向左右滑动后的操作事件，加载更多数据事件，以及没有数据后提示用户
		contentView.setContainerInterface(new ContainerInterface() {

			@Override
			public void onOperat(int data, int type) {
				String tips = "right";
				if (type == DiscoverContainerView.TYPE_LEFT) {
					tips = "left";
				}
				Log.i("test", tips + ":" + data);
			}

			@Override
			public void loadMore() {
				//加载更多数据
				dataList.add(R.color.red);
				dataList.add(R.color.green);
				dataList.add(R.color.blue);
				dataList.add(R.color.white);
				dataList.add(R.color.red);
				dataList.add(R.color.green);
				dataList.add(R.color.blue);
				dataList.add(R.color.white);

			}

			@Override
			public void loadEmpty() {
				Toast.makeText(getApplicationContext(), "wait data",
						Toast.LENGTH_LONG).show();
			}
		});
		contentView.initCardView(MainActivity.this);
	}

}
