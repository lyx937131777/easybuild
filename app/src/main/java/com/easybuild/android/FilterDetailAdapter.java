package com.easybuild.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easybuild.android.db.Type;
import com.easybuild.android.util.LogUtil;

import java.util.List;

public class FilterDetailAdapter extends RecyclerView.Adapter<FilterDetailAdapter.ViewHolder>
{
    private Context mContext;
    private List<Detail> mDetailList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView detail_name;
        EditText detail_edit;
        Button show_chooser;
        LinearLayout chooser_layout;
        Button detail_button1, detail_button2, detail_button3;

        public ViewHolder(View view)
        {
            super(view);
            detail_name = view.findViewById(R.id.detail_name);
            detail_edit = view.findViewById(R.id.detail_edit);
            show_chooser = view.findViewById(R.id.show_chooser);
            chooser_layout = view.findViewById(R.id.chooser_layout);
            detail_button1 = view.findViewById(R.id.detail_button1);
            detail_button2 = view.findViewById(R.id.detail_button2);
            detail_button3 = view.findViewById(R.id.detail_button3);
        }
    }

    public FilterDetailAdapter(List<Detail> DetailList)
    {
        mDetailList = DetailList;
    }

    @NonNull
    @Override
    public FilterDetailAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int
            viewType)
    {
        if (mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.filter_detail_item, parent,
                false);
        final FilterDetailAdapter.ViewHolder holder = new FilterDetailAdapter.ViewHolder(view);
        holder.show_chooser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (holder.chooser_layout.getVisibility() == View.VISIBLE)
                {
                    holder.chooser_layout.setVisibility(View.GONE);
                } else
                {
                    holder.chooser_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.detail_button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button button = (Button)v;
                holder.detail_edit.setText(button.getText().toString());
            }
        });
        holder.detail_button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button button = (Button)v;
                holder.detail_edit.setText(button.getText().toString());
            }
        });
        holder.detail_button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button button = (Button)v;
                holder.detail_edit.setText(button.getText().toString());
            }
        });
        holder.detail_edit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int position = holder.getAdapterPosition();
                String value = holder.detail_edit.getText().toString();
                SearchActivity searchActivity = (SearchActivity)mContext;
                searchActivity.key_value[position*2+1] = value;
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FilterDetailAdapter.ViewHolder holder, int position)
    {
        Detail detail = mDetailList.get(position);
        holder.detail_name.setText(detail.getDetail_name() + ":");
        holder.detail_button1.setText(detail.getChooser1());
        holder.detail_button2.setText(detail.getChooser2());
        holder.detail_button3.setText(detail.getChooser3());
        SearchActivity searchActivity = (SearchActivity)mContext;
        searchActivity.key_value[position*2] = detail.getDetail_attribute();
    }

    @Override
    public int getItemCount()
    {
        return mDetailList.size();
    }
}
