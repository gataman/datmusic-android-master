/*
 * Copyright 2015. Alashov Berkeli
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package tm.alashow.datmusic.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tm.alashow.datmusic.R;
import tm.alashow.datmusic.model.MenuActivity;
import tm.alashow.datmusic.model.MenuCategory;


public class MenuAdapter extends BaseAdapter {

    private Context mContext;
    private List<Object> menuItems;

    public MenuAdapter(Context context, List<Object> menuItems) {
        mContext = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof MenuActivity ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof MenuActivity;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Object menuItem = getItem(position);

        if (menuItem instanceof MenuCategory) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.view_navigation_drawer_item, parent, false);
            }
            ((TextView) view).setText(((MenuCategory) menuItem).mTitle);
        } else {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.view_navigation_drawer_item, parent, false);
            }

            TextView itemTitle = (TextView) view;
            itemTitle.setText(((MenuActivity) menuItem).mTitle);

            //Setting icon menu item
            int iconResource = ((MenuActivity) menuItem).mIconResource;
            if (iconResource > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    itemTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(iconResource, 0, 0, 0);
                } else {
                    itemTitle.setCompoundDrawablesWithIntrinsicBounds(iconResource, 0, 0, 0);
                }
            }
        }
        return view;
    }
}
