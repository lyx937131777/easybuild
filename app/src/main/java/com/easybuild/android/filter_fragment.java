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

import java.util.List;

public class filter_fragment extends Fragment
{

    private RecyclerView recyclerView;
    private List<Type> typeList;
    private FilterTypeAdapter filterTypeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_filter,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.filter_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        typeList = DataSupport.where("typeName != ? and typeName != ?","all","item").find(Type.class);
        filterTypeAdapter = new FilterTypeAdapter(typeList);
        recyclerView.setAdapter(filterTypeAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        SearchActivity activity =(SearchActivity) getActivity();
        activity.getType();
    }
}
