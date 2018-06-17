package com.easybuild.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class RegisterActivity extends AppCompatActivity implements OnClickListener
{
    private EditText username, password, confirm_password, nickname;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_confirm_pwd_clear;
    private Button bt_nickname_clear;
    private Button bt_register;
    String username_text, password_text, confirm_password_text, nickname_text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
                username_text = username.getText().toString();
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
                password_text = password.getText().toString();
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
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        // 监听文本框内容变化
        confirm_password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                confirm_password_text = confirm_password.getText().toString();
                if ("".equals(confirm_password_text))
                {
                    // 用户名为空,设置按钮不可见
                    bt_confirm_pwd_clear.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    bt_confirm_pwd_clear.setVisibility(View.VISIBLE);
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
        nickname = (EditText) findViewById(R.id.nickname);
        // 监听文本框内容变化
        nickname.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                nickname_text = nickname.getText().toString();
                if ("".equals(nickname_text))
                {
                    // 用户名为空,设置按钮不可见
                    bt_nickname_clear.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    bt_nickname_clear.setVisibility(View.VISIBLE);
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
        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);
        bt_confirm_pwd_clear = (Button) findViewById(R.id.bt_confirm_pwd_clear);
        bt_confirm_pwd_clear.setOnClickListener(this);
        bt_nickname_clear = (Button) findViewById(R.id.bt_nickname_clear);
        bt_nickname_clear.setOnClickListener(this);
        bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 清除
            case R.id.bt_username_clear:
                username.setText("");
                break;
            case R.id.bt_pwd_clear:
                password.setText("");
                break;
            case R.id.bt_confirm_pwd_clear:
                confirm_password.setText("");
                break;
            case R.id.bt_nickname_clear:
                nickname.setText("");
                break;

            // TODO 注册按钮
            case R.id.bt_register:
                username_text = username.getText().toString();
                password_text = password.getText().toString();
                confirm_password_text = confirm_password.getText().toString();
                nickname_text = nickname.getText().toString();
                //确认密码不正确、邮箱格式不正确、昵称已被占用
                if (!username_text.matches(Patterns.EMAIL_ADDRESS.toString()))
                {
                    Toast.makeText(RegisterActivity.this, "请输入正确的邮箱", Toast.LENGTH_LONG).show();
                } else if (password_text.length() < 6)
                {
                    Toast.makeText(RegisterActivity.this, "请输入至少6位的密码", Toast.LENGTH_LONG).show();
                } else if (!password.getText().toString().equals(confirm_password.getText()
                        .toString()))
                {
                    Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_LONG).show();
                } else if (nickname_text.length() < 1)
                {
                    Toast.makeText(RegisterActivity.this, "昵称不得为空", Toast.LENGTH_LONG).show();
                } else
                {
                    Log.e("Register", nickname_text);
                    String address = HttpUtil.LocalAddress + "/user/register";
                    HttpUtil.registerRequest(address, username_text, password_text,
                            nickname_text, new Callback()
                    {
                        @Override
                        public void onFailure(Call call, IOException e)
                        {
                            e.printStackTrace();
                            LogUtil.e("Register", "Faled!!!!!!!!!");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws
                                IOException
                        {
                            final String responseData = response.body().string();
                            LogUtil.e("Register", "源码 : " + responseData);
                            if (Utility.checkCode(responseData).equals("true"))
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        new AlertDialog.Builder(RegisterActivity.this)
                                                .setTitle("提示")
                                                .setMessage("注册成功！")
                                                .setPositiveButton("确定", new
                                                        DialogInterface.OnClickListener()
                                                        {
                                                            @Override
                                                            public void onClick(DialogInterface
                                                                                        dialog,
                                                                                int which)
                                                            {
                                                                finish();
                                                            }
                                                        })
                                                .show();
                                    }
                                });

                            } else if (responseData.equals("same"))
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        new AlertDialog.Builder(RegisterActivity.this)
                                                .setTitle("提示")
                                                .setMessage("该账户已被注册！")
                                                .setPositiveButton("确定", null)
                                                .show();
                                    }
                                });
                            } else
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        new AlertDialog.Builder(RegisterActivity.this)
                                                .setTitle("提示")
                                                .setMessage("由于未知原因注册失败，请重试！")
                                                .setPositiveButton("确定", null)
                                                .show();
                                    }
                                });
                            }
                        }
                    });
                }
                break;

            default:
                break;
        }


    }
}

