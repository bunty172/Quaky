package com.example.quaky;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {

	public EarthQuakeAdapter(@NonNull Context context, @NonNull ArrayList<EarthQuake> objects) {
		super(context, 0, objects);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View individualView = convertView;

		if (individualView == null)
			individualView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, parent, false);

		TextView magnitude_textview = individualView.findViewById(R.id.magnitude_textview);
		TextView offsetlocation_textview = individualView.findViewById(R.id.offsetlocation_textview);
		TextView mainlocation_textview = individualView.findViewById(R.id.mainlocation_textview);
		TextView year_textview = individualView.findViewById(R.id.year_textview);
		TextView time_textview = individualView.findViewById(R.id.time_textview);
		EarthQuake quake = getItem(position);
		GradientDrawable magnitudeCircle = (GradientDrawable) magnitude_textview.getBackground();
		int magnitudeColor = getMagnitudeColor(quake.getMagnitude());


		magnitude_textview.setText(quake.getMagnitude());
		magnitudeCircle.setColor(magnitudeColor);

		offsetlocation_textview.setText(quake.getOffsetlocation());

		mainlocation_textview.setText(quake.getMainlocation());

		year_textview.setText(quake.getYear());

		time_textview.setText(quake.getTime());

		return individualView;
	}

	private int getMagnitudeColor(String magnitude) {
		int magnitudeColorResourceId;
		Double mag = Double.parseDouble(magnitude);
		int magnitudeFloor = (int) Math.floor(mag);

		switch (magnitudeFloor) {
			case 0:
			case 1:
				magnitudeColorResourceId = R.color.magnitude1;
				break;
			case 2:
				magnitudeColorResourceId = R.color.magnitude2;
				break;
			case 3:
				magnitudeColorResourceId = R.color.magnitude3;
				break;
			case 4:
				magnitudeColorResourceId = R.color.magnitude4;
				break;
			case 5:
				magnitudeColorResourceId = R.color.magnitude5;
				break;
			case 6:
				magnitudeColorResourceId = R.color.magnitude6;
				break;
			case 7:
				magnitudeColorResourceId = R.color.magnitude7;
				break;
			case 8:
				magnitudeColorResourceId = R.color.magnitude8;
				break;
			case 9:
				magnitudeColorResourceId = R.color.magnitude9;
				break;
			default:
				magnitudeColorResourceId = R.color.magnitude10plus;
				break;
		}
		return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
	}

}
