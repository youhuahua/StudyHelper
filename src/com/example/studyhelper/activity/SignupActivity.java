package com.example.studyhelper.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.bmob.v3.listener.SaveListener;
import com.example.studyhelper.R;
import com.example.studyhelper.base.BaseActivity;
import com.example.studyhelper.user.User;

public class SignupActivity extends BaseActivity {
    private EditText etUserName, etPassWord, etReputPas;
    private Button btSubmit;
    private RadioGroup rg;
    private RadioButton rbStudent, rbTeacher, rbParent;
    private int type = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    protected void initView() {
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassWord = (EditText) findViewById(R.id.etPassWord);
        etReputPas = (EditText) findViewById(R.id.etReputPas);
        btSubmit = (Button) findViewById(R.id.btSubmit);
        rg = (RadioGroup) findViewById(R.id.rg);
        rbStudent = (RadioButton) findViewById(R.id.rbStudent);
        rbTeacher = (RadioButton) findViewById(R.id.rbTeacher);
        rbParent = (RadioButton) findViewById(R.id.rbParent);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbStudent:
                        type = 0;
                        break;
                    case R.id.rbTeacher:
                        type = 1;
                        break;
                    case R.id.rbParent:
                        type = 2;
                        break;
                }
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUserName.getText().toString().trim();
                String password = etUserName.getText().toString().trim();
                String rePas = etReputPas.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    showToast("用户名不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("密码不能为空!");
                    return;
                }
                if (!rePas.equals(password)) {
                    showToast("两次输入的密码不正确，请重新输入!");
                    return;
                }
                signUp(username, password);
            }
        });
    }

    private void signUp(final String username, final String password) {
        final User myUser = new User();
        myUser.setUsername(username);
        myUser.setPassword(password);
        myUser.setType(type);
        myUser.signUp(this, new SaveListener() {

            @Override
            public void onSuccess() {
                System.out.println("Token值:" + myUser.getSessionToken());
                editor.putString("password", username);
                editor.putString("password", password).commit();
                showToast("注册成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                showToast("注册失败" + msg);
            }
        });
    }

    @Override
    protected void initData() {

    }

}
