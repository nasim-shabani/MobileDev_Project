package be.pxl.hasseling.Fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import be.pxl.hasseling.categories.Category;


public class CategoryDetailsFragment extends Fragment {
    private String categoryStr;
    private View rootView;
    String KEYWORD_TAG;
    Bundle bundle;
    public CategoryDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category_details, container, false);

        //ADDED bY NASIM
         bundle = getArguments();
        String id = bundle.getString("categoryPlaceID");
        KEYWORD_TAG = bundle.getString("Keyword");

        CategoryDetailsFragment.FetchCategoryDetailTask categoryDetailTask = new CategoryDetailsFragment.FetchCategoryDetailTask();
        categoryDetailTask.execute(id);

        Button btn_back= (Button) rootView.findViewById(R.id.backCategory_btn);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment categoryFragment= new CategoryFragment();
                Bundle bundle = new Bundle();//DANIE
                bundle.putString("Keyword", KEYWORD_TAG);//DANIE
                categoryFragment.setArguments(bundle);//DANIE
                //Fragment fr= new fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                transaction.replace(R.id.content_frame, categoryFragment);
                transaction.commitAllowingStateLoss();
            }
        });


        return rootView;
    }

    public class FetchCategoryDetailTask extends AsyncTask<String, Void, Category> {
        private final String LOG_TAG = CategoryDetailsFragment.FetchCategoryDetailTask.class.getSimpleName();

        /**
         * Take the String representing the complete category in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private Category getCategoryDataFromJson(String categoryJsonStr)
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

            JSONObject categoryJson = new JSONObject(categoryJsonStr);
            JSONObject categoryJsonResult = categoryJson.getJSONObject(OWM_RESULT);

            Category categoryObj = new Category();
            String formatted_address;
            String formatted_phone_number;
            String international_phone_number;
            String name;
            Boolean openNow;
            String weekday_text = null;
            String photo_reference;
            Long rating;
            String url, website;

            formatted_address = categoryJsonResult.has(OWM_ADDRESS) ? categoryJsonResult.getString(OWM_ADDRESS) : "No address";
            formatted_phone_number= categoryJsonResult.has(OWM_FORMPHONE) ? categoryJsonResult.getString(OWM_FORMPHONE): "No Phonenumber";
            international_phone_number= categoryJsonResult.has(OWM_INTPHONE) ? categoryJsonResult.getString(OWM_INTPHONE): "No International Phonenumber";
            name =  categoryJsonResult.has(OWM_NAME) ? categoryJsonResult.getString(OWM_NAME): "No Name available";

            if (categoryJsonResult.has(OWN_OPENINGHOURS)) {
                JSONObject openingHoursObject = categoryJsonResult.getJSONObject(OWN_OPENINGHOURS);
                openNow = openingHoursObject.getBoolean(OWN_OPENNOW);
                if(openingHoursObject.has(OWN_WEEKDAY)){
                    JSONArray categorysrWeekdayArray = openingHoursObject.getJSONArray(OWN_WEEKDAY);
                    weekday_text = "\n";
                    for (int i=0; i < categorysrWeekdayArray.length(); i++) {

                        weekday_text += categorysrWeekdayArray.getString(i) + "\n";
                    }
                }
            } else {
                openNow = null;
                weekday_text = "No Openings hours found. "+ "\n" + "Sorry :(";
            }

            if (categoryJsonResult.has(OWN_PHOTOS)) {
                JSONArray categorysrPhotosArray = categoryJsonResult.getJSONArray(OWN_PHOTOS);
                JSONObject categoryPhotosObj = categorysrPhotosArray.getJSONObject(0);
                photo_reference = categoryPhotosObj.getString(OWM_PHOTOREFERENCE);
            } else {
                photo_reference = getString(R.string.defaultpic);
            }

            if (categoryJsonResult.has(OWM_RATING)) {
                rating = categoryJsonResult.getLong(OWM_RATING);
            } else {
                rating = null;
            }

            url =categoryJsonResult.has(OWM_URL) ? categoryJsonResult.getString(OWM_URL): "No route available";
            website = categoryJsonResult.has(OWM_WEBSITE) ?categoryJsonResult.getString(OWM_WEBSITE): "No website available";


            categoryObj.setAddress(formatted_address);
            categoryObj.setFormatted_phone_number(formatted_phone_number);
            categoryObj.setInternational_phone_number(international_phone_number);
            categoryObj.setName(name);
            categoryObj.setOpenNow(openNow);
            categoryObj.setWeekday_text(weekday_text);
            categoryObj.setPhotoReference(photo_reference);
            categoryObj.setRating(rating);
            categoryObj.setUrl(url);
            categoryObj.setWebsite(website);
            categoryObj.setKEYWORD_TAG(KEYWORD_TAG);

            return categoryObj;

        }

        @Override
        protected Category doInBackground(String... params) {
            // If there's no location , there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String categorysJsonStr = null;

            try {
                // Construct the URL for the Google places API query
                // Possible parameters are available at API page, at
                // https://developers.google.com/places/web-service/search
                final String CATEGORY_BASE_URL =
                        "https://maps.googleapis.com/maps/api/place/details/json?";
                final String PLACEID_PARAM = "placeid";
                final String KEY_PARAM = "key";

                Uri builtUri = Uri.parse(CATEGORY_BASE_URL).buildUpon()
                        .appendQueryParameter(PLACEID_PARAM, params[0])
                        .appendQueryParameter(KEY_PARAM, BuildConfig.GOOGLE_PLACES_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    categorysJsonStr = null;
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
                    categorysJsonStr = null;
                    return null;
                }
                categorysJsonStr = buffer.toString();
                //    Log.v(LOG_TAG, "category JSON String " + categorysJsonStr); //Debugging purpose

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the category data, there's no point in attempting
                // to parse it.
                categorysJsonStr = null;
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
                return getCategoryDataFromJson(categorysJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the category.
            return null;
        }

        @Override
        protected void onPostExecute(Category result) {

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
                Picasso.with(getContext()).load(result.getPhotoDefault()).into(photo_reference);
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
                ratingBar_stars.setRating((float) result.getRating());
            }


            Switch openNowSwitch = (Switch) rootView.findViewById(R.id.openNow_switch);
            openNowSwitch.setClickable(false);
            openNowSwitch.setText(result.formatOpen());

            if(result.getOpenNow() != null){
            if(result.getOpenNow() == true){
                openNowSwitch.setChecked(false);
                openNowSwitch.setTextColor(Color.parseColor("green"));
            }else{
                openNowSwitch.setChecked(true);
                openNowSwitch.setTextColor(Color.parseColor("red"));

            }
            }


            TextView url = (TextView) rootView.findViewById(R.id.mapsUrl_text  );
            if(result.getUrl().startsWith("No")){
                url.setText(result.getWebsite());
            }else{
                url.setClickable(true);
                url.setMovementMethod(LinkMovementMethod.getInstance());
                String mapsURL = "<a href='" + result.getUrl() + "'>" + getString(R.string.route) + "</a>";
                url.setText(Html.fromHtml(mapsURL));
            }



            TextView website  = (TextView) rootView.findViewById(R.id.website_text  );
            if(result.getWebsite().startsWith("No")){
                website.setText(result.getWebsite());
            }else{
                website.setClickable(true);
                website.setMovementMethod(LinkMovementMethod.getInstance());
                String websiteURL = "<a href='" + result.getWebsite() + "'>" + getString(R.string.website)+"</a>";
                website.setText(Html.fromHtml(websiteURL));
            }
        }
    }
}
