package com.easybuild.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easybuild.android.db.Type;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class filter_fragment extends Fragment
{
    private String type;
    private RecyclerView recyclerView;
    private List<Type> typeList;
    private FilterTypeAdapter filterTypeAdapter;
    private FilterDetailAdapter filterDetailAdapter;
    private List<Detail> detailList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_filter,container,false);
        recyclerView = view.findViewById(R.id.filter_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        SearchActivity activity =(SearchActivity) getActivity();
        type = activity.getType();
        if(type.equals("all") || type.equals("items"))
        {
            typeList = DataSupport.where("typeName != ? and typeName != ?","all","items").find(Type.class);
            filterTypeAdapter = new FilterTypeAdapter(typeList);
            recyclerView.setAdapter(filterTypeAdapter);
        }else if(type.equals("cpu") || type.equals("gpu"))
        {
            initDetailList(type);
            filterDetailAdapter = new FilterDetailAdapter(detailList);
            recyclerView.setAdapter(filterDetailAdapter);
        }
    }

    private void initDetailList(String type)
    {
        detailList.clear();
        switch (type)
        {
            case "cpu":
                Detail[] details = {new Detail("型号","Name","A10-5700","A10-9700E","A12-9800E"),
                new Detail("架构","Codename","Kaveri","Llano","Trinity"),
                new Detail("核心数","Cores","2","3","4"),
                new Detail("线程数","Threads","0","1","2"),
                new Detail("接口","Socket","AM4","FM1","FT4"),
                new Detail("工艺","Process","32","28","180"),
                new Detail("核心频率","Clock","10000","8000","90000"),
                new Detail("一级缓存","CacheL1","128","96","256")};
                for (int i = 0; i < details.length; i++)
                {
                    detailList.add(details[i]);
                }
                break;
            case "gpu":
                Detail[] details2 = {new Detail("型号","Name","3D Rage","GeForce","FirePro"),
                        new Detail("架构","Chip","G92","GP108","GM206"),
                        new Detail("接口","Bus","PCI","AGP 2X","PCIe"),
                        new Detail("核心频率","GPU_ Clock","450","360","550"),
                        new Detail("显存类型","Memory_Type","DDR2","GDDR3","SDR"),
                        new Detail("显存位宽","Memory_Bus","64","128","256"),
                        new Detail("着色单元","Shaders","405","500","660"),
                        new Detail("TMUs","TMUs","7","8","16")};
                for (int i = 0; i < details2.length; i++)
                {
                    detailList.add(details2[i]);
                }
                break;
        }
    }
}
