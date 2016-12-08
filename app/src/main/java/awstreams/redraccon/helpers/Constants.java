package awstreams.redraccon.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import awstreams.redraccon.R;

/**
 * Created by LENOVO on 29/08/2016.
 */
public class Constants {
    public static SharedPreferences sharedPreferences;
    public static Typeface typeface_Light, typeface_Medium;

    public static String Font_Medium = "DINPro_Medium.otf";
    public static String Font_Light = "DINPro_Light.otf";


    public static Typeface getTypeface_Light(Context context) {
        typeface_Light = Typeface.createFromAsset(context.getAssets(), Font_Light);
        return typeface_Light;
    }

    public static Typeface getTypeface_Medium(Context context) {
        typeface_Medium = Typeface.createFromAsset(context.getAssets(), Font_Medium);
        return typeface_Medium;
    }

    public static final String TWITTER_KEY = "DvLs2Oa1sSp8ol4vK5rVTBVKq";
    public static final String TWITTER_SECRET = "psQb3sFx3jB9sPo2achD3QUzLUnNnS2ZFSwlRtfLlJBLmAhq04";

    public static final String KEY_Prefs_Nonce = "nonce_key";
    public static final String Ssl = "&insecure=cool";
    public static final String URL_BASE = "http://redracc.com/api-GfjSL8AeXa/";
    public static final String URL_GET_NONCE = URL_BASE + "get_nonce/?controller=user&method=register";
    public static final String URL_REGISTER = URL_BASE + "user/register/?nonce=%s&username=%s&display_name=%s&email=%s&user_pass=%s&age=%s" + Ssl;
    public static final String URL_SIGN_IN = URL_BASE + "auth/generate_auth_cookie/?insecure=cool&username=%s&password=%s";
    public static final String URL_GET_RECENT_POSTS = URL_BASE + "core/get_recent_posts/";
    public static final String URL_GET_POST = URL_BASE + "get_post/?post_id=%s";
    public static final String URL_GET_CATEGORY_INDEX = URL_BASE + "core/get_category_index";
    public static final String URL_GET_CATEGORIES = URL_BASE + "core/get_category_posts/?id=%s";
    public static final String URL_GET_BASE_USER = URL_BASE + "user/";
    public static final String URL_GET_USER_INFO = URL_GET_BASE_USER + "get_userinfo/?insecure=cool&user_id=%s";
    public static final String URL_GET_SEARCH_RESULTS = URL_BASE + "core/get_search_results/?search=%s";
    public static final String URL_FB_LOGIN = URL_BASE + "user/fb_connect/?insecure=cool&access_token=%s";
    public static final String URL_FEEDBACK = "http://redracc.com/mobile-api/feedback.php/?username=%s&feedback=%s";
    public static final String URL_NOTIFICATIONS = "http://redracc.com/pnfw/register/";
    public static final String URL_GET_TAG = URL_BASE + "get_tag_posts/?id=%s";

    public static final String isLoggedin = "isLogged";
    public static final String User_ID = "userid";
    public static final String User_NAME = "username";
    public static final String defaultTextSize = "default";
    public static final String fbAccessToken = "token";
    public static final String categoryID_Key = "categID";

    ///////Notifications////////////
    public static final String ifNotifications = "notifications";
    public static final String Notifications_SenderID = "881010448427";
    public static final String isNotification = "notification";


    public static float getTextAppSize(Context context, boolean isTitle, boolean isSubtitle, boolean isButton) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        float textSize = sharedPreferences.getInt(Constants.defaultTextSize, 50);
        if (isTitle) {
            if (textSize <= 10) {
                textSize = (int) context.getResources().getDimension(R.dimen.xxsTitle);

            } else if (textSize > 10 && textSize <= 30) {
                textSize = (int) context.getResources().getDimension(R.dimen.xsTitle);

            } else if (textSize > 30 && textSize < 50) {
                textSize = (int) context.getResources().getDimension(R.dimen.sTitle);

            } else if (textSize == 50) {
                textSize = (int) context.getResources().getDimension(R.dimen.nTitle);

            } else if (textSize > 50 && textSize <= 65) {
                textSize = (int) context.getResources().getDimension(R.dimen.lTitle);

            } else if (textSize > 65 && textSize <= 80) {
                textSize = (int) context.getResources().getDimension(R.dimen.xlTitle);

            } else if (textSize > 80) {
                textSize = (int) context.getResources().getDimension(R.dimen.xxlTitle);

            }
        } else if (isSubtitle) {
            if (textSize <= 10) {
                textSize = (int) context.getResources().getDimension(R.dimen.xxsSubTitle);
            } else if (textSize > 10 && textSize <= 30) {
                textSize = (int) context.getResources().getDimension(R.dimen.xsSubTitle);
            } else if (textSize > 30 && textSize <50) {
                textSize = (int) context.getResources().getDimension(R.dimen.sSubTitle);
            } else if (textSize == 50) {
                textSize = (int) context.getResources().getDimension(R.dimen.nSubTitle);
            } else if (textSize > 50 && textSize <= 65) {
                textSize = (int) context.getResources().getDimension(R.dimen.lSubTitle);
            } else if (textSize > 65 && textSize <= 80) {
                textSize = (int) context.getResources().getDimension(R.dimen.xlSubTitle);
            } else if (textSize > 80) {
                textSize = (int) context.getResources().getDimension(R.dimen.xxlSubTitle);
            }
        } else if (isButton) {
            if (textSize <= 10) {

                textSize = (int) context.getResources().getDimension(R.dimen.xxsButton);
            } else if (textSize > 10 && textSize <= 30) {

                textSize = (int) context.getResources().getDimension(R.dimen.xsButton);
            } else if (textSize > 30 && textSize < 50) {

                textSize = (int) context.getResources().getDimension(R.dimen.sButton);
            } else if (textSize == 50) {

                textSize = (int) context.getResources().getDimension(R.dimen.nButton);
            } else if (textSize > 50 && textSize <= 65) {

                textSize = (int) context.getResources().getDimension(R.dimen.lButton);
            } else if (textSize > 65 && textSize <= 80) {

                textSize = (int) context.getResources().getDimension(R.dimen.xlButton);
            } else if (textSize > 80) {

                textSize = (int) context.getResources().getDimension(R.dimen.xxlButton);
            }
        }
        float realTextSize = textSize / 2;

        return realTextSize;
    }
}
