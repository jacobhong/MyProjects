package com.hong.framework.implementations;

import java.util.ArrayList;
import java.util.List;

import com.hong.framework.Input;
import com.hong.framework.implementations.Pool.PoolObjectFactory;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/*
 * Handles user touch
 * 
 * Synchronize methods because calls come from
 * UI thread
 */
public class GameInput implements Input, OnTouchListener {
	boolean isTouched;
	int touchX;
	int touchY;
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;

	public GameInput(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public boolean onTouch(View v, MotionEvent event) {
		// get touch events and add to buffer
		synchronized (this) {
			TouchEvent touchEvent = touchEventPool.newObject();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				isTouched = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouched = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouched = false;
				break;
			}
			// must scale coordinates from device to target resolution
			touchEvent.x = touchX = (int) (event.getX() * scaleX);
			touchEvent.y = touchY = (int) (event.getY() * scaleY);
			touchEventsBuffer.add(touchEvent);
			return true;
		}
	}

	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			if (pointer == 0)
				return isTouched;
			else
				return false;
		}
	}

	public int getTouchX(int pointer) {
		synchronized (this) {
			return touchX;
		}
	}

	public int getTouchY(int pointer) {
		synchronized (this) {
			return touchY;
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized (this) {
			int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				// add objects to pool
				touchEventPool.free(touchEvents.get(i));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}

}
