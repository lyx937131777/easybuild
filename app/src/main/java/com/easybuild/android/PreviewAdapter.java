package com.easybuild.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.easybuild.android.db.Favorite;
import com.easybuild.android.db.Type;
import com.easybuild.android.gson.items;
import com.easybuild.android.util.HttpUtil;
import com.easybuild.android.util.LogUtil;
import com.easybuild.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder>
{
    private Context mContext;
    private List<items> mItemsList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View preview;
        ImageView previewImage;
        TextView previewTitle;
        TextView previewPrice;
        TextView previewSource;
        TextView previewComments;
        ToggleButton previewFavorite;

        public  ViewHolder(View view)
        {
            super(view);
            preview = view;
            previewImage = view.findViewById(R.id.preview_img);
            previewTitle = view.findViewById(R.id.preview_title);
            previewPrice = view.findViewById(R.id.preview_price);
            previewComments = view.findViewById(R.id.preview_comments);
            previewSource = view.findViewById(R.id.preview_source);
            previewFavorite = view.findViewById(R.id.preview_favorite);
        }
    }

    public PreviewAdapter(List<items> itemsList)
    {
        mItemsList = itemsList;
    }
    @NonNull
    @Override
    public PreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.preview_item,parent,false);
        final PreviewAdapter.ViewHolder holder = new PreviewAdapter.ViewHolder(view);
        holder.preview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int position = holder.getAdapterPosition();
                final items mitems = mItemsList.get(position);

                if(MainActivity.getCommond() == 1)
                {

                    Intent intent = new Intent(mContext,DisplayActivity.class);
                    if(mitems.getType().equals("freeplan"))
                    {
                        MainActivity.setCommond(2);
                        intent.putExtra("type","items");
                        intent.putExtra("plan","free");
                        FreePlanActivity.free_item = mitems;
                    }else
                    {
                        intent.putExtra("favorite_id",mitems.get_id().get$oid());
                        intent.putExtra("type",mitems.getType());
                    }
                    mContext.startActivity(intent);
                }else if(MainActivity.getCommond() == 2)
                {
                    String address = HttpUtil.LocalAddress + "/search/idSearch";
                    HttpUtil.searchIdRequest(address, mitems.getType(), mitems.get_id().get$oid(), new Callback()
                    {
                        @Override
                        public void onFailure(Call call, IOException e)
                        {

                        }

                        @Override
                        public void onResponse(final Call call, Response response) throws IOException
                        {
                            final String responseData = response.body().string();
                            LogUtil.e("FavoriteActivity", responseData);
                            String code = Utility.checkCode(responseData);
                            switch (code)
                            {
                                case "cpu":
                                    Utility.handleoneCPUResponse(responseData);
                                    break;
                                case "gpu":
                                    Utility.handleoneGPUResponse(responseData);
                                    break;
                                case "false":
                                    break;
                                default:
                                    Utility.handleoneHardwareResponse(responseData);
                                    break;
                            }
                            final SearchActivity searchActivity =(SearchActivity) mContext;
                            searchActivity.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if(mitems.getType().equals("hdd"))
                                    {
                                        Intent intent = new Intent(mContext,DisplayActivity.class);
                                        intent.putExtra("type","items");
                                        intent.putExtra("plan","free");
                                        mContext.startActivity(intent);
                                        searchActivity.finish();
                                    }else
                                    {
                                        Intent intent = new Intent(mContext,SearchActivity.class);
                                        switch (mitems.getType())
                                        {
                                            case "cpu":
                                                intent.putExtra("type","motherboard");
                                                intent.putExtra("plan",searchActivity.getIntent().getStringExtra("plan"));
                                                break;
                                            case "motherboard":
                                                intent.putExtra("type","gpu");
                                                break;
                                            case "gpu":
                                                intent.putExtra("type","memory");
                                                break;
                                            case "memory":
                                                intent.putExtra("type","power");
                                                break;
                                            case "power":
                                                intent.putExtra("type","cooler_wind");
                                                break;
                                            case "cooler_wind":
                                                intent.putExtra("type","cooler_water");
                                                break;
                                            case "cooler_water":
                                                intent.putExtra("type","ssd");
                                                break;
                                            case "ssd":
                                                intent.putExtra("type","hdd");
                                                break;
                                        }
                                        mContext.startActivity(intent);
                                        searchActivity.finish();
                                    }
                                }
                            });
                        }
                    });
                }else if(MainActivity.getCommond() == 3)
                {
                    String address = HttpUtil.LocalAddress + "/search/idSearch";
                    HttpUtil.searchIdRequest(address, mitems.getType(), mitems.get_id().get$oid(), new Callback()
                    {
                        @Override
                        public void onFailure(Call call, IOException e)
                        {

                        }

                        @Override
                        public void onResponse(final Call call, Response response) throws IOException
                        {
                            final String responseData = response.body().string();
                            LogUtil.e("FavoriteActivity", responseData);
                            String code = Utility.checkCode(responseData);
                            switch (code)
                            {
                                case "cpu":
                                    CompareActivity.compareitem = Utility.handleoneCPUResponse(responseData);
                                    break;
                                case "gpu":
                                    CompareActivity.compareitem = Utility.handleoneGPUResponse(responseData);
                                    break;
                                case "items":
                                    CompareActivity.compareitem = Utility.handleoneItemResponse(responseData);
                                case "false":
                                    break;
                                default:
                                    CompareActivity.compareitem =Utility.handleoneHardwareResponse(responseData);
                                    break;
                            }
                            final SearchActivity searchActivity = (SearchActivity)mContext;
                            searchActivity.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    searchActivity.setResult(Activity.RESULT_OK);
                                    searchActivity.finish();
                                }
                            });
                        }
                    });
                }
            }
        });
        holder.previewFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int position = holder.getAdapterPosition();
                items mitems = mItemsList.get(position);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                String userID = pref.getString("userID", null);
                if(mitems.getType().equals("freeplan"))
                {
                    LogUtil.e("PreviewAdapter","FreePlan");
                }else
                {
                    String favorite_id = mitems.get_id().get$oid();
                    if(isChecked)
                    {
                        LogUtil.e("PreviewAdapter","ON!!!      : "+position);
                        Favorite favorite = DataSupport.where("favorite_id = ? and userID = ?",favorite_id,userID).findFirst(Favorite.class);
                        if(favorite == null)
                        {
                            favorite = new Favorite();
                            favorite.setFavorite_id(favorite_id);
                            favorite.setUserID(userID);
                            favorite.setType(mitems.getType());
                            favorite.save();
                        }
                    }else
                    {
                        DataSupport.deleteAll(Favorite.class,"favorite_id = ? and userID = ?",favorite_id,userID);
                        LogUtil.e("PreviewAdapter","OFF!!!    "+position);
                    }
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewAdapter.ViewHolder holder, int position)
    {
        items mitems = mItemsList.get(position);
        holder.previewTitle.setText("hello");
        holder.previewTitle.setText(mitems.getTitle());
        holder.previewSource.setText(mitems.getSource());
        holder.previewComments.setText(mitems.getComments());
        holder.previewPrice.setText(mitems.getPrice());
        Glide.with(mContext).load(mitems.getImg()).into(holder.previewImage);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userID = pref.getString("userID", null);
        if(mitems.getType().equals("freeplan"))
        {
            LogUtil.e("PreviewAdapter","FreePlan");
        }else
        {
            String favorite_id = mitems.get_id().get$oid();

            if(DataSupport.where("favorite_id = ? and userID = ?",favorite_id,userID).findFirst(Favorite.class) != null)
            {
                holder.previewFavorite.setChecked(true);
                LogUtil.e("PreviewAdapter","init!!!"+position);
            }else
            {
                holder.previewFavorite.setChecked(false);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return mItemsList.size();
    }
}
