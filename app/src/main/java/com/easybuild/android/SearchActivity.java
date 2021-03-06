package com.easybuild.android;

import android.content.ClipData;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.easybuild.android.gson.items;
import com.easybuild.android.util.HttpUtil;
import com.easybuild.android.util.LogUtil;
import com.easybuild.android.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener
{
    private String type;
    private String order = "综合";
    private String key = "";
    private String low = "";
    private String high = "";
    private String source = "全部";
    private String include = "1000000000";
    private int commond;
    private String plan;

    private Button order_button;
    private DrawerLayout mDrawerLayout;

    private PreviewAdapter previewAdapter;
    private List<items> itemsList = new ArrayList<>();
    public String[] key_value = new String[16];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        type = getIntent().getStringExtra("type");
        commond = MainActivity.getCommond();
        plan = getIntent().getStringExtra("plan");

        initDrawerLayout();
        for(int i = 0; i < key_value.length; i++)
        {
            key_value[i] = "";
        }

        initItems();

        RecyclerView recyclerView = findViewById(R.id.display_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        previewAdapter = new PreviewAdapter(itemsList);
        recyclerView.setAdapter(previewAdapter);
        initsetting();
    }

    public void initItems()
    {
        String address = HttpUtil.LocalAddress + "/search/easySearch";
        if(!type.equals("cpu") && !type.equals("gpu"))
        {
            if(commond == 2)
            {
                if(type.equals("motherboard"))
                {
                    if (plan.equals("amd"))
                    {
                        key = "AM4";
                    }else
                    {
                        key = "LGA 1151";
                    }
                }else if(type.equals("memory"))
                {
                    key = "ddr4";
                }else if(type.equals("power"))
                {
                    int cpuTDP = Integer.valueOf(FreePlanActivity.free_item.getCpu().getTDP());
                    int gpuTDP = Integer.valueOf(FreePlanActivity.free_item.getGpu().getTDP());
                    if(cpuTDP + gpuTDP <= 94)
                    {
                        key = "450w";
                    }else if(cpuTDP + gpuTDP <= 113)
                    {
                        key = "550w";
                    }else if (cpuTDP + gpuTDP <= 159)
                    {
                        key = "650w";
                    }else
                    {
                        key = "750w";
                    }
                }
            }

            HttpUtil.searchItemRequest(address, type, include, translate(order), key, low, high, new
                    Callback()
                    {
                        @Override
                        public void onFailure(Call call, IOException e)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(SearchActivity.this, "连接错误", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException
                        {
                            final String responseData = response.body().string();
                            LogUtil.e("SearchActivity", responseData);
                            String code = Utility.checkCode(responseData);
                            switch (code)
                            {
                                case "all":
                                case "items":
                                    initItems(Utility.handleItemsResponse(responseData));
                                    break;
                                case "false":
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            Toast.makeText(SearchActivity.this, "未知错误", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    break;
                                default:
                                    initItems(Utility.handleHardwareResponse(responseData));
                                    break;
                            }
                        }
                    });
        }else
        {
            if(commond == 2 &&type.equals("cpu"))
            {
                if (plan.equals("amd"))
                {
                    key_value[8] = "Socket";
                    key_value[9] = "AM4";
                }else if(plan.equals("intel"))
                {
                    key_value[0] = "Name";
                    key_value[1] = "core i";
                    key_value[8] = "Socket";
                    key_value[9] = "1151";
                }
            }else if(commond == 2 && type.equals("gpu"))
            {
                key_value[4] = "Bus";
                key_value[5] = "PCIe 3.0 x16";
            }

            LogUtil.e("SearchActivity","发送请求");
            HttpUtil.searchCPUGPURequest(address, type, key_value, new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(SearchActivity.this, "连接错误", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    final String responseData = response.body().string();
                    LogUtil.e("SearchActivity", responseData);
                    String code = Utility.checkCode(responseData);
                    switch (code)
                    {
                        case "cpu":
                            initItems(Utility.handleCPUResponse(responseData));
                            break;
                        case "gpu":
                            initItems(Utility.handleGPUResponse(responseData));
                            break;
                        default:
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(SearchActivity.this, "无结果", Toast.LENGTH_LONG)
                                            .show();
                                }
                            });
                            break;
                    }
                }
            });
        }

    }

    private void initItems(List<items> newList)
    {
        if (newList == null)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(SearchActivity.this, "搜索无结果", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        itemsList.clear();
        itemsList.addAll(newList);
        LogUtil.e("SearchActivity", "得到itemslist！！啦啦啦啦");
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                previewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initDrawerLayout()
    {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState)
            {
            }
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
        if (type.equals("all") || type.equals("items"))
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
                if(commond == 1 ||commond == 3)
                {
                    EditText keytext = findViewById(R.id.search_key);
                    EditText lowtext = findViewById(R.id.edit_low);
                    EditText hightext = findViewById(R.id.edit_high);
                    key = keytext.getText().toString();
                    low = lowtext.getText().toString();
                    high = hightext.getText().toString();
                    initItems();
                }
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
        if (type.equals("all") || type.equals("items") || type.equals("cpu") || type.equals("gpu"))
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
        LogUtil.e("SearchActivity", order);
        initItems();
        return false;
    }

    private String translate(String origin)
    {
        String result = origin;
        switch (origin)
        {
            case "综合":
                result = "comprehensive";
                break;
            case "性价比":
                result = "cost_perfomance";
                break;
            case "价格升序":
                result = "price_asc";
                break;
            case "价格降序":
                result = "price_des";
                break;
        }
        return result;
    }

    public String getType()
    {
        return type;
    }

    public String getInclude()
    {
        return include;
    }

    public void setInclude(String include)
    {
        this.include = include;
    }
}
