package com.easybuild.android.util;

import android.util.Log;

import java.io.File;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil
{
    public static final String LocalAddress = "http://47.96.102.28:8080/liteServer";

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback)
    {
        Log.e("HttpUtil",address);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    //登陆界面
    public static void loginRequest(String address,String userID, String password, okhttp3.Callback callback)
    {
        LogUtil.e("HttpUtil",address);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userID",userID)
                .add("password",password)
                .build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    //注册界面
    public static void registerRequest(String address, String userID, String password, String nickname,okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        String jsonStr = "{\"userID\":\""+userID+"\",\"password\":\""+password+"\",\"nickname\":\""+nickname+"\"}";//json数据.
        RequestBody requestBody = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    //搜索界面
    public static void searchItemRequest(String address, String type, String include, String order, String key, String low, String high, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        String jsonStr = "{\"type\":\""+type+"\",\"include\":\""+include+"\",\"order\":\""+order+"\",\"key\":\""+key+"\",\"low\":\""+low+"\",\"high\":\""+high+"\"}";//json数据.
        RequestBody requestBody = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    //收藏界面
    public static void serchIdRequest(String address, String type, String favorite_id, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        String jsonStr = "{\"type\":\""+type+"\",\"favorite_id\":\""+favorite_id+"\"}";//json数据.
        RequestBody requestBody = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    public static void modifyImgRequest(String address, String userID, File photo_file, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType fileType = MediaType.parse("image/png");//数据类型为File格式，
        RequestBody fileBody = RequestBody.create(fileType , photo_file );
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", userID, fileBody)
                .build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void modifyInfoRequest(String address, String userID, String phone_number, String nickname,okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        String jsonStr = "{\"userID\":\""+userID+"\",\"phone_number\":\""+phone_number+"\",\"nickname\":\""+nickname+"\"}";//json数据.
        RequestBody requestBody = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void searchKeyRequest(String address, String key, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        String jsonStr = "{\"key\":\""+key+"\"}";//json数据.
        RequestBody requestBody = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void modifypasswordRequest(String address, String userID, String password, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userID",userID)
                .add("password",password)
                .build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
