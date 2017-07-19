package com.ecolemultimedia.openstore.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecolemultimedia.openstore.R;
import com.ecolemultimedia.openstore.Utils.Preference;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "Detail";
    private TextView storeTitle;
    private ImageView storeImage;
    private TextView storeDay;
    private TextView storeStartHour;
    private TextView storeEndHour;
    private TextView storeDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.e(TAG, String.valueOf(Preference.getPrefStartHour(DetailActivity.this)));

        // Link to View
        storeTitle = (TextView) findViewById(R.id.StoreTitle);
        storeImage = (ImageView) findViewById(R.id.ImageHead);
        storeDay = (TextView) findViewById(R.id.StoreDay);
        storeStartHour = (TextView) findViewById(R.id.StoreStartHour);
        storeEndHour = (TextView) findViewById(R.id.StoreEndHour);
        storeDescription = (TextView) findViewById(R.id.StoreDescription);
        // SET VALUE
        Picasso.with(DetailActivity.this)
                .load(Preference.getPrefImage(DetailActivity.this))
                .into(storeImage);
        storeTitle.setText(Preference.getPrefName(DetailActivity.this));
        Log.e(TAG, "Detail: "+ Preference.getPrefDay(DetailActivity.this));
        storeDescription.setText(Preference.getPrefDay(DetailActivity.this));
        storeDay.setText("Fermé le Dimanche");
        storeStartHour.setText("Ouvre à : "+String.valueOf(Preference.getPrefStartHour(DetailActivity.this))+"H");
        storeEndHour.setText("Ferme à : "+String.valueOf(Preference.getPrefEndHour(DetailActivity.this))+"H");
        invalidateOptionsMenu();

    }

}
