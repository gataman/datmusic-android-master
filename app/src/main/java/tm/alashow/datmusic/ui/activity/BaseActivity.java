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

package tm.alashow.datmusic.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tm.alashow.datmusic.Config;
import tm.alashow.datmusic.R;
import tm.alashow.datmusic.android.IntentManager;
import tm.alashow.datmusic.model.MenuActivity;
import tm.alashow.datmusic.ui.adapter.MenuAdapter;
import tm.alashow.datmusic.util.U;


public abstract class BaseActivity extends AppCompatActivity {

    public static final int TYPE_NEW = 0;
    public static final int TYPE_REFRESH = 1;
    public static final int TYPE_PAGINATION = 2;

    private boolean shouldGoInvisible;
    private MenuAdapter menuAdapter;
    private final List<Object> menuItems = new ArrayList<>();
    protected Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mOldTitle;
    private String mOldSubtitle;
    private Handler mHandler;

    @BindView(R.id.drawer) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_drawer_list_view) ListView mDrawerList;
    @BindView(R.id.powered_by) TextView poweredBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mHandler = new Handler();
        setSupportActionBar(mToolbar);

        poweredBy.setText(Html.fromHtml(getString(R.string.powered_by)));
        poweredBy.setMovementMethod(LinkMovementMethod.getInstance());
        U.stripUnderlines(poweredBy);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View arg0) {
                shouldGoInvisible = false;
                if (mOldTitle != null) {
                    mToolbar.setTitle(mOldTitle);
                }
                if (mOldSubtitle != null) {
                    mToolbar.setSubtitle(mOldSubtitle);
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View arg0) {
                shouldGoInvisible = true;
                mOldTitle = mToolbar.getTitle().toString();
                mOldSubtitle = (mToolbar.getSubtitle() != null) ? mToolbar.getSubtitle().toString() : null;
                mToolbar.setTitle(R.string.app_name);
                mToolbar.setSubtitle(null);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(!isChildActivity());
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    onBackPressed();
                }
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark_material_dark));

        //Adding items menu
        populateMenu();

        //Showing back button on action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Menu listItem click callback
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                mDrawerLayout.closeDrawers();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeActivity(((MenuActivity) menuItems.get(position)).mActivityTag);
                    }
                }, 100);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++)
            menu.getItem(i).setVisible(!shouldGoInvisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //Close MenuDrawer if opened
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    /**
     * Starts activity if activity tag handled, else close menu
     *
     * @param activityTag activityTag field of mainactivity object
     */
    private void changeActivity(String activityTag) {
        if (!getActivityTag().equals(activityTag)) {
            if (activityTag.equals(Config.ACTIVITY_TAG_MAIN)) {
                IntentManager.with(this).main();
            } else if (activityTag.equals(Config.ACTIVITY_TAG_PREFERENCES)) {
                IntentManager.with(this).preferences();
            } else if (activityTag.equals("web")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.MAIN_SERVER));
                startActivity(intent);
            }
        }
    }

    private void populateMenu() {
        menuItems.clear();
        menuItems.add(new MenuActivity(getString(R.string.app_name), R.mipmap.ic_launcher, Config.ACTIVITY_TAG_MAIN));
        menuItems.add(new MenuActivity(getString(R.string.preferences_title), R.mipmap.ic_settings, Config.ACTIVITY_TAG_PREFERENCES));
        menuItems.add(new MenuActivity(getString(R.string.web_version), R.mipmap.ic_web, "web"));
        menuAdapter = new MenuAdapter(this, menuItems);
        mDrawerList.setAdapter(menuAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        populateMenu();
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    /**
     * Layout resource for activity content.
     *
     * @return int, layout resource
     */
    protected abstract
    @LayoutRes
    int getLayoutResourceId();

    /**
     * If true sets action bar title click callback finish activity,
     * else open menu drawer.
     *
     * @return boolean is child or main activity
     */
    protected abstract Boolean isChildActivity();

    /**
     * Tag of activity. For not opening same activity
     * twice after click in menu drawer.
     *
     * @return String activity tag
     */
    protected abstract String getActivityTag();
}