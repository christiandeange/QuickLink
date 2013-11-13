package com.deange.quicklink.ui.misc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.deange.quicklink.R;

public class TextFontView extends TextView {

	private String mFontName;

	public TextFontView(final Context context, final String defaultFont) {
		super(context);
		init(context, null, 0, defaultFont);
	}

	public TextFontView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0, null);
	}

	public TextFontView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle, null);
	}

	private void init(final Context context, final AttributeSet attrs, final int defStyle, final String defaultFont) {

		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.QLView, defStyle, 0);
		if (a != null) {
			mFontName = a.getString(R.styleable.QLView_ql_fontName);
			a.recycle();
		} else {
			mFontName = defaultFont;
		}

		if (isInEditMode()) {
			// Fix to view the TextFontView in Eclipse's graphical editor
			setTypeface(Typeface.SANS_SERIF);
			return;
		}

		if (!TextUtils.isEmpty(mFontName)) {
			setTypeface(Typeface.createFromAsset(getContext().getAssets(), mFontName));
		}
	}

}
