package com.streameus.android.dataProvider;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.otto.Subscribe;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.ChangeConferenceRegisterStatusEvent;
import com.streameus.android.bus.FollowUserEvent;
import com.streameus.android.bus.GetAgendaEvent;
import com.streameus.android.bus.GetConferenceEvent;
import com.streameus.android.bus.GetConferencesCategories;
import com.streameus.android.bus.GetLecturesEvent;
import com.streameus.android.bus.GetLoggedUserEvent;
import com.streameus.android.bus.GetNewsFeedEvent;
import com.streameus.android.bus.GetProfilDataEvent;
import com.streameus.android.bus.GetProviderInfosEvent;
import com.streameus.android.bus.GetResourceEvent;
import com.streameus.android.bus.GetUserListEvent;
import com.streameus.android.bus.OnRegisterCallBack;
import com.streameus.android.bus.ReceiveAgendaEvent;
import com.streameus.android.bus.ReceiveAllLecturesEvent;
import com.streameus.android.bus.ReceiveConferenceCategories;
import com.streameus.android.bus.ReceiveLectureEvent;
import com.streameus.android.bus.ReceiveLoggedUserEvent;
import com.streameus.android.bus.ReceiveNewsFeedEvent;
import com.streameus.android.bus.ReceiveProfilDataEvent;
import com.streameus.android.bus.ReceiveProviderInfoEvent;
import com.streameus.android.bus.ReceiveResourceEvent;
import com.streameus.android.bus.ReceiveUserList;
import com.streameus.android.bus.RegisterEvent;
import com.streameus.android.bus.SearchEvent;
import com.streameus.android.bus.UnfollowUserEvent;
import com.streameus.android.gui.ConferencesListFragment;
import com.streameus.android.gui.UsersListFragment;
import com.streameus.android.model.Conference;
import com.streameus.android.model.ConferenceCategorie;
import com.streameus.android.model.ConferenceSet;
import com.streameus.android.model.EventItem;
import com.streameus.android.model.IConferenceComparator;
import com.streameus.android.model.SearchWrapper;
import com.streameus.android.model.StreameusResources;
import com.streameus.android.model.User;
import com.streameus.android.utils.StreameusPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pol on 08/05/14.
 */
public class DataService {
    public static final String TAG = "DataService";
    public static class NotLoggedInException extends Exception {}

    private static DataService dataService = null;
    private enum ServiceType {
        OFFLINE, ONLINE, NONE, DEBUG
    }
    private static ServiceType serviceType;
    private RESTClient restClient;

    public static DataService get(Context c) throws NotLoggedInException {
        if (serviceType != ServiceType.ONLINE && serviceType != ServiceType.DEBUG) {
            removeCurrentService();

           String token = StreameusPreferences.getToken(c);
            if (token.equals("")) {
                throw new NotLoggedInException();
            } else {
                dataService = new DataService(token);
                serviceType = ServiceType.ONLINE;

            }
        }
        return dataService;
    }

    static public void setDebug() {
        if (dataService != null) {
            BusProvider.get().unregister(dataService);
        }
        dataService = null;
        serviceType = ServiceType.DEBUG;
    }

    static public void removeCurrentService() {
        if (dataService != null) {
            BusProvider.get().unregister(dataService);
        }
        dataService = null;
       if (serviceType != ServiceType.DEBUG) {
           serviceType = ServiceType.NONE;
       }
    }

    public static DataService getNotLogged() {
        if (serviceType != ServiceType.OFFLINE && serviceType != ServiceType.DEBUG) {
            removeCurrentService();
            dataService = new DataService("");
            serviceType = ServiceType.OFFLINE;
        }
        return dataService;
    }

    private DataService(String token) {
        restClient = new RESTClient(token);
        BusProvider.get().register(this);
    }


