package com.easybuild.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.easybuild.android.gson.items;

public class CompareActivity extends AppCompatActivity
{
    public CompareFragment leftfragment,rightfragment;
    public static items compareitem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        leftfragment = (CompareFragment) getSupportFragmentManager().findFragmentById(R.id.left_fragment);
        rightfragment = (CompareFragment) getSupportFragmentManager().findFragmentById(R.id.right_fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        MainActivity.setCommond(1);
        switch (requestCode)
        {
            case 1:
                if(resultCode == RESULT_OK)
                {
                    leftfragment.changeView();
                }
                break;
            case 2:
                if(resultCode == RESULT_OK)
                {
                    rightfragment.changeView();
                }
        }
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
