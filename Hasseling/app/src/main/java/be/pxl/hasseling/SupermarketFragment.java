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
import android.widget.TextView;

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

        myView = inflater.inflate(R.layout.supermarket_layout, container, false);
        return myView;
    }


    
}
