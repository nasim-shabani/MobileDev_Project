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

    private String supermarketStr;

    public SupermarketDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_supermarket_details, container, false);
   /*     Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            supermarketStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.supermarketdetail_text))
                    .setText(supermarketStr);
        }*/
        //ADDED bY NASIM
        Bundle bundle = getArguments();
        String txt = bundle.getString("supermarketdetail_text");
        TextView sampleText = (TextView) rootView.findViewById(R.id.supermarketdetail_text);
        sampleText.setText(txt);

        return rootView;
    }

}
