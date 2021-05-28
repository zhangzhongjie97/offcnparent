package com.offcn;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApplicationTest.class})
public class ScwUserApplicationTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate strRedisTemplate;

    @Test
    public void convertStr() {
//        redisTemplate.opsForValue().set("name", "zhangsan");
//        String name = (String) redisTemplate.opsForValue().get("name");
//        System.out.println("name:" + name);
//

        strRedisTemplate.opsForValue().set("name","张忠杰");
        String name = strRedisTemplate.opsForValue().get("name");
        System.out.println(name);
    }


}
