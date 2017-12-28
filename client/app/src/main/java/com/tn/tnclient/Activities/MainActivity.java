package com.tn.tnclient.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.stephentuso.welcome.WelcomeHelper;
import com.tn.tnclient.Fragments.MeFragment;
import com.tn.tnclient.Fragments.MessageFragment;
import com.tn.tnclient.Fragments.NearbyFragment;
import com.tn.tnclient.Models.IMTokenRequest;
import com.tn.tnclient.R;
import com.tn.tnclient.SharedContext;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RongIM.UserInfoProvider{

      private boolean doubleBackToExitPressedOnce = false;
      private TextView messageView;
      private WelcomeHelper welcomeScreen;
      private String userList;
      ArrayList<IMTokenRequest> userIdList = new ArrayList<IMTokenRequest>();
      private ProgressDialog mProgress;
      private SharedPreferences SPreference = SharedContext.getInstance().getSharedPreferences();
      String token = "NiBcYNYqqP8ROwlK1K35z9FDUu2TS+4qjunlybVM5n9VD4DFwFs3eKiMfOC70Y/lHKKD8K4ivKNqH3S90v+yyVrKW6wjKa8e";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Render Welcome screen
        welcomeScreen = new WelcomeHelper(this, TNWelcomeActivity.class);
        welcomeScreen.show(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        messageView = (TextView) findViewById(R.id.messageView);

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment selectedFragment = null;
                switch (tabId) {
                    case R.id.ic_nearby:
                        getSupportActionBar().setTitle("附近人");
                        selectedFragment = NearbyFragment.newInstance();
                        break;
                    case R.id.ic_message:
                        getSupportActionBar().setTitle("消息");
                        selectedFragment = new MessageFragment();
                        break;
                    case R.id.ic_me:
                        getSupportActionBar().setTitle("我的");
                        selectedFragment = new MeFragment();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
            }
        });

        BottomBarTab message = bottomBar.getTabWithId(R.id.ic_message);
        message.setBadgeCount(2);

        connectRongServer();
        initUserInfo();

        /*
        progress bar for login
         */
        mProgress = new ProgressDialog(MainActivity.this);
        mProgress.setTitle("正在获取数据..");
        mProgress.setMessage("请稍等...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                new MainActivity.showAndDismissProgress().execute();
//            }
//        });
    }

    private class showAndDismissProgress extends AsyncTask<Void, Void, Boolean> {
        Boolean dismis = false;
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.show();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mProgress.dismiss();
        }
    }

    private void connectRongServer() {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {


            @Override
            public void onSuccess(String userId) {
                Log.e("onSuccess","onSuccess userId: " + userId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("onError","onError errorCode: " + errorCode);
            }


            @Override
            public void onTokenIncorrect() {
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            finish();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {


    }

    private void initUserInfo() {
        userIdList = new ArrayList<IMTokenRequest>();
        userIdList.add(new IMTokenRequest("18649168411", "almett01", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=634098145,264198475&fm=27&gp=0.jpg"));
        userIdList.add(new IMTokenRequest("18649168412", "almett02", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2770691011,100164542&fm=27&gp=0.jpg"));
        RongIM.setUserInfoProvider(this, true);
    }

    @Override
    public UserInfo getUserInfo(String s) {

        for (IMTokenRequest i : userIdList) {
            if (i.getUserId().equals(s)) {
                return new UserInfo(i.getUserId(), i.getName(), Uri.parse(i.getPortraitUrl()));
            }
        }

        Log.e("MainActivity", "UserId is : " + s);
        return null;
    }
}