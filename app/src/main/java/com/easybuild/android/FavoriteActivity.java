package com.easybuild.android;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.easybuild.android.db.Favorite;
import com.easybuild.android.gson.items;
import com.easybuild.android.util.HttpUtil;
import com.easybuild.android.util.LogUtil;
import com.easybuild.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FavoriteActivity extends AppCompatActivity
{

    private PreviewAdapter previewAdapter;
    private List<items> itemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        MainActivity.setCommond(1);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.display_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        previewAdapter = new PreviewAdapter(itemsList);
        recyclerView.setAdapter(previewAdapter);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String userID = pref.getString("userID", null);

        String address = HttpUtil.LocalAddress + "/buildup/ideal2";
        HttpUtil.searchPlanRequest(address, userID, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String responseString = response.body().string();
                LogUtil.e("FavoriteActivity", responseString);
                MainActivity.setCommond(2);
                List<items> itemsList = Utility.handleItemsResponse(responseString);
                MainActivity.setCommond(1);
               for (items newitem: itemsList)
               {
                   Random random = new Random();
                   int index = random.nextInt(2);
                   if(index == 1)
                   {
                       newitem.setImg("http://47.96.102.28:8080/liteServer/imgs/imgamd.png");
                       newitem.setComments("amd平台");
                   }else
                   {
                       newitem.setImg("http://47.96.102.28:8080/liteServer/imgs/imgintel.png");
                       newitem.setComments("intel平台");
                   }
                   newitem.setTitle("自由组装");
                   newitem.setPrice("未知");
                   newitem.setSource(userID);
                   newitem.setUeserID(userID);

                   newitem.setType("freeplan");
                   initItems(newitem);
               }
            }
        });

        List<Favorite> favoriteList = DataSupport.where("userID = ?", userID).find(Favorite.class);
        for (Favorite favorite : favoriteList)
        {
            String address2 = HttpUtil.LocalAddress + "/search/idSearch";
            HttpUtil.searchIdRequest(address2, favorite.getType(), favorite.getFavorite_id(),
                    new Callback()
                    {
                        @Override
                        public void onFailure(Call call, IOException e)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(FavoriteActivity.this,"连接错误",Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException
                        {
                            final String responseData = response.body().string();
                            LogUtil.e("FavoriteActivity", responseData);
                            String code = Utility.checkCode(responseData);
                            switch (code)
                            {
                                case "items":
                                    initItems(Utility.handleoneItemResponse(responseData));
                                    break;
                                case "cpu":
                                    initItems(Utility.handleoneCPUResponse(responseData));
                                    break;
                                case "gpu":
                                    initItems(Utility.handleoneGPUResponse(responseData));
                                    break;
                                case "false":
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            Toast.makeText(FavoriteActivity.this,"有一个过期的收藏已被删除",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    break;
                                default:
                                    initItems(Utility.handleoneHardwareResponse(responseData));
                                    break;
                            }
                        }
                    });
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        MainActivity.setCommond(1);
    }

    private void initItems(items newitem)
    {
        if (newitem == null)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(FavoriteActivity.this,"有一个过期的收藏已被删除",Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        itemsList.add(newitem);
        LogUtil.e("SearchActivity", "得到item！！");
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                previewAdapter.notifyDataSetChanged();
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
