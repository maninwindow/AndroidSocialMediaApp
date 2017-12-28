package com.tn.tnclient.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.tn.tnclient.R;

/**
 * Created by alimjan on 10/27/17.
 */

public class ConversationActivity extends FragmentActivity {

    private TextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mName = (TextView) findViewById(R.id.name);

        String sId = getIntent().getData().getQueryParameter("targetId");//targetId:单聊即对方ID，群聊即群组ID
        String sName = getIntent().getData().getQueryParameter("title");//获取昵称

    }
}
