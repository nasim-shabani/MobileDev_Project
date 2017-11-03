package be.pxl.hasseling.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.pxl.hasseling.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment{


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

        return view;
    }

}
