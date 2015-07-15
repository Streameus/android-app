package com.streameus.android.gui;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.streameus.android.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DemoPageFragment extends Fragment {
    View rootView;

    private static String IMAGE_RES_ID = "IMAGE_RES_ID";
    private static String IMAGE_LEGEND = "IMAGE_LEGEND";

    public  static Fragment createFragment(int imageResID, String legend) {
        Fragment f = new DemoPageFragment();
        Bundle b = new Bundle();
        b.putInt(IMAGE_RES_ID, imageResID);
        b.putString(IMAGE_LEGEND, legend);
        f.setArguments(b);
        return f;
    }

    public DemoPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String legend = getArguments().getString(IMAGE_LEGEND);
        int imageID = getArguments().getInt(IMAGE_RES_ID);

        rootView = inflater.inflate(R.layout.fragment_demo_page, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.demoImage);
        imageView.setImageDrawable(getResources().getDrawable(imageID));
        TextView legendTextview = (TextView) rootView.findViewById(R.id.demoImageLegend);
        legendTextview.setText(legend);

        return rootView;
    }


}
