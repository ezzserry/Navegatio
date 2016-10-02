package awstreams.redraccon.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.SubMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import awstreams.redraccon.R;
import awstreams.redraccon.fragments.Home_Fragment;
import awstreams.redraccon.helpers.ConnectionDetector;
import awstreams.redraccon.helpers.Constants;
import awstreams.redraccon.helpers.CustomTypefaceSpan;
import awstreams.redraccon.helpers.ServicesHelper;
import awstreams.redraccon.helpers.Utils;
import awstreams.redraccon.interfaces.OnNewsItemClickLitener;
import awstreams.redraccon.models.Category;
import awstreams.redraccon.models.NewsItem;

public class Base_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnNewsItemClickLitener {

    private NavigationView navigationView;
    private String Bold = "bold";
    private String Light = "light";
    private List<Category> categoryList;
    private Category category;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private TextView tvUsername, tvNickname, tvWelcome;
    private Button btnSign_out;

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
        View hView = navigationView.getHeaderView(0);
        tvUsername = (TextView) hView.findViewById(R.id.profile_username_tv);
        tvNickname = (TextView) hView.findViewById(R.id.profile_nickname_tv);
        tvWelcome = (TextView) hView.findViewById(R.id.welcome_tv);
        tvUsername.setTypeface(Constants.getTypeface_Light(this));
        tvNickname.setTypeface(Constants.getTypeface_Light(this));
        tvWelcome.setTypeface(Constants.getTypeface_Medium(this));
        btnSign_out = (Button) hView.findViewById(R.id.signout_btn);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        btnSign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(Constants.isLoggedin, false);
                    editor.putString(Constants.User_ID, "");
                    editor.apply();
                    Intent intent = new Intent(Base_Activity.this, Sign_up_Activity.class);
                    startActivity(intent);
                    finish();

                } else
                    Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();
            }
        });
        if (isInternetPresent) {
            getUserInfo();
            getCategoriesList();
        } else
            Toast.makeText(this, "No internet connection to load the categories", Toast.LENGTH_LONG).show();


    }

    private void getUserInfo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String id = prefs.getString(Constants.User_ID, "");
        ServicesHelper.getInstance().getUserInfo(getApplicationContext(), id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("ok")) {
                        tvUsername.setText(response.getString("displayname"));
//                        tvNickname.setText(response.getString("nickname"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void getCategoriesList() {
        ServicesHelper.getInstance().getCategoryIndex(getApplicationContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("ok")) {
                        categoryList = new ArrayList<Category>();
                        JSONArray categories = response.getJSONArray("categories");
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        categoryList = Arrays.asList(gson.fromJson(categories.toString(), Category[].class));
                        updateNavigationList(categoryList);
                        onNavigationItemSelected(navigationView.getMenu().getItem(0));
//                        navigationView.getMenu().getItem(1).setChecked(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


    private void updateNavigationList(List<Category> categoryList) {

        for (int i = 0; i < categoryList.size(); i++) {
            category = categoryList.get(i);
            int id = Integer.parseInt(category.getId());
            navigationView.getMenu().add(1, id, i, category.getTitle());
        }
        setMenuFont();
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
                    applyFontToMenuItem(subMenuItem, Bold);
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
        String id = String.valueOf(item.getItemId());
        if (id.equals("") || id.equals(null) || id.isEmpty()) {
            id = "62";
        }
        Fragment fragment = new Home_Fragment(id, null);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment).commitAllowingStateLoss();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        setMenuFont();
        applyFontToMenuItem(item, Bold);
        return true;
    }

    @Override
    public void onNewsItemClick(NewsItem newsItem) {
        String id = newsItem.getId();
        Intent intent = new Intent(this, DetailedNews_Activity.class);
        intent.putExtra(getResources().getString(R.string.post_intent_key), id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setGravity(Gravity.END);
        final EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint(getResources().getString(R.string.action_search));
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.WHITE);
        // Configure the search info and add any event listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = txtSearch.getText().toString();
                Fragment fragment = new Home_Fragment(null, query);
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
//        searchItem.expandActionView();
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Write your code here
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
