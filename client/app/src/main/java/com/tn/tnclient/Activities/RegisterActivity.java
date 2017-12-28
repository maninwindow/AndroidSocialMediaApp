package com.tn.tnclient.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tn.tnclient.Api.ApiClient;
import com.tn.tnclient.Api.ApiInterface;
import com.tn.tnclient.Models.RegisterResponse;
import com.tn.tnclient.Models.RegisterUser;
import com.tn.tnclient.R;
import com.tn.tnclient.Utils.SessionHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    /*declaration
     */
    private ProgressDialog mProgress;
    private static final String TAG = "RegisterActivity";
    Bundle bundle;
    EditText et_name, et_password;
    Context c = RegisterActivity.this;
    Button bt_register;
    TextView tv_login;
    SessionHelper sessionHelper;
    boolean nameIsTrue = false;
    boolean passwordIsTrue = false;
    TextView wrongUsername;
    TextView wrongPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Context c = RegisterActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
          /*
          set up session helper
           */
        sessionHelper = new SessionHelper(c);

        /*
        inflating views
         */
        et_name = (EditText) findViewById(R.id.register_user_name);
        et_password = (EditText) findViewById(R.id.register_password);
        bt_register = (Button) findViewById(R.id.register_button);
        tv_login = (TextView) findViewById(R.id.login_act);


        /*
        disable register button
         */
        bt_register.setEnabled(false);
        bt_register.setBackgroundColor(getResources().getColor(R.color.grey));


        /*
        progress bar
         */
        mProgress = new ProgressDialog(RegisterActivity.this);
        mProgress.setTitle("注册中...");
        mProgress.setMessage("请稍等...");

        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        /**
         * Receive bundle extras
         */
        bundle = getIntent().getExtras();

        /*
         Click Listners
        */
        registerbuttonlistner();
        existingUser();
        checkNullEntries();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    void existingUser() {
        tv_login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    }
                }
        );
    }


    void registerbuttonlistner() {
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                if (isInternetAvailable() == true) {
                    registerUser();
                } else {
                    mProgress.dismiss();
                    showalert("");
                }
            }
        });


    }

    private void checkNullEntries() {
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_name.length() > 0) {
                    nameIsTrue = true;
                    removeWarningForWrongField(1);
                } else {
                    nameIsTrue = false;
                    setUpWarningForWrongField(1);
                }
                if (nameIsTrue == true && passwordIsTrue == true) {
                    setRegButtonOnOff(1);
                } else {
                    setRegButtonOnOff(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_password.length() >= 6) {
                    passwordIsTrue = true;
                    removeWarningForWrongField(3);
                } else {
                    passwordIsTrue = false;
                    setUpWarningForWrongField(3);
                }
                if (nameIsTrue == true && passwordIsTrue == true) {
                    setRegButtonOnOff(1);
                } else {
                    setRegButtonOnOff(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void registerUser() {

        final String username = et_name.getText().toString();
        String password = et_password.getText().toString();
        //Send this to the api
        RegisterUser user = new RegisterUser(username, bundle.getString("phone"), password);
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface client = retrofit.create(ApiInterface.class);
        Call<RegisterResponse> call = client.register(user);
        call.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                mProgress.dismiss();
                RegisterResponse registerResponse = response.body();
                if (registerResponse == null) {
                    showalert("网络繁忙，请稍后再试");
                } else {
                    if (registerResponse.getStatus() == 1) {
                        handleRegisteredUser(registerResponse.getPhone(), registerResponse.getSid(),username,registerResponse.getOccupation());
                    } else if (registerResponse.getStatus() == 0) {
                        if (registerResponse.getCode() == 1) {
                            setUpWarningForWrongField(4);
                        }else if (registerResponse.getCode() == 2) {
                            setUpWarningForWrongField(5);
                        }else {
                            removeWarningForWrongField(4);
                            removeWarningForWrongField(5);
                            showalert("我靠，你在干嘛呀？");
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(RegisterActivity.this, "怎么你注册就失败啊？", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void handleRegisteredUser(String phone, String sid,String username,String occupation) {
        sessionHelper.createLoginSession(phone, sid);
        sessionHelper.passUserInformation(username,occupation);
        sessionHelper.apply();
        startActivity(new Intent(c, MainActivity.class).putExtras(bundle));
        finish();
    }

    /*
    on back pressed
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    private void setRegButtonOnOff(int code) {
        if (code == 1) {
            bt_register.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            bt_register.setEnabled(true);

        } else {
            bt_register.setBackgroundColor(getResources().getColor(R.color.grey));
            bt_register.setEnabled(false);
        }
    }


    private void setUpWarningForWrongField(int id) {

        switch (id) {
            case 1:
                wrongUsername = (TextView) findViewById(R.id.invalid_username);
                wrongUsername.setText(R.string.wrong_username);
                break;
            case 3:
                wrongPassword = (TextView) findViewById(R.id.invalid_password);
                wrongPassword.setText(R.string.small_pass);
                break;
            case 4:
                wrongUsername = (TextView) findViewById(R.id.invalid_username);
                wrongUsername.setText(R.string.username_exist);
                break;
        }
    }

    private void removeWarningForWrongField(int id) {

        switch (id) {
            case 1:
                wrongUsername = (TextView) findViewById(R.id.invalid_username);
                wrongUsername.setText(null);
                break;
            case 3:
                wrongPassword = (TextView) findViewById(R.id.invalid_password);
                wrongPassword.setText(null);
                break;
            case 4:
                wrongUsername = (TextView) findViewById(R.id.invalid_username);
                wrongUsername.setText(null);
                break;
        }
    }

    /*
    check for connection
     */
    public boolean isInternetAvailable() {
        try {
            final InetAddress address = InetAddress.getByName("www.baidu.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            showalert("网络连接失败");
        }
        return false;
    }

    /*
    Alert builder
     */
    void showalert(String text) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
        builder1.setMessage(text);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "关闭",
                new DialogInterface.OnClickListener()

                {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}