package be.pxl.hasseling.Fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.net.MalformedURLException;
import java.net.URL;

import be.pxl.hasseling.BuildConfig;
import be.pxl.hasseling.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment{

    public static TextView txtV_data;
    public static ImageView imgV_weatherIcon;

    //JSON Node Names
    private static final String TAG_DATA = "data";
    private static final String TAG_CONDITION = "current_condition";
    private static final String TAG_TIME = "observation_time";
    private static final String TAG_TEMP = "temp_C";

    private static final String TAG_IMAGE = "weatherIconUrl";
    private static final String TAG_IMAGE_VALUE = "value";

    private static final String TAG_WEATHER_DESC = "weatherDesc";
    private static final String TAG_WEATHER_DESC_VALUE = "value";



    Button btn_supermarket, btn_restaurant, btn_laundry, btn_drink,
            btn_transport, btn_club, btn_fitness, btn_todo;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        btn_supermarket= (Button) view.findViewById(R.id.btn_supermarket);
        Fragment supermarketFragment= new SupermarketFragment();
        setOnClickButtons(btn_supermarket, supermarketFragment);

        btn_restaurant= (Button) view.findViewById(R.id.btn_restaurant);
        Fragment restaurantFragment= new RestaurantFragment();
        setOnClickButtons(btn_restaurant, restaurantFragment);

        btn_laundry= (Button) view.findViewById(R.id.btn_laundry);
        Fragment laundryFragment= new LaundryFragment();
        setOnClickButtons(btn_laundry, laundryFragment);

        btn_drink= (Button) view.findViewById(R.id.btn_drink);
        Fragment drinkFragment= new DrinkFragment();
        setOnClickButtons(btn_drink, drinkFragment);

        btn_transport= (Button) view.findViewById(R.id.btn_transport);
        Fragment transportFragment= new TransportFragment();
        setOnClickButtons(btn_transport, transportFragment);

        btn_club= (Button) view.findViewById(R.id.btn_club);
        Fragment clubFragment= new ClubFragment();
        setOnClickButtons(btn_club, clubFragment);

        btn_fitness= (Button) view.findViewById(R.id.btn_fitness);
        Fragment fitnessFragment= new FitnessFragment();
        setOnClickButtons(btn_fitness, fitnessFragment);

        btn_todo= (Button) view.findViewById(R.id.btn_todo);
        Fragment todoFragment= new TodoFragment();
        setOnClickButtons(btn_todo, todoFragment);

        txtV_data = (TextView) view.findViewById(R.id.fetcheddata);
        imgV_weatherIcon = (ImageView) view.findViewById(R.id.img_weather);

        fetchData weather = new fetchData();
        weather.execute();

        return view;
    }

    public void setOnClickButtons(Button btn, final Fragment fragment){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment fr= new fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                transaction.replace(R.id.content_frame, fragment);
                transaction.commitAllowingStateLoss();
            }
        });
    }

    public class fetchData extends AsyncTask<Void, Void, Void>{

        String data = "";
        String dataParsed = "";
        String imageParsed = "";
        @Override
        protected Void doInBackground(Void... voids) {

            String location = "Hasselt,belgium";
            String date = "today";
            String tp = "24"; // Specifies the weather forecast time interval in hours.
                              // Options are: 1 hour, 3 hourly, 6 hourly, 12 hourly (day/night) or
                              // 24 hourly (day average).
            String format = "json";

            try {

                final String WEATHER_BASE_URL =
                        "https://api.worldweatheronline.com/premium/v1/weather.ashx?";
                final String KEY_PARAM = "key";
                final String LOCATION_PARAM = "q";
                final String DATE_PARAM = "date";
                final String TP_PARAM = "tp";
                final String FORMAT_PARAM = "format";

                Uri builtUri = Uri.parse(WEATHER_BASE_URL).buildUpon()
                        .appendQueryParameter(KEY_PARAM, BuildConfig.WEATHER_API_KEY)
                        .appendQueryParameter(LOCATION_PARAM, location)
                        .appendQueryParameter(DATE_PARAM, date)
                        .appendQueryParameter(DATE_PARAM, tp)
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .build();
                URL url = new URL(builtUri.toString());

                //URL url = new URL("https://api.worldweatheronline.com/premium/v1/weather.ashx?key=97680c0ec3284afa8cd150311170211&q=Hasselt,belgium&date=today&tp=24&format=json");
                //URL url = new URL("https://api.myjson.com/bins/1gomoj");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                data = buffer.toString();

                //String time;
                String temp;
                String image;
                String imageDesc;


                JSONObject jsonObject = new JSONObject(data);

                JSONObject dataObj = jsonObject.getJSONObject(TAG_DATA);

                JSONArray currentConditionArr = dataObj.getJSONArray(TAG_CONDITION);
                JSONObject currentConditionObj = currentConditionArr.getJSONObject(0);

                JSONArray currentConditionImageArr = currentConditionObj.getJSONArray(TAG_IMAGE);
                JSONObject currentConditionImageObj = currentConditionImageArr.getJSONObject(0);

                JSONArray currentConditionImageDescArr = currentConditionObj.getJSONArray(TAG_WEATHER_DESC);
                JSONObject currentConditionImageDescObj = currentConditionImageDescArr.getJSONObject(0);

                //time = currentConditionObj.getString(TAG_TIME);
                temp = currentConditionObj.getString(TAG_TEMP);
                image = currentConditionImageObj.getString(TAG_IMAGE_VALUE);
                imageDesc = currentConditionImageDescObj.getString(TAG_WEATHER_DESC_VALUE);

                /*dataParsed = "Now is "+time +" o'clock"+ "\n" +
                            "temprature is : "+temp+"°c"+ "\n" ;*/

                dataParsed = "temprature is "+temp+"°c"+ "\n"+
                            "Today is "+imageDesc;

                imageParsed = image;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            HomeFragment.txtV_data.setText(this.dataParsed);
            Picasso.with(getContext()).load(this.imageParsed).resize(94,94).into(imgV_weatherIcon);
        }
    }

}
