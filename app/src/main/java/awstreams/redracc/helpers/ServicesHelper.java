package awstreams.redracc.helpers;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * Created by LENOVO on 30/08/2016.
 */
public class ServicesHelper {
    private String URL;
    private static ServicesHelper sharedInstance;


    public synchronized static ServicesHelper getInstance() {
        if (sharedInstance == null) {
            sharedInstance = new ServicesHelper();
        }
        return sharedInstance;
    }

    public void get_Nonce(Context context, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        URL = Constants.URL_GET_NONCE;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }

    public void signUp(Context context, String nonce, String sUsername, String sEmail, String sPassword, String sAge, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        String URL = Constants.URL_REGISTER;
        URL = String.format(URL, nonce, sUsername, sUsername, sEmail, sPassword, sAge);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);

    }

    public void signIn(Context context, String sUsername, String sPassword, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        String URL = Constants.URL_SIGN_IN;
        URL = String.format(URL, sUsername, sPassword);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);

    }

    public void getCategory(Context context, String id, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        String URL = Constants.URL_GET_CATEGORIES;
        URL = String.format(URL, id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }

    public void getHomePosts(Context context, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        String URL = Constants.URL_GET_HOME_POSTS;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }

    public void getTag(Context context, String id, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        String URL = Constants.URL_GET_TAG;
        URL = String.format(URL, id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }

    public void getPostbyID(Context context, String id, String slug, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        String URL = null;
        if (id.isEmpty() && !slug.isEmpty()) {
            URL = Constants.URL_GET_POST_BY_SLUG;
            URL = String.format(URL, slug);
        } else if (!id.isEmpty() && slug.isEmpty()) {
            URL = Constants.URL_GET_POST_BY_ID;
            URL = String.format(URL, id);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
//        StringRequest stringRequest = new StringRequest(URL, success, errorListener);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void getPostbySlug(Context context, String slug, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        String URL = Constants.URL_GET_POST_BY_SLUG;
        URL = String.format(URL, slug);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
//        StringRequest stringRequest = new StringRequest(URL, success, errorListener);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void getCategoryIndex(Context context, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        URL = Constants.URL_GET_CATEGORY_INDEX;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }

    public void getUserInfo(Context context, String id, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        URL = Constants.URL_GET_USER_INFO;
        URL = String.format(URL, id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }

    public void getSearchResults(Context context, String query, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        String URL = Constants.URL_GET_SEARCH_RESULTS;
        URL = String.format(URL, query);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }

    public void fbLogin(Context context, String accessToken, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        String URL = Constants.URL_FB_LOGIN;
        URL = String.format(URL, accessToken);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }

    public void sendFeedback(Context context, String feedback, String username, Response.Listener<JSONObject> success, Response.ErrorListener errorListener) {
        String URL = Constants.URL_FEEDBACK;
        URL = String.format(URL, username, feedback);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);

    }

    public void resetPassword(Context context,String username,Response.Listener<JSONObject> success,Response.ErrorListener errorListener){
        String URL = Constants.URL_REST_PASSWORD;
        URL = String.format(URL, username);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), success, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);

    }
}

