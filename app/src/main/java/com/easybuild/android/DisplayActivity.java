package com.easybuild.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.easybuild.android.db.Favorite;
import com.easybuild.android.gson.CPU;
import com.easybuild.android.gson.GPU;
import com.easybuild.android.gson.Hardware;
import com.easybuild.android.gson.items;
import com.easybuild.android.util.HttpUtil;
import com.easybuild.android.util.LogUtil;
import com.easybuild.android.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DisplayActivity extends AppCompatActivity
{
    private String favorite_id;
    private String type;
    private int commond;
    private String plan;

    private ImageView display_img;
    private TextView display_text, relative_text;
    private ToggleButton display_favorite;
    private CardView display_relative;
    private CollapsingToolbarLayout collapsingToolbar;
    private String responseData;

    private PreviewAdapter previewAdapter;
    private List<items> itemsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        commond = MainActivity.getCommond();
        plan = getIntent().getStringExtra("plan");
        type = getIntent().getStringExtra("type");
        favorite_id = getIntent().getStringExtra("favorite_id");

        display_img = findViewById(R.id.display_image_view);
        display_text = findViewById(R.id.display_text);
        display_favorite = findViewById(R.id.display_favorite);
        display_relative = findViewById(R.id.display_relative);
        relative_text = findViewById(R.id.relative_text);
        if (type.equals("items"))
        {
            display_relative.setVisibility(View.GONE);
        }
        if (commond == 2)
        {
            display_relative.setVisibility(View.VISIBLE);
            if (plan.equals("auto"))
            {
                relative_text.setText("换一个");
                display_relative.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        getplan();
                    }
                });
            } else if (plan.equals("free"))
            {
                relative_text.setText("保存并重新选择");
                display_relative.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        items i = FreePlanActivity.free_item;
                        String address = HttpUtil.LocalAddress + "/buildup/add";
                        HttpUtil.addPlanRequest(address, i.getUeserID(), i.getCpu().get_id()
                                .get$oid(), i.getMotherboard().get_id().get$oid(), i.getGpu()
                                .get_id().get$oid(), i.getMemory().get_id().get$oid(), i
                                .getCooler_wind().get_id().get$oid(), i.getSsd().get_id().get$oid
                                (), i.getPower().get_id().get$oid(), i.getHdd().get_id().get$oid(), new Callback()
                        {
                            @Override
                            public void onFailure(Call call, IOException e)
                            {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException
                            {

                            }
                        });
                        FreePlanActivity.free_item.save();
                        finish();
                    }
                });
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");

        if (commond == 2 && plan.equals("free"))
        {
            initItems(FreePlanActivity.free_item);
        } else if (commond == 2 && plan.equals("auto"))
        {
            getplan();
        }
        //正常展示
        if (commond == 1)
        {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String userID = pref.getString("userID", null);

            if (DataSupport.where("favorite_id = ? and userID = ?", favorite_id, userID)
                    .findFirst(Favorite.class) != null)
            {
                display_favorite.setChecked(true);
            } else
            {
                display_favorite.setChecked(false);
            }
            display_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences
                            (DisplayActivity.this);
                    String userID = pref.getString("userID", null);
                    if (isChecked)
                    {
                        Favorite favorite = DataSupport.where("favorite_id = ? and userID = ?",
                                favorite_id, userID).findFirst(Favorite.class);
                        if (favorite == null)
                        {
                            favorite = new Favorite();
                            favorite.setFavorite_id(favorite_id);
                            favorite.setUserID(userID);
                            favorite.setType(type);
                            favorite.save();
                        }
                    } else
                    {
                        DataSupport.deleteAll(Favorite.class, "favorite_id = ? and userID = ?",
                                favorite_id, userID);
                    }
                }
            });

            RecyclerView recyclerView = findViewById(R.id.relative_recycler);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            previewAdapter = new PreviewAdapter(itemsList);
            recyclerView.setAdapter(previewAdapter);

            String address = HttpUtil.LocalAddress + "/search/idSearch";
            HttpUtil.searchIdRequest(address, type, favorite_id, new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(DisplayActivity.this, "连接错误", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    responseData = response.body().string();
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
                                    Toast.makeText(DisplayActivity.this, "出现异常错误", Toast
                                            .LENGTH_LONG).show();
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


    private void getplan()
    {
        String address = HttpUtil.LocalAddress + "/buildup/ideal2";
        HttpUtil.searchPlanRequest(address, "admin@qq.com", new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String responseString = response.body().string();
                LogUtil.e("DisplayActivity", responseString);
                List<items> itemsList = Utility.handleItemsResponse(responseString);
                int max = itemsList.size();
                Random random = new Random();
                int index = random.nextInt(max);
                items newitem = itemsList.get(index);
                newitem.setImg("http://47.96.102.28:8080/liteServer/imgs/imgitem.png");
                newitem.setTitle("系统推荐");
                newitem.setPrice("未知");
                newitem.setSource("admin@qq.com");
                newitem.setComments("多平台");
                newitem.setType("items");
                initItems(newitem);
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(DisplayActivity.this, "换了一个", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void initItems(final items newitem)
    {
        if (newitem == null)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(DisplayActivity.this, "未找到该商品", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        LogUtil.e("SearchActivity", "得到item！！");
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                collapsingToolbar.setTitle(newitem.getTitle());
                Glide.with(DisplayActivity.this).load(newitem.getImg()).into(display_img);
                StringBuilder stringBuilder = new StringBuilder();
                switch (type)
                {
                    case "items":
                        stringBuilder.append("————————————产品————————————\n\n");
                        stringBuilder.append("名称：\n" + newitem.getTitle() + "\n\n");
                        stringBuilder.append("价格：" + newitem.getPrice() + "\n\n");
                        stringBuilder.append(newitem.getComments() + "\n\n");
                        stringBuilder.append("来源：" + newitem.getSource() + "\n\n");
                        if(commond == 1)
                        {
                            try
                            {
                                JSONObject prices = new JSONObject(newitem.getPrices());
                                Iterator<String> iterator= prices.keys();
                                stringBuilder.append("历史价格:\n");
                                while(iterator.hasNext())
                                {
                                    String date=iterator.next();
                                    String price=prices.getString(date);
                                    stringBuilder.append("      "+date+" ： ￥"+price+"\n");
                                }
                                stringBuilder.append("\n");
                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        if (newitem.getCpu() != null)
                        {
                            CPU cpu = newitem.getCpu();
                            stringBuilder.append("————————————CPU————————————\n\n");
                            stringBuilder.append("型号：" + cpu.getName() + "\n\n");
                            stringBuilder.append("架构：" + cpu.getCodename() + "\n\n");
                            stringBuilder.append("核心数：" + cpu.getCores() + "\n\n");
                            stringBuilder.append("线程数：" + cpu.getThreads() + "\n\n");
                            stringBuilder.append("接口：" + cpu.getSocket() + "\n\n");
                            stringBuilder.append("Clock：" + cpu.getClock() + "\n\n");
                            stringBuilder.append("Multi：" + cpu.getMulti() + "\n\n");
                            stringBuilder.append("一级缓存：" + cpu.getCacheL1() + "\n\n");
                            stringBuilder.append("二级缓存：" + cpu.getCacheL2() + "\n\n");
                            stringBuilder.append("三级缓存：" + cpu.getCacheL3() + "\n\n");
                            stringBuilder.append("TDP：" + cpu.getTDP() + "\n\n");
                            stringBuilder.append("上市日期：" + cpu.getReleased() + "\n\n");
                        }
                        if (newitem.getGpu() != null)
                        {
                            GPU gpu = newitem.getGpu();
                            stringBuilder.append("————————————GPU————————————\n\n");
                            stringBuilder.append("型号：" + gpu.getName() + "\n\n");
                            stringBuilder.append("架构：" + gpu.getChip() + "\n\n");
                            stringBuilder.append("接口：" + gpu.getBus() + "\n\n");
                            stringBuilder.append("显存大小：" + gpu.getMemory_Size() + "\n\n");
                            stringBuilder.append("显存类型：" + gpu.getMemory_Type() + "\n\n");
                            stringBuilder.append("显存位宽：" + gpu.getMemory_Bus() + "\n\n");
                            stringBuilder.append("核心频率：" + gpu.getGPU_Clock() + "\n\n");
                            stringBuilder.append("着色单元：" + gpu.getShaders() + "\n\n");
                            stringBuilder.append("TMUs：" + gpu.getTMUs() + "\n\n");
                            stringBuilder.append("ROPs：" + gpu.getROPs() + "\n\n");
                            stringBuilder.append("TDP：" + gpu.getTDP() + "\n\n");
                            stringBuilder.append("上市日期：" + gpu.getReleased() + "\n\n");
                        }
                        if (newitem.getMycase() != null)
                        {
                            Hardware hardware = newitem.getMycase();
                            stringBuilder.append("————————————机箱————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getPower() != null)
                        {
                            Hardware hardware = newitem.getPower();
                            stringBuilder.append("————————————电源————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getCooler_water() != null)
                        {
                            Hardware hardware = newitem.getCooler_water();
                            stringBuilder.append("————————————水冷————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getCooler_wind() != null)
                        {
                            Hardware hardware = newitem.getCooler_wind();
                            stringBuilder.append("————————————风冷————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getHdd() != null)
                        {
                            Hardware hardware = newitem.getHdd();
                            stringBuilder.append("————————————HDD————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getSsd() != null)
                        {
                            Hardware hardware = newitem.getSsd();
                            stringBuilder.append("————————————SSD————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getMemory() != null)
                        {
                            Hardware hardware = newitem.getMemory();
                            stringBuilder.append("————————————内存————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getMotherboard() != null)
                        {
                            Hardware hardware = newitem.getMotherboard();
                            stringBuilder.append("————————————主板————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        break;
                    case "cpu":
                        if (newitem.getCpu() != null)
                        {
                            CPU cpu = newitem.getCpu();
                            stringBuilder.append("————————————CPU————————————\n\n");
                            stringBuilder.append("型号：" + cpu.getName() + "\n\n");
                            stringBuilder.append("架构：" + cpu.getCodename() + "\n\n");
                            stringBuilder.append("核心数：" + cpu.getCores() + "\n\n");
                            stringBuilder.append("线程数：" + cpu.getThreads() + "\n\n");
                            stringBuilder.append("接口：" + cpu.getSocket() + "\n\n");
                            stringBuilder.append("Clock：" + cpu.getClock() + "\n\n");
                            stringBuilder.append("Multi：" + cpu.getMulti() + "\n\n");
                            stringBuilder.append("一级缓存：" + cpu.getCacheL1() + "\n\n");
                            stringBuilder.append("二级缓存：" + cpu.getCacheL2() + "\n\n");
                            stringBuilder.append("三级缓存：" + cpu.getCacheL3() + "\n\n");
                            stringBuilder.append("TDP：" + cpu.getTDP() + "\n\n");
                            stringBuilder.append("上市日期：" + cpu.getReleased() + "\n\n");
                            List<String> itemIDs = cpu.getItemIDs();
                            for (String itemID : itemIDs)
                            {
                                LogUtil.e("DisplayActivity", itemID);
                                String address = HttpUtil.LocalAddress + "/search/itemIDSearch";
                                HttpUtil.searchItemIdRequest(address, itemID, new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws
                                            IOException
                                    {
                                        final String responseString = response.body().string();
                                        LogUtil.e("DisplayActivity", responseString);
                                        relationItem(Utility.handleoneItemResponse(responseString));
                                    }
                                });
                            }
                        }
                        break;
                    case "gpu":
                        if (newitem.getGpu() != null)
                        {
                            GPU gpu = newitem.getGpu();
                            stringBuilder.append("————————————GPU————————————\n\n");
                            stringBuilder.append("型号：" + gpu.getName() + "\n\n");
                            stringBuilder.append("架构：" + gpu.getChip() + "\n\n");
                            stringBuilder.append("接口：" + gpu.getBus() + "\n\n");
                            stringBuilder.append("显存大小：" + gpu.getMemory_Size() + "\n\n");
                            stringBuilder.append("显存类型：" + gpu.getMemory_Type() + "\n\n");
                            stringBuilder.append("显存位宽：" + gpu.getMemory_Bus() + "\n\n");
                            stringBuilder.append("核心频率：" + gpu.getGPU_Clock() + "\n\n");
                            stringBuilder.append("着色单元：" + gpu.getShaders() + "\n\n");
                            stringBuilder.append("TMUs：" + gpu.getTMUs() + "\n\n");
                            stringBuilder.append("ROPs：" + gpu.getROPs() + "\n\n");
                            stringBuilder.append("TDP：" + gpu.getTDP() + "\n\n");
                            stringBuilder.append("上市日期：" + gpu.getReleased() + "\n\n");
                            List<String> itemIDs = gpu.getItemIDs();
                            for (String itemID : itemIDs)
                            {
                                LogUtil.e("DisplayActivity", itemID);
                                String address = HttpUtil.LocalAddress + "/search/itemIDSearch";
                                HttpUtil.searchItemIdRequest(address, itemID, new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws
                                            IOException
                                    {
                                        final String responseString = response.body().string();
                                        LogUtil.e("DisplayActivity", responseString);
                                        relationItem(Utility.handleoneItemResponse(responseString));
                                    }
                                });
                            }
                        }
                        break;
                    case "case":
                        if (newitem.getMycase() != null)
                        {
                            Hardware hardware = newitem.getMycase();
                            stringBuilder.append("————————————机箱————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                            List<String> itemIDs = hardware.getItemIDs();
                            for (String itemID : itemIDs)
                            {
                                LogUtil.e("DisplayActivity", itemID);
                                String address = HttpUtil.LocalAddress + "/search/itemIDSearch";
                                HttpUtil.searchItemIdRequest(address, itemID, new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws
                                            IOException
                                    {
                                        final String responseString = response.body().string();
                                        LogUtil.e("DisplayActivity", responseString);
                                        relationItem(Utility.handleoneItemResponse(responseString));
                                    }
                                });
                            }
                        }
                        break;
                    case "power":
                        if (newitem.getPower() != null)
                        {
                            Hardware hardware = newitem.getPower();
                            stringBuilder.append("————————————电源————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                            List<String> itemIDs = hardware.getItemIDs();
                            for (String itemID : itemIDs)
                            {
                                LogUtil.e("DisplayActivity", itemID);
                                String address = HttpUtil.LocalAddress + "/search/itemIDSearch";
                                HttpUtil.searchItemIdRequest(address, itemID, new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws
                                            IOException
                                    {
                                        final String responseString = response.body().string();
                                        LogUtil.e("DisplayActivity", responseString);
                                        relationItem(Utility.handleoneItemResponse(responseString));
                                    }
                                });
                            }
                        }
                        break;
                    case "cooler_water":
                        if (newitem.getCooler_water() != null)
                        {
                            Hardware hardware = newitem.getCooler_water();
                            stringBuilder.append("————————————水冷————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                            List<String> itemIDs = hardware.getItemIDs();
                            for (String itemID : itemIDs)
                            {
                                LogUtil.e("DisplayActivity", itemID);
                                String address = HttpUtil.LocalAddress + "/search/itemIDSearch";
                                HttpUtil.searchItemIdRequest(address, itemID, new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws
                                            IOException
                                    {
                                        final String responseString = response.body().string();
                                        LogUtil.e("DisplayActivity", responseString);
                                        relationItem(Utility.handleoneItemResponse(responseString));
                                    }
                                });
                            }
                        }
                        break;
                    case "cooler_wind":
                        if (newitem.getCooler_wind() != null)
                        {
                            Hardware hardware = newitem.getCooler_wind();
                            stringBuilder.append("————————————风冷————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                            List<String> itemIDs = hardware.getItemIDs();
                            for (String itemID : itemIDs)
                            {
                                LogUtil.e("DisplayActivity", itemID);
                                String address = HttpUtil.LocalAddress + "/search/itemIDSearch";
                                HttpUtil.searchItemIdRequest(address, itemID, new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws
                                            IOException
                                    {
                                        final String responseString = response.body().string();
                                        LogUtil.e("DisplayActivity", responseString);
                                        relationItem(Utility.handleoneItemResponse(responseString));
                                    }
                                });
                            }
                        }
                        break;
                    case "hdd":
                        if (newitem.getHdd() != null)
                        {
                            Hardware hardware = newitem.getHdd();
                            stringBuilder.append("————————————HDD————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                            List<String> itemIDs = hardware.getItemIDs();
                            for (String itemID : itemIDs)
                            {
                                LogUtil.e("DisplayActivity", itemID);
                                String address = HttpUtil.LocalAddress + "/search/itemIDSearch";
                                HttpUtil.searchItemIdRequest(address, itemID, new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws
                                            IOException
                                    {
                                        final String responseString = response.body().string();
                                        LogUtil.e("DisplayActivity", responseString);
                                        relationItem(Utility.handleoneItemResponse(responseString));
                                    }
                                });
                            }
                        }
                        break;
                    case "ssd":
                        LogUtil.e("DisplayActivity", "ssd!!!!  1");
                        if (newitem.getSsd() != null)
                        {
                            LogUtil.e("DisplayActivity", "ssd!!!!  2");
                            Hardware hardware = newitem.getSsd();
                            stringBuilder.append("————————————SSD————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                            List<String> itemIDs = hardware.getItemIDs();
                            for (String itemID : itemIDs)
                            {
                                LogUtil.e("DisplayActivity", itemID);
                                String address = HttpUtil.LocalAddress + "/search/itemIDSearch";
                                HttpUtil.searchItemIdRequest(address, itemID, new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws
                                            IOException
                                    {
                                        final String responseString = response.body().string();
                                        LogUtil.e("DisplayActivity", responseString);
                                        relationItem(Utility.handleoneItemResponse(responseString));
                                    }
                                });
                            }
                        }
                        break;
                    case "memory":
                        if (newitem.getMemory() != null)
                        {
                            Hardware hardware = newitem.getMemory();
                            stringBuilder.append("————————————内存————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                            List<String> itemIDs = hardware.getItemIDs();
                            for (String itemID : itemIDs)
                            {
                                LogUtil.e("DisplayActivity", itemID);
                                String address = HttpUtil.LocalAddress + "/search/itemIDSearch";
                                HttpUtil.searchItemIdRequest(address, itemID, new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws
                                            IOException
                                    {
                                        final String responseString = response.body().string();
                                        LogUtil.e("DisplayActivity", responseString);
                                        relationItem(Utility.handleoneItemResponse(responseString));
                                    }
                                });
                            }
                        }
                        break;
                    case "motherboard":
                        if (newitem.getMotherboard() != null)
                        {
                            Hardware hardware = newitem.getMotherboard();
                            stringBuilder.append("————————————主板————————————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                            List<String> itemIDs = hardware.getItemIDs();
                            for (String itemID : itemIDs)
                            {
                                LogUtil.e("DisplayActivity", itemID);
                                String address = HttpUtil.LocalAddress + "/search/itemIDSearch";
                                HttpUtil.searchItemIdRequest(address, itemID, new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws
                                            IOException
                                    {
                                        final String responseString = response.body().string();
                                        LogUtil.e("DisplayActivity", responseString);
                                        relationItem(Utility.handleoneItemResponse(responseString));
                                    }
                                });
                            }
                        }
                        break;
                    default:
                        break;
                }
                display_text.setText(stringBuilder.toString());
            }
        });
    }

    //不是items才访问此处
    private void relationItem(items relationItem)
    {
        if (relationItem == null)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(DisplayActivity.this, "有一个过期的相关商品已被删除", Toast.LENGTH_LONG)
                            .show();
                }
            });
            return;
        }
        itemsList.add(relationItem);
        LogUtil.e("SearchActivity", "得到item！！");
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                relative_text.setText("相关商品");
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
