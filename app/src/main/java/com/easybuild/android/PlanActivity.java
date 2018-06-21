package com.easybuild.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;

public class PlanActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CardView auto_plan = findViewById(R.id.auto_plan);
        CardView free_plan = findViewById(R.id.free_plan);
        auto_plan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity.setCommond(2);
                Intent intent_auto = new Intent(PlanActivity.this,DisplayActivity.class);
                intent_auto.putExtra("type","items");
                intent_auto.putExtra("plan","auto");
                startActivity(intent_auto);
            }
        });
        free_plan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity.setCommond(2);
                Intent intent_free = new Intent(PlanActivity.this,FreePlanActivity.class);
                startActivity(intent_free);
            }
        });
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        MainActivity.setCommond(1);
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
