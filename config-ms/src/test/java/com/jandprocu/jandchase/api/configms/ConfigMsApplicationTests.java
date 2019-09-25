package com.jandprocu.jandchase.api.configms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfigMsApplicationTests {


    @Test
    public void test() {
        //assert
        assertThat("Dell Inspiron".equals("Dell Inspiron"));
    }

}
