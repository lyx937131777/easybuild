package com.easybuild.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easybuild.android.util.HttpUtil;
import com.easybuild.android.util.LogUtil;
import com.easybuild.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 登录界面Demo
 *
 * @author Leblanc
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener
{
    private EditText username, password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private Button bt_login;
    private Button bt_go_register;
    private boolean isOpen = false;
    String username_text, password_text;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        username_text = pref.getString("userID", null);
        password_text = pref.getString("password", null);
        if (username_text != null & password_text != null)
        {
            Intent intent_login = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent_login);
            finish();
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

    private void initView()
    {
        username = (EditText) findViewById(R.id.username);
        // 监听文本框内容变化
        username.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                username_text = username.getText().toString().trim();
                if ("".equals(username_text))
                {
                    // 用户名为空,设置按钮不可见
                    bt_username_clear.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    bt_username_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        password = (EditText) findViewById(R.id.password);
        // 监听文本框内容变化
        password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                password_text = password.getText().toString().trim();
                if ("".equals(password_text))
                {
                    // 用户名为空,设置按钮不可见
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        //初始化button
        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(this);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        bt_go_register = (Button) findViewById(R.id.bt_go_register);
        bt_go_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 清除登录名
            case R.id.bt_username_clear:
                username.setText("");
                break;

            // 清除密码
            case R.id.bt_pwd_clear:
                password.setText("");
                break;

            // 密码可见与不可见的切换
            case R.id.bt_pwd_eye:
                if (isOpen)
                {
                    isOpen = false;
                } else
                {
                    isOpen = true;
                }
                // 默认isOpen是false,密码不可见
                changePwdOpenOrClose(isOpen);
                break;

            // TODO 登录按钮
            case R.id.bt_login:
                username_text = username.getText().toString();
                password_text = password.getText().toString();

                if (!username_text.matches(Patterns.EMAIL_ADDRESS.toString()))
                {
                    Toast.makeText(LoginActivity.this, "邮箱格式不正确", Toast.LENGTH_LONG).show();
                } else if (password_text.length() < 6)
                {
                    Toast.makeText(LoginActivity.this, "密码位数不正确", Toast.LENGTH_LONG).show();
                } else
                {
                    String address = HttpUtil.LocalAddress + "/user/login";
                    HttpUtil.loginRequest(address, username_text, password_text, new Callback()
                    {
                        @Override
                        public void onFailure(Call call, IOException e)
                        {
                            e.printStackTrace();
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(LoginActivity.this, "服务器连接错误", Toast
                                            .LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException
                        {
                            final String responsDate = response.body().string();
                            LogUtil.e("Login", responsDate);
                            if (Utility.storeLoginUser(responsDate))
                            {
                                editor = pref.edit();
                                editor.putString("userID", username_text);
                                editor.putString("password", password_text);
                                editor.putString("latest", String.valueOf(System.currentTimeMillis()));
                                editor.apply();
                                Intent intent_login = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent_login);
                                finish();
                            } else
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast
                                                .LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });

                }
                break;

            // 注册按钮
            case R.id.bt_go_register:
                Intent reg = new Intent();
                reg.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(reg);
                break;
            default:
                break;
        }
    }

    /**
     * 密码可见与不可见的切换
     *
     * @param flag
     */
    private void changePwdOpenOrClose(boolean flag)
    {
        // 第一次过来是false，密码不可见
        if (flag)
        {
            // 密码可见
            bt_pwd_eye.setBackgroundResource(R.drawable.pwd_open);
            // 设置EditText的密码可见
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else
        {
            // 密码不可见
            bt_pwd_eye.setBackgroundResource(R.drawable.pwd_closed);
            // 设置EditText的密码隐藏
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}