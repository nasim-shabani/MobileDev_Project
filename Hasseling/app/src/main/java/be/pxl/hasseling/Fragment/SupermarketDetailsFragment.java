package be.pxl.hasseling.Fragment;


import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
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

import be.pxl.hasseling.BuildConfig;
import be.pxl.hasseling.R;
import be.pxl.hasseling.categories.Supermarket;

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
        String id = bundle.getString("supermarketPlaceID");

        SupermarketDetailsFragment.FetchSupermarketDetailTask supermarketDetailTask = new SupermarketDetailsFragment.FetchSupermarketDetailTask();
        supermarketDetailTask.execute(id);
       Button btn_back= (Button) rootView.findViewById(R.id.backCategory_btn);
        final Fragment supermarketFragment= new SupermarketFragment();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment fr= new fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                transaction.replace(R.id.content_frame, supermarketFragment);
                transaction.commitAllowingStateLoss();
            }
        });


        return rootView;
    }

    public class FetchSupermarketDetailTask extends AsyncTask<String, Void, Supermarket> {
        private final String LOG_TAG = SupermarketDetailsFragment.FetchSupermarketDetailTask.class.getSimpleName();

        /**
         * Take the String representing the complete supermarkets in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private Supermarket getSupermarketDataFromJson(String supermarketJsonStr)
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

            Supermarket supermarketObj = new Supermarket();
            String formatted_address;
            String formatted_phone_number;
            String international_phone_number;
            String name;
            Boolean openNow;
            String weekday_text = null;
            String photo_reference;
            Long rating;
            String url, website;

             formatted_address = supermarketJsonResult.has(OWM_ADDRESS) ? supermarketJsonResult.getString(OWM_ADDRESS) : "No address";
            formatted_phone_number= supermarketJsonResult.has(OWM_FORMPHONE) ? supermarketJsonResult.getString(OWM_FORMPHONE): "No Phonenumber";
            international_phone_number= supermarketJsonResult.has(OWM_INTPHONE) ? supermarketJsonResult.getString(OWM_INTPHONE): "No International Phonenumber";
            name =  supermarketJsonResult.has(OWM_NAME) ? supermarketJsonResult.getString(OWM_NAME): "No Name available";

            if (supermarketJsonResult.has(OWN_OPENINGHOURS)) {
                JSONObject openingHoursObject = supermarketJsonResult.getJSONObject(OWN_OPENINGHOURS);
                openNow = openingHoursObject.getBoolean(OWN_OPENNOW);
                if(openingHoursObject.has(OWN_WEEKDAY)){
                    JSONArray supermarketsrWeekdayArray = openingHoursObject.getJSONArray(OWN_WEEKDAY);
                    weekday_text = "";
                    for (int i=0; i < supermarketsrWeekdayArray.length(); i++) {

                        weekday_text += supermarketsrWeekdayArray.getString(i) + "\n";
                    }
                }
            } else {
                openNow = null;
                weekday_text = "UNKOWN";
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

            url =supermarketJsonResult.has(OWM_URL) ? supermarketJsonResult.getString(OWM_URL): "No URL available";
            website = supermarketJsonResult.has(OWM_WEBSITE) ?supermarketJsonResult.getString(OWM_WEBSITE): "No Website available";


            supermarketObj.setAddress(formatted_address);
            supermarketObj.setFormatted_phone_number(formatted_phone_number);
            supermarketObj.setInternational_phone_number(international_phone_number);
            supermarketObj.setName(name);
            supermarketObj.setOpenNow(openNow);
            supermarketObj.setWeekday_text(weekday_text);
            supermarketObj.setPhotoReference(photo_reference);
            supermarketObj.setRating(rating);
            supermarketObj.setUrl(url);
            supermarketObj.setWebsite(website);

     //    Log.v(LOG_TAG, supermarketObj.toString());

            return supermarketObj;

        }

        @Override
        protected Supermarket doInBackground(String... params) {
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

          //Log.v(LOG_TAG, "Built URI " + builtUri.toString());
           //Log.v(LOG_TAG, "Forecast string: " + supermarketsJsonStr);
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
        protected void onPostExecute(Supermarket result) {

            //TEST OP WAARDE NULL - EXCEPTION
            TextView formatted_address  = (TextView) rootView.findViewById(R.id.address_text);
            formatted_address.setText(result.getAddress());

            TextView formatted_phone_number   = (TextView) rootView.findViewById(R.id.phone_text);
            formatted_phone_number.setText(result.getFormatted_phone_number());

            TextView international_phone_number    = (TextView) rootView.findViewById(R.id.internationalPhone_text);
            international_phone_number.setText(result.getInternational_phone_number());

            TextView name_text = (TextView) rootView.findViewById(R.id.name_text);
            name_text.setText(result.getName());

            TextView weekday_text = (TextView) rootView.findViewById(R.id.weekdays_text );
            weekday_text.setText(result.getWeekday_text());

            ImageView photo_reference = (ImageView)rootView.findViewById(R.id.photo_view );
            if(result.getPhotoReference().equals("default")){
                Picasso.with(getContext()).load("https://png.icons8.com/ingredients/color/50/000000").resize(250,250).into(photo_reference);
            }else{
                Picasso.with(getContext()).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + result.getPhotoReference() + "&key=" + BuildConfig.GOOGLE_PLACES_API_KEY).resize(250,250).into(photo_reference);
            }

            RatingBar ratingBar_stars = (RatingBar) rootView.findViewById(R.id.details_ratingbar);
            TextView ratingView = (TextView) rootView.findViewById(R.id.ratingDetail_text );

            if(result.getRating() == null){
                ratingBar_stars.setVisibility(ratingBar_stars.INVISIBLE);
                ratingView.setVisibility(ratingBar_stars.VISIBLE);
                ratingView.setText("No rating");
            }else {
                ratingBar_stars.setVisibility(ratingBar_stars.VISIBLE);
                ratingView.setVisibility(ratingBar_stars.INVISIBLE);

  /*              Drawable drawable = ratingBar_stars.getProgressDrawable();
                drawable.setColorFilter(Color.parseColor("#FF7F7F"), PorterDuff.Mode.SRC_ATOP);*/
                ratingBar_stars.setRating((float) result.getRating());
            }


            Switch openNowSwitch = (Switch) rootView.findViewById(R.id.openNow_switch);
            openNowSwitch.setClickable(false);
            openNowSwitch.setText(result.formatOpen());

            if(result.getOpenNow() == true){
                    openNowSwitch.setChecked(false);
                    openNowSwitch.setTextColor(Color.parseColor("green"));
                openNowSwitch.setBackgroundColor(Color.parseColor("green"));
            }else{
                openNowSwitch.setChecked(true);
                openNowSwitch.setTextColor(Color.parseColor("red"));
                openNowSwitch.setBackgroundColor(Color.parseColor("red"));

            }

            TextView url = (TextView) rootView.findViewById(R.id.mapsUrl_text  );
            url.setText(result.getUrl());

            TextView website  = (TextView) rootView.findViewById(R.id.website_text  );
            website.setText(result.getWebsite());
        }
    }

}