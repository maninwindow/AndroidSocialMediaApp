package com.tn.tnclient.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tn.tnclient.Activities.ConversationActivity;
import com.tn.tnclient.Api.ApiClient;
import com.tn.tnclient.Api.ApiInterface;
import com.tn.tnclient.Models.IMToken;
import com.tn.tnclient.Models.User;
import com.tn.tnclient.R;
import com.tn.tnclient.SharedContext;
import com.tn.tnclient.Utils.CustomListAdapter;
import com.yalantis.phoenix.PullToRefreshView;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NearbyFragment extends Fragment implements RongIM.UserInfoProvider{

    public final String TABLENAME = "user";
    private PullToRefreshView mPullToRefreshView;
    private View nearbyView;
    private TopRightMenu mTopRightMenu;
    private ImageView filter;
    ArrayList<User> arrayList;
    ListView user_list;
    CustomListAdapter adapter;
    ViewGroup container;
    private JsonArray jsonArray;
    int jsonArraySize;
    private int ROWS = 20;
    private int userSize;
    private boolean lastPage = false;
    private SharedPreferences SPreference = SharedContext.getInstance().getSharedPreferences();
    private SharedPreferences.Editor editor = SPreference.edit();
    private String userList;
    private User thisUser;
    private String phone;
    private String token;

    public static NearbyFragment newInstance() {
        NearbyFragment fragment = new NearbyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        nearbyView = getActivity().getLayoutInflater().inflate(R.layout.fragment_nearby, container, false);
        user_list = (ListView) nearbyView.findViewById(R.id.list_view);
        user_list.setOverScrollMode(View.OVER_SCROLL_NEVER);
        userSize();
        pullAndLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        filterPopupWindow();
        arrayList = new ArrayList<>();
        userList = SPreference.getString("userList","");
        if(userList.isEmpty()) {
            furtherRequest(ROWS);
        }else{
            setListToAdapter(userList);
        }
        user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //ListEntry entry= (ListEntry) parent.getAdapter().getItem(position);
                thisUser = (User) parent.getItemAtPosition(position);
                phone = thisUser.getPhone();
                new getToken();
                connectToRongCloud(token);
//                RongIM.getInstance().startPrivateChat(getContext(),thisUser.getPhone(),thisUser.getUsername());
                Intent intent = new Intent(getContext(),ConversationActivity.class);
//                intent.putExtra(EXTRA_MESSAGE, entry.getMessage());
                startActivity(intent);
            }
        });

        return nearbyView;
    }

    private void connectToRongCloud(String token){
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            @Override
            public void onSuccess(String userId) {
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(userId,thisUser.getUsername(),Uri.parse(thisUser.getAvatar())));
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                // Log.e("onError", "onError userid:" + errorCode.getValue());//获取错误的错误码
                Log.e("NearbyFragment", "connect failure errorCode is : " + errorCode.getValue());
            }


            @Override
            public void onTokenIncorrect() {
                Log.e("NearbyFragment", "token is error ,please check token and appkey");
            }
        });
    }

    private class getToken extends AsyncTask<Void, Void, Void>{

        protected Void doInBackground(Void... params){

            return null;
        }

        protected void onPostExecute(String result) {

        }

        protected void onPreExecute() {
            Retrofit retrofit = ApiClient.getClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            String newPhone = phone.substring(1,phone.length()-1);
            Call<IMToken> call = apiInterface.retrieveToken(newPhone);
            call.enqueue(new Callback<IMToken>() {

                @Override
                public void onResponse(Call<IMToken> call, Response<IMToken> response) {
                    IMToken tokenResult = response.body();
                    token = tokenResult.getToken();
                }

                @Override
                public void onFailure(Call<IMToken> call, Throwable t) {
                }
            });
        }
    }

    private void userSize(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getUserSize().execute(ApiClient.USER_SIZE);
            }
        });
    }

    private void furtherRequest(final int limit){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute(ApiClient.FURTHER_REQUEST + "?limit=" + limit);
            }
        });
    }

    private void pullAndLoad(){
        // Set a listener to be invoked when the list should be refreshed.
        ((PullAndLoadListView) user_list)
                .setOnRefreshListener(new OnRefreshListener() {

                    public void onRefresh() {
                        // Do work to refresh the list here.
                        new PullToRefreshDataTask().execute();
                    }
                });

        // set a listener to be invoked when the list reaches the end
        ((PullAndLoadListView) user_list)
                .setOnLoadMoreListener(new OnLoadMoreListener() {

                    public void onLoadMore() {
                        // Do the work to load more items at the end of list
                        // here
                        new LoadMoreDataTask().execute();
                    }
                });
    }


    private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            if (isCancelled()) {
                return null;
            }

            // Simulates a background task
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            int user_size_sp = SPreference.getInt("user_size", 0);
            if(user_size_sp != 0)
                userSize = user_size_sp;
            else
                userSize();
            while(ROWS < userSize){
                if(lastPage == true){
                    break;
                }else if(userSize - ROWS < 10){
                    for(int i = 0; i<10; i++){
                        ROWS += 1;
                        if(ROWS == userSize){
                            lastPage = true;
                            furtherRequest(ROWS);
                            break;
                        }
                    }
                }else{
                    furtherRequest(ROWS);
                    ROWS += 10;
                    break;
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            // We need notify the adapter that the data have been changed
            ((BaseAdapter) adapter).notifyDataSetChanged();

            // Call onLoadMoreComplete when the LoadMore task, has finished
            ((PullAndLoadListView) user_list).onLoadMoreComplete();

            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            // Notify the loading more operation has finished
            ((PullAndLoadListView) user_list).onLoadMoreComplete();
        }
    }

    private class PullToRefreshDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            if (isCancelled()) {
                return null;
            }

            // Simulates a background task
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

