package com.tn.tnclient.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tn.tnclient.Api.ApiClient;
import com.tn.tnclient.Api.ApiInterface;
import com.tn.tnclient.Models.PassResetResponse;
import com.tn.tnclient.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by alimjan on 10/23/17.
 */

public class RegisterPhoneActivity extends AppCompatActivity {

    Context c = RegisterPhoneActivity.this;
    private static final String TAG = "RegisterPhoneActivity";
    private ProgressDialog mProgress;
    EditText phone_number;
    EditText validation_code;
    Button send_vcode;
    Button next_button;
    TextView incorrect_contact;
    TextView wrong_validation_code;
    private boolean phoneistrue;
    private boolean validationistrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        getSupportActionBar().hide();

        /*
        progress bar for login
         */
        mProgress = new ProgressDialog(RegisterPhoneActivity.this);
        mProgress.setTitle("正在发送验证码..");
        mProgress.setMessage("请稍等...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        /*
        Inflating Views
         */
        phone_number = (EditText) findViewById(R.id.send_validation_phone);
        validation_code = (EditText)findViewById(R.id.validation_code_register);
        send_vcode = (Button) findViewById(R.id.send_validation_code_register);
        next_button = (Button)findViewById(R.id.register_next);
        /*
        click listeners
         */
        sendValidationCode();
        checkNullEntries();
        pleaseRetrieveVlidationCode();

        next_button.setEnabled(false);
        next_button.setBackgroundColor(getResources().getColor(R.color.grey));
    }

    private void sendValidationCode() {
        send_vcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneValue = phone_number.getText().toString();
                if(isInternetAvailable()==true)
                {
                    mProgress.show();
                    sendAttempt(phoneValue);
                }else
                {
                    mProgress.dismiss();
                    showalert("请检查网络连接");

                }
            }
        });
    }

    private void pleaseRetrieveVlidationCode(){
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTextForWrongField(6);
            }
        });
    }

    public void sendAttempt(final String phone) {

        Log.d(TAG, "LoginAttempt: Attempting login");
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<PassResetResponse> call = apiInterface.registerValidate(phone);
        call.enqueue(new Callback<PassResetResponse>() {

            @Override
            public void onResponse(Call<PassResetResponse> call, Response<PassResetResponse> response) {
                Log.d(TAG, "onResponse: Response parsing");

                PassResetResponse result = response.body();
                if (result.getStatus() == 0) {
                    mProgress.dismiss();
                    setUpTextForWrongField(0);
                } else {
                    Log.d(TAG, "onResponse: Got the response: " + result.getStatus());
                    setUpTextForWrongField(1);
                    countDownTimer();
                    final String vCode = result.getV_code();
                    next_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        if(isInternetAvailable()==true)
                        {
                            clickConfirmButton(phone, vCode);
                        }else
                        {
                            mProgress.dismiss();
                            showalert("请检查网络连接");
                        }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PassResetResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(RegisterPhoneActivity.this, "发送验证码失败？", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clickConfirmButton(String phone, String v_code){
        if(!(validation_code.getText().toString()).equals(v_code)){
            setUpTextForWrongField(3);
        }else{
            setUpTextForWrongField(4);
            Bundle bundle = new Bundle();
            bundle.putString("phone",phone);
            startActivity(new Intent(RegisterPhoneActivity.this, RegisterActivity.class).putExtras(bundle));
        }
    }

    private void countDownTimer(){
        send_vcode.setBackgroundColor(getResources().getColor(R.color.grey));
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                send_vcode.setText("还剩" + millisUntilFinished / 1000 + "重新发送");
                send_vcode.setClickable(false);
            }
            public void onFinish() {
                send_vcode.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                send_vcode.setText("重新获取验证码");
                send_vcode.setClickable(true);
            }
        }.start();
    }

    /*
     Setup text for wrong field
    */
    public void setUpTextForWrongField(int id) {

        /*
         dismiss progress bar
        */
        mProgress.dismiss();
        incorrect_contact = (TextView) findViewById(R.id.wrong_validation_phone);
        wrong_validation_code = (TextView)findViewById(R.id.wrong_validation_code_register);
        if (id == 0){
            incorrect_contact.setText(R.string.user_doesnt_exist);
        } else if (id == 1) {
            incorrect_contact.setText(null);
        }else if (id == 2) {
            wrong_validation_code.setText(R.string.invalid_validation_code);
        } else if (id == 3) {
            wrong_validation_code.setText(R.string.validation_code_not_match);
        }else if (id == 4) {
            wrong_validation_code.setText(null);
        }else if (id == 5){
            incorrect_contact.setText(R.string.invalid_phone_number);
        }else if (id == 6){
            wrong_validation_code.setText(R.string.please_retrieve_validation_code);
        }
    }

    public void checkNullEntries(){
        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int phone = phone_number.getText().toString().length();
                if (phone != 11) {
                    setUpTextForWrongField(5);
                    phoneistrue = false;
                } else {
                    setUpTextForWrongField(1);
                    phoneistrue = true;
                }
                if (validationistrue == true && phoneistrue == true) {
                    setRegButtonOnOff(1);
                } else if (validationistrue == false || phoneistrue == false) {
                    setRegButtonOnOff(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        validation_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int vcode = validation_code.getText().toString().length();
                if (vcode != 6) {
                    setUpTextForWrongField(2);
                    validationistrue = false;
                } else {
                    setUpTextForWrongField(4);
                    validationistrue = true;
                }
                if (validationistrue == true && phoneistrue == true) {
                    setRegButtonOnOff(1);
                } else if (validationistrue == false || phoneistrue == false) {
                    setRegButtonOnOff(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    void setRegButtonOnOff(int code) {
        if (code == 1) {
            next_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            next_button.setEnabled(true);
        } else {
            next_button.setBackgroundColor(getResources().getColor(R.color.grey));
            next_button.setEnabled(false);
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

    private void showalert(String text) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterPhoneActivity.this);
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
