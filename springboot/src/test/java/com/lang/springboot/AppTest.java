package com.lang.springboot;

import junit.framework.TestCase;

import com.lang.dto.RegistDto;
import com.lang.utils.MyUrlUtil;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    public void testRegist() {
        RegistDto info = new RegistDto();
        MyUrlUtil.postJson("http://localhost:8080/ctrl/register", info);
    }

}
