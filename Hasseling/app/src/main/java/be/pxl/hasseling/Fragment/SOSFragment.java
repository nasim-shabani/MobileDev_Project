package be.pxl.hasseling.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.pxl.hasseling.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SOSFragment extends Fragment {


    public SOSFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_sos, container, false);

        // tvMoreInfo has links specified by putting <a> tags in the string
        // resource.  By default these links will appear but not
        // respond to user input.  To make them active, you need to
        // call setMovementMethod() on the TextView object.

        TextView tvMoreInfo = (TextView) rootView.findViewById(R.id.tvMoreInfo);
        tvMoreInfo.setMovementMethod(LinkMovementMethod.getInstance());

        return rootView;
    }

}
