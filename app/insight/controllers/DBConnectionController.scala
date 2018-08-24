package insight.controllers

import javax.inject._
import scala.concurrent.Future
import scala.sys.process.Process
import javax.inject._
import com.redis.RedisClient
import insight.controllers.ControllerImplicits._
import insight.entities.{GlobalFilters, _}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{Json, _}
import insight.services.jobModules._
import insight.services.Constants._
import scala.sys.process.ProcessBuilder
import scala.sys.process._
import scala.concurrent.Future
import akka.actor.ActorSystem
import play.api.mvc._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

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
class DBConnectionController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem, config: play.api.Configuration) extends AbstractController(cc) {

  val redis = new RedisClient(REDIS_HOST, REDIS_PORT.asInstanceOf[Int], REDIS_DATABASE.asInstanceOf[Int])

  private def getJobDetails(i: Int, key: String): JsValue = {
    val sessions = redis.hkeys("RUNNINGJOB") // Returns list[String] do a for loop
    val gh = sessions.toList.flatMap { case (a) => Seq(a) }
    if (gh(0).size <= i) {
      return Json.obj("status" -> "SERVER IS BUSY") // return as Json
    }

    val value = redis.hget("RUNNINGJOB", gh(0)(i))
    val redisJsonString: String = value match {
      case None => ""
      case Some(s: String) => s
    }

    var rjs = Json.parse(redisJsonString)

    if ((rjs \ "jobid").as[String] == "null") {
      rjs = rjs.as[JsObject] ++ Json.obj("jobid" -> key)
      val dg = redis.hsetnx("LOCKJOB", gh(0)(i), key)
      if (dg) {
        return Json.obj("sessionid" -> gh(0)(i), "value" -> rjs) // retun Json
      } else {
        return getJobDetails(i + 1, key)
      }

    } else {
      return getJobDetails(i + 1, key)
    }

  }

  private def getCode(code: JsValue, sessionId: String): String = {
    println("get code type" + (code \ "Type").as[String])
    (code \ "Type").as[String] match {
      case "filter-data" =>
        return "/home/ubuntu/spark/bin/spark-submit --class com.bmw.sip.ProcessApp --driver-memory 12g --executor-memory 20G --total-executor-cores 16 --master spark://ip-172-31-36-179.eu-central-1.compute.internal:7077 --deploy-mode client /home/ubuntu/codebase/" +
          "spark-backend/target/bmw-smart-insight-0.0.1-SNAPSHOT-jar-with-dependencies.jar " + (code \ "Type").as[String] + " " + (code \ "config").as[String] +
          " " + sessionId;
      case "cluster-data" =>
        return "/home/ubuntu/spark/bin/spark-submit --class com.bmw.sip.ProcessApp --driver-memory 12g --executor-memory 20G --total-executor-cores 16 --master spark://ip-172-31-36-179.eu-central-1.compute.internal:7077 --deploy-mode client /home/ubuntu/codebase/" +
          "spark-backend/target/bmw-smart-insight-0.0.1-SNAPSHOT-jar-with-dependencies.jar " + (code \ "Type").as[String] + " " + (code \ "config").as[String] +
          " " + sessionId;
      case "profile-characteristics-data" =>
        return "/home/ubuntu/spark/bin/spark-submit --class com.bmw.sip.ProcessApp --driver-memory 12g --executor-memory 20G --total-executor-cores 16 --master spark://ip-172-31-36-179.eu-central-1.compute.internal:7077 --deploy-mode client /home/ubuntu/codebase/" +
          "spark-backend/target/bmw-smart-insight-0.0.1-SNAPSHOT-jar-with-dependencies.jar " + (code \ "Type").as[String] + " " + (code \ "config").as[String] +
          " " + sessionId;
      case "profile-characteristics" =>
        return "/home/ubuntu/spark/bin/spark-submit --class com.bmw.sip.ProcessApp --driver-memory 12g --executor-memory 20G --total-executor-cores 16 --master spark://ip-172-31-36-179.eu-central-1.compute.internal:7077 --deploy-mode client /home/ubuntu/codebase/" +
          "spark-backend/target/bmw-smart-insight-0.0.1-SNAPSHOT-jar-with-dependencies.jar " + (code \ "Type").as[String] + " " + (code \ "config").as[String] +
          " " + sessionId;
      case "extended-care" =>
        return "/home/ubuntu/spark/bin/spark-submit --class com.bmw.sip.ProcessApp --driver-memory 12g --executor-memory 20G --total-executor-cores 16 --master spark://ip-172-31-36-179.eu-central-1.compute.internal:7077 --deploy-mode client /home/ubuntu/codebase/" +
          "spark-backend/target/bmw-smart-insight-0.0.1-SNAPSHOT-jar-with-dependencies.jar " + (code \ "Type").as[String] + " " + (code \ "config").as[String] +
          " " + sessionId;
    }
  }

