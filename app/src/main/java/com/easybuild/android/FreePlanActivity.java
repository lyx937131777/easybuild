package com.easybuild.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;

import com.easybuild.android.gson.items;

public class FreePlanActivity extends AppCompatActivity
{
    public static items free_item;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_plan);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CardView amd_plan = findViewById(R.id.amd_plan);
        final CardView intel_plan = findViewById(R.id.intel_plan);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String userID = pref.getString("userID", null);
        amd_plan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                free_item = new items();
                free_item.setType("freeplan");
                free_item.setTitle("自由组装");
                free_item.setComments("AMD平台");
                free_item.setPrice("未知");
                free_item.setSource(userID);
                free_item.setUeserID(userID);
                free_item.setImg("http://47.96.102.28:8080/liteServer/imgs/imgamd.png");
                Intent intent_amd = new Intent(FreePlanActivity.this,SearchActivity.class);
                intent_amd.putExtra("type","cpu");
                intent_amd.putExtra("plan","amd");
                startActivity(intent_amd);
            }
        });
        intel_plan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                free_item = new items();
                free_item.setType("freeplan");
                free_item.setTitle("自由组装");
                free_item.setComments("Intel平台");
                free_item.setPrice("未知");
                free_item.setSource(userID);
                free_item.setUeserID(userID);
                free_item.setImg("http://47.96.102.28:8080/liteServer/imgs/imgintel.png");
               Intent intent_intel = new Intent(FreePlanActivity.this,SearchActivity.class);
               intent_intel.putExtra("type","cpu");
               intent_intel.putExtra("plan","intel");
               startActivity(intent_intel);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
