package com.cylan.smart.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import com.cylan.smart.base.utils.PathUtils;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * <p>
 * modify lxh on 15-08-03
 *
 * @author user
 */
public class CrashHandler implements UncaughtExceptionHandler {

    /**
     * CrashHandler 实例
     */
    private static CrashHandler INSTANCE = new CrashHandler();

    /**
     * 程序的 Context 对象
     */
    private Context mContext;

    /**
     * 系统默认的 UncaughtException 处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> infos = new HashMap<String, String>();

    /**
     * 用于格式化日期,作为日志文件名的一部分
     */
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE);


    /**
     * 广播action,用来通知程序，有异常将要重启。如果需要保存文件的，请速度保存。
     */
    private final String action = "com.cylan.camera_crash";

    private static String path = PathUtils.INSTANCE.getAPP_CRASH_REPORT_DIR();


    /**
     * 保证只有一个 CrashHandler 实例
     */
    private CrashHandler() {
    }

    /**
     * 获取 CrashHandler 实例 ,单例模式
     */
    public static CrashHandler getInstance(String dir) {
        if (null != dir && !"".equals(dir)) {
            setPath(dir);
        }
        return INSTANCE;
    }

    private static void setPath(String dir) {
        path = dir;
    }


    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            if (!handleException(ex) && mDefaultHandler != null) {
                // 如果用户没有处理则让系统默认的异常处理器来处理
                mDefaultHandler.uncaughtException(thread, ex);
                ex.printStackTrace();
            } else {
                SystemClock.sleep(3000);
                // 退出程序
                ex.printStackTrace();
                Log.e("DEBUG", "uncaughtException: app will kill!!!");
                android.os.Process.killProcess(android.os.Process.myPid());
                Log.e("DEBUG", "uncaughtException: APP WILL EXIT WITH EXCEPTION");
                System.exit(1);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private boolean handleException(Throwable ex) throws UnsupportedEncodingException {
        if (ex == null) {
            return false;
        }
        //发广播
        Intent reboot = new Intent(action);
        mContext.sendBroadcast(reboot);
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        //上传日志   ////光荣退伍
//        if (URLEncoder.encode(android.os.Build.MODEL, "UTF-8").equals("DOG-3G72")
//                || URLEncoder.encode(android.os.Build.MODEL, "UTF-8").equals("DOG-4G")) {
//            CreateRedmineIssue("DOG-3G-Crash", saveCrashInfo2File(ex));
//        } else
//            CreateRedmineIssue();

        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {

            }
        }
    }

    /**
     * 保存错误信息到文件中
     * *
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String time = formatter.format(System.currentTimeMillis());
            String fileName = time + "_crash.txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
                File[] file = dir.listFiles();
                //保存20个文件
                if (file.length > 10) {
                    Arrays.sort(file, new Comparator<File>() {
                        @Override
                        public int compare(File file, File t1) {
                            if (file.lastModified() < t1.lastModified()) {
                                return -1;
                            }
                            if (file.lastModified() > t1.lastModified()) {
                                return 1;
                            }
                            return 0;
                        }
                    });
                    for (int i = 0; i < file.length / 4; i++) {
                        file[i].delete();
                    }
                }
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}