package com.deange.quicklink.ui.activity;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

import com.deange.quicklink.R;
import com.deange.quicklink.content.ContentHelper;
import com.deange.quicklink.content.NavDrawerAdapter;
import com.deange.quicklink.model.GoogleUrl;
import com.deange.quicklink.task.ShortenTask;
import com.deange.quicklink.task.ormlite.OrmQueryTask;
import com.deange.quicklink.ui.fragment.ShareUrlFragment;
import com.deange.quicklink.ui.misc.AboutDialog;
import com.deange.quicklink.utils.HttpUtils;
import com.deange.quicklink.utils.Utils;
import com.j256.ormlite.stmt.QueryBuilder;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.List;

public class LandingActivity extends FragmentActivity implements ShortenTask.Callback, ShareUrlFragment.Callback,
		OrmQueryTask.Callback<GoogleUrl>, OnItemClickListener {

	private static final String DEFAULT_SCHEME = "http://";

	private AlertDialog mAboutDialog;
	private ShareUrlFragment mMainFragment;

	private DrawerLayout mNavDrawerLayout;
	private ActionBarDrawerToggle mNavDrawerToggle;
	private JazzyListView mNavDrawerList;
    private NavDrawerAdapter mAdapter;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_nav_drawer);

		mMainFragment = (ShareUrlFragment) getSupportFragmentManager().findFragmentByTag(ShareUrlFragment.TAG);
		if (mMainFragment == null) {
			mMainFragment = ShareUrlFragment.createInstance();
		}

		mMainFragment.setCallback(this);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mMainFragment, ShareUrlFragment.TAG)
				.commit();

		setupNavigationDrawer();
		getNavigationDrawerItems();
	}

	@Override
	protected void onDestroy() {
		if (mAboutDialog != null) {
			mAboutDialog.dismiss();
			mAboutDialog = null;
		}

		super.onDestroy();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mNavDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mNavDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onQueryCompleted(List<GoogleUrl> itemCollection) {
		if (itemCollection != null) {
            if (mAdapter == null) {
                mAdapter = new NavDrawerAdapter(this, itemCollection);
                mNavDrawerList.setAdapter(mAdapter);
            } else {
                mAdapter.clear();
                mAdapter.addAll(itemCollection);
            }

            mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mNavDrawerLayout.closeDrawer(mNavDrawerList);
		final GoogleUrl googleUrl = (GoogleUrl) parent.getItemAtPosition(position);
		copyAndShowShortURL(googleUrl, R.string.failed_copy_shortened_url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (mNavDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		} else if (item.getItemId() == R.id.action_open_about_menu) {
			showAboutDialog();
			return true;
		}
		return false;
	}

	@Override
	public void onShortenLinkAsked(final EditText editText, String url) {

		if (!TextUtils.isEmpty(url)) {
			if (mNavDrawerLayout.isDrawerOpen(mNavDrawerList)) {
				mNavDrawerLayout.closeDrawer(mNavDrawerList);
			}

			if (HttpUtils.isOnline(this)) {
				if (!Utils.matchPattern(url, "^(https?|ftp|file):\\/\\/.*")) {
					// Fall back onto default scheme
					url = DEFAULT_SCHEME + url;
					editText.setText(url);
				}

				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

				new ShortenTask(this, this, url).execute();
			} else {
				showMessage(getString(R.string.need_network));
			}
		}
	}

	@Override
	public void onShortenUrlDone(final GoogleUrl shortenedUrl) {
		copyAndShowShortURL(shortenedUrl, R.string.failed_create_shortened_url);
	}

	@Override
	public void onUrlSavedToDatabase(GoogleUrl googleUrl) {
		getNavigationDrawerItems();
	}

	private void setupNavigationDrawer() {
		mNavDrawerList = (JazzyListView) findViewById(R.id.nav_drawer_listview);
		mNavDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
		mNavDrawerToggle = new ActionBarDrawerToggle(this, mNavDrawerLayout, R.drawable.ic_drawer,
				R.string.nav_drawer_opened, R.string.nav_drawer_closed);

		mNavDrawerLayout.setDrawerShadow(R.drawable.nav_drawer_shadow, GravityCompat.START);
		mNavDrawerLayout.setDrawerListener(mNavDrawerToggle);

		mNavDrawerList.setDuration(300);
		mNavDrawerList.setShouldOnlyAnimateNewItems(true);
		mNavDrawerList.setTransitionEffect(JazzyHelper.CARDS);
		mNavDrawerList.setOnItemClickListener(this);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@SuppressWarnings("unchecked")
	private void getNavigationDrawerItems() {
		final QueryBuilder<GoogleUrl, Long> query = ContentHelper.getInstance(this).getDao(GoogleUrl.class)
				.queryBuilder();
		query.orderBy(GoogleUrl.TIMESTAMP, false);
		OrmQueryTask<GoogleUrl> queryTask = new OrmQueryTask<GoogleUrl>(this, this, GoogleUrl.class);
		queryTask.execute(query);
	}

	private void showAboutDialog() {
		mAboutDialog = AboutDialog.create(this);
		mAboutDialog.show();
		AboutDialog.onDialogCreateView(mAboutDialog);
	}

	private void copyAndShowShortURL(final GoogleUrl googleUrl, final int errorResId) {
		if ((googleUrl == null) || (googleUrl.getShortenedUrl() == null)) {
			showMessage(getString(errorResId));

		} else {
			final String shortLink = googleUrl.getShortenedUrl();
			Utils.copyToClipboard(this, shortLink);
			showMessage(shortLink);
		}
	}

	private void showMessage(final String messageText) {
		if (mMainFragment != null) {
			mMainFragment.showMessage(messageText);
		}
	}

}