  private def create(code: JsValue, sessionId: String): String = {
    println("creating Job" + code + " with sessionId=>" + sessionId)
    val result = getCode(code, sessionId)
    println("Result" + result)
    //    Process("ssh -i /home/ubuntu/key.pem ubuntu@ip-172-31-36-179 " + result).run //runs in detached mode for linux
    Process("plink -i C://Users/kiruthika.kumar/Downloads/forbmw2.ppk ubuntu@18.196.121.63 " + result).run //runs in detached mode for windows
    val hg = "success"
    return hg
  }

  private def setJob(job: JsValue, code: JsValue): JsObject = {
    println("setting job " + job + " with " + code)
    println("value " + Json.stringify(job))
    val value = (job \ "value").as[JsObject]
    redis.hset("RUNNINGJOB", (job \ "sessionid").as[String], Json.stringify(value))
    var cjob = create(code, (job \ "sessionid").as[String])
    return Json.obj("status" -> "Job Started", "sessionid" -> (job \ "sessionid").as[String], "jobid" -> (job \ "value" \\ "jobid"))
  }

  def createSparkJob = Action.async(parse.json) { implicit request =>
    val connection = Json.fromJson[Input](request.body)
    println("Inside Connection " + connection)
    connection match {
      case JsSuccess(connection: Input, _) => {
        val job: JsValue = getJobDetails(0, (connection.config))
        println("Inside job " + job)
        val result: JsObject = if ((job \ "sessionid").asOpt[String] == None) {
          println("Inside If")
          Json.obj("status" -> "SERVER IS BUSY")
        } else {
          println("Inside else" + job + " with " + connection)
          val sjob = setJob(job, Json.obj("Type" -> connection.Type, "config" -> connection.config))
          val j: JsObject = if (sjob != "null") {
            sjob
          }
          else {
            Json.obj("status" -> "SERVER IS BUSY")
          }
          j
        }
        Future {
          Ok(result)
        }
      }
    }
  }

  def statusQuery = Action.async(parse.json) { implicit request =>
    println("Becker" + request)
    val connection = Json.fromJson[statusQuery](request.body)
    connection match {
      case JsSuccess(connection: statusQuery, _) => {
        val redisConfiguration = getRedisJson(connection.hashDb, connection.redisKey, Map("redisIp" -> insight.services.Constants.REDIS_HOST, "redisPort" -> insight.services.Constants.REDIS_PORT, "redisdb" -> insight.services.Constants.REDIS_DATABASE, "redispwd" -> insight.services.Constants.REDIS_PASSWORD))
        Future {
          Ok(redisConfiguration._2)
        }
      }
    }
  }

  def InputJson = Action.async(parse.json) { implicit request =>
    val con = (request.body).as[JsValue]
    var result = saveJson(redis, (con \ "Type").as[String], (con \ "config").as[String])
    Future {
      Ok(result)
    }
  }

  def InputJsonPf = Action.async(parse.json) { implicit request =>
    val con = (request.body).as[JsValue]
    println("con " + con)
    var result = saveJsonPf(redis, (con \ "Type").as[String], (con \ "config_name").as[List[String]])
    Future {
      Ok(result)
    }
  }

  private def handleValidationError(e: JsError): Future[Result] = Future.successful(Status(400)(Json.obj("errors" -> JsError.toJson(e).toString())))
}
