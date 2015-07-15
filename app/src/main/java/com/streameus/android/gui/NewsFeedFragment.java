package com.streameus.android.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.paging.listview.PagingListView;
import com.squareup.otto.Subscribe;
import com.streameus.android.R;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.GetNewsFeedEvent;
import com.streameus.android.bus.ReceiveNewsFeedEvent;
import com.streameus.android.model.EventInfo;
import com.streameus.android.model.EventItem;
import com.streameus.android.utils.NewsFeedAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedFragment extends ListFragment {
    private static final String TAG = "FluxDActuFragment";
    private MainActivity ma;
    static public String INTENT_PARAMETER_USER_TO_DISPLAY = "UserToDisplayId";
    private NewsFeedAdapter fluxDActuAdapter;
    private ViewGroup rootView;
    private int userId = 0;
    private View loadingView;
    List<EventItem> eventList = new ArrayList<EventItem>();
    PagingListView listView;


    final long ELEMENT_TO_BE_LOAD = 20;
    long element_loaded = 0;
    private boolean stopLoading = false;


    /**
     * @param userID 0 pour le flux d'actu, sinon l'id de l'user dont on veut les event
     * @return
     */
    public static Fragment createFragment(int userID) {
        Fragment fragment = new NewsFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(NewsFeedFragment.INTENT_PARAMETER_USER_TO_DISPLAY, userID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userId = getArguments().getInt(INTENT_PARAMETER_USER_TO_DISPLAY);
        ma = (MainActivity) getActivity();
        rootView = (ViewGroup) inflater.inflate(R.layout.paginable_listview, container, false);
        loadingView = rootView.findViewById(R.id.loading);
        listView = (PagingListView) rootView.findViewById(android.R.id.list);

        fluxDActuAdapter = new NewsFeedAdapter(ma, ma.getLayoutInflater(), eventList);
        listView.setAdapter(fluxDActuAdapter);
        listView.setHasMoreItems(true);
        listView.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (!stopLoading) {
                    BusProvider.get().post(new GetNewsFeedEvent(userId, ELEMENT_TO_BE_LOAD, eventList.size()));
                } else {
                    listView.onFinishLoading(false, null);
                }

            }
        });
        if (userId <= 0) {
            ma.setTitle(getString(R.string.fluxdactu));
        }
        BusProvider.get().register(this);
        BusProvider.get().post(new GetNewsFeedEvent(userId, ELEMENT_TO_BE_LOAD, eventList.size()));
        fluxDActuAdapter.notifyDataSetChanged();
        return rootView;
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
    public void OnReceiveFluxDActuEvent(ReceiveNewsFeedEvent e) {
        Log.d(TAG, "on receive flux d'actu event");

        loadingView.setVisibility(View.GONE);
        if (e.getErrorMSG()!= null) {
            Toast.makeText(getActivity(), e.getErrorMSG(), Toast.LENGTH_SHORT).show();
        } else {
            if (e.getEventItemList().size() < ELEMENT_TO_BE_LOAD) {
                stopLoading = true;
            }
            eventList.addAll(e.getEventItemList());
            listView.onFinishLoading(true, e.getEventItemList());
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        int infoImportant = -1;

        if (eventList != null) {
            EventItem event = eventList.get(position);
            switch ((int)event.getType()) {

                case 9:
                    infoImportant = 1;
                    break;
                case 11:
                    infoImportant = 1;
                    break;
                case 13:
                    infoImportant = 1;
                    break;
                case 14:
                    infoImportant = 1;
                    break;

            }
            if (infoImportant != -1) {
                List<EventInfo> infoList = event.getItems();
                for (EventInfo e: infoList) {
                    if (e.getPos() == infoImportant) {
                        switch (e.getTargetType()) {
                            case EventInfo.TYPE_USER:
                                ma.loadFragment(UserFragment.createFragment(e.getTargetId()));
                                break;
                            case EventInfo.TYPE_CONFERENCE:
                                ma.loadFragment(ConferenceFragment.createFragment(e.getTargetId()));
                                break;
                            default:
                                Toast.makeText(ma, "Not implemented yet", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        }
    }



}