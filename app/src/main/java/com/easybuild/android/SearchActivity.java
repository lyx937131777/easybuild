package com.easybuild.android;

import android.content.ClipData;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.easybuild.android.util.LogUtil;

public class SearchActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener
{
    private String type;
    private String order = "综合";
    private String key = "";
    private int low;
    private int high;
    private String source = "全部";

    private Button order_button;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        type = getIntent().getStringExtra("type");
        initDrawerLayout();

        initsetting();
    }

    private void initDrawerLayout()
    {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initsetting()
    {
        type = getIntent().getStringExtra("type");
        if (type.equals("all") || type.equals("item"))
        {
            order_button = findViewById(R.id.order_button);
            order_button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    PopupMenu pop = new PopupMenu(SearchActivity.this, v);
                    pop.getMenuInflater().inflate(R.menu.order_menu, pop.getMenu());
                    pop.setOnMenuItemClickListener(SearchActivity.this);
                    pop.show();
                }
            });
            final ToggleButton source_all = findViewById(R.id.source_all);
            final ToggleButton source_jingdong = findViewById(R.id.source_jingdong);
            final ToggleButton source_taobao = findViewById(R.id.source_taobao);
            source_all.setChecked(true);
            source_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        source = buttonView.getText().toString();
                        source_jingdong.setChecked(false);
                        source_taobao.setChecked(false);
                    } else if (source.equals(buttonView.getText().toString()))
                    {
                        buttonView.setChecked(true);
                    }
                    LogUtil.e("SearchActivity", source);
                }
            });

            source_jingdong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        source = buttonView.getText().toString();
                        source_all.setChecked(false);
                        source_taobao.setChecked(false);
                    } else if (source.equals(buttonView.getText().toString()))
                    {
                        buttonView.setChecked(true);
                    }
                    LogUtil.e("SearchActivity", source);
                }
            });
            source_taobao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        source = buttonView.getText().toString();
                        source_jingdong.setChecked(false);
                        source_all.setChecked(false);
                    } else if (source.equals(buttonView.getText().toString()))
                    {
                        buttonView.setChecked(true);
                    }
                    LogUtil.e("SearchActivity", source);
                }
            });
        } else
        {
            LinearLayout source_ll = findViewById(R.id.source_layout);
            LinearLayout setting_ll = findViewById(R.id.setting_layout);
            source_ll.setVisibility(View.GONE);
            setting_ll.setVisibility(View.GONE);
            if (type.equals("cpu") || type.equals("gpu"))
            {

            } else
            {

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
            case R.id.menu_search:
                //HTTP rua
                Toast.makeText(this, "Search!!!!", Toast.LENGTH_LONG).show();
                Intent display_intent = new Intent(SearchActivity.this, DisplayActivity.class);
                startActivity(display_intent);
                break;
            case R.id.menu_filter:
                mDrawerLayout.openDrawer(GravityCompat.END);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        type = getIntent().getStringExtra("type");
        if (type.equals("all") || type.equals("item") || type.equals("cpu") || type.equals("gpu"))
        {
            menu.findItem(R.id.menu_filter).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        order_button.setText(item.getTitle().toString() + " ▼");
        order = item.getTitle().toString();
        LogUtil.e("SearchActivity",order);
        return false;
    }

    public String getType()
    {
        return type;
    }
}