//            for (int i = 0; i < mAnimals.length; i++)
//                mListItems.addFirst(mAnimals[i]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            // We need notify the adapter that the data have been changed
            ((BaseAdapter) adapter).notifyDataSetChanged();

            // Call onLoadMoreComplete when the LoadMore task, has finished
            ((PullAndLoadListView) user_list).onRefreshComplete();

            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            // Notify the loading more operation has finished
            ((PullAndLoadListView) user_list).onLoadMoreComplete();
        }
    }

    private class getUserSize extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            userSize = Integer.parseInt(content.replaceAll("\\D+",""));
            editor.putInt("user_size",userSize);
        }
    }

    private class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            editor.putString("userList",content);
            editor.commit();
            setListToAdapter(content);
        }
    }

    private void setListToAdapter(String content){
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(content);
            jsonArray = jsonElement.getAsJsonArray();

//                JSONObject jsonObject = new JSONObject();
//                JSONArray jsonArray =  jsonObject.getJSONArray(content);
            jsonArraySize = jsonArray.size();
            arrayList.clear();
            for (int i = 0; i < jsonArraySize; i++) {
                JsonObject element = (JsonObject) jsonArray.get(i);
                String thisUserName = SPreference.getString("username",null);
                String userFromDB = element.get("username").toString();
                if (element != null && !userFromDB.substring(1,userFromDB.length()-1).equals(thisUserName)) {
                    arrayList.add(new User(
                            element.get("phone").toString(),
                            element.get("username").toString(),
                            element.get("avatar").toString(),
                            element.get("tags").toString(),
                            element.get("distance").toString()
                    ));
                }
            }
        }catch (JsonIOException e) {
            e.printStackTrace();
        }
        RongIM.setUserInfoProvider(this, true);

        adapter = new CustomListAdapter(
                getContext(), R.layout.custom_list_layout, arrayList
        );
//        ((LinearLayout)nearbyView.findViewById(R.id.list_view_linear)).getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
//        ((LinearLayout)nearbyView.findViewById(R.id.loadviewer)).setVisibility(LinearLayout.GONE);
        user_list.setAdapter(adapter);
    }

    @Override
    public UserInfo getUserInfo(String s) {

        for (User i : arrayList) {
            if (i.getPhone().equals(s)) {
                return new UserInfo(i.getPhone(), i.getUsername(), Uri.parse(i.getAvatar()));
            }
        }
        Log.e("getUserInfo: ","User info is " + s);
        return null;
    }


    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    //Right top user filter
    private void filterPopupWindow(){
        filter = (ImageView)nearbyView.findViewById(R.id.more);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTopRightMenu = new TopRightMenu(getActivity());
                List<MenuItem> menuItems = new ArrayList<>();
                menuItems.add(new com.zaaach.toprightmenu.MenuItem("女性"));
                menuItems.add(new com.zaaach.toprightmenu.MenuItem("男性"));
                menuItems.add(new com.zaaach.toprightmenu.MenuItem("中国人"));
                menuItems.add(new com.zaaach.toprightmenu.MenuItem("外国人"));
                mTopRightMenu
                        .setHeight(560)
                        .setWidth(365)
                        .showIcon(false)
                        .dimBackground(true)
                        .needAnimationStyle(true)
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                        .addMenuList(menuItems)
                        .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                Toast.makeText(getActivity(), "点击菜单:" + position, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .showAsDropDown(filter, -225, 0);
            }
        });
    }

    //Pull to refresh component
//    private void pullToRefreshComponent(){
//        mPullToRefreshView = (PullToRefreshView) nearbyView.findViewById(R.id.pull_to_refresh);
//        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
//            public static final int REFRESH_DELAY = 1;
//
//            @Override
//            public void onRefresh() {
//                mPullToRefreshView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPullToRefreshView.setRefreshing(false);
//                    }
//                }, REFRESH_DELAY);
//            }
//        });
//    }



    private void retrieveUsers() {

        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<User>> call = apiInterface.retrieveUsers(TABLENAME);
        call.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> userList = response.body();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), "登录失败？", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

