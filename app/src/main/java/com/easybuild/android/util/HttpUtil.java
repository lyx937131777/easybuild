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

    public static void registerRequest(String address, String userID, String password, String nickname,okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        String jsonStr = "{\"userID\":\""+userID+"\",\"password\":\""+password+"\",\"nickname\":\""+nickname+"\"}";//json数据.
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

    public static void friendshipRequest(String address,String userID1, String userID2, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userID1",userID1)
                .add("userID2",userID2)
                .build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void myfriendsRequest(String address,String userID, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userID",userID)
                .build();
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
