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

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import tm.alashow.datmusic.Config;
import tm.alashow.datmusic.R;
import tm.alashow.datmusic.ui.fragment.PreferencesFragment;
import tm.alashow.datmusic.util.U;


/**
 * Created by alashov on 18/01/15.
 */
public class PreferencesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment preferencesFragment = new PreferencesFragment();
        U.attachFragment(this, preferencesFragment);

        setTitle(R.string.preferences_title);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected Boolean isChildActivity() {
        return true;
    }

    @Override
    protected String getActivityTag() {
        return Config.ACTIVITY_TAG_PREFERENCES;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setTitle(R.string.preferences_title);
        Fragment preferencesFragment = new PreferencesFragment();
        U.attachFragment(this, preferencesFragment);
    }
}
