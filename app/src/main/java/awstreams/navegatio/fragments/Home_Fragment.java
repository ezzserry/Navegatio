package awstreams.navegatio.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import awstreams.navegatio.interfaces.OnNewsItemClickListener;
import awstreams.navegatio.R;
import awstreams.navegatio.adapters.NewslistAdapter;
import awstreams.navegatio.databases.HomePostsModel;
import awstreams.navegatio.databases.HomePostsModel_Table;
import awstreams.navegatio.helpers.ConnectionDetector;
import awstreams.navegatio.helpers.Constants;
import awstreams.navegatio.helpers.ServicesHelper;
import awstreams.navegatio.helpers.Utils;
import awstreams.navegatio.models.NewsItem;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by LENOVO on 05/09/2016.
 */
public class Home_Fragment extends Fragment {
    private View view;
    private GridLayoutManager lLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar, topProgressBar;
    private LinearLayout fragmentLinearLayout;
    private NewslistAdapter newslistAdapter;
    private List<NewsItem> postItemsList;
    private String sCategory_id, sQuery;
    private Boolean isInternetPresent = false, isHome = false;
    private ConnectionDetector cd;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Boolean bRefresh;
    private ImageView topImage;
    private TextView tvTitle, tvDescription;
    private NewsItem newsItem;
    private FrameLayout topContainer;
    private int imgHeight;
    private ImageView gif;

    public Home_Fragment(String category_id, String query, Boolean isHome) {
        this.sCategory_id = category_id;
        this.sQuery = query;
        this.isHome = isHome;
    }

    public Home_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews();
        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            if (sCategory_id != null && sQuery == null && !isHome) {
                bRefresh = false;
                getCategoryPosts();
            } else if (sCategory_id == null && sQuery != null && !isHome) {
                bRefresh = false;
                getSearchResults(sQuery);
            } else if (sCategory_id == null && sQuery == null && isHome) {
                bRefresh = false;
                getHomePosts();
            }
        } else {
            bRefresh = false;
            postItemsList = new ArrayList<>();
            List<HomePostsModel> homePostsModels = SQLite.select().from(HomePostsModel.class).where(HomePostsModel_Table.category_id.eq(sCategory_id)).queryList();
            if (homePostsModels.size() > 0) {
                for (HomePostsModel homePostsModel : homePostsModels) {
                    postItemsList.add(homePostsModel.getNewsItem());
                }
                split(postItemsList);
            } else {
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                if (bRefresh == true)
                    swipeRefreshLayout.setRefreshing(false);
               
                gif.setVisibility(View.GONE);
                fragmentLinearLayout.setVisibility(View.VISIBLE);
                topProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "no internet connection", Toast.LENGTH_LONG).show();
            }
