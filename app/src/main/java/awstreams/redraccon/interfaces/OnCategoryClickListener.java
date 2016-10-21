package awstreams.redraccon.interfaces;

import android.widget.TextView;

import awstreams.redraccon.models.Category;
import awstreams.redraccon.models.NewsItem;

/**
 * Created by LENOVO on 08/10/2016.
 */
public interface OnCategoryClickListener {
    void onCategoryClick(Category category, TextView tvCategoryName);

}
