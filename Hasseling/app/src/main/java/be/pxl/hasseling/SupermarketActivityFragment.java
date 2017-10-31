package be.pxl.hasseling;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SupermarketActivityFragment extends Fragment {

    private ArrayAdapter<String> supermarketsAdapter;

    public SupermarketActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String [] arraySupermarkets = {
                "OEPS!",
                "Something went wrong!", "While collecting the data",
                "Please check the following", "Connection with internet"};

        List<String> supermarkets = new ArrayList<String>(
                Arrays.asList(arraySupermarkets));

        supermarketsAdapter = new ArrayAdapter<String>(
                //current context
                getActivity(),
                //ID of list item layout
                R.layout.list_item_categorie,
                //ID of the textview to populate
                R.id.list_item_categorie_textview,
                //weekForecast data
                supermarkets
        );

        View rootview = inflater.inflate(R.layout.fragment_supermarket, container);
        ListView listView = (ListView) rootview.findViewById(
                R.id.listview_supermarket);
        listView.setAdapter(supermarketsAdapter);

        FetchSupermarketTask supermarketTask = new FetchSupermarketTask();
        supermarketTask.execute("50.931348,5.343312");
        return rootview;
    }

    public class FetchSupermarketTask extends AsyncTask<String, Void, List<String>> {
        private final String LOG_TAG = FetchSupermarketTask.class.getSimpleName();

        /* The date/time conversion code is going to be moved outside the asynctask later,
         * so for convenience we're breaking it out into its own method now.
         */
        private String getReadableDateString(long time){
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }


        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private List<String> getSupermarketDataFromJson(String supermarketJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWN_OPENINGHOURS = "opening_hours";
            final String OWN_OPENNOW = "open_now";

            final String OWM_NAME = "name";
            final String OWM_RATING= "rating";
            final String OWM_VICINITY = "vicinity";

            JSONObject supermarketJson = new JSONObject(supermarketJsonStr);
            JSONArray supermarketsrArray = supermarketJson.getJSONArray(OWM_RESULTS);//result

            Time dayTime = new Time();
            dayTime.setToNow();
            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
            // now we work exclusively in UTC
            dayTime = new Time();

            List<String> resultStrs = new ArrayList<>();
            for(int i = 0; i < supermarketsrArray.length(); i++) {
                // For now, using the format "Name, Adres, openNow, Rating"
                String day;
                String name;
                String openNow;
                String rating;
                String vicinity;

                // Get the JSON object representing the day
                JSONObject supermarket = supermarketsrArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay+i);
                day = getReadableDateString(dateTime);

                name = supermarket.getString(OWM_NAME);
                vicinity= supermarket.getString(OWM_VICINITY);
                if (supermarket.has(OWM_RATING)) {
                    rating = Math.round(supermarket.getLong(OWM_RATING)) + " sterren";
                }else{
                    rating = "Geen rating";
                }
                // description is in a child array called "weather", which is 1 element long.
                if (supermarket.has(OWN_OPENINGHOURS)) {
                    JSONObject openingHoursObject = supermarket.getJSONObject(OWN_OPENINGHOURS);
                    boolean openNowBool = openingHoursObject.getBoolean(OWN_OPENNOW);
                    openNow = ((openNowBool == true) ? "OPEN!" : "GESLOTEN");
                }else{
                    openNow = "Niet gekend";
                }
                resultStrs.add("NAAM =" + name  + "ADRES =" + vicinity +"RATING =" + rating + "NU open =" +  openNow);
            }

          /*  for (String s : resultStrs) {
               Log.v(LOG_TAG, "Supermarket entry: " + s);
            }*/
            return resultStrs;

        }

        @Override
        protected List<String> doInBackground(String... params) {
            // If there's no location , there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
                // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String supermarketsJsonStr = null;

            String location = "50.931348,5.343312";
            String radius = "2000";
            String keyword = "convenience_store";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String SUPERMARKET_BASE_URL =
                        "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
                final String LOCATION_PARAM = "location";
                final String RADIUS_PARAM = "radius";
                final String KEYWORD_PARAM = "keyword";
                final String KEY_PARAM = "key";

                Uri builtUri = Uri.parse(SUPERMARKET_BASE_URL).buildUpon()
                        .appendQueryParameter(LOCATION_PARAM, params[0])
                        .appendQueryParameter(RADIUS_PARAM, radius)
                        .appendQueryParameter(KEYWORD_PARAM, keyword)
                        .appendQueryParameter(KEY_PARAM, BuildConfig.GOOGLE_PLACES_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                   //   Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                 //  Log.v(LOG_TAG, "Forecast string: " + supermarketsJsonStr);

                // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
          /*      String baseUrl = "http://api.openweathermap.org/data/2.5/forecast?q=94043&mode=json&units=metric&cnt=7";
                String apiKey = "&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));*/

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    supermarketsJsonStr = null;
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    supermarketsJsonStr = null;
                    return null;
                }
                supermarketsJsonStr = buffer.toString();
             //   Log.v(LOG_TAG, "Supermarket JSON String " + supermarketsJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                supermarketsJsonStr = null;
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getSupermarketDataFromJson(supermarketsJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (result != null) {
                supermarketsAdapter.clear();
                for(String supermarketStr : result) {
                    supermarketsAdapter.add(supermarketStr);
                }
            }
        }
    }

}
