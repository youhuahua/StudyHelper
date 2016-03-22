package com.example.studyhelper.user;

import cn.bmob.v3.BmobUser;

/**
 * Title:
 * Description:
 * Company:
 *
 * @author qianchao
 * @date 2016-3-2221:38
 */
public class User extends BmobUser {
    /**0表示学生，1表示老师，2表示家长*/
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
