package com.streameus.android.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.streameus.android.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserAproposFragment extends Fragment {
    private ListView listView;
    private int UserId;
    private MainActivity ma;
    public static String INTENT_PARAMETER_USERID = "UserToDisplayId";
    public static String PARAMETER_DATA_ARRAY = "data_to_display";
    private HashMap<String, Integer> apiKeyToStringIdDictionary = new HashMap<String, Integer>();

    public static Fragment creatFragment(HashMap<String, String> values) {
        Fragment fragment = new UserAproposFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString(PARAMETER_DATA_ARRAY, gson.toJson(values));
        fragment.setArguments(bundle);
        return fragment;
    }

    public String matchKeyToStringId(String s) {
        if (apiKeyToStringIdDictionary.containsKey(s)) {
            return ma.getString(apiKeyToStringIdDictionary.get(s));
            }
        return s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        apiKeyToStringIdDictionary.put("Pseudo", R.string.pseudo);
        apiKeyToStringIdDictionary.put("Email", R.string.email);
        apiKeyToStringIdDictionary.put("FirstName", R.string.firstName);
        apiKeyToStringIdDictionary.put("LastName", R.string.lastName);
        apiKeyToStringIdDictionary.put("Birthday", R.string.birthday);
        apiKeyToStringIdDictionary.put("Phone", R.string.phone);
        apiKeyToStringIdDictionary.put("Address", R.string.address);
        apiKeyToStringIdDictionary.put("City", R.string.city);
        apiKeyToStringIdDictionary.put("Country", R.string.country);
        apiKeyToStringIdDictionary.put("Website", R.string.website);
        apiKeyToStringIdDictionary.put("Profession", R.string.profession);
        apiKeyToStringIdDictionary.put("Description", R.string.description);
        View rootView = inflater.inflate(R.layout.fragment_profile_apropos,
                container, false);
        ma = (MainActivity) getActivity();

        Gson gson = new Gson();
        HashMap<String, String> prop;
        Type collectionType = new TypeToken<HashMap<String, String>>(){}.getType();
        prop = gson.fromJson(getArguments().getString(PARAMETER_DATA_ARRAY), collectionType);

        listView = (ListView) rootView.findViewById(R.id.listView1);
        List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> element;
        for (String key : prop.keySet()) {
            element = new HashMap<String, String>();
            element.put("infoName", matchKeyToStringId(key));
            element.put("infoValue", prop.get(key));
            liste.add(element);
        }

        ListAdapter adapter = new SimpleAdapter(ma, liste,
                R.layout.listitem_profil_apropos,
                new String[] {"infoName", "infoValue"},
                new int[] {R.id.infoName, R.id.infoValue});
        listView.setAdapter(adapter);

        return rootView;
    }

}