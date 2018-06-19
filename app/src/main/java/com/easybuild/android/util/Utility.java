package com.easybuild.android.util;

import android.text.TextUtils;
import android.util.Log;

import com.easybuild.android.db.User;
import com.easybuild.android.gson.items;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Utility
{
    public static boolean handleUserResponse(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                DataSupport.deleteAll(User.class);
                JSONArray allUsers = new JSONArray(response);
                for (int i = 0; i < allUsers.length(); i++)
                {
                    JSONObject userObject = allUsers.getJSONObject(i);
                    User user = new User();
                    user.setNickname(userObject.getString("nickname"));
                    user.setUserID(userObject.getString("userID"));
                    user.setPassword(userObject.getString("password"));
                    user.setPhone_number(userObject.getString("phone_number"));
                    user.setProfile_photo(userObject.getString("profile_photo"));
                    Log.e("test", user.getUserID());
                    Log.e("test", user.getPassword());
                    Log.e("test", user.getNickname());
                    Log.e("test", user.getPhone_number());
                    Log.e("test", user.getProfile_photo());
                    user.save();
                }
                return true;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    //注册界面
    public static boolean storeLoginUser(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                LogUtil.e("Utility", response);
                JSONObject dataObject = new JSONObject(response);
                LogUtil.e("Utility", "111111111111111111");
                if (dataObject.getString("code").equals("true"))
                {
                    LogUtil.e("Utility", "2222222222222222222222222222");
                    JSONObject userObject = dataObject.getJSONObject("data");
                    LogUtil.e("Utility", userObject.getString("userID"));
                    LogUtil.e("Utility", userObject.getString("password"));
                    LogUtil.e("Utility", userObject.getString("nickname"));
                    LogUtil.e("Utility", userObject.getString("profile_photo"));
                    LogUtil.e("Utility", userObject.getString("phone_number"));
                    User user = new User();
                    user.setNickname(userObject.getString("nickname"));
                    user.setUserID(userObject.getString("userID"));
                    user.setPassword(userObject.getString("password"));
                    user.setPhone_number(userObject.getString("phone_number"));
                    user.setProfile_photo(userObject.getString("profile_photo"));
                    DataSupport.deleteAll(User.class, "userID = ?", user.getUserID());
                    user.save();
                    return true;
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    //返回Json数据的code值
    public static String checkCode(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            return dataObject.getString("code");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }return "000";
    }

    //解析items对象
    public static List<items> handleItemsResponse (String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            JSONArray jsonArray = dataObject.getJSONArray("data");
            String itemsJson = jsonArray.toString();
            LogUtil.e("Utility",itemsJson);
            return new Gson().fromJson(itemsJson,new TypeToken<List<items>>(){}.getType());
        } catch (JSONException e)
        {
            e.printStackTrace();
            LogUtil.e("Utility","GSON Wrong!!!!");
        }
        return null;
    }
    //收藏界面 解析单个item对象
    public static items handleoneItemResponse(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            JSONObject jsonObject = dataObject.getJSONObject("data");
            String itemJson = jsonObject.toString();
            LogUtil.e("Utility",itemJson);
            return new Gson().fromJson(itemJson,items.class);
        } catch (JSONException e)
        {
            e.printStackTrace();
            LogUtil.e("Utility","GSON Wrong!!!!");
        }
        return null;
    }

    public static boolean searchUser(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONArray allUsers = new JSONArray(response);
                for (int i = 0; i < allUsers.length(); i++)
                {
                    JSONObject userObject = allUsers.getJSONObject(i);
                    User user = new User();
                    user.setNickname(userObject.getString("nickname"));
                    user.setUserID(userObject.getString("userID"));
                    user.setPassword(userObject.getString("password"));
                    user.setPhone_number(userObject.getString("phone_number"));
                    user.setProfile_photo(userObject.getString("profile_photo"));
                    Log.e("test", user.getUserID());
                    Log.e("test", user.getPassword());
                    Log.e("test", user.getNickname());
                    Log.e("test", user.getPhone_number());
                    Log.e("test", user.getProfile_photo());
                    user.save();
                }
                return true;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static ArrayList<User> storeUserResponse(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                ArrayList<User> users = new ArrayList<>();
                JSONArray allUsers = new JSONArray(response);
                for (int i = 0; i < allUsers.length(); i++)
                {
                    JSONObject userObject = allUsers.getJSONObject(i);
                    User user = new User();
                    user.setNickname(userObject.getString("nickname"));
                    user.setUserID(userObject.getString("userID"));
                    user.setPassword(userObject.getString("password"));
                    user.setPhone_number(userObject.getString("phone_number"));
                    user.setProfile_photo(userObject.getString("profile_photo"));
                    Log.e("test", user.getUserID());
                    Log.e("test", user.getPassword());
                    Log.e("test", user.getNickname());
                    Log.e("test", user.getPhone_number());
                    Log.e("test", user.getProfile_photo());
                    DataSupport.deleteAll(User.class, "userID = ?", user.getUserID());
                    user.save();
                    users.add(user);
                }
                return users;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        ArrayList<User> users2 = new ArrayList<>();
        return users2;
    }
}
