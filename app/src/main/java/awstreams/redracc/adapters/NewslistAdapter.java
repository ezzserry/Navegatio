package awstreams.redracc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import awstreams.redracc.R;
import awstreams.redracc.helpers.Constants;
import awstreams.redracc.helpers.Utils;
import awstreams.redracc.interfaces.OnNewsItemClickListener;
import awstreams.redracc.models.NewsItem;

public class NewslistAdapter extends RecyclerView.Adapter<NewslistAdapter.MyCustomViewHolder> {


    private List<NewsItem> newsItemList;
    private Context context;

    private NewsItem newsItem;
    OnNewsItemClickListener mListener;

    public NewslistAdapter(Context context, List<NewsItem> newsItemList) {
        this.context = context;
        this.newsItemList = newsItemList;
        mListener = (OnNewsItemClickListener) context;

    }

    @Override
    public MyCustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, null);
        MyCustomViewHolder viewHolder = new MyCustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyCustomViewHolder holder, final int position) {
        newsItem = newsItemList.get(position);
        final String imgUrl = newsItem.getThumbnail();
//        //Download image using picasso
        if (imgUrl != null) {
            Picasso.with(context)
                    .load(imgUrl).networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.imgPhoto, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.imgPhoto.setVisibility(View.VISIBLE);
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(imgUrl)
                                    .into(holder.imgPhoto, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            holder.imgPhoto.setVisibility(View.VISIBLE);
                                            holder.progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {
                                            holder.progressBar.setVisibility(View.GONE);
                                            holder.imgPhoto.setVisibility(View.VISIBLE);
                                            holder.imgPhoto.setImageResource(R.mipmap.placeholder);

                                        }
                                    });

                        }
                    });
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.imgPhoto.setVisibility(View.VISIBLE);
            holder.imgPhoto.setImageResource(R.mipmap.placeholder);
        }

        //Setting text view title
        holder.txtPhotoTitle.setText(Utils.convertHTMLtoString(newsItem.getTitle()));
        holder.txtContent.setText(Utils.convertHTMLtoString(newsItem.getExcerpt()));
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    NewsItem newsItem = newsItemList.get(position);
                    mListener.onNewsItemClick(newsItem);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return newsItemList.size();
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        private TextView txtPhotoTitle, txtContent;
        private ProgressBar progressBar;
        private RelativeLayout rlContainer;

        public MyCustomViewHolder(View view) {
            super(view);
            this.imgPhoto = (ImageView) view.findViewById(R.id.news_iv);
            this.txtPhotoTitle = (TextView) view.findViewById(R.id.title_tv);
            this.txtContent = (TextView) view.findViewById(R.id.subtitle_tv);
            this.progressBar = (ProgressBar) view.findViewById(R.id.loadingpanel);
            this.rlContainer = (RelativeLayout) view.findViewById(R.id.cardview_rl);

            this.txtPhotoTitle.setTypeface(Constants.getTypeface_Medium(view.getContext()));
            this.txtContent.setTypeface(Constants.getTypeface_Light(view.getContext()));
            this.txtPhotoTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(view.getContext(), true, false, false));
            this.txtContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(view.getContext(), false, true, false));

        }
    }
}
