package com.dhg.myapplicationui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends FragmentActivity {


    private String TAG = "UIdhg";
    TextView tv;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawableEditText pwd = findViewById(R.id.password);
        setPasswordSwitch(pwd);

        String time = getPreDay();
        tv = findViewById(R.id.tv);
        tv.setText(time);
        Log.i("sss", "a");
        tv.setOnClickListener(v -> {
//            Decibelformat();
            startActivity(new Intent(this,MainActivity2.class));
//            ARouter.getInstance().build("/home/activity2").navigation();
        });


    }

    private void Decibelformat() {
        int a = 150977;
        int b = 151000;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String num = df.format((float) a / b * 100);//返回的是String类型
        double value = Math.floor((double) a / b * 100);

        double c = 15844745216d;
        double d = 15915991040d;
        double scale = 1000000000;

        BigDecimal b1 = new BigDecimal(Double.toString(c));
        BigDecimal b2 = new BigDecimal(Double.toString(scale));

        double result = b1.divide(b2, 1, BigDecimal.ROUND_HALF_DOWN).doubleValue();

        tv.setText(result + "");
    }

    private void JSONFormat() {
        String result = "{\"cid\":\"349045895\",\"mac\":\"djafoid\",\"os\":164}";
        String result2 = "{\"cid\":\"12323535\"}";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            String cid = jsonObject.getString("cid");
            String mac = jsonObject.getString("mac");
            int os = jsonObject.getInt("os");
            Log.i(TAG, "scan cid is:" + cid + " mac:" + mac + "os :" + os);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getPreDay() {

        long currentTime = System.currentTimeMillis();
//        long preTime = currentTime - 24 * 60 * 60 * 1000;
        SimpleDateFormat timeFormat = new SimpleDateFormat("YYYYmmdd HHmm");
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(" yyyyMMdd");
        return mSimpleDateFormat.format(currentTime);


    }


    boolean passwordVisible = false;

    void setPasswordSwitch(final DrawableEditText editText) {
        editText.setDrawableOnClickListener(new DrawableEditText.RightDrawableOnClickListener() {
            @Override
            public void onClickDrawable(boolean status) {
                if (status) {
                    //明文显示
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editText.setRightDrawable(R.mipmap.ic_launcher);
                } else {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editText.setRightDrawable(R.mipmap.not_see_password_icon);
                }
            }
            //            @Override
//            public void onDrawableRightClick() {
//                Drawable drawableRight;
//                if (passwordVisible) {
//                    drawableRight = getResources().getDrawable(R.mipmap.not_see_password_icon);
////              editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
//                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    passwordVisible = false;
//                } else {
//                    drawableRight = getResources().getDrawable(R.drawable.ic_launcher_background);
////              editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    passwordVisible = true;
//                }
//                drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
//                editText.setCompoundDrawables(null, null, drawableRight, null);
//            }
        });
    }

    public static int compareVersion(String one, String two) {
        if (!one.contains(".") || !two.contains(".")) {
            return -1;
        } else {
            String oneSplit = one.replace(".", "");
            String twoSplit = two.replace(".", "");
            if (oneSplit.equals(twoSplit)) {
                return 0;
            } else {
                if (oneSplit.length() > twoSplit.length()) {
                    int current = -3;
                    for (int i = 0; i < twoSplit.length(); i++) {
                        if (Integer.valueOf(oneSplit.charAt(i)) > Integer.valueOf(twoSplit.charAt(i))) {
                            return 1;
                        } else if (Integer.valueOf(oneSplit.charAt(i)) < Integer.valueOf(twoSplit.charAt(i))) {
                            return 2;
                        } else {
                            continue;
                        }
                    }
                    if (current == -3) {
                        return 1;
                    }
                } else if (oneSplit.length() < twoSplit.length()) {
                    int current1 = -3;
                    for (int i = 0; i < oneSplit.length(); i++) {
                        if (Integer.valueOf(oneSplit.charAt(i)) > Integer.valueOf(twoSplit.charAt(i))) {
                            return 1;
                        } else if (Integer.valueOf(oneSplit.charAt(i)) < Integer.valueOf(twoSplit.charAt(i))) {
                            return 2;
                        } else {
                            continue;
                        }
                    }
                    if (current1 == -3) {
                        return 2;
                    }
                } else if (oneSplit.length() == twoSplit.length()) {
                    if (Integer.valueOf(oneSplit) > Integer.valueOf(twoSplit)) {
                        return 1;
                    } else if (Integer.valueOf(oneSplit) < Integer.valueOf(twoSplit)) {
                        return 2;
                    }
                }
            }

        }
        return -2;
    }

}