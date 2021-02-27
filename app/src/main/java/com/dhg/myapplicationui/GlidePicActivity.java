package com.dhg.myapplicationui;

import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@Route(path = "/home/GlidePicActivity")
public class GlidePicActivity extends FragmentActivity {


    private String TAG = "UIdhg";
    TextView tv;
    ImageView img;
    ImageView img1;
    private  SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feedback_chat_pic_left);
        img =findViewById(R.id.pic_msg_req);
        img1=findViewById(R.id.img1);
        String url2="https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3641664764,1145827789&fm=26&gp=0.jpg";
        String url = "https://jiafeigou.oss-cn-hangzhou.aliyuncs.com/283600001784/1613634652_1.jpg?OSSAccessKeyId=xjBdwD1du8lf2wMI&Expires=1613641579&Signature=J7g%2FYbiWz6UIf9aBgHXSeebk4PI%3D";
String url3="http://jiafeigou.oss-cn-hangzhou.aliyuncs.com/283600001784/1613644303_1.jpg?Expires=3227299838&OSSAccessKeyId=STS.NSkHAVY4Qm4UzTpNgzazJSBd9&Signature=JcxoM/KTs8ne0ypomce6InPcKpo%3D&security-token=CAISvgN1q6Ft5B2yfSjIr5DeA/vitOtw2va%2BeHLBqmcvbfVmvIfP2zz2IHpPendgAu8ev/o/mGpR6PsYlqx4WptMQxRm1RO/fdIFnzm6aq/t5uZxh95t0e9rcwr%2BIhr/29CoPYedZdjBe/CrRknZnytou9XTfimjWFrXR//sjoV8POwKQi6ybzdNGLUzIRB5%2BuAXKVzbN/umLmRm6wi2NkdzvRdmgm5S8Lm2xtbmiiDTl1rn0OQY1SuYQNStZNI%2BO4xkAZXnnr50d6PP3S9f7AUNrvct0vQcoW%2Be4YHBRBszqhyNKLjT6cY9bl07NKo7EqBJrff7iLpzu%2BCUnJXzwhAKf4MhWi/EFoe725mGSqH7NNcwe7v6NHPQldSIMJay8WFQaHkAZgRRYIhjeD0iCx0lRDzbJai6vw/GaxbkCcrw2aotg51u1Aeqr5jYJVmGTbiY3iEFfZ45ZkwlcFx0lGXqaf0BaBceMQElVbSURIt%2BbR9ZtOTsugKVXTxkx3cQ/d%2BePKuK4flBOdvFM8gYgdZHVvNvqHA3Sln7cbWqh3oPeXZtKbQsi/O2Z8Hvs%2BDdkbvDPbSfVK1doD5daivBAfEkLxqAATV0kGe7HvSXcyIj3iNgUo82JFPMnOCRJ7VeLFE%2BXcK9AqzLjHwWsQN7Mi%2Bi1WqXwWJaQrAC4J3KiQfTk4j1k126wQpmHDOW6Kr%2BlYqvkWwfLd7XCZe8fBxVOJ3Vz8wc1CAyvwOYJcts8RAscD845Z2MXtkEcHoe43szmUXjiJl2";
        String filePath1 = "http://dpic.tiankong.com/e6/pj/QJ6277745786.jpg?x-oss-process=style/shows";
        String filePath2="https://pics7.baidu.com/feed/2fdda3cc7cd98d10e6a7ede92dae0b0a7aec9009.jpeg?token=302b0915c473c58c6b97cab61606cf18&s=E58A67B3068D2BEF729049A40300A032";
        Glide.with(this).asDrawable()
                .load(filePath1)
                .override( dp2px(this,270), dp2px(this,180))
                .into(img);
        Glide.with(this).asDrawable()
                .load(filePath2)
                .override( dp2px(this,270), dp2px(this,180))
                .into(img1);
//        Glide.with(this).asDrawable().load(filePath1).dontAnimate().centerCrop().diskCacheStrategy(DiskCacheStrategy.DATA).into(img1);

    }
    public  int dp2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


}