//                Snackbar.make(mRecyclerView, "no internet connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }
        return view;
    }


    private void initViews() {
        gif = (ImageView) view.findViewById(R.id.gif);
        Glide.with(getApplicationContext())
                .load(R.raw.loading_gif)
                .into(new GlideDrawableImageViewTarget(gif));
        progressBar = (ProgressBar) view.findViewById(R.id.loadingpanel);
        topProgressBar = (ProgressBar) view.findViewById(R.id.topimage_pb);
        fragmentLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_ll);

        topImage = (ImageView) view.findViewById(R.id.top_iv);

        tvTitle = (TextView) view.findViewById(R.id.title_tv);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), true, false, false));
        tvTitle.setTypeface(Constants.getTypeface_Medium(getActivity()));

        tvDescription = (TextView) view.findViewById(R.id.subtitle_tv);
        tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), false, true, false));
        tvDescription.setTypeface(Constants.getTypeface_Medium(getActivity()));


        topContainer = (FrameLayout) view.findViewById(R.id.top_contatiner);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);

        lLayout = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (sCategory_id != null && !isHome) {
                    bRefresh = true;
                    getCategoryPosts();
                } else if (sCategory_id == null && isHome) {
                    bRefresh = true;
                    getHomePosts();
                } else {
                    Snackbar.make(mRecyclerView, "could't refresh now", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


    }

    private void getCategoryPosts() {
        ServicesHelper.getInstance().getCategory(
                getActivity(), sCategory_id, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("ok")) {
                                postItemsList = new ArrayList<>();
                                JSONArray jsonArray = response.getJSONArray("posts");
                                Gson gson = new GsonBuilder().serializeNulls().create();
                                postItemsList = Arrays.asList(gson.fromJson(jsonArray.toString(), NewsItem[].class));
                                for (NewsItem newsItem : postItemsList) {
                                    HomePostsModel postsModel = null;
//                                    String imgUrlFull = null;
//                                    String imgUrlThumbnail = null;
//                                    try {
//                                    imgUrlFull = newsItem.getImages().getFull().getUrl();
//                                    imgUrlThumbnail = newsItem.getThumbnail();
//                                        if (imgUrlFull != null && imgUrlThumbnail != null)
                                    postsModel = new HomePostsModel(newsItem.getId(), newsItem.getSlug(), newsItem.getTitle(), newsItem.getExcerpt(), sCategory_id);
//                                    }
//                                    catch (NullPointerException e) {
//
//                                        if (imgUrlFull == null && imgUrlThumbnail != null)
//                                            postsModel = new HomePostsModel(newsItem.getId(), newsItem.getSlug(), newsItem.getTitle(), newsItem.getExcerpt(), newsItem.getThumbnail(), null, sCategory_id);
//                                        else if (imgUrlFull != null && imgUrlThumbnail == null)
//                                            postsModel = new HomePostsModel(newsItem.getId(), newsItem.getSlug(), newsItem.getTitle(), newsItem.getExcerpt(), null, newsItem.getImages().getFull().getUrl(), sCategory_id);
//                                        else if (imgUrlFull == null && imgUrlThumbnail == null)
//                                            postsModel = new HomePostsModel(newsItem.getId(), newsItem.getSlug(), newsItem.getTitle(), newsItem.getExcerpt(), null, null, sCategory_id);
//                                    }
                                    postsModel.save();
                                }
                                split(postItemsList);
//                                final List<NewsItem> firstList = postItemsList.subList(0, 1);
//                                updateTopPost(firstList);
//
//                                List<NewsItem> secondList = postItemsList.subList(1, postItemsList.size());
//                                updateUI(secondList);
                                progressBar.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            gif.setVisibility(View.GONE);
                           
                            fragmentLinearLayout.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            if (bRefresh == true)
                                swipeRefreshLayout.setRefreshing(false);

                            gif.setVisibility(View.GONE);
                           
                            fragmentLinearLayout.setVisibility(View.VISIBLE);
                            topProgressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "connection error ", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        if (bRefresh == true)
                            swipeRefreshLayout.setRefreshing(false);
                        gif.setVisibility(View.GONE);
                       
                        fragmentLinearLayout.setVisibility(View.VISIBLE);
                        topProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void getSearchResults(String sQuery) {
        ServicesHelper.getInstance().getSearchResults(getActivity(), sQuery, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("ok")) {
                        JSONArray posts = response.getJSONArray("posts");
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        postItemsList = Arrays.asList(gson.fromJson(posts.toString(), NewsItem[].class));
                        updateUI(postItemsList);
                        progressBar.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        topContainer.setVisibility(View.GONE);
                        gif.setVisibility(View.GONE);
                       
                        fragmentLinearLayout.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    gif.setVisibility(View.GONE);
                   
                    fragmentLinearLayout.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "connection error ", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
               
                gif.setVisibility(View.GONE);
                fragmentLinearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getHomePosts() {
        ServicesHelper.getInstance().getHomePosts(getActivity(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("ok")) {
                        postItemsList = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("posts");
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        postItemsList = Arrays.asList(gson.fromJson(jsonArray.toString(), NewsItem[].class));
                        for (NewsItem newsItem : postItemsList) {
                            HomePostsModel postsModel = null;
                            postsModel = new HomePostsModel(newsItem.getId(), newsItem.getSlug(), newsItem.getTitle(), newsItem.getExcerpt(), sCategory_id);

                            postsModel.save();
                        }
                        split(postItemsList);
                        progressBar.setVisibility(View.GONE);

                        mRecyclerView.setVisibility(View.VISIBLE);
                    }

                    gif.setVisibility(View.GONE);
                   
                    fragmentLinearLayout.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (bRefresh == true)
                        swipeRefreshLayout.setRefreshing(false);
                   
                    fragmentLinearLayout.setVisibility(View.VISIBLE);
                    topProgressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "connection error ", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                if (bRefresh == true)
                    swipeRefreshLayout.setRefreshing(false);
                gif.setVisibility(View.GONE);
               
                fragmentLinearLayout.setVisibility(View.VISIBLE);
                topProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void split(List<NewsItem> postItemsList) {
        final List<NewsItem> firstList = postItemsList.subList(0, 1);
        updateTopPost(firstList);

        List<NewsItem> secondList = postItemsList.subList(1, this.postItemsList.size());
        updateUI(secondList);
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        gif.setVisibility(View.GONE);
       
        fragmentLinearLayout.setVisibility(View.VISIBLE);
    }

    private void updateTopPost(final List<NewsItem> firstList) {
        newsItem = firstList.get(0);
        try {
            Glide.with(getApplicationContext())
                    .load(newsItem.getImages().getFull().getUrl())
                    .fitCenter()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            FrameLayout.LayoutParams lParams;
                            lParams = new FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            topProgressBar.setVisibility(View.GONE);
                            topImage.setVisibility(View.VISIBLE);
                            topImage.setImageResource(R.mipmap.placeholder);
                            topImage.setLayoutParams(lParams);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            imgHeight = Integer.parseInt(newsItem.getImages().getFull().getHeight());

                            if (imgHeight > 500) {
                                imgHeight = 500;
                            }
                            FrameLayout.LayoutParams lParams;
                            lParams = new FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    imgHeight);
                            topProgressBar.setVisibility(View.GONE);
                            topImage.setVisibility(View.VISIBLE);
                            topImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            topImage.setLayoutParams(lParams);
                            return false;
                        }
                    }).into(topImage);
        } catch (NullPointerException error) {

            topProgressBar.setVisibility(View.GONE);
            topImage.setVisibility(View.VISIBLE);
            topImage.setImageResource(R.mipmap.placeholder);
        }
        //Setting text view title
        tvTitle.setText(Utils.convertHTMLtoString(newsItem.getTitle()));
        tvDescription.setText(Utils.convertHTMLtoString(newsItem.getExcerpt()));
        final OnNewsItemClickListener mListener = (OnNewsItemClickListener) getActivity();

        topContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    NewsItem newsItem = firstList.get(0);
                    mListener.onNewsItemClick(newsItem);
                }
            }
        });
    }

    private void updateUI(List<NewsItem> secondList) {
        // adapter

        newslistAdapter = new NewslistAdapter(getActivity(), secondList);
        mRecyclerView.setAdapter(newslistAdapter);
        newslistAdapter.notifyDataSetChanged();
        if (bRefresh == true)
            swipeRefreshLayout.setRefreshing(false);

    }
}
