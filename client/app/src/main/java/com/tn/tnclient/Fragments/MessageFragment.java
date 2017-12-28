package com.tn.tnclient.Fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tn.tnclient.R;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    private View message_layout;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initConversationList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        message_layout = inflater.inflate(R.layout.fragment_message, container, false);
        return message_layout;
    }

    private Fragment initConversationList() {

        /**
         * appendQueryParameter对具体的会话列表做展示
         */
        ConversationListFragment listFragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getContext().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationList")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true")//设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                // .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                //.appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置私聊会是否聚合显示
                .build();
        listFragment.setUri(uri);
        return listFragment;
    }

}
