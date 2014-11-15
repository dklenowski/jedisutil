package com.orbious.jedisutil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisSet {

  private JedisPool pool;

  private String ip;
  private int port;
  private String setname;

  protected static final Logger logger = Logger.getLogger(JedisConfig.logrealm);

  public RedisSet(String ip, int port, String setname) { 
    this.ip = ip;
    this.port = port;
    this.setname = setname;
  }

  public synchronized void connect() throws RedisException {
    if ( pool != null ) return;

    logger.info("connecting to redis on " + ip + ": " + port);
    
    pool = Pool.get(ip, port);
    if ( !isConnected() ) 
      throw new RedisException("Failed to connect to redis at " + ip + ": " +port);
  }

  public synchronized boolean isConnected() {
    Jedis jedis = null;
    boolean connected = false;
    
    try {
      jedis = pool.getResource();
      connected = jedis.isConnected();
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
    
    return connected;
  }

//  public synchronized void disconnect()  {
//    try {
//      pool.destroy();
//    } catch ( JedisException je ) { 
//      logger.warn("failed to destroy jedis pool");
//    } finally {
//      pool = null;
//    }
//  }

  public synchronized boolean contains(String key) {
    logger.debug("searching for key " + key);
    
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.sismember(setname, key);
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
  }

  public synchronized void put(String key) {
    logger.debug("adding " + key + " to set " + setname);
    
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.sadd(setname, key);
    } finally {
      if ( jedis != null ) pool.returnResource(jedis);
    }
  }

  public synchronized void dump(File outputfile) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(outputfile));
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      Set<String> set = jedis.smembers(setname);
      Iterator<String> iter = set.iterator();
      while ( iter.hasNext() ) 
        writer.write(iter.next() + "\n");
    } finally {
      writer.close();
      if ( jedis != null ) pool.returnResource(jedis);
    }
  }
  
  
  public synchronized String connectStr() {
    return ip + ": " + port + " (" + setname + ")";
  }
}
