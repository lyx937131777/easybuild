package com.easybuild.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.easybuild.android.db.Type;
import com.easybuild.android.db.User;
import com.easybuild.android.gson.items;
import com.easybuild.android.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private DrawerLayout mDrawerLayout;
    public static MainActivity instance = null;
    private static int commond = 1;

    private SharedPreferences pref;
    private String userID;

    private Type[] types = {new Type("全部", R.drawable.imgall,"all"), new Type("商品", R.drawable.imgitem,"items")
            , new Type("CPU", R.drawable.imgcpu,"cpu"), new Type("GPU", R.drawable.imggpu,"gpu"),
            new Type("机箱", R.drawable.imgcase,"case"), new Type("电源", R.drawable.imgpower,"power"),
            new Type("水冷", R.drawable.imgcoolerwater,"cooler_water"), new Type("风冷", R.drawable.imgcoolerwind,"cooler_wind"),
            new Type("HDD", R.drawable.imghdd,"hdd"), new Type("SSD", R.drawable.imgssd,"ssd"),
            new Type("内存", R.drawable.imgmemory,"memory"), new Type("主板", R.drawable.imgmotherboard,"motherboard")};
    private List<Type> typeList = new ArrayList<>();
    private TypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_reorder_black_24dp);
        }
        initTypes();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TypeAdapter(typeList,0);
        recyclerView.setAdapter(adapter);

        navView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_compare:
                        Intent intent_compare = new Intent(MainActivity.this,CompareActivity.class);
                        startActivity(intent_compare);
                        break;
                    case R.id.nav_plan:
                        Intent intent_plan = new Intent(MainActivity.this,PlanActivity.class);
                        startActivity(intent_plan);
                        break;
                    case R.id.nav_article:
                        Intent intent_article = new Intent(MainActivity.this,ArticleActivity.class);
                        startActivity(intent_article);
                        break;
                    case R.id.nav_favorite:
                        Intent intent_favorite = new Intent(MainActivity.this,FavoriteActivity.class);
                        startActivity(intent_favorite);
                        break;
                    case R.id.nav_setting:
                        Intent intent_setting = new Intent(MainActivity.this, SettingsActivity
                                .class);
                        startActivity(intent_setting);
                        break;
                }
                return true;
            }
        });

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userID = pref.getString("userID", null);
        View view = navView.getHeaderView(0);
        TextView tv_username = view.findViewById(R.id.nav_username);
        tv_username.setText(userID);
        User user = DataSupport.where("userID = ?", userID).findFirst(User.class);
        String nickname = user.getNickname();
        String profile_photo = user.getProfile_photo();
        TextView nick_text = view.findViewById(R.id.nav_nickname);
        nick_text.setText(nickname);
        de.hdodenhof.circleimageview.CircleImageView photo = view.findViewById(R.id.icon_image);
        Glide.with(MainActivity.this).load(profile_photo).signature(new StringSignature(pref
                .getString("latest", ""))).into(photo);
        LogUtil.e("test", "Hello");

        photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent_toUserInfo = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivityForResult(intent_toUserInfo, 1);
            }
        });
    }

    private void initTypes()
    {
        typeList.clear();
        //DataSupport.deleteAll(Type.class);
        for (int i = 0; i < types.length; i++)
        {
            typeList.add(types[i]);
            Type newtype = DataSupport.where("TypeName = ?",types[i].getTypeName()).findFirst(Type.class);
            if(newtype == null)
            {
                types[i].save();
                LogUtil.e("MainActivity","=========save==========");
            }else
            {
                LogUtil.e("MainActivity","Not save!!!!!!!!!!!");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            default:
                break;
        }
    }

    public static int getCommond()
    {
        return commond;
    }

    public static void setCommond(int commond)
    {
        MainActivity.commond = commond;
    }
}