    @Subscribe
    public void onRegisterEvent(RegisterEvent e) {
        new AsyncTask<String, Void, OnRegisterCallBack>() {

            @Override
            protected OnRegisterCallBack doInBackground(String... strings) {

                String username = strings[0];
                String pseudo = strings[1];
                String password = strings[2];
                String confirmePassword = strings[3];

                RESTClient.RegisterAnswer registerAnswer = null;
                if (!password.equals(confirmePassword)) {
                    return new OnRegisterCallBack("Both password doesn't match");
                } else {

                    try {

                        //return null on success
                        registerAnswer = restClient.register(username, pseudo, password);

                        if (registerAnswer == null) {
                            //success
                            return new OnRegisterCallBack(username, password);
                        } else {
                            return new OnRegisterCallBack(registerAnswer.getErrorMSG());
                        }
                    } catch (Exception error) {
                        // Log.e(TAG, "registerAnswer on s'en fout on a eu une erreur" + new String(error.getResponse().getBody().) + "    " + error.getClass().getName());

                        return new OnRegisterCallBack(error.getMessage());
                    }
                }
            }

            @Override
            protected void onPostExecute(OnRegisterCallBack event) {
                Log.v(TAG, "Before post to login activity:" + event.getUserName() + ", " + event.getPassword() + "Error msg == " + event.getErrorMSG());

                BusProvider.get().post(event);
            }

        }.execute(e.getEmail(), e.getPseudo(), e.getPassword(), e.getComfirmPassword());
    }

