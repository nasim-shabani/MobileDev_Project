package be.pxl.hasseling.Fragment;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import be.pxl.hasseling.BuildConfig;
import be.pxl.hasseling.R;
import be.pxl.hasseling.categories.GenericArrayAdapter;
import be.pxl.hasseling.categories.Supermarket;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment {

    private GenericArrayAdapter<Supermarket> restaurantAdapter;

    public RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_restaurant, container, false);
        RestaurantFragment.FetchRestaurantTask restaurantTask = new RestaurantFragment.FetchRestaurantTask();
        restaurantTask.execute("50.931348,5.343312");

        // Construct the data source

        ArrayList<Supermarket> arrayOfUsers = new ArrayList<Supermarket>();
// Create the adapter to convert the array to views
        restaurantAdapter = new GenericArrayAdapter(this.getContext(), arrayOfUsers);
// Attach the adapter to a ListView
        ListView listView = (ListView) rootview.findViewById(
                R.id.listview_restaurant);
        listView.setAdapter(restaurantAdapter);


/*        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Access the row position here to get the correct data item
                Supermarket restaurant = restaurantAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("restaurantPlaceID", String.valueOf(restaurant.getPlaceId()));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                RestaurantDetailsFragment restaurantDetailsFragment = new RestaurantDetailsFragment();
                restaurantDetailsFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.content_frame, restaurantDetailsFragment);
                fragmentTransaction.commit();
            }
        });*/

        return rootview;
    }

    public class FetchRestaurantTask extends AsyncTask<String, Void, List<Supermarket>> {
        private final String LOG_TAG = RestaurantFragment.FetchRestaurantTask.class.getSimpleName();

        private List<Supermarket> getRestaurantDataFromJson(String restaurantJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";

            final String OWN_OPENINGHOURS = "opening_hours";
            final String OWN_OPENNOW = "open_now";
            final String OWN_PHOTOS = "photos";

            final String OWM_NAME = "name";
            final String OWM_RATING = "rating";
            final String OWM_VICINITY = "vicinity";
            final String OWM_PLACEID = "place_id";
            final String OWM_PHOTOREFERENCE = "photo_reference";

            JSONObject restaurantJson = new JSONObject(restaurantJsonStr);

            JSONArray restaurantsrArray = restaurantJson.getJSONArray(OWM_RESULTS);

            List<Supermarket> resultStrs = new ArrayList<>();
            for (int i = 0; i < restaurantsrArray.length(); i++) {
                String name;
                Boolean openNow;
                Long rating;
                String vicinity;
                String placeID;
                String photoReference = "";

                // Get the JSON object representing the  restaurants
                JSONObject restaurant = restaurantsrArray.getJSONObject(i);

                name = restaurant.getString(OWM_NAME);
                vicinity = restaurant.getString(OWM_VICINITY);
                placeID = restaurant.getString(OWM_PLACEID);

                if (restaurant.has(OWM_RATING)) {
                    rating = restaurant.getLong(OWM_RATING);
                } else {
                    rating = null;
                }

                if (restaurant.has(OWN_OPENINGHOURS)) {
                    JSONObject openingHoursObject = restaurant.getJSONObject(OWN_OPENINGHOURS);
                    openNow = openingHoursObject.getBoolean(OWN_OPENNOW);
                } else {
                    openNow = null;
                }

                if (restaurant.has(OWN_PHOTOS)) {
                    JSONArray restaurantsrPhotosArray = restaurant.getJSONArray(OWN_PHOTOS);
                    JSONObject restaurantPhotosObj = restaurantsrPhotosArray.getJSONObject(0);
                    photoReference = restaurantPhotosObj.getString(OWM_PHOTOREFERENCE);
                } else {
                    photoReference = "default";
                }

                resultStrs.add(new Supermarket(placeID, name, rating, openNow, vicinity, photoReference));

            }
          /*  for (String s : resultStrs) { //for testing porpuse
               Log.v(LOG_TAG, "Supermarket entry: " + s);
            }*/

            return resultStrs;

        }

        @Override
        protected List<Supermarket> doInBackground(String... params) {
            // If there's no location , there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String restaurantsJsonStr = null;

            String location = "50.931348,5.343312";
            String radius = "2000";
            String keyword = "restaurants";

            try {
                // Construct the URL for the Google places API query
                // Possible parameters are available at API page, at
                // https://developers.google.com/places/web-service/search
                final String SUPERMARKET_BASE_URL =
                        "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
                final String LOCATION_PARAM = "location";
                final String RADIUS_PARAM = "radius";
                final String KEYWORD_PARAM = "KEYWORD_TAG";
                final String KEY_PARAM = "key";

                Uri builtUri = Uri.parse(SUPERMARKET_BASE_URL).buildUpon()
                        .appendQueryParameter(LOCATION_PARAM, params[0])
                        .appendQueryParameter(RADIUS_PARAM, radius)
                        .appendQueryParameter(KEYWORD_PARAM, keyword)
                        .appendQueryParameter(KEY_PARAM, BuildConfig.GOOGLE_PLACES_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                //   Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                //  Log.v(LOG_TAG, "Forecast string: " + restaurantsJsonStr);
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
                    restaurantsJsonStr = null;
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
                    restaurantsJsonStr = null;
                    return null;
                }
                restaurantsJsonStr = buffer.toString();
                //   Log.v(LOG_TAG, "Supermarket JSON String " + restaurantsJsonStr); //Debugging purpose

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the restaurant data, there's no point in attempting
                // to parse it.
                restaurantsJsonStr = null;
                return null;
            } finally {
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
                return getRestaurantDataFromJson(restaurantsJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the restaurant.
            return null;
        }

        @Override
        protected void onPostExecute(List<Supermarket> result) {
            if (result != null) {
                restaurantAdapter.clear();
                for (Supermarket restaurantStr : result) {
                    restaurantAdapter.add(restaurantStr);
                }
            }
        }
    }


}
