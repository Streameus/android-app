package com.streameus.android.gui;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.streameus.android.R;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.GetConferencesCategories;
import com.streameus.android.bus.ReceiveConferenceCategories;
import com.streameus.android.dataProvider.RESTClient;
import com.streameus.android.model.ConferenceCategorie;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class CategoriesListFragment extends Fragment {


    private View rootview;
    private List<ConferenceCategorie> conferenceCategorieList = new ArrayList<ConferenceCategorie>();
    private GridView gridview;
    public CategoriesListFragment() {
        // Required empty public constructor
    }
    private String TAG = "CategoriesListFragment";

    public static Fragment createFragment() {
        return new CategoriesListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview =  inflater.inflate(R.layout.fragment_categories_list, container, false);

        gridview = (GridView) rootview.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Fragment fragment = ConferencesListFragment.createFragment(ConferencesListFragment.ConferenceListType.CATEGORIE, (int) id);
                ((MainActivity) getActivity()).loadFragment(fragment);
            }
        });

        BusProvider.get().register(this);
        BusProvider.get().post(new GetConferencesCategories());

        return rootview;
    }

    @Subscribe
    public void onReceiveConferenceCategories(ReceiveConferenceCategories e) {
        if (e.getConferenceCategorieList() == null) {
            Toast.makeText(getActivity(), "An error happened while getting categories list.", Toast.LENGTH_SHORT).show();
        } else {
            conferenceCategorieList = e.getConferenceCategorieList();
            ((ImageAdapter) gridview.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.get().unregister(this);

    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public class ViewHolder {
            public ImageView thumbnailImageView;
            public TextView titleTextView;
        }


        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return conferenceCategorieList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return conferenceCategorieList.get(position).getId();
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {  // if it's not recycled, initialize some attributes


                // Inflate the custom row layout from your XML.
                convertView = getActivity().getLayoutInflater().inflate(R.layout.category_grid_cell, null);

                // create a new "Holder" with subviews
                holder = new ViewHolder();
                holder.thumbnailImageView = (ImageView) convertView
                        .findViewById(R.id.category_picture);
                holder.titleTextView = (TextView) convertView
                        .findViewById(R.id.category_name);

                // hang onto this holder for future recyclage
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Log.d(TAG, "on demande l'image pour la categorie #" + conferenceCategorieList.get(position).getId());
            Picasso.with(mContext).load(RESTClient.API_URL + "/Picture/Category/" + conferenceCategorieList.get(position).getId())
                    .placeholder(R.drawable.default_avatar)
                    .into(holder.thumbnailImageView);
            holder.titleTextView.setText(conferenceCategorieList.get(position).getName());
            return convertView;
        }


    }


}
