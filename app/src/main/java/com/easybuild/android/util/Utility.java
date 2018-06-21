package com.easybuild.android.util;

import android.text.TextUtils;
import android.util.Log;

import com.easybuild.android.CompareActivity;
import com.easybuild.android.FreePlanActivity;
import com.easybuild.android.MainActivity;
import com.easybuild.android.R;
import com.easybuild.android.db.User;
import com.easybuild.android.gson.CPU;
import com.easybuild.android.gson.GPU;
import com.easybuild.android.gson.Hardware;
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
        }
        return "000";
    }

    //解析items对象
    public static List<items> handleItemsResponse(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            JSONArray jsonArray = dataObject.getJSONArray("data");
            String itemsJson = jsonArray.toString();
            LogUtil.e("Utility", itemsJson);
            List<items> itemsList = new Gson().fromJson(itemsJson, new TypeToken<List<items>>()
            {
            }.getType());
            List<items> newitemList = new ArrayList<>();
            for (items item : itemsList)
            {
                if(MainActivity.getCommond()== 2)
                {
                    newitemList.add(item);
                }
                else if (Float.valueOf(item.getPrice()) > 0)
                {
                    item.setPrice("￥" + item.getPrice());
                    item.setType("items");
                    item.setSource("京东");
                    item.setComments("评论数：" + item.getComments());
                    newitemList.add(item);
                }
            }
            return newitemList;
        } catch (JSONException e)
        {
            e.printStackTrace();
            LogUtil.e("Utility", "GSON Wrong!!!!");
        }
        return null;
    }

    //解析CPU对象
    public static List<items> handleCPUResponse(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            JSONArray jsonArray = dataObject.getJSONArray("data");
            String itemsJson = jsonArray.toString();
            LogUtil.e("Utility", itemsJson);
            List<items> itemsList = new ArrayList<>();
            List<CPU> cpuList = new Gson().fromJson(itemsJson, new TypeToken<List<CPU>>()
            {
            }.getType());
            for (CPU cpu : cpuList)
            {
                items item = new items();
                item.set_id(cpu.get_id());
                item.setType("cpu");
                item.setCpu(cpu);
                item.setTitle(cpu.getName() + "\n" + cpu.getCodename());
                if(cpu.getClock() == null)
                {
                    LogUtil.e("Utility","There is a null");
                    item.setPrice("未知");
                }else
                {
                    item.setPrice(Float.valueOf(cpu.getClock())/1000+ "GHz");
                }
                item.setComments("Cores:" + cpu.getCores());
                item.setImg(cpu.getImg());
                item.setSource(cpu.getSocket());
                itemsList.add(item);
            }
            return itemsList;
        } catch (JSONException e)
        {
            e.printStackTrace();
            LogUtil.e("Utility", "GSON Wrong!!!!");
        }
        return null;
    }

    //解析GPU对象
    public static List<items> handleGPUResponse(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            JSONArray jsonArray = dataObject.getJSONArray("data");
            String itemsJson = jsonArray.toString();
            LogUtil.e("Utility", itemsJson);
            List<items> itemsList = new ArrayList<>();
            List<GPU> gpuList = new Gson().fromJson(itemsJson, new TypeToken<List<GPU>>() {}.getType());
            for (GPU gpu : gpuList)
            {
                items item = new items();
                item.set_id(gpu.get_id());
                item.setType("gpu");
                item.setGpu(gpu);
                item.setTitle(gpu.getName() + "\n" + gpu.getChip());
                if(gpu.getGPU_Clock() == null)
                {
                    LogUtil.e("Utility","There is a null");
                    item.setPrice("未知");
                }else
                {
                    item.setPrice(gpu.getGPU_Clock()+"MHz");
                }
                item.setComments("");
                item.setImg(gpu.getImg());
                item.setSource(gpu.getBus());
                itemsList.add(item);
            }
            return itemsList;
        } catch (JSONException e)
        {
            e.printStackTrace();
            LogUtil.e("Utility", "GSON Wrong!!!!");
        }
        return null;
    }

    //解析其他Hardware对象
    public static List<items> handleHardwareResponse(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            String type = dataObject.getString("code");
            JSONArray jsonArray = dataObject.getJSONArray("data");
            String itemsJson = jsonArray.toString();
            LogUtil.e("Utility", itemsJson);
            List<items> itemsList = new ArrayList<>();
            List<Hardware> hardwareList = new Gson().fromJson(itemsJson, new TypeToken<List<Hardware>>() {}.getType());
            for (Hardware hardware : hardwareList)
            {
                items item = new items();
                item.set_id(hardware.get_id());
                item.setType(type);
                switch (type)
                {
                    case "case":
                        item.setMycase(hardware);
                        break;
                    case "power":
                        item.setPower(hardware);
                        break;
                    case "cooler_water":
                        item.setCooler_water(hardware);
                        break;
                    case "cooler_wind":
                        item.setCooler_wind(hardware);
                        break;
                    case "hdd":
                        item.setHdd(hardware);
                        break;
                    case "ssd":
                        item.setSsd(hardware);
                        break;
                    case "memory":
                        item.setMemory(hardware);
                        break;
                    case "motherboard":
                        item.setMotherboard(hardware);
                        break;
                    default:
                        LogUtil.e("Utility","Type 不存在："+type);
                        break;
                }
                item.setTitle(hardware.getString1());
                item.setPrice(hardware.getString2());
                item.setComments("");
                item.setImg(hardware.getImg());
                item.setSource(hardware.getString3());
                itemsList.add(item);
            }
            return itemsList;
        } catch (JSONException e)
        {
            e.printStackTrace();
            LogUtil.e("Utility", "GSON Wrong!!!!");
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
            LogUtil.e("Utility", itemJson);
            items item =new Gson().fromJson(itemJson, items.class);
            item.setPrice("￥" + item.getPrice());
            item.setType("items");
            item.setSource("京东");
            item.setComments("评论数：" + item.getComments());
            return item;
        } catch (JSONException e)
        {
            e.printStackTrace();
            LogUtil.e("Utility", "GSON Wrong!!!!");
        }
        return null;
    }

    //收藏界面 解析单个cpu对象
    public static items handleoneCPUResponse(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            JSONObject jsonObject = dataObject.getJSONObject("data");
            String itemJson = jsonObject.toString();
            LogUtil.e("Utility", itemJson);
            CPU cpu = new Gson().fromJson(itemJson, CPU.class);
            if(MainActivity.getCommond() == 2)
            {
                FreePlanActivity.free_item.setCpu(cpu);
            }
            items item = new items();
            item.set_id(cpu.get_id());
            item.setType("cpu");
            item.setCpu(cpu);
            item.setTitle(cpu.getName() + "\n" + cpu.getCodename());
            if(cpu.getClock() == null)
            {
                LogUtil.e("Utility","There is a null");
                item.setPrice("未知");
            }else
            {
                item.setPrice(Float.valueOf(cpu.getClock())/1000+ "GHz");
            }
            item.setComments("Cores:" + cpu.getCores());
            item.setImg(cpu.getImg());
            item.setSource(cpu.getSocket());
            return item;
        } catch (JSONException e)
        {
            e.printStackTrace();
            LogUtil.e("Utility", "GSON Wrong!!!!");
        }
        return null;
    }

    //收藏界面 解析单个gpu对象
    public static items handleoneGPUResponse(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            JSONObject jsonObject = dataObject.getJSONObject("data");
            String itemJson = jsonObject.toString();
            LogUtil.e("Utility", itemJson);
            GPU gpu = new Gson().fromJson(itemJson, GPU.class);
            if(MainActivity.getCommond() == 2)
            {
                FreePlanActivity.free_item.setGpu(gpu);
            }
            items item = new items();
            item.set_id(gpu.get_id());
            item.setType("gpu");
            item.setGpu(gpu);
            item.setTitle(gpu.getName()+"\n"+gpu.getChip());
            item.setPrice(gpu.getGPU_Clock()+"MHz");
            item.setComments("");
            item.setImg(gpu.getImg());
            item.setSource(gpu.getBus());
            return item;
        } catch (JSONException e)
        {
            e.printStackTrace();
            LogUtil.e("Utility", "GSON Wrong!!!!");
        }
        return null;
    }

    //收藏界面 解析单个Hardware对象
    public static items handleoneHardwareResponse(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            String type = dataObject.getString("code");
            JSONObject jsonObject = dataObject.getJSONObject("data");
            String itemJson = jsonObject.toString();
            LogUtil.e("Utility", itemJson);
            Hardware hardware = new Gson().fromJson(itemJson, Hardware.class);
            if(MainActivity.getCommond() == 2)
            {
                switch (type)
                {
                    case "case":
                        FreePlanActivity.free_item.setMycase(hardware);
                        break;
                    case "power":
                        FreePlanActivity.free_item.setPower(hardware);
                        break;
                    case "cooler_water":
                        FreePlanActivity.free_item.setCooler_water(hardware);
                        break;
                    case "cooler_wind":
                        FreePlanActivity.free_item.setCooler_wind(hardware);
                        break;
                    case "hdd":
                        FreePlanActivity.free_item.setHdd(hardware);
                        break;
                    case "ssd":
                        FreePlanActivity.free_item.setSsd(hardware);
                        break;
                    case "memory":
                        FreePlanActivity.free_item.setMemory(hardware);
                        break;
                    case "motherboard":
                        FreePlanActivity.free_item.setMotherboard(hardware);
                        break;
                }
            }
            items item = new items();
            item.set_id(hardware.get_id());
            item.setType(type);
            switch (type)
            {
                case "case":
                    item.setMycase(hardware);
                    break;
                case "power":
                    item.setPower(hardware);
                    break;
                case "cooler_water":
                    item.setCooler_water(hardware);
                    break;
                case "cooler_wind":
                    item.setCooler_wind(hardware);
                    break;
                case "hdd":
                    item.setHdd(hardware);
                    break;
                case "ssd":
                    item.setSsd(hardware);
                    break;
                case "memory":
                    item.setMemory(hardware);
                    break;
                case "motherboard":
                    item.setMotherboard(hardware);
                    break;
            }
            item.setTitle(hardware.getString1());
            item.setPrice(hardware.getString2());
            item.setComments("");
            item.setImg(hardware.getImg());
            item.setSource(hardware.getString3());
            return item;
        } catch (JSONException e)
        {
            e.printStackTrace();
            LogUtil.e("Utility", "GSON Wrong!!!!");
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
