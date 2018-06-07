package com.easybuild.android;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MyService extends Service
{

    private MyThread myThread = null;
    private Thread newThread = null;
    public static boolean isRun = true;


    private final class MyThread implements Runnable
    {
        private Context context;

        private MyThread(Context context)
        {
            this.context = context;
        }

        String topActivity = "com.example.hasee.test";

        @Override
        public void run()
        {
            Set<String> defaultLaunchers = getDefaultLaunchers();
            while (isRun)
            {
                try
                {
                    TimeUnit.SECONDS.sleep(2);
                    getTopApp(context);
                    if (!topActivity.equals("com.example.hasee.test") && !defaultLaunchers
                            .contains(topActivity))
                    {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            stopSelf();
        }

        //获取当前系统的默认桌面包名
        public Set<String> getDefaultLaunchers()
        {
            Set<String> defaultLaunchers = new HashSet<String>();
            PackageManager packageManager = getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            // 查询所有已安装的桌面应用
            List<ResolveInfo> launcherList = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            // 获取默认的桌面应用，没有指定默认桌面时返回"android"
            ResolveInfo defaultLauncher = packageManager.resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            if (launcherList != null && defaultLauncher != null)
            {
                String pkgName = defaultLauncher.activityInfo.packageName;
                Log.e("MyService:", "default launcher is : " + pkgName);
                // 没有指定默认桌面时返回的是“android”，故这里过滤下防止“android”被添加到默认桌面列表中
                for (ResolveInfo info : launcherList)
                {
                    Log.e("MyService:", "launcher : " + info.activityInfo.packageName);
                    if (info.activityInfo.packageName.equals(pkgName))
                    {
                        defaultLaunchers.add(pkgName);
                    }
                }
            }
            // 有多个桌面应用，且目前没有指定默认桌面时，返回所有的桌面应用
            if (defaultLaunchers.isEmpty() && launcherList != null)
            {
                for (ResolveInfo resolveInfo : launcherList)
                {
                    defaultLaunchers.add(resolveInfo.activityInfo.packageName);
                }
            }
            Log.e("MyService:", "defaultLaunchers : " + defaultLaunchers);
            return defaultLaunchers;
        }

        private void getTopApp(Context context)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context
                        .USAGE_STATS_SERVICE);
                if (m != null)
                {
                    long now = System.currentTimeMillis();
                    //获取3秒之内的应用数据
                    List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                            now - 3 * 1000, now);

                    //取得最近运行的一个app，即当前运行的app
                    if ((stats != null) && (!stats.isEmpty()))
                    {
                        int j = 0;
                        for (int i = 0; i < stats.size(); i++)
                        {
                            if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed())
                            {
                                j = i;
                            }
                        }
                        topActivity = stats.get(j).getPackageName();
                    }
                    Log.e("MyService", "top running app is : " + topActivity);

                }
            }
        }

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        myThread = new MyThread(this);
        newThread = new Thread(myThread);
        newThread.start();
        Log.e("MyService", "Service is start.");
        if (MainActivity.time != 0)
        {
            try
            {
                TimeUnit.SECONDS.sleep(MainActivity.time);
                isRun = false;
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("MyService", "Service is stop.");
    }

}
