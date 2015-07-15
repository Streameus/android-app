package com.streameus.android.dataProvider;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.otto.Subscribe;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.GetTokenEvent;
import com.streameus.android.bus.ReceiveTokenEvent;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Pol on 05/09/14.
 */
public class AuthNative {
  public static class AuthNativeAnswer {
    private String access_token;
    private Long expires_in;
    private String userName;
    private String error;
    private String error_description;

    public String getAccess_token() {
      return access_token;
    }

    public void setAccess_token(String access_token) {
      this.access_token = access_token;
    }

    public Long getExpires_in() {
      return expires_in;
    }

    public void setExpires_in(Long expires_in) {
      this.expires_in = expires_in;
    }

    public String getUserName() {
      return userName;
    }

    public void setUserName(String userName) {
      this.userName = userName;
    }

    public String getError() {
      return error;
    }

    public void setError(String error) {
      this.error = error;
    }

    public String getError_description() {
      return error_description;
    }

    public void setError_description(String error_description) {
      this.error_description = error_description;
    }
  }

  public static final String API_URL = RESTClient.SERVER_URL;

  public static final String TAG = "AuthNative";


  private RestAdapter restAdapter;
  private AuthNativeService service;

  public interface AuthNativeService {
    /**
     * *** LOGIN ***
     */
    @FormUrlEncoded
    @POST("/Token")
    AuthNativeAnswer getToken(@Field("grant_type") String grant_type,
                              @Field("userName") String userName,
                              @Field("password") String password);
  }

  public AuthNative() {
    restAdapter = new RestAdapter.Builder()
        .setEndpoint(API_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .setErrorHandler(new StreameusErrorHandler())
        .build();
    service = restAdapter.create(AuthNativeService.class);
    BusProvider.get().register(this);
  }

  private AuthNativeAnswer getToken(String login, String password) {
    return service.getToken("password", login, password);
  }

  class StreameusErrorHandler implements ErrorHandler {
    @Override
    public Throwable handleError(RetrofitError cause) {
      Response r = cause.getResponse();
//            if (cause.getResponse().getStatus() == 400) {
//                throw new StreameusException("Wrong login/Password");
//            }
      Log.v("Error handler", "status: " + cause.getResponse());
//            if (r != null && r.getStatus() == 401) {
//                return new UnauthorizedException(cause);
//            }
      if (r != null && r.getStatus() == 204) {
        return null;
      }
      return cause;
    }


  }


  @Subscribe
  public void onGetTokenEvent(GetTokenEvent e) {
    Log.v(TAG, "get a GetTokenEvent");
    new AsyncTask<String, Void, ReceiveTokenEvent>() {
      @Override
      protected ReceiveTokenEvent doInBackground(String... strings) {
        try {
          AuthNativeAnswer answer = getToken(strings[0], strings[1]);

          Log.d("Lolipop", answer.getAccess_token());
          return new ReceiveTokenEvent(answer.getAccess_token());

        } catch (Exception e) {
          return null;
        }

      }

      @Override
      protected void onPostExecute(ReceiveTokenEvent receiveTokenEvent) {
        super.onPostExecute(receiveTokenEvent);
        BusProvider.get().post(receiveTokenEvent != null ? receiveTokenEvent : new ReceiveTokenEvent(null));
      }
    }.execute(e.getLogin(), e.getPassword());
  }
}
