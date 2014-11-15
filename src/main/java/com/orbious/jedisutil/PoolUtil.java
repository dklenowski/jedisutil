package com.orbious.jedisutil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PoolUtil {
  
  private PoolUtil() { }
  
  public static synchronized boolean connected(JedisPool pool) {
    if ( pool == null ) return false;
    
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.isConnected();
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
  }
}
