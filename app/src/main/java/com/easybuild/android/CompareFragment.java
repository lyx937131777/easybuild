package com.easybuild.android;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.easybuild.android.db.Type;
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
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CompareFragment extends Fragment
{

    private RecyclerView recyclerView;
    private List<Type> typeList;
    private TypeAdapter adapter;
    private NestedScrollView display_view;
    private TextView display_text;
    private CardView display_change;
    public int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState)
    {
        View view = inflater.inflate(R.layout.compare_fragment, container, false);
        recyclerView = view.findViewById(R.id.type_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        display_view = view.findViewById(R.id.display_view);
        display_change = view.findViewById(R.id.display_change);
        display_text = view.findViewById(R.id.display_text);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (getId() == R.id.left_fragment)
        {
            position = 1;
        } else if (getId() == R.id.right_fragment)
        {
            position = 2;
        }
        typeList = DataSupport.findAll(Type.class);
        adapter = new TypeAdapter(typeList,position);
        recyclerView.setAdapter(adapter);

        display_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeView();
            }
        });
    }

    public void changeView()
    {
        if (recyclerView.getVisibility() == View.VISIBLE)
        {
            recyclerView.setVisibility(View.GONE);
            display_view.setVisibility(View.VISIBLE);
            initItems(CompareActivity.compareitem);
        } else
        {
            recyclerView.setVisibility(View.VISIBLE);
            display_view.setVisibility(View.GONE);
        }
    }

    private void initItems(final items newitem)
    {
        CompareActivity compareActivity = (CompareActivity) getContext();
        if (newitem == null)
        {
            compareActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(getContext(), "未找到该商品", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        LogUtil.e("SearchActivity", "得到item！！");
        compareActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                StringBuilder stringBuilder = new StringBuilder();
                switch (newitem.getType())
                {
                    case "items":
                        stringBuilder.append("————产品————\n\n");
                        stringBuilder.append("名称：\n" + newitem.getTitle() + "\n\n");
                        stringBuilder.append("价格：" + newitem.getPrice() + "\n\n");
                        stringBuilder.append(newitem.getComments() + "\n\n");
                        stringBuilder.append("来源：" + newitem.getSource() + "\n\n");
                        try
                        {
                            JSONObject prices = new JSONObject(newitem.getPrices());
                            Iterator<String> iterator = prices.keys();
                            stringBuilder.append("历史价格:\n");
                            while (iterator.hasNext())
                            {
                                String date = iterator.next();
                                String price = prices.getString(date);
                                stringBuilder.append("      " + date + " ： ￥" + price + "\n");
                            }
                            stringBuilder.append("\n");
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        if (newitem.getCpu() != null)
                        {
                            CPU cpu = newitem.getCpu();
                            stringBuilder.append("————CPU————\n\n");
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
                            stringBuilder.append("————GPU————\n\n");
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
                            stringBuilder.append("————机箱————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getPower() != null)
                        {
                            Hardware hardware = newitem.getPower();
                            stringBuilder.append("————电源————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getCooler_water() != null)
                        {
                            Hardware hardware = newitem.getCooler_water();
                            stringBuilder.append("————水冷————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getCooler_wind() != null)
                        {
                            Hardware hardware = newitem.getCooler_wind();
                            stringBuilder.append("————风冷————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getHdd() != null)
                        {
                            Hardware hardware = newitem.getHdd();
                            stringBuilder.append("————HDD————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getSsd() != null)
                        {
                            Hardware hardware = newitem.getSsd();
                            stringBuilder.append("————SSD————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getMemory() != null)
                        {
                            Hardware hardware = newitem.getMemory();
                            stringBuilder.append("————内存————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        if (newitem.getMotherboard() != null)
                        {
                            Hardware hardware = newitem.getMotherboard();
                            stringBuilder.append("————主板————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        break;
                    case "cpu":
                        if (newitem.getCpu() != null)
                        {
                            CPU cpu = newitem.getCpu();
                            stringBuilder.append("————CPU————\n\n");
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
                        break;
                    case "gpu":
                        if (newitem.getGpu() != null)
                        {
                            GPU gpu = newitem.getGpu();
                            stringBuilder.append("————GPU————\n\n");
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
                        break;
                    case "case":
                        if (newitem.getMycase() != null)
                        {
                            Hardware hardware = newitem.getMycase();
                            stringBuilder.append("————机箱————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        break;
                    case "power":
                        if (newitem.getPower() != null)
                        {
                            Hardware hardware = newitem.getPower();
                            stringBuilder.append("————电源————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        break;
                    case "cooler_water":
                        if (newitem.getCooler_water() != null)
                        {
                            Hardware hardware = newitem.getCooler_water();
                            stringBuilder.append("————水冷————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        break;
                    case "cooler_wind":
                        if (newitem.getCooler_wind() != null)
                        {
                            Hardware hardware = newitem.getCooler_wind();
                            stringBuilder.append("————风冷————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        break;
                    case "hdd":
                        if (newitem.getHdd() != null)
                        {
                            Hardware hardware = newitem.getHdd();
                            stringBuilder.append("————HDD————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        break;
                    case "ssd":
                        if (newitem.getSsd() != null)
                        {
                            Hardware hardware = newitem.getSsd();
                            stringBuilder.append("————SSD————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        break;
                    case "memory":
                        if (newitem.getMemory() != null)
                        {
                            Hardware hardware = newitem.getMemory();
                            stringBuilder.append("————内存————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        break;
                    case "motherboard":
                        if (newitem.getMotherboard() != null)
                        {
                            Hardware hardware = newitem.getMotherboard();
                            stringBuilder.append("————主板————\n\n");
                            stringBuilder.append(hardware.getString1() + "\n\n");
                            stringBuilder.append(hardware.getString2() + "\n\n");
                            stringBuilder.append(hardware.getString3() + "\n\n");
                        }
                        break;
                    default:
                        break;
                }
                display_text.setText(stringBuilder.toString());
            }
        });
    }
}
