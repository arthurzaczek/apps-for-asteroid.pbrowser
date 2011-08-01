package net.zaczek.PBrowser;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Browser extends Activity {
	private static final int DLG_WAIT = 1;
	private static final int MENU_HOME = 1;
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		webview = new WebView(this);
		setContentView(webview);

		final Activity activity = this;
		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				navigate(url);
				return true;
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
			
			public void onPageFinished (WebView view, String url) {
				activity.dismissDialog(Browser.DLG_WAIT);
			}
		});

		Intent intent = getIntent();
		String url = intent.getStringExtra("url");

		navigate(url);
	}

	private void navigate(String url) {
		showDialog(DLG_WAIT);
		webview.loadUrl(url);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webview.canGoBack()) {
				showDialog(DLG_WAIT);
				webview.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog;
		switch (id) {
		case DLG_WAIT:
			dialog = new ProgressDialog(this);
			dialog.setMessage("Loading");
			break;
		default:
			dialog = null;
		}
		return dialog;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_HOME, 0, "Home");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case MENU_HOME:
			startActivity(new Intent(this, Main.class));
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}
}
