package insight.controllers

import javax.inject._
import play.api.mvc._
import akka.util.Timeout
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import com.redis.RedisClient
import scala.sys.process.ProcessBuilder
import scala.sys.process._
import akka.actor.{ActorSystem, Props}
import play.api.mvc.{Action, Controller}
import insight.services.jobModules._
import insight.services.Constants._
import scala.concurrent.duration._
import play.api.libs.json.{JsError, JsObject, JsSuccess, Json}
import scala.concurrent.{ExecutionContext, Future, Promise}
import insight.entities.{GlobalFilters, ParallelEntityunclus, PostEntity, PostEntity2, _}
import insight.controllers.ControllerImplicits._

/**
  * This controller creates an `Action` that demonstrates how to write
  * simple asynchronous code in a controller. It uses a timer to
  * asynchronously delay sending a response for 1 second.
  *
  * @param cc          standard controller components
  * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
  *                    run code after a delay.
  * @param exec        We need an `ExecutionContext` to execute our
  *                    asynchronous code.  When rendering content, you should use Play's
  *                    default execution context, which is dependency injected.  If you are
  *                    using blocking operations, such as database or network access, then you should
  *                    use a different custom execution context that has a thread pool configured for
  *                    a blocking API.
  */
@Singleton
class ConfigController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem, config: play.api.Configuration) extends AbstractController(cc) {

  implicit val duration: Timeout = 20 seconds

  //READ - GET - /v1/manageconfig
  def provideconfig() = Action.async { implicit request =>
    Logger.debug("Receved request to give all configuration data ")
    val red = new RedisClient(REDIS_HOST, REDIS_PORT.asInstanceOf[Int], REDIS_DATABASE.asInstanceOf[Int])
    val listOfJob = red.hgetall1("config").get.map(i => (Json.parse(i._2)))
    println("listOfJob " + listOfJob)
    red.disconnect
    Future {
      Ok(Json.toJson(listOfJob))
    }
  }

  //CREATE & Update - POST - /v1/manageconfig
  def saveconfig() = Action.async(parse.json) { implicit request =>
    val connection = Json.fromJson[PostEntity](request.body)
    Logger.debug(connection.toString)
    connection match {
      case JsSuccess(connection: PostEntity, _) => {
        val red = new RedisClient(REDIS_HOST, REDIS_PORT.asInstanceOf[Int], REDIS_DATABASE.asInstanceOf[Int])
        red.hset("config", connection.config_name, Json.toJson(connection))
        val listOfJob = red.hgetall1("config").get.map(i => (Json.parse(i._2)))
        red.disconnect
        Future {
          Ok(Json.toJson(listOfJob))
        }
      }
      case e: JsError => handleValidationError(e)
    }
  }

  def deleteConfig() = Action.async { implicit request =>

    if (request.queryString.get("configname").nonEmpty) {
      val configToDelete: String = request.queryString.get("configname").get(0)
      val red = new RedisClient(REDIS_HOST, REDIS_PORT.asInstanceOf[Int], REDIS_DATABASE.asInstanceOf[Int])
      if (red.hexists("config", configToDelete)) {
        red.hdel("config", configToDelete).get
        val listOfJob = red.hgetall1("config").get.map(i => (Json.parse(i._2)))
        red.disconnect
        Future {
          Ok(Json.toJson(listOfJob))
        }
      }
      else {
        Future.successful(Status(400)(Json.obj("ERROR" -> " There is not such configuration exist")))
      }
    }
    else {
      Future.successful(Status(400)(Json.obj("ERROR" -> "no proper Configuration Name provided ")))
    }
  }

  //CREATE & Update
  def updateGroup() = Action.async(parse.json) { implicit request =>
    val connection = Json.fromJson[List[ParallelEntityunclus]](request.body)
    Logger.debug(connection.toString)
    connection match {
      case JsSuccess(connection: List[ParallelEntityunclus], _) => {
        Logger.debug("Grouped Variable : " + connection.toString)
        updateConfig(connection) map { result =>
          Ok(Json.toJson(result))
        }
      }
      case e: JsError => handleValidationError(e)
    }
  }

  def updateConfig(meta: List[ParallelEntityunclus]): Future[Map[String, String]] = {

    val red = new RedisClient(REDIS_HOST, REDIS_PORT.asInstanceOf[Int], REDIS_DATABASE.asInstanceOf[Int])
    var rel = red.hget("config", meta(0).config_name).get
    val res = Json.parse(rel)
    val Overlay_VIN = (res \ "Overlay_VIN").as[List[String]]
    val Overlay_new = Overlay_VIN(0).split(",").toList
    val post = PostEntity2(None, (res \ "country_code").as[List[String]], (res \ "motor").as[List[String]], (res \ "series").as[List[String]], (res \ "Fuel").as[List[String]], (res \ "transmission").as[List[String]], (res \ "displacement").as[List[String]], (res \ "drive").as[List[String]], (res \ "hybrid_0_1").as[List[String]], (res \ "mileage").as[List[String]], (res \ "car_age").as[List[String]], (res \ "plant_name").as[List[String]], (res \ "dealer_name").as[List[String]], (res \ "KIEFA").as[List[String]], (res \ "Defect_Code_06").as[List[String]], (res \ "Defect_Code_08").as[List[String]], Overlay_new, (res \ "config_name").as[String], (res \ "FromDate").as[String], (res \ "ToDate").as[String], None)
    val obj = Json.toJson(post.copy(Variable_Group = Some(meta)))
    val as_obj: JsObject = obj.as[JsObject]
    Logger.debug(as_obj.toString)
    red.hset("config", meta(0).config_name(0), as_obj)
    red.disconnect
    val map = Map("result" -> "successful")
    Future.successful(map)
  }

  private def handleValidationError(e: JsError): Future[Result] = Future.successful(Status(400)(Json.obj("errors" -> JsError.toJson(e).toString())))

}
