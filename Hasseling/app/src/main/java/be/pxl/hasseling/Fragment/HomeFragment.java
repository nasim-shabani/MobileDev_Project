package be.pxl.hasseling.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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



    Button btn_supermarket, btn_restaurant;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        btn_supermarket= (Button) view.findViewById(R.id.btn_supermarket);
        btn_supermarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new SupermarketFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                transaction.replace(R.id.content_frame, fragment);
                transaction.commitAllowingStateLoss();
            }
        });

        /*
        btn_restaurant= (Button) view.findViewById(R.id.btn_restaurant);
        btn_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new RestaurantFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                transaction.replace(R.id.content_frame, fragment);
                transaction.commitAllowingStateLoss();
            }
        });
        */


        txtV_data = (TextView) view.findViewById(R.id.fetcheddata);
        imgV_weatherIcon = (ImageView) view.findViewById(R.id.img_weather);

        fetchData weather = new fetchData();
        weather.execute();

        return view;
    }


    public class fetchData extends AsyncTask<Void, Void, Void>{

        String data = "";
        String dataParsed = "";
        String imageParsed = "";
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL("https://api.worldweatheronline.com/premium/v1/weather.ashx?key=97680c0ec3284afa8cd150311170211&q=Hasselt,belgium&date=today&tp=24&format=json");
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
