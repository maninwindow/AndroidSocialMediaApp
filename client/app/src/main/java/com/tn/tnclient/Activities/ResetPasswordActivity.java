package com.tn.tnclient.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by alimjan on 10/18/17.
 */

public class ResetPasswordActivity extends AppCompatActivity {

    Context c = ResetPasswordActivity.this;
    Bundle bundle;
    private static final String TAG = "ResetPasswordActivity";
    private ProgressDialog mProgress;
    EditText new_password;
    EditText confirm_new_password;
    Button confirm_button;
    TextView new_wrong_password;
    TextView confirm_new_wrong_password;
    TextView confirm_issue;
    boolean newpasswordistrue = false;
    boolean confirmnewpasswordistrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();

         /*
        progress bar for login
         */
        mProgress = new ProgressDialog(ResetPasswordActivity.this);
        mProgress.setTitle("正在发送验证码..");
        mProgress.setMessage("请稍等...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        /*
        Inflating Views
         */
        new_password = (EditText)findViewById(R.id.new_password);
        confirm_new_password = (EditText)findViewById(R.id.confirm_new_password);
        confirm_button = (Button)findViewById(R.id.reset_password_confirm);

        /**
         * Receive bundle extras
         */
        bundle = getIntent().getExtras();

        /**
         * Listeners
         */
        confirmPasswordReset();
        passwordListner();
    }

    private void confirmPasswordReset() {
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                String newPass = new_password.getText().toString();
                String confirmNewPassword = confirm_new_password.getText().toString();
                if(!newPass.equals(confirmNewPassword)){
                    mProgress.dismiss();
                    setUpTextForWrongField(5);
                    confirm_button.setEnabled(false);
                    confirm_button.setBackgroundColor(getResources().getColor(R.color.grey));
                } else if(isInternetAvailable()==false) {
                    mProgress.dismiss();
                    showalert("请检查网络连接");
                } else {
                    setUpTextForWrongField(55);
                    confirm_button.setEnabled(true);
                    confirm_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    confirmAttemp(newPass);
                }
            }
        });
    }

    public void confirmAttemp(String newPass) {

        Log.d(TAG, "LoginAttempt: Attempting login");
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<PassResetResponse> call = apiInterface.resetPassword(bundle.getString("phone"), newPass);
        call.enqueue(new Callback<PassResetResponse>() {

            @Override
            public void onResponse(Call<PassResetResponse> call, Response<PassResetResponse> response) {
                Log.d(TAG, "onResponse: Response parsing");

                PassResetResponse result = response.body();
                if(result.getStatus() == 1){
                    setUpTextForWrongField(0);
                    startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                } else {
                    mProgress.dismiss();
                    setUpTextForWrongField(00);
                    Log.d(TAG, "onResponse: Got the response: " + result.getStatus());
                }
            }

            @Override
            public void onFailure(Call<PassResetResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(ResetPasswordActivity.this, "发送验证码失败？", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void passwordListner() {
        new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newPass = new_password.getText().toString();
                if (newPass.length() < 6) {
                    newpasswordistrue = false;
                    setUpTextForWrongField(3);
                } else {
                    newpasswordistrue = true;
                    setUpTextForWrongField(33);
                }
                if (newpasswordistrue == true) {
                    confirm_button.setEnabled(true);
                    confirm_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else if (newpasswordistrue == false) {
                    confirm_button.setEnabled(false);
                    confirm_button.setBackgroundColor(getResources().getColor(R.color.grey));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        confirm_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String confirmNewPass = confirm_new_password.getText().toString();
                if (confirmNewPass.length() < 6) {
                    confirmnewpasswordistrue = false;
                    setUpTextForWrongField(4);
                } else {
                    confirmnewpasswordistrue = true;
                    setUpTextForWrongField(44);
                }
                if (confirmnewpasswordistrue == true) {
                    confirm_button.setEnabled(true);
                    confirm_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else if (confirmnewpasswordistrue == false) {
                    confirm_button.setEnabled(false);
                    confirm_button.setBackgroundColor(getResources().getColor(R.color.grey));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

     /*
        Setup text for wrong field
     */
    public void setUpTextForWrongField(int id) {

        /*
         dismiss progress bar
        */
        mProgress.dismiss();
        new_wrong_password = (TextView)findViewById(R.id.new_wrong_password);
        confirm_new_wrong_password = (TextView)findViewById(R.id.confirm_new_wrong_password);
        confirm_issue = (TextView)findViewById(R.id.confirm_issue);
        if (id == 0){
            confirm_issue.setText(R.string.empty_password);
        } else if (id == 3) {
            new_wrong_password.setText(R.string.new_password_invalid);
        } else if (id == 4) {
            confirm_new_wrong_password.setText(R.string.new_password_invalid);
        } else if (id == 5) {
            confirm_new_wrong_password.setText(R.string.new_passwords_not_match);
        } else if (id == 6){
            confirm_issue.setText(R.string.bad_network_connection);
        }else if (id == 00){
            confirm_issue.setText(null);
        } else if (id == 33){
            new_wrong_password.setText(null);
        } else if (id == 44){
            confirm_new_wrong_password.setText(null);
        } else if (id == 55){
            confirm_new_wrong_password.setText(null);
            new_wrong_password.setText(null);
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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ResetPasswordActivity.this);
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
