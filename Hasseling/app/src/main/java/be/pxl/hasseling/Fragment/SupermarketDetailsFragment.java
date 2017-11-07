package be.pxl.hasseling.Fragment;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        String txt = bundle.getString("supermarketdetail_text");
        TextView sampleText = (TextView) rootView.findViewById(R.id.supermarketId_text);
        sampleText.setText(txt);

        SupermarketDetailsFragment.FetchSupermarketDetailTask supermarketDetailTask = new SupermarketDetailsFragment.FetchSupermarketDetailTask();
        supermarketDetailTask.execute(txt);



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

            final String OWM_NAME = "name";
            final String OWM_RATING= "rating";
            final String OWM_VICINITY = "vicinity";
            final String OWM_PLACEID = "place_id";

            JSONObject supermarketJson = new JSONObject(supermarketJsonStr);
            JSONObject supermarketJsonResult = supermarketJson.getJSONObject(OWM_RESULT);

            List<String> resultStrs = new ArrayList<>();
                String name;
                Boolean openNow;
                Long rating;
                String vicinity;

                // Get the JSON object representing the  supermarket
               // JSONObject supermarket = supermarketsrArray.getJSONObject(i);

                name = supermarketJsonResult.getString(OWM_NAME);
                vicinity= supermarketJsonResult.getString(OWM_VICINITY);

                resultStrs.add(name + " ");
            resultStrs.add(vicinity);
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
            TextView name_text = (TextView) rootView.findViewById(R.id.name_text);
            name_text.setText(result.get(0));
            TextView adress_text = (TextView) rootView.findViewById(R.id.adress_text);
            adress_text.setText(result.get(1));

        }
    }

}
