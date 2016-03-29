package com.example.studyhelper.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import cn.bmob.v3.listener.SaveListener;
import com.example.studyhelper.R;
import com.example.studyhelper.application.StudyHelperApplication;
import com.example.studyhelper.base.BaseActivity;
import com.example.studyhelper.user.User;
import org.w3c.dom.Text;

/**
 * Title:
 * Description:
 * Company:
 *
 * @author qianchao
 * @date 2016-3-2222:20
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText etUserName, etPassWord;
    private Button btLogin;
    private RadioGroup rg;
    private RadioButton rbStudent, rbTeacher, rbParent;
    private int type = 0;
    private TextView tvForgetPs,tvSignup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassWord = (EditText) findViewById(R.id.etPassWord);
        tvForgetPs= (TextView) findViewById(R.id.tvForgetPs);
        tvSignup= (TextView) findViewById(R.id.tvSignup);
        btLogin = (Button) findViewById(R.id.btLogin);
        rg = (RadioGroup) findViewById(R.id.rg);
        rbStudent = (RadioButton) findViewById(R.id.rbStudent);
        rbTeacher = (RadioButton) findViewById(R.id.rbTeacher);
        rbParent = (RadioButton) findViewById(R.id.rbParent);
        btLogin.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
        tvForgetPs.setOnClickListener(this);
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
    }

    private void login(final String username, final String password) {
        final User myUser = new User();
        myUser.setUsername(username);
        myUser.setPassword(password);
        myUser.setType(type);
        myUser.login(this, new SaveListener() {

            @Override
            public void onSuccess() {
                editor.putString("password", username);
                editor.putString("password", password).commit();
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                showToast("登录成功");
                application.setMyUser(myUser);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(int code, String msg) {
                showToast("登录失败" + msg);
            }
        });
    }

    @Override
    protected void initData() {
        String username = spUtil.getString("username","");
        String password = spUtil.getString("password", "");
        if (!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
            progressDialog.show();
            login(username, password);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btLogin:
                String username = etUserName.getText().toString().trim();
                String password = etUserName.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    showToast("用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("密码不能为空");
                    return;
                }
                login(username, password);
                break;
            case R.id.tvSignup:
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
                break;
        }
    }
}