    @Subscribe
    public void onGetLoggedUserEvent(GetLoggedUserEvent e) {
        restClient.setToken(e.getToken());
        new AsyncTask<Void, Void, ReceiveLoggedUserEvent>() {

            @Override
            protected ReceiveLoggedUserEvent doInBackground(Void... voids) {
                try {
                    User user = restClient.getLoggedUser();
                    return new ReceiveLoggedUserEvent(user);

                } catch (Exception e) {
                    Log.e(TAG, "Erreur lors de la recuperation du utilisateur courant.");
                    return new ReceiveLoggedUserEvent(e.getMessage() == null ? " no message in exception" : e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(ReceiveLoggedUserEvent user) {
                BusProvider.get().post(user);
            }

        }.execute();
    }

    @Subscribe
    public void onGetProfilDataEvent(GetProfilDataEvent e) {
        final int userID =  e.getID();
        final boolean askFollowing = e.isAskingFriendshipRelation();
        new AsyncTask<Integer, Void, ReceiveProfilDataEvent>() {

            @Override
            protected ReceiveProfilDataEvent doInBackground(Integer... ints) {

                try {
                    User user = restClient.getUser(ints[0]);
                    if (askFollowing) {
                        boolean isFollowing = restClient.amIFollowing(userID);
                        return new ReceiveProfilDataEvent(user, true, isFollowing);
                    } else {
                        return new ReceiveProfilDataEvent(user, false, false);
                    }
                } catch (Exception e) {
                    return new ReceiveProfilDataEvent(e.getMessage());
                }

            }

            @Override
            protected void onPostExecute(ReceiveProfilDataEvent answer) {
                BusProvider.get().post(answer);
            }

        }.execute(userID);

    }

    @Subscribe
    public void onGetUserListEvent(final GetUserListEvent e) {
        new AsyncTask<GetUserListEvent, Void, ReceiveUserList>() {

            @Override
            protected ReceiveUserList doInBackground(GetUserListEvent... events) {
                GetUserListEvent event = events[0];

                try {
                    List<User> users = null;

                    switch (event.getType()) {
                        case ALL_USERS:
                            users = restClient.getAllUsers();
                            break;
                        case RECOMMANDED_FRIEND:
                            users = restClient.getRecommendedUsers();
                            break;
                        case USER_FOLLOWING:
                            users = restClient.getFollowing(event.getmID());
                            break;
                        case USER_FOLLOWER:
                            users = restClient.getFollower(event.getmID());
                            break;
                        case CONFERENCE_ATTENDENT:
                            users = restClient.getConferenceUsersRegistered(event.getmID());
                            break;
                    }

                    return new ReceiveUserList(event.getType(), users);
                } catch (Exception e) {
                    return new ReceiveUserList(event.getType(), e.getMessage());
                }

            }

            @Override
            protected void onPostExecute(ReceiveUserList e) {
                BusProvider.get().post(e);
            }

        }.execute(e);

    }


    @Subscribe
    public void onFollowUserEvent(FollowUserEvent e) {
        final int userID =  e.getID();
        new AsyncTask<Integer, Void, Integer>() {

            @Override
            protected Integer doInBackground(Integer... ints) {
                try {
                    restClient.follow(ints[0]);
                } catch (Exception e) {
                    return 0;
                }
                return 1;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
            }
        }.execute(userID);
    }

    @Subscribe
    public void onUnfollowUserEvent(UnfollowUserEvent e) {
        final int userID =  e.getID();
        new AsyncTask<Integer, Void, Integer>() {

            @Override
            protected Integer doInBackground(Integer... ints) {
                try {
                    restClient.unfollow(ints[0]);
                    return 1;

                } catch (Exception e) {
                    return 0;
                }
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
            }
        }.execute(userID);
    }

    @Subscribe
    public void onGetResources(GetResourceEvent e) {
        new AsyncTask<Void, Void, ReceiveResourceEvent>() {

            @Override
            protected ReceiveResourceEvent doInBackground(Void... voids) {
                try {
                    StreameusResources streameusResources = restClient.getStreameusResource();
                    return new ReceiveResourceEvent(streameusResources);
                } catch (Exception e) {
                    return new ReceiveResourceEvent(e.getMessage());
                }

            }

            @Override
            protected void onPostExecute(ReceiveResourceEvent receiveResourceEvent) {
                BusProvider.get().post(receiveResourceEvent);
            }
        }.execute();
    }

    @Subscribe
    public void onGetProviderInfosEvent(GetProviderInfosEvent e) {
        new AsyncTask<Void, Void, ReceiveProviderInfoEvent>() {

            @Override
            protected ReceiveProviderInfoEvent doInBackground(Void... voids) {
                try {
                    return new ReceiveProviderInfoEvent(restClient.getProviderInfo());
                } catch (Exception e) {
                    return new ReceiveProviderInfoEvent(e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(ReceiveProviderInfoEvent r) {
                BusProvider.get().post(r);
            }
        }.execute();
    }


    @Subscribe
    public void onGetFluxDActuEvent(GetNewsFeedEvent e) {
        final int userID =  e.getID();
        new AsyncTask<Integer, Void, List<EventItem>>() {

            @Override
            protected List<EventItem> doInBackground(Integer... ints) {
                List<EventItem> l = null;

                int userID = ints[0];
                int top = ints[1];
                int skip = ints[2];
                try {
                    if (userID > 0) {
                        l = restClient.getUserFluxDActu(userID, top, skip);
                    } else {
                        l = restClient.getFluxDActu(top, skip);
                    }

                    if (l != null) {
                        Collections.sort(l, new EventItem.EventItemComparator());
                    } else {
                        return new ArrayList<EventItem>();
                    }
                    return l;

                } catch (Exception e) {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(List<EventItem> l) {
                if (l == null) {
                    BusProvider.get().post(new ReceiveNewsFeedEvent("An Error Happened While rerieving the news Feed"));
                } else {
                    BusProvider.get().post(new ReceiveNewsFeedEvent(l));
                }
            }
        }.execute(userID, (int) e.getTop(), (int) e.getSkip());
    }

    @Subscribe
    public void onGetAgendaEvent(GetAgendaEvent e) {
        Log.v(TAG, "getConferenceList a GetAgendaEvent");

        new AsyncTask<Void, Void, ReceiveAgendaEvent>() {

            @Override
            protected ReceiveAgendaEvent doInBackground(Void... voids) {
                try {
                    List<ConferenceSet> l = restClient.agenda();
                    if (l == null) {
                        l = new ArrayList<ConferenceSet>();
                    }

                    return  new ReceiveAgendaEvent(l);
                } catch (Exception e) {
                    return new ReceiveAgendaEvent(e.getMessage());
                }

            }

            @Override
            protected void onPostExecute(ReceiveAgendaEvent e) {
                BusProvider.get().post(e);
            }
        }.execute();
    }


//    USER, ALL, LIVE, SOON, CATEGORIE, RECOMMANDED, ATTENDED, REGISTERED

    @Subscribe
    public void onGetAllLecturesEvent(final GetLecturesEvent e) {
        Log.v(TAG, "getConferenceList a GetAgendaEvent");

        new AsyncTask<GetLecturesEvent, Void, ReceiveAllLecturesEvent>() {

            @Override
            protected ReceiveAllLecturesEvent doInBackground(GetLecturesEvent... p) {
                try {

                    final GetLecturesEvent e = p[0];
                    List<Conference> l = null;

                    switch (e.getType()) {
                        case USER:
                            l = restClient.getUsersLectures(e.getParamValue());
                            break;
                        case ALL:
                            l = restClient.getAllLectures();
                            break;
                        case LIVE:
                            l = restClient.getConferenceLive();
                            break;
                        case SOON:
                            l = restClient.getConferenceSoon();
                            break;
                        case CATEGORIE:
                            l = restClient.getCategorysLectures(e.getParamValue());
                            break;
                        case RECOMMANDED:
                            l = restClient.getRecommandedLectures();
                            break;
                        case ATTENDED:
                            l = restClient.getAttendedLectures(e.getParamValue());
                            break;
                        case REGISTERED:
                            l = restClient.getRegisteredLectures(e.getParamValue());
                            break;
                    }

                    if (l == null) {
                        l = new ArrayList<Conference>();
                    }
                    Collections.sort(l, new IConferenceComparator());
                    return new ReceiveAllLecturesEvent(e.getType(), l);
                } catch (Exception exception) {
                    Log.e(TAG, "-----------------------");
                    Log.e(TAG, exception.getClass().getName() + " " + exception.getMessage());
                    Log.e(TAG, exception.getClass().getSimpleName() + " " + exception.getCause());
                    Log.e(TAG, exception.getClass().getSimpleName() + "\n-------------------------");
                    if (exception.getMessage().contains("No events are present")) {
                        return (new ReceiveAllLecturesEvent(e.getType(), new ArrayList<Conference>()));
                    }
                    return new ReceiveAllLecturesEvent(e.getType(), "An Error Happened: " + exception.getMessage() + "\nPlease try again later");
                }
            }

            @Override
            protected void onPostExecute(ReceiveAllLecturesEvent e) {
                BusProvider.get().post(e);
            }
        }.execute(e);
    }


    @Subscribe
    public void onGetConferenceEvent(GetConferenceEvent e) {
        Log.v(TAG, "getConferenceList a onGetConferenceEvent");
        new AsyncTask<Integer, Void, ReceiveLectureEvent>() {

            @Override
            protected ReceiveLectureEvent doInBackground(Integer... ints) {
                try {
                    Conference l = restClient.getConference(ints[0]);
                    User conferencier = restClient.getUser(l.getOwner());
                    return new ReceiveLectureEvent(l, conferencier);

                } catch (Exception e) {
                    return new ReceiveLectureEvent(e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(ReceiveLectureEvent l) {
                BusProvider.get().post(l);
            }
        }.execute(e.getID());
    }

    @Subscribe
    public void onChangeConferenceRegisterStatusEvent(ChangeConferenceRegisterStatusEvent e) {
        Log.v(TAG, "getConferenceList a ChangeConferenceRegisterStatusEvent");
        new AsyncTask<Integer, Void, String>() {

            @Override
            protected String doInBackground(Integer... ints) {
                int conferenceID = ints[0];
                int register = ints[1];
                try {
                    String out = "";
                    if (register == 1) {
                        out = restClient.conferenceSuscribe(conferenceID);
                    } else {
                        out = restClient.conferenceUnsuscribe(conferenceID);
                    }
                    if (out.equals("")) {
                        return "";
                    } else {
                        return null;
                    }

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String l) {

            }
        }.execute(e.getConferenceID(),  (e.isRegister() ? 1 : 0));
    }



    @Subscribe
    public void onSearchEvent(SearchEvent e) {
        new AsyncTask<String, Void, SearchWrapper>() {

            @Override
            protected SearchWrapper doInBackground(String... strings) {

                try {
                    return restClient.search(strings[0]);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + " " + e.getCause());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(SearchWrapper l) {
                if (l == null) {
                    BusProvider.get().post(new ReceiveUserList(UsersListFragment.UserListType.SEARCH, "An Error happened while searching"));
                    BusProvider.get().post(new ReceiveAllLecturesEvent(ConferencesListFragment.ConferenceListType.SEARCH, "An error Happened While searching"));
                } else {
                    BusProvider.get().post(new ReceiveUserList(UsersListFragment.UserListType.SEARCH, l.getUsers()));
                    BusProvider.get().post(new ReceiveAllLecturesEvent(ConferencesListFragment.ConferenceListType.SEARCH, l.getConferences()));
                }
            }
        }.execute(e.getSearchQuery());
    }



    @Subscribe
    public void onSearchEvent(GetConferencesCategories e) {
        new AsyncTask<Void, Void, List<ConferenceCategorie>>() {

            @Override
            protected List<ConferenceCategorie> doInBackground(Void... voids) {
                try {
                    return restClient.getCategorys();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<ConferenceCategorie> l) {
                BusProvider.get().post(new ReceiveConferenceCategories(l));
            }
        }.execute();
    }



}
