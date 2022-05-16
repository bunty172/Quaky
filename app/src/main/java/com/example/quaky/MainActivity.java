package com.example.quaky;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuake>> {

	ArrayList<EarthQuake> earthQuakes = new ArrayList<EarthQuake>();
	EarthQuakeAdapter earthQuakeAdapter;
	ListView words_listview;
	ProgressBar progressBar;
	TextView emptyview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.words_list);

		progressBar = findViewById(R.id.loading_spinner);

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			getSupportLoaderManager().initLoader(1, null, this).forceLoad();

			earthQuakeAdapter = new EarthQuakeAdapter(this, earthQuakes);

			words_listview = findViewById(R.id.words_list_view);

			words_listview.setAdapter(earthQuakeAdapter);

			words_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					EarthQuake e = earthQuakeAdapter.getItem(i);
					Uri uri = Uri.parse(e.getLink());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
			});
		} else {
			progressBar.setVisibility(View.GONE);
			 emptyview = findViewById(R.id.empty_view);
			emptyview.setText("Connect to the internet");
		}
	}

	@NonNull
	@Override
	public Loader<ArrayList<EarthQuake>> onCreateLoader(int id, @Nullable Bundle args) {
		return new EarthQuakeAsyncTaskLoader(MainActivity.this);
	}

	@Override
	public void onLoadFinished(@NonNull Loader<ArrayList<EarthQuake>> loader, ArrayList<EarthQuake> data) {

		if (data != null) {
			progressBar.setVisibility(View.GONE);
			earthQuakeAdapter.addAll(data);
		} else {
			progressBar.setVisibility(View.GONE);
			emptyview = findViewById(R.id.empty_view);
			emptyview.setText(R.string.oops_no_internet_connection);
			words_listview.setEmptyView(emptyview);
		}
	}

	@Override
	public void onLoaderReset(@NonNull Loader<ArrayList<EarthQuake>> loader) {
		earthQuakes = null;
	}


	private static class EarthQuakeAsyncTaskLoader extends AsyncTaskLoader<ArrayList<EarthQuake>> {

		public EarthQuakeAsyncTaskLoader(@NonNull Context context) {
			super(context);
		}

		@Nullable
		@Override
		public ArrayList<EarthQuake> loadInBackground() {
			ArrayList<EarthQuake> earthquakesReadyToParse = JsonParser.retrieveFromHTTPURL();
			return earthquakesReadyToParse;
		}
	}
}