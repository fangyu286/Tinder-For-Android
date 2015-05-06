package com.tinder.slidingcard;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SlidingView extends ImageView {

	private OnViewChangeListener mOnPageChangeListener;

	public void setOnPageChangeListener(OnViewChangeListener listener) {
		mOnPageChangeListener = listener;
	}

	public SlidingView(Context context) {
		super(context);
	}

	public SlidingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlidingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private float mLastMotionX;
	private float mLastMotionY;

	private int mActivePointerId;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action & MotionEventCompat.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mActivePointerId = MotionEventCompat.getPointerId(event, 0);
			mLastMotionX = event.getX();
			mLastMotionY = event.getY();
		case MotionEvent.ACTION_MOVE:
			final int activePointerIndex = findPointerIndex(event,
					mActivePointerId);
			final float x = MotionEventCompat.getX(event, activePointerIndex);

			float deltaX = mLastMotionX - x;
			float oldScrollX = getScrollX();
			float scrollX = oldScrollX + deltaX;
			mLastMotionX += scrollX - (int) scrollX;
			scrollTo((int) scrollX, getScrollY());
			pageScrolled((int) scrollX);
			break;
		case MotionEvent.ACTION_UP:

			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEventCompat.ACTION_POINTER_DOWN:
			break;
		case MotionEventCompat.ACTION_POINTER_UP:
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		super.onLayout(changed, 0, 0, width, height);
	}

	private int findPointerIndex(MotionEvent event, int pointerId) {
		int index = MotionEventCompat.findPointerIndex(event, pointerId);
		if (index == -1) {
			index = 0;
		}
		return index;
	}

	private void pageScrolled(int xpos) {
		final int widthWithMargin = getWidth();
		final int position = xpos / widthWithMargin;
		final int offsetPixels = xpos % widthWithMargin;
		final float offset = (float) offsetPixels / widthWithMargin;

		if (mOnPageChangeListener != null) {
			mOnPageChangeListener.onPageScrolled(this, position, offset,
					offsetPixels);
		}
	}

	/**
	 * Callback interface for responding to changing state of the selected page.
	 */
	public interface OnViewChangeListener {

		/**
		 * This method will be invoked when the current page is scrolled, either
		 * as part of a programmatically initiated smooth scroll or a user
		 * initiated touch scroll.
		 * 
		 * @param position
		 *            Position index of the first page currently being
		 *            displayed. Page position+1 will be visible if
		 *            positionOffset is nonzero.
		 * @param positionOffset
		 *            Value from [0, 1) indicating the offset from the page at
		 *            position.
		 * @param positionOffsetPixels
		 *            Value in pixels indicating the offset from position.
		 */
		public void onPageScrolled(SlidingView v, int position,
				float positionOffset, int positionOffsetPixels);

		/**
		 * This method will be invoked when a new page becomes selected.
		 * Animation is not necessarily complete.
		 * 
		 * @param position
		 *            Position index of the new selected page.
		 */
		public void onPageSelected(SlidingCard v, int prevPosition,
				int curPosition);

		/**
		 * This method will be invoked when a new page becomes selected. after
		 * animation has completed.
		 * 
		 * @param position
		 *            Position index of the new selected page.
		 */
		public void onPageSelectedAfterAnimation(SlidingCard v,
				int prevPosition, int curPosition);

		/**
		 * Called when the scroll state changes. Useful for discovering when the
		 * user begins dragging, when the pager is automatically settling to the
		 * current page, or when it is fully stopped/idle.
		 * 
		 * @param state
		 *            The new scroll state.
		 * @see SlidingCard#SCROLL_STATE_IDLE
		 * @see SlidingCard#SCROLL_STATE_DRAGGING
		 * @see SlidingCard#SCROLL_STATE_SETTLING
		 */
		public void onPageScrollStateChanged(SlidingCard v, int state);

	}
}
