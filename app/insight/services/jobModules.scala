package insight.services

import com.redis.RedisClient
import play.api.libs.json.{JsResultException, JsValue, Json}
import play.api.libs.json.{Json, _}

object jobModules {

  def getRedisJson(hashDb: String, key: String, Redis: Map[String, String]): Tuple2[RedisClient, JsValue] = {

    try {
      println("Entering Get Redis Method" + key + Redis)
      val redis = new RedisClient(Redis("redisIp").toString(), Redis("redisPort").toString().toInt, Redis("redisdb").toString().toInt)
      println("After Establishing Redis Connection")
      val redisJson1 = redis.hget(hashDb, key)
      val redisJson = redis.hget(hashDb, key)
      println("After Reading from redis" + redisJson)

      val redisJsonString: String = redisJson match {
        case None => ""
        case Some(s: String) => s
      }
      println("redisJsonString" + redisJsonString)

      (redis, Json.parse(redisJsonString))
    } catch {
      case a@(_: NullPointerException | _: InterruptedException | _: JsResultException | _: NoSuchElementException) =>
        println("Data Frame either data is null or json is empty")
        (null, null)
      case t: Throwable => println("Found an Unknown Exception " + t)
        (null, null)
    }

  }

  def saveJson(redis: RedisClient, fname: String, json_value: String): JsObject = {
    redis.hset("INPUTJSON", fname, json_value)
    Json.obj("status" -> "SUCCESS")
  }

  def saveJsonPf(redis: RedisClient, fname: String, json_value: List[String]): JsObject = {
    redis.hset("INPUTJSON", fname, Json.obj("config_name" -> json_value))
    Json.obj("status" -> "SUCCESS")
  }

}
