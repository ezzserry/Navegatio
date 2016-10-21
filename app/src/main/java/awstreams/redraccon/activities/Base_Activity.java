package awstreams.redraccon.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.Gravity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import awstreams.redraccon.fragments.Feedback_Fragment;
import awstreams.redraccon.fragments.Home_Fragment;
import awstreams.redraccon.fragments.Setting_Fragment;
import awstreams.redraccon.helpers.ConnectionDetector;
import awstreams.redraccon.helpers.Constants;
import awstreams.redraccon.helpers.ServicesHelper;
import awstreams.redraccon.interfaces.OnCategoryClickListener;
import awstreams.redraccon.interfaces.OnNewsItemClickLitener;
import awstreams.redraccon.models.Category;
import awstreams.redraccon.models.NewsItem;

public class Base_Activity extends AppCompatActivity
        implements OnNewsItemClickLitener, OnCategoryClickListener, View.OnClickListener {

    private String Bold = "bold";
    private String Light = "light";
    private List<Category> categoryList;
    private Category category;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private TextView tvUsername;
    private LinearLayout rlCategoriesList;
    private TextView tvFeedback, tvRate, tvSetting;
    private OnCategoryClickListener mListener;
    private TextView[] myTextViews;

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
        initViews();
        mListener = (OnCategoryClickListener) this;

        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            getUserInfo();
            getCategoriesList();
        } else
            Toast.makeText(this, "No internet connection to load the categories", Toast.LENGTH_LONG).show();


    }

    private void getUserInfo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = prefs.edit();
        String id = prefs.getString(Constants.User_ID, "");
        ServicesHelper.getInstance().getUserInfo(getApplicationContext(), id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("ok")) {
                        tvUsername.setText(response.getString("displayname"));
                        editor.putString(Constants.User_NAME, response.getString("displayname"));
                        editor.apply();
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
                        onCategoryClick(categoryList.get(0), myTextViews[0]);
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


    private void updateNavigationList(final List<Category> categoryList) {
        final int noTextviews = categoryList.size();
        myTextViews = new TextView[noTextviews]; // create an empty array;

        for (int i = 0; i < noTextviews; i++) {
            category = categoryList.get(i);
            category.setId(categoryList.get(i).getId());
            category.setSlug(categoryList.get(i).getSlug());
            category.setTitle(categoryList.get(i).getTitle());
            LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextView tvCategoryName = new TextView(this);
            tvCategoryName.setText(category.getTitle());
//            tvCategoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP,Constants.getTextAppSize(this,false,true,false));
            tvCategoryName.setTextSize(15);
            tvCategoryName.setLayoutParams(lprams);
            tvCategoryName.setId(Integer.parseInt(category.getId()));
            tvCategoryName.setTypeface(Constants.getTypeface_Light(this));
            tvCategoryName.setGravity(Gravity.CENTER);
            lprams.setMargins(0, 10, 0, 30);
            rlCategoriesList.addView(tvCategoryName);
            myTextViews[i] = tvCategoryName;

            final int finalI = i;
            tvCategoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        Category categoryItem = categoryList.get(finalI);
                        mListener.onCategoryClick(categoryItem, myTextViews[finalI]);
                    }
                }
            });
        }
//        setMenuFont();


    }

    private void setMenuFont() {
        for (int i = 0; i < myTextViews.length; i++) {
            applySelectedCategFont(myTextViews[i], Light);
            myTextViews[i].setTextSize(15);
        }

    }

    private void applySelectedCategFont(TextView category, String fonttype) {
        if (fonttype.equals(Light)) {
            Typeface font = Constants.getTypeface_Light(this);
            category.setTypeface(font);

        } else if (fonttype.equals(Bold)) {
            Typeface font = Constants.getTypeface_Medium(this);
            category.setTypeface(font);
        }
    }

    private void initViews() {
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scrollview);
        rlCategoriesList = (LinearLayout) nestedScrollView.findViewById(R.id.categories_rl);


        tvFeedback = (TextView) nestedScrollView.findViewById(R.id.menu_feedback_tv);
        tvFeedback.setTypeface(Constants.getTypeface_Light(this));
        tvFeedback.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

        tvRate = (TextView) nestedScrollView.findViewById(R.id.menu_rate_us_tv);
        tvRate.setTypeface(Constants.getTypeface_Light(this));
        tvRate.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

        tvSetting = (TextView) nestedScrollView.findViewById(R.id.menu_setting_tv);
        tvSetting.setTypeface(Constants.getTypeface_Light(this));
        tvSetting.setOnClickListener(this);
        tvSetting.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

        tvUsername = (TextView) findViewById(R.id.profile_username_tv);
        tvUsername.setTypeface(Constants.getTypeface_Light(this));
        tvUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
//                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCategoryClick(Category category, TextView tvCategoryName) {
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();
        } else {
            String id = String.valueOf(category.getId());
            if (id.equals("") || id.equals(null) || id.isEmpty()) {
                id = "62";
            }
            Fragment fragment = new Home_Fragment(id, null);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commitAllowingStateLoss();

            setMenuFont();
            applySelectedCategFont(tvCategoryName, Bold);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        switch (v.getId()) {
            case R.id.menu_setting_tv:
                fragment = new Setting_Fragment();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commitAllowingStateLoss();
                setMenuFont();
                applySelectedCategFont(tvSetting, Bold);

                break;
            case R.id.menu_feedback_tv:
                fragment = new Feedback_Fragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commitAllowingStateLoss();
                setMenuFont();
                applySelectedCategFont(tvFeedback, Bold);
                break;
            case R.id.menu_rate_us_tv:
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


    }
}
