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

import com.stephentuso.welcome.WelcomeHelper;
import com.tn.tnclient.Api.ApiClient;
import com.tn.tnclient.Api.ApiInterface;
import com.tn.tnclient.Models.LoginResponse;
import com.tn.tnclient.Models.LoginUser;
import com.tn.tnclient.R;
import com.tn.tnclient.Utils.SessionHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
//Test Commit
public class LoginActivity extends AppCompatActivity {

    private WelcomeHelper welcomeScreen;
    private ProgressDialog mProgress;
    private static final String TAG = "LoginActivity";
    TextView incorrect_contact;
    TextView incorrect_password;
    Context c = LoginActivity.this;
    TextView registerStudent;
    TextView forgotPassword;
    EditText phone;
    EditText password;
    Button bt_signin;
    SessionHelper sessionHelper;
    boolean contactistrue = false;
    boolean passwordistrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Render Welcome screen
        welcomeScreen = new WelcomeHelper(this, TNWelcomeActivity.class);
        welcomeScreen.show(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        /*
        progress bar for login
         */
        mProgress = new ProgressDialog(LoginActivity.this);
        mProgress.setTitle("正在登陆..");
        mProgress.setMessage("请稍等...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        /*
        Initializing session helper
         */
        sessionHelper = new SessionHelper(LoginActivity.this);

        /*
        Inflating Views
         */
        phone = (EditText) findViewById(R.id.login_contact);
        password = (EditText) findViewById(R.id.login_password);
        bt_signin = (Button) findViewById(R.id.sign_in_button);

       /*
       Checking for previous session
        */
        checksharedpreferences();

        /*
        click listeners
         */
        signInButtonListner();
        registerbuttonclicklistner();
        fortgotPasswordButtonClickListner();
        contactListner();

        /**
         * Disable signin button by default
         */
        bt_signin.setEnabled(false);
        bt_signin.setBackgroundColor(getResources().getColor(R.color.grey));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
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

    private void signInButtonListner() {
        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String phoneValue = phone.getText().toString();
            String passwordValue = password.getText().toString();

            if(isInternetAvailable()==true)
            {
                mProgress.show();
                LoginAttempt(phoneValue, passwordValue);
            }else
            {
                mProgress.dismiss();
                showalert("请检查网络连接");
            }
            }
        });
    }

    public void LoginAttempt(String phone, String password) {

        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        LoginUser user = new LoginUser(phone, password);
        Call<LoginResponse> call = apiInterface.login(user);
        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse login_results = response.body();
                if (login_results.getStatus() == 0) {
                    mProgress.dismiss();
                    setUpTextForWrongField(0);
                } else {
                    if (login_results.getStatus() == 1) {
                        if (login_results.getCode() == 1) {
                            setUpTextForWrongField(1);
                        }else if (login_results.getCode() == 2) {
                            setUpTextForWrongField(2);
                        } else{
                            handleresponse(login_results.getPhone(), login_results.getSid(),login_results.getUsername(),login_results.getOccupation());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(LoginActivity.this, "登录失败？", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void handleresponse(String contact, String sid, String username, String occupation) {
        sessionHelper.createLoginSession(contact, sid);
        sessionHelper.passUserInformation(username,occupation);
        sessionHelper.apply();
        mProgress.dismiss();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private void registerbuttonclicklistner() {
        registerStudent = (TextView) findViewById(R.id.register_student);
        registerStudent.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterPhoneActivity.class));
                }
            }
        );
    }

    private void fortgotPasswordButtonClickListner() {

        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                }
            }
        );
    }

    private void contactListner() {
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int phone_length = phone.length();
                if (phone_length != 11) {
                    setUpTextForWrongField(3);
                    contactistrue = false;
                } else {
                    setUpTextForWrongField(4);
                    contactistrue = true;
                }

                if (passwordistrue == true && contactistrue == true) {
                    bt_signin.setEnabled(true);
                    bt_signin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else if (passwordistrue == false || contactistrue == false) {
                    bt_signin.setEnabled(false);
                    bt_signin.setBackgroundColor(getResources().getColor(R.color.grey));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

       password.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               int passLength = password.getText().length();
               if (passLength < 5) {
                   passwordistrue = false;
                   setUpTextForWrongField(5);
               } else {
                   passwordistrue = true;
                   setUpTextForWrongField(6);
               }
               if (passwordistrue == true && contactistrue == true) {
                   bt_signin.setEnabled(true);
                   bt_signin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
               } else if (passwordistrue == false || contactistrue == false) {
                   bt_signin.setEnabled(false);
                   bt_signin.setBackgroundColor(getResources().getColor(R.color.grey));
               }
           }

           @Override
           public void afterTextChanged(Editable s) {}
       });
    }

    private void checksharedpreferences() {
        boolean isLoggedIn = sessionHelper.isLoggedIn();
        if (isLoggedIn) {
            startActivity(new Intent(c, MainActivity.class));
            finish();
        }
    }

     //Setup text for wrong field
     public void setUpTextForWrongField(int id) {
       //dismiss progress bar
       mProgress.dismiss();
       incorrect_contact = (TextView) findViewById(R.id.wrong_contact);
       incorrect_password = (TextView) findViewById(R.id.wrong_password);
        if (id == 0){
            incorrect_contact.setText(R.string.user_doesnt_exist);
        }else if (id == 1) {
            incorrect_contact.setText(R.string.exist_phone_no);
        } else if (id == 2){
            incorrect_password.setText(R.string.incorrect_password);
        } else if (id == 3) {
            incorrect_contact.setText(R.string.wrong_phone_no);
        } else if (id == 4) {
            incorrect_contact.setText(null);
        } else if (id == 5) {
            incorrect_password.setText(R.string.small_pass);
        } else if (id == 6) {
            incorrect_password.setText(null);
        }

    }

    void showalert(String text) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
        builder1.setMessage(text);
        builder1.setCancelable(true);
        builder1.setPositiveButton("关闭",
            new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}

