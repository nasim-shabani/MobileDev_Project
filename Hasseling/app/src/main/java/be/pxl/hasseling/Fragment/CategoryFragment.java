package be.pxl.hasseling.Fragment;

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
import android.widget.ListView;
import android.widget.TextView;

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
import be.pxl.hasseling.categories.Category;
import be.pxl.hasseling.categories.GenericArrayAdapter;

public class CategoryFragment extends Fragment {
    private GenericArrayAdapter<Category> categoriesAdapter;
    public CategoryFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_category, container, false);

        Bundle bundle = getArguments();
        final String keyword = bundle.getString("Keyword");

        TextView keyword_txt = (TextView) rootview.findViewById(R.id.keyword_txt );
        keyword_txt.setText(keyword);

    //    Toast.makeText(getContext(), "Param0 = "+ keyword,  Toast.LENGTH_SHORT).show();
        CategoryFragment.FetchCategoryTask categoryTask = new CategoryFragment.FetchCategoryTask();
        categoryTask.execute("50.931348,5.343312",keyword);

        // Construct the data source

        ArrayList<Category> arrayOfUsers = new ArrayList<Category>();
// Create the adapter to convert the array to views
        categoriesAdapter = new GenericArrayAdapter(this.getContext(), arrayOfUsers);
// Attach the adapter to a ListView
        ListView listView = (ListView) rootview.findViewById(
                R.id.listview_category);
        listView.setAdapter(categoriesAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Access the row position here to get the correct data item
                Category category = categoriesAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.clear();
                bundle.putString("categoryPlaceID", String.valueOf(category.getPlaceId()));
                bundle.putString("Keyword",keyword);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                CategoryDetailsFragment categoryDetailsFragment = new CategoryDetailsFragment();
                categoryDetailsFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.content_frame, categoryDetailsFragment);
                fragmentTransaction.commit();
            }
        });

        return rootview;
    }

    public class FetchCategoryTask extends AsyncTask<String, Void, List<Category>> {
        private final String LOG_TAG = CategoryFragment.FetchCategoryTask.class.getSimpleName();

        private List<Category> getCategoryDataFromJson(String categoryJsonStr)
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

            JSONObject categoryJson = new JSONObject(categoryJsonStr);
            JSONArray categoriesrArray = categoryJson.getJSONArray(OWM_RESULTS);

            List<Category> resultStrs = new ArrayList<>();
            for (int i = 0; i < categoriesrArray.length(); i++) {
                String name;
                Boolean openNow;
                Long rating;
                String vicinity;
                String placeID;
                String photoReference = "";

                // Get the JSON object representing the  category
                JSONObject category = categoriesrArray.getJSONObject(i);

                name = category.getString(OWM_NAME);
                vicinity = category.getString(OWM_VICINITY);
                placeID = category.getString(OWM_PLACEID);

                if (category.has(OWM_RATING)) {
                    rating = category.getLong(OWM_RATING);
                } else {
                    rating = null;
                }

                if (category.has(OWN_OPENINGHOURS)) {
                    JSONObject openingHoursObject = category.getJSONObject(OWN_OPENINGHOURS);
                    openNow = openingHoursObject.getBoolean(OWN_OPENNOW);
                } else {
                    openNow = null;
                }

                if (category.has(OWN_PHOTOS)) {
                    JSONArray categoriesrPhotosArray = category.getJSONArray(OWN_PHOTOS);
                    JSONObject categoryPhotosObj = categoriesrPhotosArray.getJSONObject(0);
                    photoReference = categoryPhotosObj.getString(OWM_PHOTOREFERENCE);
                } else {
                    photoReference = "default";
                }

                resultStrs.add(new Category(placeID, name, rating, openNow, vicinity, photoReference));

            }
          /*  for (Category s : resultStrs) { //for testing porpuse
               Log.v(LOG_TAG, "Category entry: " + s.toString());
            }*/

            return resultStrs;

        }

        @Override
        protected List<Category> doInBackground(String... params) {
            // If there's no location , there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String categoriesJsonStr = null;

           // String location = "50.931348,5.343312";
            String radius = "2000";
         //   String KEYWORD_TAG = "convenience_store";

            try {
                // Construct the URL for the Google places API query
                // Possible parameters are available at API page, at
                // https://developers.google.com/places/web-service/search
                final String CATEGORY_BASE_URL =
                        "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
                final String LOCATION_PARAM = "location";
                final String RADIUS_PARAM = "radius";
                final String KEYWORD_PARAM = "keyword";
                final String KEY_PARAM = "key";

                Uri builtUri = Uri.parse(CATEGORY_BASE_URL).buildUpon()
                        .appendQueryParameter(LOCATION_PARAM, params[0])
                        .appendQueryParameter(RADIUS_PARAM, radius)
                        .appendQueryParameter(KEYWORD_PARAM, params[1])
                        .appendQueryParameter(KEY_PARAM, BuildConfig.GOOGLE_PLACES_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

             //   Log.v(LOG_TAG, "Built URI CategoryFragment " + builtUri.toString());
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
                    categoriesJsonStr = null;
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
                    categoriesJsonStr = null;
                    return null;
                }
                categoriesJsonStr = buffer.toString();
              //    Log.v(LOG_TAG, "Category JSON String " + categoriesJsonStr); //Debugging purpose

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the category data, there's no point in attempting
                // to parse it.
                categoriesJsonStr = null;
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
                return getCategoryDataFromJson(categoriesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the category.
            return null;
        }

        @Override
        protected void onPostExecute(List<Category> result) {
            if (result != null) {
                categoriesAdapter.clear();
                for (Category categoryStr : result) {
                    categoriesAdapter.add(categoryStr);
                }
            }
        }
    }
}
