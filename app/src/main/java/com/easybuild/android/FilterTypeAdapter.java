package com.easybuild.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easybuild.android.db.Type;
import com.easybuild.android.util.LogUtil;

import java.util.List;

public class FilterTypeAdapter  extends RecyclerView.Adapter<FilterTypeAdapter.ViewHolder>
{
    private Context mContext;
    private List<Type> mTypeList;
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox typeCheckBox;

        public  ViewHolder(View view)
        {
            super(view);
            typeCheckBox = view.findViewById(R.id.type_checkbox);
        }
    }

    public FilterTypeAdapter(List<Type> typeList)
    {
        mTypeList = typeList;
    }
    @NonNull
    @Override
    public FilterTypeAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.filter_type_item,parent,false);
        final FilterTypeAdapter.ViewHolder holder = new FilterTypeAdapter.ViewHolder(view);
        holder.typeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int position = holder.getAdapterPosition();
                SearchActivity searchActivity = (SearchActivity)mContext;
                StringBuilder include =  new StringBuilder(searchActivity.getInclude());
                if(isChecked)
                {
                    LogUtil.e("FilterTypeAdapter","Before Change : "+ include);
                    include.replace(position,position+1,"1");
                    LogUtil.e("FilterTypeAdapter","After Change : "+ include);
                }else
                {
                    LogUtil.e("FilterTypeAdapter","Before Change : "+ include);
                    include.replace(position,position+1,"0");
                    LogUtil.e("FilterTypeAdapter","After Change : "+ include);
                }
                searchActivity.setInclude(include.toString());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FilterTypeAdapter.ViewHolder holder, int position)
    {
        Type type = mTypeList.get(position);
        holder.typeCheckBox.setText("  "+type.getName()+"  ");
        SearchActivity searchActivity = (SearchActivity)mContext;
        String include = searchActivity.getInclude();
        if(include.charAt(position) == '1')
        {
            holder.typeCheckBox.setChecked(true);
        }else
        {
            holder.typeCheckBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount()
    {
        return mTypeList.size();
    }
}
