package com.easybuild.android;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.easybuild.android.db.Favorite;
import com.easybuild.android.gson.items;
import com.easybuild.android.util.HttpUtil;
import com.easybuild.android.util.LogUtil;
import com.easybuild.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        String userID = pref.getString("userID", null);
        List<Favorite> favoriteList = DataSupport.where("userID = ?", userID).find(Favorite.class);
        for (Favorite favorite : favoriteList)
        {
            String address = HttpUtil.LocalAddress + "/search/idSearch";
            HttpUtil.serchIdRequest(address, favorite.getType(), favorite.getFavorite_id(), new
                    Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    LogUtil.e("FavoriteActivity", "连接错误");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    final String responseData = response.body().string();
                    LogUtil.e("FavoriteActivity", responseData);
                    if (!Utility.checkCode(responseData).equals("false"))
                    {
                        initItems(Utility.handleoneItemResponse(responseData), Utility.checkCode
                                (responseData));
                    } else
                    {
                        LogUtil.e("SearchActivity", "提交格式错误");
                    }
                }
            });
        }
    }

    private void initItems(items newitem, String codetype)
    {
        if (Integer.valueOf(newitem.getPrice()) > 0)
        {
            newitem.setType(codetype);
            itemsList.add(newitem);
        }
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
