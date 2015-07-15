package com.streameus.android.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.streameus.android.R;
import com.streameus.android.dataProvider.RESTClient;
import com.streameus.android.model.Conference;
import com.streameus.android.model.User;
import com.streameus.android.utils.CircleTransform;
import com.streameus.android.utils.Dates;

import java.util.HashMap;

public class ConferenceAproposFragment extends Fragment {
    private ListView listView;
    private int UserId;
    public static String INTENT_PARAMETER_USERID = "UserToDisplayId";
    public static String PARAMETER_CONFERENCE = "data_to_display";
    public static String PARAMETER_CONFERENCIER = "PARAMETER_CONFERENCIER";
    private HashMap<String, Integer> apiKeyToStringIdDictionary = new HashMap<String, Integer>();

    private TextView descriptionTextView;
    private TextView categoryTextView;
    private ImageView auteurPicture;
    private TextView auteurNameTextView;
    private TextView auteurJob;
    private View auteurInfosWrapper;
    private TextView dateTextView;

    public static Fragment createFragment(String conference, String conferencier) {
        Fragment fragment = new ConferenceAproposFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMETER_CONFERENCE, conference);
        bundle.putString(PARAMETER_CONFERENCIER, conferencier);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_conference_apropos,
                container, false);
        descriptionTextView = (TextView) rootView.findViewById(R.id.conference_descritpion);
        categoryTextView = (TextView) rootView.findViewById(R.id.conference_category);
        auteurPicture = (ImageView) rootView.findViewById(R.id.imageView1);
        auteurNameTextView = (TextView) rootView.findViewById(R.id.textView1);
//        auteurJob = (TextView) rootView.findViewById(R.id.conference_auteur_job);
        auteurInfosWrapper = rootView.findViewById(R.id.contact_listitem);
        dateTextView = (TextView) rootView.findViewById(R.id.conference_date);

        Gson g = new Gson();
        final Conference conference = g.fromJson(getArguments().getString(PARAMETER_CONFERENCE), Conference.class);
        final User conferencier = g.fromJson(getArguments().getString(PARAMETER_CONFERENCIER), User.class);

        descriptionTextView.setText(conference.getDescription());
        categoryTextView.setText(conference.getCategory().getName());
        categoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(ConferencesListFragment.createFragment(
                        ConferencesListFragment.ConferenceListType.CATEGORIE,
                        conference.getCategory().getId()));

            }
        });
//        auteurJob.setText(conferencier.getProfession());
        auteurNameTextView.setText(conferencier.getPseudo());
        auteurInfosWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).loadFragment(UserFragment.createFragment(conferencier.getId()));
            }
        });
        Picasso.with(((MainActivity) getActivity())).load(RESTClient.API_URL + "/Picture/User/" + conferencier.getId()).transform(new CircleTransform())
                .placeholder(R.drawable.default_avatar)
                .into(auteurPicture);

        dateTextView.setText(Dates.parse3339ToReadable(conference.getTime()));

        return rootView;
    }

}