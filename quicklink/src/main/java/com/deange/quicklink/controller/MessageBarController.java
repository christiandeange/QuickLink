package com.deange.quicklink.controller;

/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import com.deange.quicklink.R;

public class MessageBarController {
	private final View mBarView;
	private final TextView mMessageView;
	private final ViewPropertyAnimator mBarAnimator;
	private final Handler mHideHandler = new Handler();

	private static final int REMOVAL_DELAY = 3500;
	private static final String SAVED_STATE_TEXT = "message_text";

	// State objects
	private String mMessageText;

	public MessageBarController(final View messageBarView) {
		mBarView = messageBarView;
		mBarAnimator = mBarView.animate();
		mMessageView = (TextView) mBarView.findViewById(R.id.messagebar_text);

		hideMessageBar(true);
	}

	public void showMessageBar(final boolean immediate, final String message) {
		mMessageText = message;
		mMessageView.setText(mMessageText);

		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, REMOVAL_DELAY);

		mBarView.setVisibility(View.VISIBLE);
		if (immediate) {
			mBarView.setAlpha(1);
		} else {
			mBarAnimator.cancel();
			mBarAnimator.alpha(1)
					.setDuration(mBarView.getResources().getInteger(android.R.integer.config_shortAnimTime))
					.setListener(null);
		}
	}

	public void hideMessageBar(final boolean immediate) {
		mHideHandler.removeCallbacks(mHideRunnable);
		if (immediate) {
			mBarView.setVisibility(View.GONE);
			mBarView.setAlpha(0);
			mMessageText = null;

		} else {
			mBarAnimator.cancel();
			mBarAnimator.alpha(0)
					.setDuration(mBarView.getResources().getInteger(android.R.integer.config_shortAnimTime))
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mBarView.setVisibility(View.GONE);
							mMessageText = null;
						}
					});
		}
	}

	public void onSaveInstanceState(final Bundle outState) {
		outState.putCharSequence(SAVED_STATE_TEXT, mMessageText);
	}

	public void onRestoreInstanceState(final Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mMessageText = savedInstanceState.getString(SAVED_STATE_TEXT);
			if (!TextUtils.isEmpty(mMessageText)) {
				showMessageBar(true, mMessageText);
			}
		}
	}

	private final Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			hideMessageBar(false);
		}
	};
}
