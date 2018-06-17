package com.easybuild.android;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener
{
    Button bt_cloud_update,bt_logout;
    ToggleButton bt_ring_toggle, bt_show_toggle, bt_vibrate_toggle, bt_white_list_toggle;
    RelativeLayout white_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
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

    private void initView()
    {
        bt_cloud_update = (Button) findViewById(R.id.bt_cloud_update);
        bt_cloud_update.setOnClickListener(this);
        bt_ring_toggle = (ToggleButton) findViewById(R.id.bt_ring_toggle);
        bt_ring_toggle.setOnClickListener(this);
        bt_vibrate_toggle = (ToggleButton) findViewById(R.id.bt_vibrate_toggle);
        bt_vibrate_toggle.setOnClickListener(this);
        bt_show_toggle = (ToggleButton) findViewById(R.id.bt_show_toggle);
        bt_show_toggle.setOnClickListener(this);
        bt_white_list_toggle = (ToggleButton) findViewById(R.id.bt_white_list_toggle);
        bt_white_list_toggle.setOnClickListener(this);
        bt_logout = (Button) findViewById(R.id.logout);
        bt_logout.setOnClickListener(this);
        white_list =(RelativeLayout)findViewById(R.id.white_l);
        white_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.bt_cloud_update:
                Toast.makeText(this, "云端同步", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_ring_toggle:
                toggleSwtich(bt_ring_toggle, "允许响铃", "不允许响铃");
                break;
            case R.id.bt_vibrate_toggle:
                toggleSwtich(bt_vibrate_toggle, "允许震动", "不允许震动");
                break;
            case R.id.bt_show_toggle:
                toggleSwtich(bt_show_toggle, "锁屏显示to-do list", "锁屏不显示");
                break;
            case R.id.logout:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.remove("userID");
                editor.remove("password");
                editor.apply();
                Intent intent_logout = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent_logout);
                MainActivity.instance.finish();
                finish();
            default:
                break;
        }
    }

    public void toggleSwtich(ToggleButton toggleButton, String textChecked, String textNotChecked)
    {
        if (toggleButton.isChecked())
        {
            Toast.makeText(this, textChecked, Toast.LENGTH_SHORT).show();
        } else if (!toggleButton.isChecked())
        {
            Toast.makeText(this, textNotChecked, Toast.LENGTH_SHORT).show();
        }
    }
}
