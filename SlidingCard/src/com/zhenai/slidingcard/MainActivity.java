package com.zhenai.slidingcard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.zhenai.slidingcard.SlidingCard.OnPageChangeListener;

public class MainActivity extends Activity implements OnPageChangeListener {

	private DiscoverContainerView contentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contentView = (DiscoverContainerView) findViewById(R.id.contentview);
		for (int i = 1; i < 4; i++) {
			SlidingCard mSlidingCard = new SlidingCard(MainActivity.this);
			mSlidingCard.setContent(R.layout.activity_item);
			mSlidingCard.setListIndex(i);
			mSlidingCard.setSlidingMode(SlidingCard.SLIDINGMODE_LEFT_RIGHT);
			mSlidingCard.setCurrentItem(1, false);
			mSlidingCard.setOnPageChangeListener(this);
			contentView.addToView(mSlidingCard);
		}

	}

	@Override
	public void onPageScrolled(com.zhenai.slidingcard.SlidingCard v,
			int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(SlidingCard v, int prevPosition, int curPosition) {
		Log.e("test", "onPageSelected:" + curPosition);
	}

	@Override
	public void onPageSelectedAfterAnimation(SlidingCard v, int prevPosition,
			int curPosition) {
		contentView.removeViewAt(contentView.getChildCount() - 1);
		SlidingCard mSlidingCard = new SlidingCard(MainActivity.this);
		mSlidingCard.setContent(R.layout.activity_item);
		mSlidingCard.setSlidingMode(SlidingCard.SLIDINGMODE_LEFT_RIGHT);
		mSlidingCard.setCurrentItem(1, false);
		mSlidingCard.setOnPageChangeListener(this);
		contentView.addToView(mSlidingCard);
		SlidingCard card = contentView.getCurrentView();
		if (card != null) {
			// 初始化
			Animation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f,
					1.0f);
			scaleAnimation.setDuration(300);
			card.startAnimation(scaleAnimation);
			// int[] location = new int[2];
			// // card.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
			// card.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
			// card.scrollToPx(location[0] / 2, location[1] / 2 - 30);
		}
		Log.e("test", "onPageSelectedAfterAnimation:" + curPosition + ","
				+ contentView.getChildCount());
	}

	@Override
	public void onPageScrollStateChanged(SlidingCard v, int state) {
		Log.e("test", "state change:" + state);
	}

}
