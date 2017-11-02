package com.dev.sunshine;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.sunshine.data.WeatherContract;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ShareActionProvider mShareActionProvider;
    private String forecastData;

    private static final int DETAIL_LOADER = 0;

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private ImageView iconImageView;
    private TextView dateTextView;
    private TextView highTempTextView;
    private TextView lowTempTextView;
    private TextView humidityTextView;
    private TextView windTextView;
    private TextView pressureTextView;
    private TextView descTextView;

    private static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COLUMN_HUMIDITY = 5;
    public static final int COLUMN_WIND_SPEED = 6;
    public static final int COLUMN_DEGREES = 7;
    public static final int COLUMN_PRESSURE = 8;
    public static final int COL_WEATHER_CONDITION_ID = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

        iconImageView = findViewById(R.id.detail_icon);
        dateTextView = findViewById(R.id.detail_date_textview);
        highTempTextView = findViewById(R.id.detail_high_temp_textview);
        lowTempTextView = findViewById(R.id.detail_low_temp_textview);
        humidityTextView = findViewById(R.id.detail_humidity_textview);
        windTextView = findViewById(R.id.detail_wind_textview);
        pressureTextView = findViewById(R.id.detail_pressure_textview);
        descTextView = findViewById(R.id.detail_forecast_textview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            if (forecastData != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, forecastData + " " + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getIntent();
        if (intent == null) {
            return null;
        }
        return new CursorLoader(this, intent.getData(), DETAIL_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);
        iconImageView.setImageResource(R.mipmap.ic_launcher);

        String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
        String weatherDescription = data.getString(COL_WEATHER_DESC);

        boolean isMetric = Utility.isMetric(this);
        String high = Utility.formatTemperature(this, data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        String low = Utility.formatTemperature(this, data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);
        String humidity = getString(R.string.format_humidity, data.getDouble(COLUMN_HUMIDITY));
        String wind = Utility.getFormattedWind(this, data.getFloat(COLUMN_WIND_SPEED), data.getFloat(COLUMN_DEGREES));
        String pressure = getString(R.string.format_pressure, data.getDouble(COLUMN_PRESSURE));

        forecastData = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

        dateTextView.setText(dateString);
        highTempTextView.setText(high);
        lowTempTextView.setText(low);
        humidityTextView.setText(humidity);
        windTextView.setText(wind);
        pressureTextView.setText(pressure);
        descTextView.setText(weatherDescription);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
