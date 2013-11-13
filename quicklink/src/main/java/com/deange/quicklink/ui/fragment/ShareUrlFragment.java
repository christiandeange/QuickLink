package com.deange.quicklink.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.deange.quicklink.R;
import com.deange.quicklink.controller.MessageBarController;
import com.deange.quicklink.utils.Utils;

public class ShareUrlFragment extends Fragment implements OnEditorActionListener, View.OnKeyListener {

	public static final String TAG = ShareUrlFragment.class.getSimpleName();

	private Callback mCallback = Fallback.INSTANCE;

    private EditText mUrlEdit;

	private MessageBarController mMessageController;

    public interface Callback {
		void onShortenLinkAsked(final EditText editText, final String urlText);
	}

	public void setCallback(final Callback callback) {
		mCallback = callback == null ? Fallback.INSTANCE : callback;
	}

	public static ShareUrlFragment createInstance() {
		final ShareUrlFragment fragment = new ShareUrlFragment();
		fragment.setRetainInstance(true);

		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_landing, null);

		mUrlEdit = (EditText) rootView.findViewById(R.id.landing_url_edit);
		Utils.setMaxWidth(getActivity(), mUrlEdit, 500, TypedValue.COMPLEX_UNIT_DIP);
        mUrlEdit.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf"));
        mUrlEdit.setOnEditorActionListener(this);
        mUrlEdit.setOnKeyListener(this);

		mMessageController = new MessageBarController(rootView.findViewById(R.id.messagebar_root));

		return rootView;
	}

	@Override
	public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
		if ((actionId == EditorInfo.IME_ACTION_GO) && (v == mUrlEdit)) {
			mCallback.onShortenLinkAsked(mUrlEdit, mUrlEdit.getText().toString().trim());
		}
		return (actionId == EditorInfo.IME_ACTION_GO);
	}

    @Override
    public boolean onKey(final View v, final int keyCode, final KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_UP) && (mUrlEdit == v)) {
            mCallback.onShortenLinkAsked(mUrlEdit, mUrlEdit.getText().toString().trim());
        }
        return false;
    }

	public void showMessage(final String messageText) {
		mMessageController.showMessageBar(false, messageText);
	}

	private static final class Fallback implements Callback {
		public static final Callback INSTANCE = new Fallback();

		@Override
		public void onShortenLinkAsked(final EditText editText, final String urlText) {
			Log.w(TAG, "Fallback! onShortenLinkAsked()");
		}

	}

}
