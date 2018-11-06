package com.example.tiggs.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherActivity extends Activity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);//setting progressbar to visible
        ForecastQuery forecast = new ForecastQuery();
        forecast.execute();

    }
/*
<current>
<city id="6094817" name="Ottawa">
<coord lon="-75.69" lat="45.42"/>
<country>CA</country>
<sun rise="2018-11-05T11:49:00" set="2018-11-05T21:43:08"/>
</city>
<temperature value="3" min="3" max="3" unit="metric"/>
<humidity value="100" unit="%"/>
<pressure value="1022" unit="hPa"/>
<wind>
<speed value="5.1" name="Gentle Breeze"/>
<gusts/>
<direction value="80" code="E" name="East"/>
</wind>
<clouds value="90" name="overcast clouds"/>
<visibility value="16093"/>
<precipitation mode="no"/>
<weather number="500" value="light rain" icon="10n"/>
<lastupdate value="2018-11-05T22:00:00"/>
</current>
*/

    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        String speed;
        String minTemp;
        String maxTemp;
        String curTemp;
        String iconName;
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
        String imageUrlString = null;
        Bitmap weatherIcon;


        public String doInBackground(String... arg) {

            try {
                //connect to api server
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);//milliseconds
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream response = conn.getInputStream();

                //read xml
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(response, "UTF-8");

                while (parser.next() != parser.END_DOCUMENT) { //if not the end tag, check if name = temperature or speed or weather
                    switch (parser.getEventType()) {
                        case XmlPullParser.START_TAG:
                            String name = parser.getName();

                            if (name.equals("temperature")) {
                                curTemp = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                    Log.i("Current Temp: ", curTemp);
                                minTemp = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                    Log.i("Min Temp: ", minTemp);
                                maxTemp = parser.getAttributeValue(null, "max");
                                publishProgress(75);
                                    Log.i("Max Temp: ", maxTemp);
                            }
                                if (name.equals("speed")) {
                                    speed = parser.getAttributeValue(null, "value");
                                    Log.i("Wind Speed: ", speed);

                                }

                            if (name.equals("weather")) {
                                iconName = parser.getAttributeValue(null, "icon");
                                Log.i("Weather Icon Name: ", iconName);
                                imageUrlString = "http://openweathermap.org/img/w/" + iconName + ".png";
                            }
                            Log.i("XML tag is: ", name);
                            break;
                        case XmlPullParser.TEXT:
                            break;
                    }

                }

            } catch (IOException e) {
                Log.i("Exception", e.getMessage());
            } catch (XmlPullParserException e) {
                Log.i("Exception", e.getMessage());
            }
            try {
                Log.i("trying to dl image", null);
                //check if image is already stored in storage
                File file = getBaseContext().getFileStreamPath(iconName + ".png");
                if (!file.exists()) {
                    //connect to image server
                    URL imageUrl = new URL(imageUrlString);
                    HttpURLConnection connUrl = (HttpURLConnection) imageUrl.openConnection();
                    connUrl.connect();
                    weatherIcon = BitmapFactory.decodeStream(connUrl.getInputStream());
                    publishProgress(100);
                    //if (connUrl != null){connUrl.disconnect();}

                    //store in local storage
                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    weatherIcon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.i("doesnt exist: ", iconName + ".png");
                } else {
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(iconName + ".png");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bm = BitmapFactory.decodeStream(fis);
                    Log.i("image file not found: ", iconName + ".png");

                }

            } catch (Exception e) {
                return null;
            }
            return "";

        }

        @Override
        public void onProgressUpdate(Integer... arg) {//update screen
            progressBar.setVisibility(View.VISIBLE);//setting progressbar to visible
            progressBar.setProgress(arg[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.windSpeed)).setText("Wind Speed: " + speed);
            ((TextView) findViewById(R.id.currentTemp)).setText("Current Temp: " + curTemp);
            ((TextView) findViewById(R.id.minTemp)).setText("Minimum Temperature" + minTemp);
            ((TextView) findViewById(R.id.maxTemp)).setText("Maximum Temperature: " + maxTemp);
            ((ImageView) findViewById(R.id.currentWeather)).setImageBitmap(weatherIcon);

        }


    }
}
