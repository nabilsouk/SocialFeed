package com.internship.socialfeed.ui.tools;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.internship.socialfeed.ui.listeners.OnListListener;
import com.internship.socialfeed.R;
import com.internship.socialfeed.components.Article;
import com.internship.socialfeed.components.Loader;
import com.internship.socialfeed.listing.ArticleItem;
import com.internship.socialfeed.listing.ListingItem;
import com.internship.socialfeed.listing.ListingItemType;
import com.internship.socialfeed.listing.LoaderItem;

import java.util.ArrayList;


public class ItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ListingItem> items = new ArrayList<>();
    Context ctx;
    private OnListListener listener = null;

    public ItemsListAdapter(ArrayList<ListingItem> items, OnListListener listener, Context ctx) {
        if (items != null) {
            this.items = items;
        }
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == ListingItemType.Loader.ordinal()) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.row_loader, parent, false);
            holder = new LoaderViewHolder(view);
        } else if (viewType == ListingItemType.ARTICLE.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_article, parent, false);
            holder = new ArticleViewHolder(view);
        }
        return holder;
    }

    /**
     * Adds new items to the list.
     *
     * @param newItems
     */
    public void addNewItems(ArrayList<ListingItem> newItems) {
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    /**
     * Adds Loader item to the end of the list.
     *
     * @param item
     */
    public void addLoader(ListingItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    /**
     * Removes loader item of the list.
     */
    public void removeLoader() {
        items.remove(items.size() - 1);
        notifyItemRemoved(items.size() - 1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListingItem item = items.get(position);
        //sets position to current binded view.
        switch (item.getType()) {
            case ARTICLE:
                ArticleItem articleItem = (ArticleItem) item;
                // get article from item container
                Article article = articleItem.getArticle();
                // extract data from article
                String body = ctx.getResources().getString(R.string.body);
                body = (body.substring(0, 100)) + "...";
                String title = article.getId() + " " + article.getTitle();
                String date = article.getDate();
                // cast holder
                final ArticleViewHolder vhArticle = (ArticleViewHolder) holder;
                // set values
                vhArticle.tvBody.setText(body);
                vhArticle.tvTitle.setText(title != null ? title : "");
                vhArticle.tvDate.setText(date != null ? date : "");
                break;
            case Loader:
                LoaderItem loaderItem = (LoaderItem) item;
                Loader loader = loaderItem.getLoader();
                final LoaderViewHolder vhAdvertisement = (LoaderViewHolder) holder;
                break;
            default:
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return items != null ? items.get(position).getType().ordinal() : 0;
    }

    class LoaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;

        public LoaderViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.card_view:
                    if (listener != null) {
                        listener.onRowClick(v, getAdapterPosition());
                    }
                    break;
            }
        }
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvTitle, tvBody, tvDate;

        ArticleViewHolder(View view) {
            super(view);
            // init views
            cardView = (CardView) view.findViewById(R.id.card_view);
            tvTitle = (TextView) view.findViewById(R.id.custom_row_title);
            tvBody = (TextView) view.findViewById(R.id.custom_row_body);
            tvDate = (TextView) view.findViewById(R.id.customRowDate);
            // CLICK events
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRowClick(v, getAdapterPosition());
                    }
                }
            });
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTitleSelected(getAdapterPosition());
                }
            });
        }
    }
}
