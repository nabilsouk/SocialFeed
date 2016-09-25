package com.internship.socialfeed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.internship.socialfeed.Configuration.Configuration;
import com.internship.socialfeed.ui.listeners.OnListListener;
import com.internship.socialfeed.R;
import com.internship.socialfeed.components.Article;
import com.internship.socialfeed.components.Loader;
import com.internship.socialfeed.data.ArticlesProvider;
import com.internship.socialfeed.listing.ArticleItem;
import com.internship.socialfeed.listing.ListingItem;
import com.internship.socialfeed.listing.LoaderItem;
import com.internship.socialfeed.settings.SettingsActivity;
import com.internship.socialfeed.ui.tools.ItemsListAdapter;

import java.util.ArrayList;

/**
 * ItemsListActivity is the activity class which lists
 * the items(Articles, Loader) in a linear way
 */
public class ItemsListActivity extends AppCompatActivity {
    Toolbar toolbar;
    ItemsListAdapter itemsListAdapter;
    private int threshold = 0;
    private int itemsCount;
    private ArticlesProvider articlesProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_list);

        initToolbar();
        initRecyclerView();
        initArticlesProvider();
        requestNewSet();
    }

    /**
     * Create new ArticlesProvider instance.
     */
    private void initArticlesProvider() {
        articlesProvider = new ArticlesProvider(articlesProviderListener, 6);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Suicide");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //create new adapter
        itemsListAdapter = new ItemsListAdapter(null, listListener, getBaseContext());
        recyclerView.setAdapter(itemsListAdapter);
        // track scrolling
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {//check for scroll down
                    // get last visible position
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    //checks if current position of bindView equals threshold
                    if (lastVisiblePosition >= threshold) {
                        requestNewSet();
                    }
                }
            }
        });
    }

    // DATA //

    private ArticlesProvider.OnArticlesProviderListener articlesProviderListener =
            new ArticlesProvider.OnArticlesProviderListener() {
                @Override
                public void onComplete(ArrayList<Article> arrayArticles, int currentCount) {
                    handleNewData(arrayArticles, currentCount);
                }

                @Override
                public void onStateChanged(boolean loading) {
                    if (loading) {
                        addLoaderItem();
                    }
                }
            };

    /**
     * Converts articles list to items list.
     */
    private ArrayList<ListingItem> convertArticlesToItems(ArrayList<Article> arrayArticles) {
        //save the received array
        ArrayList<ListingItem> newListingItems = new ArrayList<>();
        for (int i = 0; i < arrayArticles.size(); i++) {
            ArticleItem aItem = new ArticleItem(arrayArticles.get(i));
            newListingItems.add(aItem);
        }
        return newListingItems;
    }

    /**
     * Requests a new set of data.
     */
    private void requestNewSet() {
        if (articlesProvider != null) {
            articlesProvider.generateNextArticlesSet();
        }
        Log.i("requestnew", "requested  threshold: " + threshold);
    }

    /**
     * Handles the data set received and updates the threshold
     * and current items count.
     * @param arrayArticles is the data set.
     * @param currentCount  is current count of the data.
     */
    private void handleNewData(ArrayList<Article> arrayArticles, int currentCount) {
        // convert new articles to items array
        ArrayList<ListingItem> newListingItems = convertArticlesToItems(arrayArticles);
        // inform adapter
        //remove loading item
        itemsListAdapter.removeLoader();
        //add new items to recyclerView
        itemsListAdapter.addNewItems(newListingItems);
        // update threshold
        if (itemsCount == 0) {
            threshold += currentCount / 2;
        } else {
            threshold += currentCount;
        }
        itemsCount += currentCount;
    }

    /**
     * Adds a loader item to the adapter.
     */
    private void addLoaderItem() {
        //create new loader
        Loader loader = new Loader();
        //create new loaderItem with loader
        LoaderItem loaderItem = new LoaderItem(loader);
        //add loaderItem to arrayList
        itemsListAdapter.addLoader(loaderItem);
    }

    OnListListener listListener = new OnListListener() {
        @Override
        public void onRowClick(View view, int position) {

        }

        @Override
        public void onTitleSelected(int position) {
            Toast.makeText(getBaseContext(), "text view: " + position, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, Configuration.AUTHENTICATION_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Configuration.TAG,"ListItemActvity");
        if (resultCode == Configuration.AUTHENTICATION_REQUEST){
            setResult(Configuration.AUTHENTICATION_REQUEST);
            finish();
        }
    }
}