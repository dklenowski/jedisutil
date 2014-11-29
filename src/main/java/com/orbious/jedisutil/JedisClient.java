package com.orbious.jedisutil;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisClient {

  protected String ip;
  protected int port;
  
  protected JedisPool pool;
  
  protected static final Logger logger = Logger.getLogger(JedisConfig.logrealm);

  public JedisClient(String ip, int port) { 
    this.ip = ip;
    this.port = port;
  }
  
  public synchronized void connect() throws RedisException { 
    if ( pool != null ) return;
    
    logger.info("connecting to redis on " + ip + ":" + port);
    
    pool = PoolUtil.get(ip, port);
    if ( !PoolUtil.connected(pool) ) 
      throw new RedisException("Failed to connect to redis at " + ip + ": " + port);
  }
  
  public synchronized boolean isConnected() {
    return PoolUtil.connected(pool);
  }
  
  public synchronized String hget(String hashname, String key) {
    Jedis jedis = null;
    String value = null;
    
    try {
      jedis = pool.getResource();
      value = jedis.hget(hashname, key);
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
    
    return value;
  }
  
  public synchronized void hset(String hashname, String key, String value) {
    Jedis jedis = null;
    
    try {
      jedis = pool.getResource();
      jedis.hset(hashname, key, value);
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
  }
  
  public synchronized Set<String> hkeys(String hashname) {
    Jedis jedis = null;
    Set<String> keys;
    
    try {
      jedis = pool.getResource();
      keys = jedis.hkeys(hashname);
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    } 
    return keys;
  }
  
  // this is only used in testing, so we dont need to validate
  public synchronized void del(String hashname) {
    Jedis jedis = null;
    
    try {
      jedis = pool.getResource();
      
      jedis.del(hashname);
      
      Set<String> keys = jedis.keys(".*");
      Iterator<String> iter = keys.iterator();
      String key;
      while ( iter.hasNext() ) {
        key = iter.next();
        logger.info("deleting key " + key);
        jedis.del(key);
      }
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
  }
  
  public synchronized void delkeys() {
    Jedis jedis = null;
    
    try {
      jedis = pool.getResource();
      
      Set<String> keys = jedis.keys("*");
      
      Iterator<String> iter = keys.iterator();
      String key;
      while ( iter.hasNext() ) {
        key = iter.next();
        logger.info("deleting key " + key);
        jedis.del(key);
      }
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
  }
  
  public synchronized int incr(String keyname) {
    Jedis jedis = null;
    int result = -1;
    long value = -1;
    
    try {
      jedis = pool.getResource();
      value = jedis.incr(keyname);
      result = (int)value;
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
    
    if ( result != value )
      throw new IllegalArgumentException("Out of range: " + value);
    
    return result;
  }
  
  public synchronized boolean exists(String keyname) {
    Jedis jedis = null;
    
    try {
      jedis = pool.getResource();
      return jedis.exists(keyname);
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
  }
  
  public synchronized String get(String key) {
    Jedis jedis = null;
    String value = null;
    
    try {
      jedis = pool.getResource();
      value = jedis.get(key);
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }

    return value;
  }
    
  public synchronized void set(String keyname, int value) {
    Jedis jedis = null;
    
    try {
      jedis = pool.getResource();
      jedis.set(keyname, String.valueOf(value));
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
  }
  
}

