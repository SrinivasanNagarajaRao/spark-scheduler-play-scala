package insight.controllers

import javax.inject._
import akka.actor.ActorSystem
import play.api.mvc._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}
import insight.entities.UserService
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{JsError, JsObject, JsSuccess, Json}
import insight.controllers.ControllerImplicits._

/** @usecase Add these imports in order to use ws Client
  *          import play.api.libs.ws._
  *          import play.api.http.HttpEntity
  *          import akka.actor.ActorSystem
  *          import akka.stream.ActorMaterializer
  *          import akka.stream.scaladsl._
  *          import akka.util.ByteString
  */

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
  * @param ws          WS will have to declare a dependency on the WSClient
  *
  */

/** Add these to inject ws client dependency
  * , ws: WSClient
  * */

@Singleton
class HomeController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  /**
    * Creates an Action that returns a plain text message after a delay
    * of 1 second.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/message`.
    */
  def index = Action.async {
    /** Get Request
      *
      * ws.url("http://lv2.node.com:8088/ws/v1/cluster/apps").get().map {
      * response => Ok(response.json.toString)
      * }
      */

    /** Post Request
      *
      * import play.api.libs.ws.JsonBodyReadables._
      * import play.api.libs.ws.JsonBodyWritables._
      * import play.api.libs.ws.DefaultBodyReadables._
      * import play.api.libs.ws.DefaultBodyWritables._
      * ws.url("http://lv2.node.com:8088/ws/v1/cluster/apps/new-application").post("").map {
      * response => Ok(response.json.toString)
      * }
      */

    getFutureMessage(0.1.second).map { msg => Ok(msg) }

  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      println("Hi!")
      promise.success("Hi!")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

  def listUsers = Action.async { implicit request =>
    UserService.listAllUsers map { users =>
      Ok(Json.toJson(users))
    }
  }


}
