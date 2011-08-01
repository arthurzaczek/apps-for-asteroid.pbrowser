package net.zaczek.PBrowser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends ListActivity {
	private ArrayAdapter<String> adapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		fillData();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(this, Browser.class);
		String url = adapter.getItem(position);
		i.putExtra("url", url);
		startActivity(i);
	}

	private void fillData() {
		try {
			ArrayList<String> urls = new ArrayList<String>();
			File root = Environment.getExternalStorageDirectory();
			File dir = new File(root, "PBrowser");
			dir.mkdir();
			File file = new File(dir, "urls.txt");
			FileReader reader = new FileReader(file);
			try {
				BufferedReader in = new BufferedReader(reader);
				while (true) {
					String url = in.readLine();
					if (url == null)
						break;
					url = url.trim();
					if (TextUtils.isEmpty(url))
						continue;
					urls.add(url);
				}
			} finally {
				reader.close();
			}
			
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, urls);
			setListAdapter(adapter);
		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		}
	}
}