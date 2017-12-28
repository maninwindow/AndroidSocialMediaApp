package com.tn.tnclient.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tn.tnclient.Activities.ImageUploader;
import com.tn.tnclient.R;
import com.tn.tnclient.Utils.SessionHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    private View me_layout;
    private TextView user_profile_name;
    private SharedPreferences sharedPreference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference= PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        me_layout = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this,me_layout);
        //Set user information
        user_profile_name = (TextView)me_layout.findViewById(R.id.user_profile_name);
        user_profile_name.setText(sharedPreference.getString("username",null));
//        user_profile_occupation = (TextView)me_layout.findViewById(R.id.user_profile_occupation);
//        user_profile_occupation.setText(sharedPreference.getString("occupation",null));
//        heade_image = (ImageView) me_layout.findViewById();
//        heade_image.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                onClickAvatar();
//            }
//        });

        //Logout
        logout();
        return me_layout;
    }

    private void logout() {
        Button logout = (Button) me_layout.findViewById(R.id.logout_button);
        logout.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionHelper sessionHelper=new SessionHelper(getActivity());
                    sessionHelper.logOutUser();
                }
            }
        );
    }

    @OnClick(R.id.user_profile_photo)
    public void onClickUserProfile(){
        startActivity(new Intent(getContext(), ImageUploader.class));
    }
}
