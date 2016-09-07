package awstreams.redraccon.helpers;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by LENOVO on 29/08/2016.
 */
public class Constants {

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
    public static final String URL_REGISTER = URL_BASE + "user/register/?nonce=%s&username=%s&display_name=%s&email=%s&password=%s&age=%s" + Ssl;
    public static final String URL_GET_RECENT_POSTS = URL_BASE + "core/get_recent_posts/";

    public static String isLoggedin = "isLogged";
}
