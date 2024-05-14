import redis.clients.jedis.params.{RestoreParams, ScanParams}
import redis.clients.jedis.resps.ScanResult
import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

class RedisService(host: String, port: Int = 6379, secret: String = "", db: Int = 0) {

  private var redisConnectionCount = 0
  private val poolConfig: JedisPoolConfig = {
    val poolConfig: JedisPoolConfig = new JedisPoolConfig()
    poolConfig.setMaxTotal(5000)
    poolConfig.setMaxIdle(1000)
    poolConfig.setMinIdle(100)
    poolConfig
  }
  private val jedisPool = new JedisPool(poolConfig, host, port, "default", secret)

  private def redisClient: Jedis = {
    val jedis: Jedis = jedisPool.getResource
    setClientName(jedis)
    if (db > 0) jedis.select(db)
    jedis
  }

  private def setClientName(jedis: Jedis) = {
    redisConnectionCount += 1
    jedis.clientSetname(s"REDIS_TOOL_BOX_$redisConnectionCount")
  }

  def connectionName: String = {
    s"$host:$port | {$db}"
  }

  def ping(): String = {
    redisClient.ping()
  }

  def scan(cursor: String, params: ScanParams): ScanResult[String] = {
    redisClient.scan(cursor, params)
  }

  def dump(key: String): Array[Byte] = {
    redisClient.dump(key)
  }

  def restore(key: String, ttl: Long, serializedValue: Array[Byte], params: RestoreParams): String = {
    redisClient.restore(key, ttl, serializedValue, params)
  }

}
