package be.pxl.hasseling.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.pxl.hasseling.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupermarketDetailsFragment extends Fragment {


    public SupermarketDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View roorView = inflater.inflate(R.layout.fragment_supermarket_details, container, false);

        //ADDED bY NASIM
        Bundle bundle = getArguments();
        String txt = bundle.getString("SampleTxt");
        TextView sampleText = (TextView) roorView.findViewById(R.id.SampleTxt);
        sampleText.setText(txt);

        return roorView;
    }

}
