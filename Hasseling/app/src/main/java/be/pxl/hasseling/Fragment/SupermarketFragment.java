package be.pxl.hasseling.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.pxl.hasseling.BuildConfig;
import be.pxl.hasseling.R;
import be.pxl.hasseling.activity.SupermarketDetailsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupermarketFragment extends Fragment {

    private ArrayAdapter<String> supermarketsAdapter;

    public SupermarketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_supermarket, container, false);

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

        ListView listView = (ListView) rootview.findViewById(
                R.id.listview_supermarket);
        listView.setAdapter(supermarketsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String forecast = supermarketsAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), SupermarketDetailsActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });

        SupermarketFragment.FetchSupermarketTask supermarketTask = new SupermarketFragment.FetchSupermarketTask();
        supermarketTask.execute("50.931348,5.343312");


        //ADDED BY NASIM - To pass a string between fragments -
        // miss kan die je helpen om de details te kunnen tonen,
        // Van de SupermarketFragment naar  de SupermarketDetailsFragment,
        final EditText sampleTxt;
        Button  submitBtn;

        sampleTxt = (EditText) rootview.findViewById(R.id.SampleTxt);
        submitBtn = (Button) rootview.findViewById(R.id.SubmitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String txt = sampleTxt.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("SampleTxt", txt);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SupermarketDetailsFragment supermarketDetailsFragment = new SupermarketDetailsFragment();
                supermarketDetailsFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.content_frame, supermarketDetailsFragment);
                fragmentTransaction.commit();
            }
        });


        return rootview;
    }

    public class FetchSupermarketTask extends AsyncTask<String, Void, List<String>> {
        private final String LOG_TAG = SupermarketFragment.FetchSupermarketTask.class.getSimpleName();

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
            final String OWM_RESULTS = "results";

            final String OWN_OPENINGHOURS = "opening_hours";
            final String OWN_OPENNOW = "open_now";

            final String OWM_NAME = "name";
            final String OWM_RATING= "rating";
            final String OWM_VICINITY = "vicinity";

            JSONObject supermarketJson = new JSONObject(supermarketJsonStr);
            JSONArray supermarketsrArray = supermarketJson.getJSONArray(OWM_RESULTS);

            List<String> resultStrs = new ArrayList<>();
            for(int i = 0; i < supermarketsrArray.length(); i++) {
                String name;
                String openNow;
                String rating;
                String vicinity;

                // Get the JSON object representing the  supermarket
                JSONObject supermarket = supermarketsrArray.getJSONObject(i);

                name = supermarket.getString(OWM_NAME);
                vicinity= supermarket.getString(OWM_VICINITY);

                if (supermarket.has(OWM_RATING)) {
                    rating = Math.round(supermarket.getLong(OWM_RATING)) + " stars";
                }else{
                    rating = "No rating";
                }

                if (supermarket.has(OWN_OPENINGHOURS)) {
                    JSONObject openingHoursObject = supermarket.getJSONObject(OWN_OPENINGHOURS);
                    boolean openNowBool = openingHoursObject.getBoolean(OWN_OPENNOW);
                    openNow = ((openNowBool == true) ? "OPEN NOW!" : "CLOSED ATM");
                }else{
                    openNow = "Unknown";
                }
                resultStrs.add(name  + "\t - " + rating + "\n"  +  openNow + "\n" + vicinity);
            }
          /*  for (String s : resultStrs) { //for testing porpuse
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

            String location = "50.931348,5.343312";
            String radius = "2000";
            String keyword = "convenience_store";

            try {
                // Construct the URL for the Google places API query
                // Possible parameters are available at API page, at
                // https://developers.google.com/places/web-service/search
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
                //   Log.v(LOG_TAG, "Supermarket JSON String " + supermarketsJsonStr); //Debugging purpose

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
            if (result != null) {
                supermarketsAdapter.clear();
                for(String supermarketStr : result) {
                    supermarketsAdapter.add(supermarketStr);
                }
            }
        }
    }

}
