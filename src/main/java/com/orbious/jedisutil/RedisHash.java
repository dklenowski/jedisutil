package com.orbious.jedisutil;

import java.util.Iterator;
import java.util.Set;
import redis.clients.jedis.Jedis;

public class RedisHash extends JedisClient {

  protected String hashname;
  
  public RedisHash(String ip, int port, String hashname) { 
    super(ip, port);
    this.hashname = hashname;
  }
  
  public synchronized void put(String key, String value) {
    hset(hashname, key, value);
  }
  
  public synchronized String get(String key) {
    return hget(hashname, key);
  }
  
  public synchronized Set<String> keys() { 
    return hkeys(hashname);
  }
  
  // for testing
  public synchronized void delete() {
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
  
  public String connectStr() {
    return ip + ": " + port + " [" + hashname + "]";
  }
}
