package com.orbious.jedisutil;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RedisSetTest {

  private String testsetname = "redisset";

  @BeforeClass
  public void before() { 
    Utils.setuplogger();
  }

  @Test(threadPoolSize = 5, invocationCount = 10,  timeOut = 5000)
  public void init() throws Exception { 
    RedisSet rs = new RedisSet(TestConfig.redis_ip, TestConfig.redis_port, testsetname);
    rs.connect();
    Assert.assertTrue(rs.isConnected());
  }

  @Test(threadPoolSize = 5, invocationCount = 10,  timeOut = 5000)
  public void put() throws Exception {
    RedisSet rs = new RedisSet(TestConfig.redis_ip, TestConfig.redis_port, testsetname);
    rs.connect();

    rs.put("1");
    Assert.assertTrue(rs.contains("1"));

    rs.put("2");
    rs.put("3");
    Assert.assertTrue(rs.contains("2"));
    Assert.assertTrue(rs.contains("3"));

    Assert.assertFalse(rs.contains("5"));
  }
  
  @Test
  public void delete() throws Exception {
    RedisSet rs = new RedisSet(TestConfig.redis_ip, TestConfig.redis_port, testsetname);
    rs.connect();

    rs.put("1");
    Assert.assertTrue(rs.contains("1"));

    rs.put("2");
    rs.put("3");
    Assert.assertTrue(rs.contains("2"));
    Assert.assertTrue(rs.contains("3"));

    rs.delete("2");
    Assert.assertFalse(rs.contains("2"));
    Assert.assertTrue(rs.contains("3"));
  }
}

