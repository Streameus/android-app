package com.streameus.android.dataProvider;

import android.util.Log;

import com.streameus.android.model.Conference;
import com.streameus.android.model.ConferenceCategorie;
import com.streameus.android.model.ConferenceSet;
import com.streameus.android.model.EventItem;
import com.streameus.android.model.OAuthProviderInfo;
import com.streameus.android.model.SearchWrapper;
import com.streameus.android.model.StreameusResources;
import com.streameus.android.model.User;

import java.util.HashMap;
import java.util.List;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Pol on 26/05/14.
 */
public class RESTClient {
    public static final String SERVER_URL = "http://streameusdev.apphb.com/";
    public static final String API_URL = SERVER_URL + "api";
    public static final int DEFAULT_TOP = 120;

    private RestAdapter restAdapter;
    private SreameusService service;
    private RequestInterceptor requestInterceptor;
    private String mToken;

    static public class RegisterAnswer {
        HashMap<String, List<String>> ModelState;

        public HashMap<String, List<String>> getModelState() {
            return ModelState;
        }

        public String getErrorMSG() {
            String out = "";
            for (String key : ModelState.keySet()) {
                out += ModelState.get(key) + " ;";
            }
            return out;
        }
    }


    public interface SreameusService {

        /**
         * *** USERS ***
         */

        @POST("/Account/Register")
        RegisterAnswer register(@Body RegisterBody registerBody);

        @GET("/User")
        List<User> getAllUsers();

        @GET("/User/me")
        User getLoggedUser();

        @GET("/User/{id}")
        User getUser(@Path("id") int id);

        @GET("/Recommendation/users")
        List<User> getRecommendedUsers();

        /**
         * *** ABONNEMENT ***
         */

        @GET("/Following/{id}")
        List<User> getFollowing(@Path("id") int id);

        @GET("/Follower/{id}")
        List<User> getFollower(@Path("id") int id);

        @POST("/Following/{id}")
        boolean follow(@Path("id") int id);

        @DELETE("/Following/{id}")
        boolean unfollow(@Path("id") int id);

        @GET("/User/AmIFollowing/{id}")
        boolean amIFollowing(@Path("id") int id);

        /**
         * *** RESOURCES ***
         */

        @GET("/Resource")
        StreameusResources getStreameusResource();

        @GET("/Account/ExternalLogins")
        List<OAuthProviderInfo> getProviderInfos(@Query("returnUrl") String returnUrl);

        /**
         * *** Flux d'actu ***
         */
        @GET("/Event/author/{id}")
        List<EventItem> getUserFluxDActu(@Path("id") int userID, @Query("$top") long top, @Query("$skip") long skip);

        @GET("/Event")
        List<EventItem> getFluxDActu(@Query("$top") long top, @Query("$skip") long skip);

        /**
         * *** Search ***
         */
        @GET("/Search")
        SearchWrapper search(@Query("query") String query);

        /**
         * *** Conferences ***
         */
        @GET("/Agenda")
        List<ConferenceSet> agenda();

        @GET("/Conference")
        List<Conference> getAllLectures();

        @GET("/User/{id}/Conferences")
        List<Conference> getUsersLectures(@Path("id") int userID);

        @GET("/Conference/{id}")
        Conference getConference(@Path("id") int conferenceID);

        @GET("/Conference/{id}/Registered")
        List<User> getConferenceUsersRegistered(@Path("id") int conferenceID);

        @GET("/Conference/{id}/Subscribe")
        String conferenceSuscribe(@Path("id") int conferenceID);

        @GET("/Conference/{id}/Unsubscribe")
        String conferenceUnsuscribe(@Path("id") int conferenceID);

        @GET("/Conference/{id}/Registered")
        String getConferenceRegisteredUsers(@Path("id") int conferenceID);

        //todo: virer le top
        @GET("/Conference/Soon")
        List<Conference> getConferenceSoon(@Query("$top") int top);

        //todo: virer le top
        @GET("/Conference/Live")
        List<Conference> getConferenceLive(@Query("$top") int top);

        @GET("/Conference/Categories")
        List<ConferenceCategorie> getCategorys();

        @GET("/Conference/Category/{id}")
        List<Conference> getCategorysLectures(@Path("id") int categoryID);

