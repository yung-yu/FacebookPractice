package andy.facebookpratice;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

/**
 * Created by andyli on 2015/12/28.
 */
public class FacebookManager {
    private static FacebookManager instance;
    private static final String LOGIN_PEMISSION = "public_profile, email, user_birthday, user_friends";

    public FacebookManager() {

    }

    public static FacebookManager getInstance(){
        if(instance == null)
            instance = new FacebookManager();
        return instance;
    }

    CallbackManager callbackManager;
    AccessToken accessToken;

    public void init(Context context){
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(context);
    }

    public AccessToken getAccessToken(){
        return accessToken;
    }
    public interface LoginCallBack{
          void onSucess();
          void onFail(String err);
    }
    public CallbackManager getCallbackManager(){
        return callbackManager;
    }
    public void login(final Activity activity , final LoginCallBack callBack){
        if(!isFacebookLoggedIn()) {
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(LOGIN_PEMISSION));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                            if(callBack!=null) callBack.onSucess();

                        }

                        @Override
                        public void onCancel() {
                            // App code
                            if(callBack!=null) callBack.onSucess();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            if(callBack!=null) callBack.onFail(exception.getCause().toString());

                        }
                    });
        }else{
            if(callBack!=null) callBack.onSucess();
        }
    }
    public boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }


}
