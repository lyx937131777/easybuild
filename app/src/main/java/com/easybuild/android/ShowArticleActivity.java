package com.easybuild.android;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

public class ShowArticleActivity extends AppCompatActivity
{

    private ImageView display_img;
    private TextView display_text, relative_text;
    private ToggleButton display_favorite;
    private CardView display_relative;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        display_img = findViewById(R.id.display_image_view);
        display_text = findViewById(R.id.display_text);
        display_favorite = findViewById(R.id.display_favorite);
        display_relative = findViewById(R.id.display_relative);
        relative_text = findViewById(R.id.relative_text);
        Article article = (Article)getIntent().getSerializableExtra("article");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(article.getTitle());

        Glide.with(this).load(article.getImgID()).into(display_img);
        display_text.setText(article.getContent());
        relative_text.setText(article.getAuthor()+"  "+article.getDate());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
