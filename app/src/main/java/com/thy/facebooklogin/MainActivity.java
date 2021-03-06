package com.thy.facebooklogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;

    TextView tvEmail, tvBirth;
    ProgressDialog dialog;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        initControls();
        loginWithFb();

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));

        if(AccessToken.getCurrentAccessToken() != null){
            tvEmail.setText(AccessToken.getCurrentAccessToken().getUserId());

            //sendInfo();

        }


    }

    private void initControls(){
        callbackManager = CallbackManager.Factory.create();

        tvEmail = findViewById(R.id.tv_email);
        tvBirth = findViewById(R.id.tv_birthday);

        iv = findViewById(R.id.iv);
    }

    private void loginWithFb(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();

                String accesstoken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        dialog.dismiss();
                        Log.d("respose", response.toString());
                        getData(object);
                    }
                });

                //Request Graph API
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, email, birthday");
                request.setParameters(parameters);
                request.executeAsync();

//                sendInfo();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }


    private void getData(JSONObject object) {
        try {
            URL profile_picture = new URL("https://graph.facebook.com"+object.getString("id")+"/picture?width=250&height=250");

            Picasso.with(this).load(profile_picture.toString()).into(iv);

            tvEmail.setText(object.getString("email"));
            tvBirth.setText(object.getString("birthday"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void sendInfo(){
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("email", tvEmail.getText());
        intent.putExtra("birthday", tvBirth.getText());
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
