package com.streameus.android.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.streameus.android.R;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.GetUserListEvent;
import com.streameus.android.bus.ReceiveUserList;
import com.streameus.android.model.User;
import com.streameus.android.utils.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

public class UsersListFragment extends ListFragment {
    private MainActivity ma;
    private static  String INTENT_PARAMETER_FRAGMENT_TITLE = "INTENT_PARAMETER_FRAGMENT_TITLE";
    private static final String INTENT_PARAM_LIST_TYPE = "PARAM_LIST_TYPE";
    private static final String INTENT_PARAM_VALUE = "INTENT_PARAM_VALUE";
    private static final String TAG = "MesContactsFragments";

    ContactAdapter contactAdapter;
    ViewGroup rootView;


    int paramValue = 0;
    private Boolean firstLaunch = true;
    private View loadingView;
    private RelativeLayout mErrorView;

    private UserListType listType;
    List<User> userList = new ArrayList<User>();


    public enum UserListType {
        USER_FOLLOWING,
        USER_FOLLOWER,
        ALL_USERS,
        RECOMMANDED_FRIEND,
        CONFERENCE_ATTENDENT,
        SEARCH
    }


    /**
     *
     * @param userID  if userID == 0, load all the users of streameus
     * @param showActionBarButton
     * @param title actionBar title
     * @return
     */
    public static Fragment createFragment(UserListType type, int param, String title) {
        Fragment fragment = new UsersListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_PARAM_LIST_TYPE, type);
        bundle.putSerializable(INTENT_PARAM_VALUE, param);
        bundle.putString(UsersListFragment.INTENT_PARAMETER_FRAGMENT_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ma = (MainActivity) getActivity();
        rootView = (ViewGroup) inflater.inflate(R.layout.simple_listview, container, false);

        listType = (UserListType) getArguments().getSerializable(INTENT_PARAM_LIST_TYPE);
        if (listType == UserListType.USER_FOLLOWING
                || listType == UserListType.USER_FOLLOWER
                || listType == UserListType.CONFERENCE_ATTENDENT) {
            paramValue = getArguments().getInt(INTENT_PARAM_VALUE);
        }
        String title = getArguments().getString(INTENT_PARAMETER_FRAGMENT_TITLE);
        if (title != null) {
            ma.setTitle(title);
        }
        loadingView = rootView.findViewById(R.id.loading);
        contactAdapter = new ContactAdapter(ma, ma.getLayoutInflater(), userList);
        setListAdapter(contactAdapter);
        mErrorView = (RelativeLayout) rootView.findViewById(R.id.error_view);

        BusProvider.get().register(this);
        if (listType != UserListType.SEARCH) {
            BusProvider.get().post(new GetUserListEvent(listType, paramValue));
        } else {
            loadingView.setVisibility(View.GONE);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.get().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.get().unregister(this);
    }

    @Subscribe
    public void OnReceiveUserList(ReceiveUserList e) {
        if (e.getType() == listType) {

            loadingView.setVisibility(View.GONE);
            if (e.getErrorMSG() != null) {
                mErrorView.setVisibility(View.VISIBLE);
                ((TextView) mErrorView.getChildAt(1)).setText(e.getErrorMSG());
            } else {
                mErrorView.setVisibility(View.GONE);
                List<User> users = e.getUserList();
                if (users == null) {
                    Toast.makeText(ma, "unable to retrieve the list of follower", Toast.LENGTH_SHORT).show();
                } else {
                    userList.clear();
                    userList.addAll(users);
                    contactAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Fragment fragment =  UserFragment.createFragment((int) id);
        ma.loadFragment(fragment);
    }


    public ViewGroup getRootView() {
        return rootView;
    }
}