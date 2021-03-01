package com.cylan.smart.base.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author yanzhendong
 */
@SuppressWarnings("ALL")
public class AppBackgroundChecker {
    private static final String TAG = "AppBackgroundChecker";
    private static AppBackgroundChecker instance;
    private CopyOnWriteArrayList<AppBackgroundListener> mBackgroundListeners = new CopyOnWriteArrayList<>();
    private AppLifeCycleMonitor mAppLifeCycleMonitor = new AppLifeCycleMonitor();
    private Handler mMainHandler;

    private AppBackgroundChecker() {
    }

    public static AppBackgroundChecker getInstance() {
        if (instance == null) {
            synchronized (AppBackgroundChecker.class) {
                if (instance == null) {
                    instance = new AppBackgroundChecker();
                }
            }
        }
        return instance;
    }

    public void init(Application application) {
        mMainHandler = new Handler(application.getMainLooper());
        application.registerActivityLifecycleCallbacks(mAppLifeCycleMonitor);
    }

    public boolean isBackground() {
        return mAppLifeCycleMonitor.isAppInBackground;
    }

    public void addAppBackgroundListener(AppBackgroundListener listener) {
        mBackgroundListeners.addIfAbsent(listener);
    }

    public void removeAppBackgroundListener(AppBackgroundListener listener) {
        mBackgroundListeners.remove(listener);
    }

    private void notifyAppBackgroundStateChanged(boolean isBackground) {
        Log.e(TAG, "notifyAppBackgroundStateChanged: isBackground: " + isBackground + ",listener size:" + mBackgroundListeners.size());
        for (AppBackgroundListener backgroundListener : mBackgroundListeners) {
            backgroundListener.onAppBackgroundStateChanged(isBackground);
        }
    }

    public interface AppBackgroundListener {
        /**
         * 用来监听 APP 是否处于后台了
         *
         * @param isBackground app 当前是否在后台运行
         */
        void onAppBackgroundStateChanged(boolean isBackground);
    }

    private class AppLifeCycleMonitor implements Application.ActivityLifecycleCallbacks {
        private static final int PENDING_STOP_DELAY = 800;
        private volatile int mCreatedActivityCount;
        private volatile int mStartedActivityCount;
        private volatile int mResumedActivityCount;
        private volatile boolean isAppInBackground = true;
        private Runnable mPendingBackgroundChecker = new Runnable() {
            @Override
            public void run() {
                mMainHandler.removeCallbacks(this);
                if (mResumedActivityCount == 0) {
                    //APP从前台切换到后台了
                    notifyAppBackgroundStateChanged(isAppInBackground = true);
                } else if (mResumedActivityCount > 0 && isAppInBackground) {
                    //APP从后台切换到前台了
                    notifyAppBackgroundStateChanged(isAppInBackground = false);
                }
            }
        };

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            isAppInBackground = mCreatedActivityCount == 0;
            mCreatedActivityCount++;
            Log.e(TAG, "onActivityCreated: " + activity + ",create count:" + mCreatedActivityCount + ",isBackground:" + isAppInBackground);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            isAppInBackground = mStartedActivityCount == 0;
            mStartedActivityCount++;
            Log.e(TAG, "onActivityStarted: " + activity + ",started count:" + mStartedActivityCount + ",isBackground:" + isAppInBackground);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            mResumedActivityCount++;
            executePendingBackgroundChecker();
            Log.e(TAG, "onActivityResumed: " + activity + ",resumed count:" + mResumedActivityCount + ",isBackground:" + isAppInBackground);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            mResumedActivityCount--;
            executePendingBackgroundChecker();
            Log.e(TAG, "onActivityPaused: " + activity + ",resumed count:" + mResumedActivityCount + ",isBackground:" + isAppInBackground);
        }

        private void executePendingBackgroundChecker() {
            mMainHandler.removeCallbacks(mPendingBackgroundChecker);
            mMainHandler.postDelayed(mPendingBackgroundChecker, PENDING_STOP_DELAY);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            mStartedActivityCount--;
            Log.e(TAG, "onActivityStopped: " + activity + ",started count:" + mStartedActivityCount + ",isBackground:" + isAppInBackground);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            mCreatedActivityCount--;
            Log.e(TAG, "onActivityDestroyed: " + activity + ",created count:" + mCreatedActivityCount + ",isBackground:" + isAppInBackground);
        }
    }
}
