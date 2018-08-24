package insight.services

import javax.inject.Inject
import com.redis.RedisClient
import play.api.{Configuration, Environment}

class AppConfig @Inject()(playConfig: Configuration) {
  val mongoHost: Option[String] = playConfig.getString("mongodb.uri")
  val readoutData: Option[String] = playConfig.getString("flatfile.dataSource.readout_data")
  val warrantyData: Option[String] = playConfig.getString("flatfile.dataSource.warranty_data")

  val redisHost: Option[String] = playConfig.getString("redis.url")
  val redisPort: Option[Int] = playConfig.getInt("redis.port")
  val redisdb: Option[Int] = playConfig.getInt("redis.db")
}

object ConfigReader {
  val config = new AppConfig(Configuration.load(Environment.simple()))

  def getmongoDbHost: String = config.mongoHost.getOrElse("localhost")

  def getreadoutData: String = config.readoutData.getOrElse("file:///home/readout_data.csv")

  def getwarrantyData: String = config.warrantyData.getOrElse("file:///home/warranty_data.csv")

  def getredisHost: String = config.redisHost.getOrElse("localhost")

  def getredisPort: Int = config.redisPort.getOrElse(6379)

  def getredisDB: Int = config.redisdb.getOrElse(0)

}