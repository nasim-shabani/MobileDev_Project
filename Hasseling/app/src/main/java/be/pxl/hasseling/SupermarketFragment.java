package be.pxl.hasseling;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nasim on 10/17/2017.
 */

public class SupermarketFragment extends Fragment {

    public SupermarketFragment(){

    }
    
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {
//Negeer dit onderste effe maar laat staan e :p

//        String [] arraySupermarkets = {
//                "ALDI - 2km - 20/10", "Spar - 4km - 20/10",
//                "LIDL - 1km - 70/00", "Delhaize - 7km - 20/10",
//                "Carrefour- 4km - 20/10", "Biologic - 500m - 20/10"};
//
//        List<String> supermarkets = new ArrayList<String>(
//                Arrays.asList(arraySupermarkets)
//        );
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                //current context
//                getActivity(),
//                //ID of list item layout
//                R.layout.activity_supermarket,
//                //ID of the textview to populate
//                R.id.restaurant_item_textview,
//                //weekForecast data
//                arraySupermarkets
//        );
//
//        View rootview = inflater.inflate(R.layout.activity_supermarket, container);
//        ListView listView = (ListView) rootview.findViewById(
//                R.id.restaurantListView);
//        listView.setAdapter(arrayAdapter);
        
       myView = inflater.inflate(R.layout.supermarket_layout, container, false);
        return myView;
        //return rootview;
    }


    
}
