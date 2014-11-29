package com.orbious.jedisutil;

import java.util.HashMap;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PoolUtil {

  public static HashMap<String, JedisPool> pools = new HashMap<>();
  
  protected static final Logger logger = Logger.getLogger(JedisConfig.logrealm);

  private PoolUtil() { }
  
  public static synchronized JedisPool get(String ip, int port, int timeout) {
    String id = ip + ":" + port;
    
    JedisPool pool =  pools.get(id);
    if ( pool != null ) {
      logger.debug("returing existing pool for " + id);
      return pool;
    }

    logger.debug("creating new pool for " + id);
    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    config.setTestOnBorrow(true);
    config.setTestOnReturn(true);
    config.setMaxTotal(10000);
    config.setMaxIdle(1000);
    config.setMinIdle(500);
    pool = new JedisPool(config, ip, port, timeout);
    
    pools.put(id, pool);
    return pool;
  }
  
  public static synchronized JedisPool get(String ip, int port) {
    return get(ip, port, JedisConfig.redistimeout);
  }
  
  public static synchronized boolean connected(JedisPool pool) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.isConnected();
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
  }
}
