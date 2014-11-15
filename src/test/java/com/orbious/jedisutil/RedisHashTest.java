package com.orbious.jedisutil;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class RedisHashTest {

  private static final String h1 = "redishashtest";
  private static final String h2 = "redishashtest";
  
  @BeforeClass
  public void before() throws Exception {
    RedisHash hash;
    hash = connect(h1);
    hash.delete();

    hash = connect(h2);
    hash.delete();
  }
  
  public RedisHash connect(String hashname) throws Exception {
    RedisHash hash = new RedisHash(TestConfig.redis_ip, 
        TestConfig.redis_port, hashname);
    hash.connect();
    return hash;
  }
  
  @Test(threadPoolSize = 5, invocationCount = 10,  timeOut = 5000)
  public void getput() throws Exception {
    RedisHash hash = connect(h1);
    String value = "String1";
    
    hash.put("one", value);
    hash.put("two", value);
    
    assertThat(hash.get("two"), is(equalTo(value)));
    assertThat(hash.get("one"), is(equalTo(value)));
    
  }

  @Test(threadPoolSize = 5, invocationCount = 10,  timeOut = 5000)
  public void disconnect() throws Exception {
    RedisHash hash = connect(h2);
    String value = "String2";
    
    hash.put("three", value);
    hash.put("four", value);
    
    assertThat(hash.get("three"), is(equalTo(value)));
    assertThat(hash.get("four"), is(equalTo(value)));
  }
}
