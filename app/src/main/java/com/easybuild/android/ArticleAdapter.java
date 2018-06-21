package com.easybuild.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.easybuild.android.db.Favorite;
import com.easybuild.android.db.Type;
import com.easybuild.android.gson.items;
import com.easybuild.android.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>
{
    private Context mContext;
    private List<Article> mArticleList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View articleView;
        de.hdodenhof.circleimageview.CircleImageView articleImage;
        TextView articleTitle;
        TextView articleAuthor;
        TextView articleDate;
        ToggleButton articleFavorite;

        public  ViewHolder(View view)
        {
            super(view);
            articleView = view;
            articleImage = view.findViewById(R.id.article_img);
            articleTitle = view.findViewById(R.id.article_title);
            articleAuthor = view.findViewById(R.id.article_author);
            articleDate = view.findViewById(R.id.article_date);
            articleFavorite = view.findViewById(R.id.article_favorite);
        }
    }

    public ArticleAdapter(List<Article> articleList)
    {
        mArticleList = articleList;
    }

    public ArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.article_item,parent,false);
        final ArticleAdapter.ViewHolder holder = new ArticleAdapter.ViewHolder(view);
        holder.articleView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int position = holder.getAdapterPosition();
                Article article = mArticleList.get(position);
                Intent intent = new Intent(mContext,ShowArticleActivity.class);
                intent.putExtra("article",article);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ViewHolder holder, int position)
    {
        Article article = mArticleList.get(position);
        holder.articleTitle.setText(article.getTitle());
        holder.articleDate.setText(article.getDate());
        holder.articleAuthor.setText(article.getAuthor());
        Glide.with(mContext).load(article.getImgID()).into(holder.articleImage);
    }

    @Override
    public int getItemCount()
    {
        return mArticleList.size();
    }
}
