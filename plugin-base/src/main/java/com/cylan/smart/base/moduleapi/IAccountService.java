package com.cylan.smart.base.moduleapi;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.template.IProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 用户账号相关信息服务类
 *
 */
@SuppressWarnings("unused")
public interface IAccountService extends IProvider {
    /**
     * 获取已登录的用户名
     *
     * @return 返回已登录的用户名, 未登录则返回 null
     */
    @Nullable
    String getUsername();

    /**
     * 获取是否已经登录成功
     *
     * @return 是否登录成功
     */
    boolean hasLogin();

    /**
     * 保存用户名
     *
     * @param user 用户名
     */
    void saveUsername(@NotNull String user);

    /**
     * 注销当前账号
     */
    void logout();

    /**
     * 注册账号状态监听器
     *
     * @param observer
     */
    void addObserver(AccountStateObserver observer);

    /**
     * 移除账号状态监听器
     *
     * @param observer
     */
    void removeObserver(AccountStateObserver observer);

    /**
     * 通过Activity的方式拉起登录页面
     */
    void activityLogin();

    /**
     * 通过Fragment的方式拉起登录页面
     */
    void fragmentLogin(AppCompatActivity activity, int containerId);

    /**
     * 通过Dialog的方式拉起登录页面
     */
    void dialogLogin(AppCompatActivity activity);

    String getToken();

    interface AccountStateObserver {

    }

}
