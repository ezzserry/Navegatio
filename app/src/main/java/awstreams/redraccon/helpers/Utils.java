package awstreams.redraccon.helpers;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by LENOVO on 29/08/2016.
 */
public class Utils {
    public static ProgressDialog loading = null;

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty() || phoneNumber.length() != 11 || !phoneNumber.startsWith("01")) {
            return false;
        } else
            return true;

    }

    public static void showloading(Context context) {
        loading = new ProgressDialog(context);
        loading.setCancelable(false);
        loading.setMessage("Loading ");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
    }

    public static void dismissloading() {
        loading.dismiss();
    }

    public static String convertHTMLtoString(String html) {
        String convertedString = android.text.Html.fromHtml(html).toString();
        return convertedString;
    }
}
