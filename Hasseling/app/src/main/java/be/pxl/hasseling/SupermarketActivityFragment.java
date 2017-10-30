package be.pxl.hasseling;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SupermarketActivityFragment extends Fragment {

    public SupermarketActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String [] arraySupermarkets = {
                "ALDI - 2km - 20/10", "Spar - 4km - 20/10",
                "LIDL - 1km - 70/00", "Delhaize - 7km - 20/10",
                "Carrefour- 4km - 20/10", "Biologic - 500m - 20/10"};

        List<String> supermarkets = new ArrayList<String>(
                Arrays.asList(arraySupermarkets));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                //current context
                getActivity(),
                //ID of list item layout
                R.layout.list_item_categorie,
                //ID of the textview to populate
                R.id.list_item_categorie_textview,
                //weekForecast data
                supermarkets
        );

        //FrameLayout frameLayout = (FrameLayout) container.findViewById(R.id.frameLayout);
        View rootview = inflater.inflate(R.layout.fragment_supermarket, container);
        ListView listView = (ListView) rootview.findViewById(
                R.id.listview_supermarket);
        listView.setAdapter(arrayAdapter);


       // return inflater.inflate(R.layout.fragment_supermarket, container, false);
        return rootview;
    }



}
