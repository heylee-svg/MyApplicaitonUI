package com.cylan.smart;

import com.cylan.smart.base.utils.CommonUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {
        boolean resutl = CommonUtils.INSTANCE.isPhoneNumber("17621972307");
        System.out.println(resutl);
    }
}