package awstreams.redraccon.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import awstreams.redraccon.R;
import awstreams.redraccon.fragments.Home_Fragment;
import awstreams.redraccon.helpers.Constants;
import awstreams.redraccon.helpers.CustomTypefaceSpan;

public class Base_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private NavigationView navigationView;
    private String Bold = "bold";
    private String Light = "light";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        setMenuFont();
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

    }

    private void setMenuFont() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            //for applying a font to main menu

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem, Light);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi, Light);
        }
    }

    private void applyFontToMenuItem(MenuItem mi, String fonttype) {
        if (fonttype.equals(Light)) {
            Typeface font = Constants.getTypeface_Light(this);
            SpannableString mNewTitle = new SpannableString(mi.getTitle());
            mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mi.setTitle(mNewTitle);
        } else if (fonttype.equals(Bold)) {
            Typeface font = Constants.getTypeface_Medium(this);
            SpannableString mNewTitle = new SpannableString(mi.getTitle());
            mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mi.setTitle(mNewTitle);
        }
    }

//    private void initViews() {
//
//        tvLatestNews = (TextView) lSidemenu.findViewById(R.id.latest_news_row_tv);
//        tvLatestNews.setOnClickListener(this);
//        tvLatestNews.setTypeface(Constants.getTypeface_Light(this));
//
//        tvTrending = (TextView) lSidemenu.findViewById(R.id.trending_row_tv);
//        tvTrending.setOnClickListener(this);
//        tvTrending.setTypeface(Constants.getTypeface_Light(this));
//
//        tvMostViewed = (TextView) lSidemenu.findViewById(R.id.most_viewed_row_tv);
//        tvMostViewed.setOnClickListener(this);
//        tvMostViewed.setTypeface(Constants.getTypeface_Light(this));
//
//        tvBuzz = (TextView) lSidemenu.findViewById(R.id.buzz_row_tv);
//        tvBuzz.setOnClickListener(this);
//        tvBuzz.setTypeface(Constants.getTypeface_Light(this));
//
//        tvMovies = (TextView) lSidemenu.findViewById(R.id.movies_row_tv);
//        tvMovies.setOnClickListener(this);
//        tvMovies.setTypeface(Constants.getTypeface_Light(this));
//
//        tvMusic = (TextView) lSidemenu.findViewById(R.id.music_row_tv);
//        tvMusic.setOnClickListener(this);
//        tvMusic.setTypeface(Constants.getTypeface_Light(this));
//
//        tvLifeStyle = (TextView) lSidemenu.findViewById(R.id.life_style_row_tv);
//        tvLifeStyle.setOnClickListener(this);
//        tvLifeStyle.setTypeface(Constants.getTypeface_Light(this));
//
//        tvPopCulture = (TextView) lSidemenu.findViewById(R.id.pop_cultural_row_tv);
//        tvPopCulture.setOnClickListener(this);
//        tvPopCulture.setTypeface(Constants.getTypeface_Light(this));
//
//        tvTravel = (TextView) lSidemenu.findViewById(R.id.travel_row_tv);
//        tvTravel.setOnClickListener(this);
//        tvTravel.setTypeface(Constants.getTypeface_Light(this));
//
//        tvEats = (TextView) lSidemenu.findViewById(R.id.eats_row_tv);
//        tvEats.setOnClickListener(this);
//        tvEats.setTypeface(Constants.getTypeface_Light(this));
//
//        tvEvents = (TextView) lSidemenu.findViewById(R.id.events_row_tv);
//        tvEvents.setOnClickListener(this);
//        tvEvents.setTypeface(Constants.getTypeface_Light(this));
//
//        tvFeedback = (TextView) lSidemenu.findViewById(R.id.menu_feedback_tv);
//        tvFeedback.setOnClickListener(this);
//        tvFeedback.setTypeface(Constants.getTypeface_Light(this));
//
//        tvRate = (TextView) lSidemenu.findViewById(R.id.menu_rate_us_tv);
//        tvRate.setOnClickListener(this);
//        tvRate.setTypeface(Constants.getTypeface_Light(this));
//
//        tvSetting = (TextView) lSidemenu.findViewById(R.id.menu_setting_tv);
//        tvSetting.setOnClickListener(this);
//        tvSetting.setTypeface(Constants.getTypeface_Light(this));
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.latest_news_row_tv:
                Toast.makeText(this, "latest news ", Toast.LENGTH_LONG).show();

                break;
            case R.id.trending_row_tv:
                Toast.makeText(this, "trending ", Toast.LENGTH_LONG).show();
                break;
            case R.id.most_viewed_row_tv:
                break;
            case R.id.buzz_row_tv:
                break;
            case R.id.music_row_tv:
                break;
            case R.id.movies_row_tv:
                break;
            case R.id.eats_row_tv:
                break;
            case R.id.pop_cultural_row_tv:
                break;
            case R.id.life_style_row_tv:
                break;
            case R.id.travel_row_tv:
                break;
            case R.id.events_row_tv:
                break;
            case R.id.menu_feedback_tv:
                break;
            case R.id.menu_rate_us_tv:
                break;
            case R.id.menu_setting_tv:
                break;

        }
        Fragment fragment = new Home_Fragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        setMenuFont();
        applyFontToMenuItem(item, Bold);
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}


