package com.internship.socialfeed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.internship.socialfeed.R;
import com.internship.socialfeed.components.Article;

public class DetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initToolbar();
        fillViews();

    }

    /**
     * Fill the views of the activity_details layout with values
     * */
    private void fillViews() {
        Intent intent = getIntent();
        Article article = (Article)intent.getSerializableExtra("article");
        String authorName = "By: "+article.getAuthorName()+" @VineLab";
        String date = "On: "+article.getDate();
        ((TextView) findViewById(R.id.details_title)).setText(article.getTitle());
        ((TextView) findViewById(R.id.details_author)).setText(authorName);
        ((TextView) findViewById(R.id.details_date)).setText(date);
    }

    private void initToolbar() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