        @GET("/Recommendation/conferences")
        List<Conference> getRecommandedLectures();

        @GET("/User/{id}/Conferences/Attended")
        List<Conference> getAttendedLectures(@Path("id") int userID);

        @GET("/User/{id}/Conferences/Registered")
        List<Conference> getRegisteredLectures(@Path("id") int userID);


    }


    public RESTClient(final String token) {
        mToken = token;
        requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                if (!mToken.equals("")) {
                    request.addHeader("Authorization",  "Bearer " + mToken);
                }
            }
        };

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setErrorHandler(new StreameusErrorHandler())
                .build();
        service = restAdapter.create(SreameusService.class);
    }

    class StreameusErrorHandler implements ErrorHandler{
        @Override
        public Throwable handleError(RetrofitError cause)  {
            Response r = cause.getResponse();
            Log.v("Error handler", "status: " + cause.getResponse());

            if (r == null) {
                return cause;
            }
//            if (r.getStatus() == 200) {
//                return new BadlyManagedEmptyAnswer();
//            }
//            if (r != null && r.getStatus() == 401) {
//                return new UnauthorizedException(cause);
//            }
//            if (r != null && r.getStatus() == 204) {
//                return new EmptyResponse();
//            }
            return cause;
        }


    }

    public class BadlyManagedEmptyAnswer extends Exception {

    }

    public class RegisterBody{
        String UserName;
        String Pseudo;
        String Password;
        String ConfirmPassword;

        public RegisterBody(String userName, String pseudo, String password, String confirmPassword) {
            UserName = userName;
            Pseudo = pseudo;
            Password = password;
            ConfirmPassword = confirmPassword;
        }
    }
    public void setToken(String token) {mToken = token;}

    public RegisterAnswer register(String userName, String pseudo, String password) {return service.register(new RegisterBody(userName, pseudo, password, password));}
    public User getUser(int id) {return service.getUser(id);}
    public List<User> getFollowing(int id) { return service.getFollowing(id);}
    public List<User> getFollower(int id) { return service.getFollower(id);}
    public List<User> getAllUsers() {return service.getAllUsers();}
    public List<User> getRecommendedUsers() { return service.getRecommendedUsers();}
    public void follow(int id) {service.follow(id);}
    public void unfollow(int id) {service.unfollow(id);}
    public boolean amIFollowing(int id) {return service.amIFollowing(id);}
    public StreameusResources getStreameusResource() { return service.getStreameusResource();}
    public List<OAuthProviderInfo> getProviderInfo() { return service.getProviderInfos("/");}
    public User getLoggedUser() {return service.getLoggedUser();}
    public List<EventItem> getUserFluxDActu(int userID, long top, long skip) { return service.getUserFluxDActu(userID, top, skip);}
    public List<EventItem> getFluxDActu(long top, long skip) {return service.getFluxDActu(top, skip);}
    public List<ConferenceSet> agenda() { return service.agenda();}
    public List<Conference> getAllLectures() {return service.getAllLectures();}
    public List<Conference> getUsersLectures(int userID) {return service.getUsersLectures(userID);}
    public Conference getConference(int conferenceID) {return service.getConference(conferenceID);}
    public List<User> getConferenceUsersRegistered(int conferenceID) { return service.getConferenceUsersRegistered(conferenceID);}
    public String conferenceSuscribe(int conferenceID) { return service.conferenceSuscribe(conferenceID);}
    public String conferenceUnsuscribe(int conferenceID) { return service.conferenceUnsuscribe(conferenceID);}
    public List<Conference> getConferenceSoon() {return  service.getConferenceSoon(DEFAULT_TOP);}
    public List<Conference> getConferenceLive() {
        return service.getConferenceLive(DEFAULT_TOP);
    }

    public List<Conference> getCategorysLectures(int categoryID) {return  service.getCategorysLectures(categoryID);}
    public List<Conference> getRecommandedLectures() { return service.getRecommandedLectures();}
    public List<Conference> getAttendedLectures(int userID) { return service.getAttendedLectures(userID);}
    public List<Conference> getRegisteredLectures(int userID) { return service.getRegisteredLectures(userID);}
    public SearchWrapper search(String query) { return service.search(query);}
    public List<ConferenceCategorie> getCategorys() { return service.getCategorys();}

}
