package com.tundem.aboutlibraries.ui;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.tundem.aboutlibraries.Libs;
import com.tundem.aboutlibraries.R;
import com.tundem.aboutlibraries.ui.utils.UIUtils;
import com.tundem.aboutlibraries.ui.view.DrawInsetsFrameLayout;

/**
 * Created by mikepenz on 04.06.14.
 */
public class LibsCompatActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean customTheme = false;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int themeId = bundle.getInt(Libs.BUNDLE_THEME, -1);
            if (themeId != -1) {
                customTheme = true;
                setTheme(themeId);
            }
        }

        setContentView(R.layout.activity_opensource);

        String title = "";
        boolean usedAccentColor = false;
        if (bundle != null) {
            String accentColorString = bundle.getString(Libs.BUNDLE_ACCENT_COLOR);
            boolean useTranslucentDecor = bundle.getBoolean(Libs.BUNDLE_TRANSLUCENT_DECOR, false);
            title = bundle.getString(Libs.BUNDLE_TITLE);

            if (!TextUtils.isEmpty(accentColorString)) {
                usedAccentColor = true;

                int accentColor = Color.parseColor(accentColorString);
                int accentSecondaryColor = Color.parseColor("#88" + Integer.toHexString(accentColor).toUpperCase().substring(2));

                UIUtils.init(getApplicationContext(), accentColor, accentSecondaryColor, true, true, true, true);

                if (useTranslucentDecor) {
                    UIUtils.getInstance().initActivity(this);
                }

                DrawInsetsFrameLayout drawInsetsFrameLayout = (DrawInsetsFrameLayout) findViewById(R.id.drawinsetsframelayout);
                drawInsetsFrameLayout.setOnInsetsCallback(new DrawInsetsFrameLayout.OnInsetsCallback() {
                    @Override
                    public void onInsetsChanged(Rect insets) {
                        findViewById(R.id.frame_container).setLayoutParams(UIUtils.getInstance().handleTranslucentDecorMargins(((FrameLayout.LayoutParams) findViewById(R.id.frame_container).getLayoutParams()), insets));
                    }
                });
            }
        }


        LibsFragment fragment = new LibsFragment();
        fragment.setArguments(bundle);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            if (!customTheme && usedAccentColor) {
                ab.setBackgroundDrawable(null);
            }
            ab.setDisplayHomeAsUpEnabled(true);

            if (TextUtils.isEmpty(title)) {
                ab.setDisplayShowTitleEnabled(false);
            } else {
                ab.setDisplayShowTitleEnabled(true);
                ab.setTitle(title);
            }


            ab.setDisplayUseLogoEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                LibsCompatActivity.this.finish();
                return true;
            }
            default:
                return false;
        }
    }
}
