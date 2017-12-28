package com.tn.tnclient.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tn.tnclient.Models.User;
import com.tn.tnclient.R;

import java.util.ArrayList;

/**
 * Created by alimjan on 11/3/17.
 */

public class CustomListAdapter extends ArrayAdapter<User> {

    ArrayList<User> products;
    Context context;
    int resource;

    public CustomListAdapter(Context context, int resource, ArrayList<User> products) {
        super(context, resource, products);
        this.products = products;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_list_layout, null, true);

        }
        User user = getItem(position);

        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
        Picasso.with(context).load(user.getAvatar().substring(1,user.getAvatar().length()-1)).into(avatar);

        TextView username = (TextView) convertView.findViewById(R.id.username);
        username.setText(user.getUsername().substring(1,user.getUsername().length()-1));

        TextView tags = (TextView) convertView.findViewById(R.id.tags);
        tags.setText(user.getTags().substring(1,user.getTags().length()-1));

        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        distance.setText(user.getDistance() + " 米");

        return convertView;
    }
}