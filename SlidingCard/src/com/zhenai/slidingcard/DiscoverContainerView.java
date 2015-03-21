package com.zhenai.slidingcard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class DiscoverContainerView extends RelativeLayout {

	public DiscoverContainerView(Context context) {
		super(context);
	}

	public DiscoverContainerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private int top = 300;

	public void addToView(View child) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = top;
		addView(child, 0, layoutParams);
		top += 30;
	}

	public SlidingCard getCurrentView() {
		if (getChildCount() > 0) {
			return (SlidingCard) getChildAt(getChildCount() - 1);
		}
		return null;
	}

	public SlidingCard getPreView() {
		if (getChildCount() - 1 > 0) {
			return (SlidingCard) getChildAt(getChildCount() - 2);
		}
		return null;
	}

}
