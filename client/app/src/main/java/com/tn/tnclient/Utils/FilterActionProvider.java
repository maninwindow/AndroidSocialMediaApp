package com.tn.tnclient.Utils;

import android.content.Context;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.tn.tnclient.R;

/**
 * Created by alimjan on 10/17/17.
 */

public class FilterActionProvider extends ActionProvider{

    private Context context;

    /**
     * Creates a new instance. ActionProvider classes should always implement a
     * constructor that takes a single Context parameter for inflating from menu XML.
     *
     * @param context Context for accessing resources.
     */
    public FilterActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu){
        subMenu.clear();
        subMenu.add(context.getString(R.string.filter_menu_female))
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
        subMenu.add(context.getString(R.string.filter_menu_male))
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
    }

    @Override
    public boolean hasSubMenu(){
        return true;
    }
}
