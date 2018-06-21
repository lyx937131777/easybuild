package com.easybuild.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private List<Article> articleList = new ArrayList<>();
    private ArticleAdapter articleAdapter;
    private Article[] articles = {new Article("微星B350M", "2018-6-10", "匿名", "R5-2600 　 3.9G," +
            "6核12线程\n" +
            "微星B350M Gaming PRO\n" +
            "威刚XPG 　DDR4-2666 8GB\n" +
            "迪兰恒进 RX580 8G　X-Serial　　\n" +
            "盒装自带\n" +
            "镁光1100 256G\n" +
            "航嘉暗夜猎手2 标准版\n" +
            "鑫谷GP600G\n" +
            "5300元\n" +
            "【点评】这一套是R5-2600+RX580的搭配，cpu性能极为强劲，比较适合大型单机以及多任务需求较为依赖的用户选择，可以用来吃鸡、玩大型单机。", R
            .drawable.imgall),
            new Article("微星B360M", "2018-6-5", "匿名", "i5 8400散片 3.8G/6核6线程\n" +
                    "微星B360M MORTAR　\n" +
                    "威刚XPG 8G DDR4 2666\n" +
                    "耕升GTX1060 6G追风\n" +
                    "酷冷至尊T400i\n" +
                    "三星PM981 256G\n" +
                    "先马塞恩1\n" +
                    "蓝暴经典450W\n" +
                    "5300元\n" +
                    "【点评】比较主流的INTEL平台，这一套可以很好地应对1080P大型单机、吃鸡等游戏需求，8400也可以根据需求更换8500" +
                    "等，预算不足可以更换显卡为1065/63。", R.drawable.imgitem),
            new Article("华擎H310M HDV", "2018-5-20", "匿名", "i3 8100散片 4核4线程-3.6GHZ\n" +
                    "华擎H310M HDV\n" +
                    "威刚万紫千红 DDR4 8GB\n" +
                    "九州风神冰凌mini旗舰版\n" +
                    "耕升1050旋风2G\n" +
                    "镁光1100 256G\n" +
                    "航嘉暗夜猎手2 标准版\n" +
                    "航嘉冷静王2.31\n" +
                    "3100元\n" +
                    "【点评】平台直接升级到了第八代i3四核，未来升级潜力巨大。当然，如果不升级，一般的吃鸡也是没有任何问题的，GTX1050性能也还是不错的。　　\n" +
                    "【提示】如果要升级GTX1060或者更高级别的显卡，请务必搭配额定功率在400W以上的电源，例如Super500N、台达NX450、蓝暴经典450" +
                    "、酷冷GX450等。　　\n", R.drawable.imgamd),
            new Article("i3 8100散片 4核4线程-3.6GHZ", "2018-5-18", "匿名", "i3 8100散片 4核4线程-3.6GHZ\n" +
                    "华擎H310M HDV\n" +
                    "威刚万紫千红 DDR4 8GB\n" +
                    "九州风神冰凌mini旗舰版\n" +
                    "耕升1050旋风2G\n" +
                    "镁光1100 256G\n" +
                    "航嘉暗夜猎手2 标准版\n" +
                    "航嘉冷静王2.31\n" +
                    "3100元\n" +
                    "【点评】平台直接升级到了第八代i3四核，未来升级潜力巨大。当然，如果不升级，一般的吃鸡也是没有任何问题的，GTX1050性能也还是不错的。　　\n" +
                    "【提示】如果要升级GTX1060或者更高级别的显卡，请务必搭配额定功率在400W以上的电源，例如Super500N、台达NX450、蓝暴经典450" +
                    "、酷冷GX450等。　　\n", R.drawable.imgintel),
            new Article("华擎A320M-HDV", "2018-5-15", "匿名", "R3-1200( 3.40GHz/8MB/四核四线程)\n" +
                    "华擎A320M-HDV\n" +
                    "宇瞻黑豹DDr4-2400 8g\n" +
                    "耕升GTX1050TI 烈风4G\n" +
                    "盒装自带\n" +
                    "镁光1100 256G\n" +
                    "航嘉暗夜猎手2 标准版\n" +
                    "台达NX350\n" +
                    "3200元\n" +
                    "【点评】　相比i3版加强了显卡，cpu比g4560略强。", R.drawable.imgcase)};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initArticle();
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        articleAdapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(articleAdapter);
    }

    private void initArticle()
    {
        articleList.clear();
        for (int i = 0; i <articles.length;i++)
        {
            articleList.add(articles[i]);
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
        }
        return true;
    }
}
