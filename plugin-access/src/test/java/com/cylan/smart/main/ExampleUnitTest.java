package com.cylan.smart.main;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {

        Calendar ca = Calendar.getInstance();
        System.out.println(ca.get(Calendar.YEAR) + "年" + (ca.get(Calendar.MONTH) + 1) + "月");
        for (int i = 0; i < 36; i++) {
            ca.add(Calendar.MONTH, -1);
            System.out.println(ca.get(Calendar.YEAR) + "年" + (ca.get(Calendar.MONTH) + 1) + "月");
        }

    }
}