package com.tinder.slidingcard;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.tinder.slidingcard.SlidingCard.OnPageChangeListener;

/**
 *tinder container
 * 
 * @author stevenFang
 * @date 2015-5-06
 * @version 2.0.0
 */
public class DiscoverContainerView extends RelativeLayout implements
		OnPageChangeListener {

	private Activity activity;

	private ConcurrentLinkedQueue<Integer> dataList = new ConcurrentLinkedQueue<Integer>();

	private ContainerInterface containerInterface;

	public static int TYPE_RIGHT = 1; //right 操作

	public static int TYPE_LEFT = -1; //left 操作

	private int operatType = TYPE_RIGHT;

	public DiscoverContainerView(Context context) {
		super(context);
	}

	public DiscoverContainerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void addToView(View child) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		addView(child, 0, layoutParams);
	}

	@Override
	public void addView(View child) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		addView(child, 0, layoutParams);
	}

	public void initCardView(Activity activity) {
		this.activity = activity;
		if (dataList != null && dataList.size() > 0) {
			for (int i = 1; i < 5; i++) {
				Integer colorId = dataList.poll();
				if (colorId != null) {
					int margin = i;
					if (i == 4) {
						margin = 3;
					}
					SlidingCard mSlidingCard = new SlidingCard(this.activity);
					mSlidingCard.setContent(R.layout.activity_item);
					mSlidingCard.initView(colorId);
					View contentView = mSlidingCard.getContentView();
					LayoutParams params = new LayoutParams(
							contentView.getLayoutParams());
					params.topMargin = (margin - 1)
							* getResources().getDimensionPixelSize(
									R.dimen.card_item_margin);
					params.leftMargin = margin
							* getResources().getDimensionPixelSize(
									R.dimen.card_item_margin_left_right);
					params.rightMargin = margin
							* getResources().getDimensionPixelSize(
									R.dimen.card_item_margin_left_right);
					contentView.setLayoutParams(params);
					mSlidingCard.setListIndex(i);
					mSlidingCard
							.setSlidingMode(SlidingCard.SLIDINGMODE_LEFT_RIGHT);
					mSlidingCard.setCurrentItem(1, false);
					mSlidingCard.setOnPageChangeListener(this);
					addToView(mSlidingCard);
				}
			}
		}
	}

	private SlidingCard getCurrentView() {
		if (getChildCount() > 0) {
			return (SlidingCard) getChildAt(getChildCount() - 1);
		}
		return null;
	}

	@Override
	public void onPageScrolled(SlidingCard v, int position,
			float positionOffset, int positionOffsetPixels) {
		if (positionOffset > 0) {
			operatType = TYPE_LEFT;
		} else if (positionOffset < 0) {
			operatType = TYPE_RIGHT;
		}
		if (positionOffset == 0f) {
			positionOffset = 1f;
		}
		if (v != null) {
			v.doSomeThing(Math.abs(positionOffset), operatType);
		}
		if (Math.abs(positionOffsetPixels) != 0) {
			for (int i = getChildCount() - 2; i > 0; i--) {
				SlidingCard slidingCard = (SlidingCard) getChildAt(i);
				if (slidingCard != null) {
					View contentView = slidingCard.getContentView();
					LayoutParams params = new LayoutParams(
							contentView.getLayoutParams());
					float leftRightMargin = (4 - i - Math.abs(positionOffset))
							* getResources().getDimensionPixelSize(
									R.dimen.card_item_margin_left_right);
					params.topMargin = (int) ((3 - i - Math.abs(positionOffset)) * getResources()
							.getDimensionPixelSize(R.dimen.card_item_margin));
					params.leftMargin = (int) leftRightMargin;
					params.rightMargin = (int) leftRightMargin;
					contentView.setLayoutParams(params);
				}

			}
		}
	}

	@Override
	public void onPageSelectedAfterAnimation(SlidingCard v, int prevPosition,
			int curPosition) {
		if (activity != null) {
			removeViewAt(getChildCount() - 1);
			if (getChildCount() == 0 && dataList.size() > 0) {
				initCardView(activity);
				return;
			}
			Integer colorId = dataList.poll();
			if (colorId != null) {
				SlidingCard mSlidingCard = new SlidingCard(activity);
				mSlidingCard.setContent(R.layout.activity_item);
				mSlidingCard.initView(colorId);
				View contentView = mSlidingCard.getContentView();
				LayoutParams params = new LayoutParams(
						contentView.getLayoutParams());
				params.topMargin = 2 * getResources().getDimensionPixelSize(
						R.dimen.card_item_margin);
				params.leftMargin = 3 * getResources().getDimensionPixelSize(
						R.dimen.card_item_margin_left_right);
				params.rightMargin = 3 * getResources().getDimensionPixelSize(
						R.dimen.card_item_margin_left_right);
				contentView.setLayoutParams(params);
				mSlidingCard.setSlidingMode(SlidingCard.SLIDINGMODE_LEFT_RIGHT);
				mSlidingCard.setCurrentItem(1, false);
				mSlidingCard.setOnPageChangeListener(this);
				addView(mSlidingCard);
			}
			if (containerInterface != null) {
				if (dataList.size() < 3) {
					containerInterface.loadMore();
				}
				if (getChildCount() == 0) {
					containerInterface.loadEmpty();
				}
				containerInterface.onOperat(v.getData(), operatType);
			}
			Log.e("test", "onPageSelectedAfterAnimation:" + curPosition + ","
					+ getChildCount());
		}
	}

	@Override
	public void onPageSelected(SlidingCard v, int prevPosition, int curPosition) {
		Log.e("test", "onPageSelected:" + curPosition);
	}

	@Override
	public void onPageScrollStateChanged(SlidingCard v, int state) {
		Log.e("test", "state change:" + state);
	}

	public ConcurrentLinkedQueue<Integer> getDataList() {
		return dataList;
	}

	public void setDataList(ConcurrentLinkedQueue<Integer> dataList) {
		this.dataList = dataList;
	}

	public interface ContainerInterface {
		public void loadMore();

		public void onOperat(int data, int type);

		public void loadEmpty();

	}

	public ContainerInterface getContainerInterface() {
		return containerInterface;
	}

	public void setContainerInterface(ContainerInterface containerInterface) {
		this.containerInterface = containerInterface;
	}

}
