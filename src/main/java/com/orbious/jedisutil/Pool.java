package com.orbious.jedisutil;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;

public class Pool {

  public static JedisPool pool = null;
  
  private Pool() { }
  
  public static synchronized JedisPool get(String ip, int port, int timeout) {
    if ( pool != null ) return pool;
    
    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    config.setTestOnBorrow(true);
    config.setTestOnReturn(true);
    config.setMaxTotal(10000);
    config.setMaxIdle(1000);
    config.setMinIdle(500);
    pool = new JedisPool(config, ip, port, timeout);
    
    return pool;
  }
  
  public static synchronized JedisPool get(String ip, int port) {
    return get(ip, port, JedisConfig.redistimeout);
  }
}
