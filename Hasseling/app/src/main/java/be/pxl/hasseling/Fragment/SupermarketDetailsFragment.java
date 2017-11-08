package be.pxl.hasseling.Fragment;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import be.pxl.hasseling.BuildConfig;
import be.pxl.hasseling.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupermarketDetailsFragment extends Fragment {

    private String supermarketStr;
    private View rootView;
    public SupermarketDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_supermarket_details, container, false);

        //ADDED bY NASIM
        Bundle bundle = getArguments();
        String id = bundle.getString("supermarketdetail_text");

        SupermarketDetailsFragment.FetchSupermarketDetailTask supermarketDetailTask = new SupermarketDetailsFragment.FetchSupermarketDetailTask();
        supermarketDetailTask.execute(id);



        return rootView;
    }

    public class FetchSupermarketDetailTask extends AsyncTask<String, Void, List<String>> {
        private final String LOG_TAG = SupermarketDetailsFragment.FetchSupermarketDetailTask.class.getSimpleName();

        /**
         * Take the String representing the complete supermarkets in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private List<String> getSupermarketDataFromJson(String supermarketJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULT = "result";

            final String OWN_OPENINGHOURS = "opening_hours";
            final String OWN_OPENNOW = "open_now";
            final String OWN_WEEKDAY = "weekday_text";
            final String OWN_PHOTOS = "photos";
            final String OWM_PHOTOREFERENCE = "photo_reference";

            final String OWM_ADDRESS = "formatted_address";
            final String OWM_FORMPHONE = "formatted_phone_number";
            final String OWM_INTPHONE = "international_phone_number";
            final String OWM_NAME = "name";
            final String OWM_RATING= "rating";
            final String OWM_URL= "url";
            final String OWM_WEBSITE= "website";

            JSONObject supermarketJson = new JSONObject(supermarketJsonStr);
            JSONObject supermarketJsonResult = supermarketJson.getJSONObject(OWM_RESULT);

            List<String> resultStrs = new ArrayList<>();
            String formatted_address;
            String formatted_phone_number;
            String international_phone_number;
            String name;
            Boolean openNow;
            String weekday_text = "UNKOWN";
            String photo_reference;
            Long rating;
            String url, website;

                // Get the JSON object representing the  supermarket
               // JSONObject supermarket = supermarketsrArray.getJSONObject(i);
            formatted_address= supermarketJsonResult.getString(OWM_ADDRESS);
            formatted_phone_number= supermarketJsonResult.getString(OWM_FORMPHONE);
            international_phone_number= supermarketJsonResult.getString(OWM_INTPHONE);
            name = supermarketJsonResult.getString(OWM_NAME);

            if (supermarketJsonResult.has(OWN_OPENINGHOURS)) {
                JSONObject openingHoursObject = supermarketJsonResult.getJSONObject(OWN_OPENINGHOURS);
                openNow = openingHoursObject.getBoolean(OWN_OPENNOW);
                if(supermarketJson.has(OWN_WEEKDAY)){
                    JSONArray supermarketsrWeekdayArray = supermarketJsonResult.getJSONArray(OWN_WEEKDAY);
                    weekday_text = supermarketsrWeekdayArray.toString();
                }
            } else {
                openNow = null;
            }

            if (supermarketJsonResult.has(OWN_PHOTOS)) {
                JSONArray supermarketsrPhotosArray = supermarketJsonResult.getJSONArray(OWN_PHOTOS);
                JSONObject supermarketPhotosObj = supermarketsrPhotosArray.getJSONObject(0);
                photo_reference = supermarketPhotosObj.getString(OWM_PHOTOREFERENCE);
            } else {
                photo_reference = "default";
            }

            if (supermarketJsonResult.has(OWM_RATING)) {
                rating = supermarketJsonResult.getLong(OWM_RATING);
            } else {
                rating = null;
            }

            url = supermarketJsonResult.getString(OWM_URL);
            website = supermarketJsonResult.getString(OWM_WEBSITE);


            resultStrs.add(formatted_address);
            resultStrs.add(formatted_phone_number);
            resultStrs.add(international_phone_number);
            resultStrs.add(name);
        //    resultStrs.add(openNow);
            resultStrs.add(weekday_text);
            resultStrs.add(photo_reference);
        //    resultStrs.add(rating);
            resultStrs.add(url);
            resultStrs.add(website);

   /*         for (String s : resultStrs) { //for testing porpuse
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

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String supermarketsJsonStr = null;

            try {
                // Construct the URL for the Google places API query
                // Possible parameters are available at API page, at
                // https://developers.google.com/places/web-service/search
                final String SUPERMARKET_BASE_URL =
                        "https://maps.googleapis.com/maps/api/place/details/json?";
                final String PLACEID_PARAM = "placeid";
                final String KEY_PARAM = "key";

                Uri builtUri = Uri.parse(SUPERMARKET_BASE_URL).buildUpon()
                        .appendQueryParameter(PLACEID_PARAM, params[0])
                        .appendQueryParameter(KEY_PARAM, BuildConfig.GOOGLE_PLACES_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

          //       Log.v(LOG_TAG, "Built URI " + builtUri.toString());
           //       Log.v(LOG_TAG, "Forecast string: " + supermarketsJsonStr);
                //Debugging purpose

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
              //    Log.v(LOG_TAG, "Supermarket JSON String " + supermarketsJsonStr); //Debugging purpose

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the supermarket data, there's no point in attempting
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

            // This will only happen if there was an error getting or parsing the supermarket.
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {

            //TEST OP WAARDE NULL - EXCEPTION
            TextView formatted_address  = (TextView) rootView.findViewById(R.id.address_text);
            formatted_address.setText(result.get(0));

            TextView formatted_phone_number   = (TextView) rootView.findViewById(R.id.phone_text);
            formatted_phone_number.setText(result.get(1));

            TextView international_phone_number    = (TextView) rootView.findViewById(R.id.internationalPhone_text);
            international_phone_number.setText(result.get(2));

            TextView name_text = (TextView) rootView.findViewById(R.id.name_text);
            name_text.setText(result.get(3));

            TextView weekday_text = (TextView) rootView.findViewById(R.id.weekdays_text );
            weekday_text.setText(result.get(4));

            ImageView photo_reference = (ImageView)rootView.findViewById(R.id.photo_view );
            Picasso.with(getContext()).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + result.get(5) + "&key=" + BuildConfig.GOOGLE_PLACES_API_KEY).resize(250,250).into(photo_reference);
//CHECK OP geen foto

            TextView url = (TextView) rootView.findViewById(R.id.mapsUrl_text  );
            url.setText(result.get(6));

            TextView website  = (TextView) rootView.findViewById(R.id.website_text  );
            website.setText(result.get(7));
        }
    }

}
