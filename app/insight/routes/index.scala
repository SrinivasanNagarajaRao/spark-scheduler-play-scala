package insight.routes

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.{SimpleRouter => Router}
import play.api.routing.sird._
import insight.controllers.{HomeController, ConfigController, DBConnectionController}

/**
  * Routes and URLs to the controller.
  */
class index @Inject()(controllers: HomeController, confController: ConfigController, dbController: DBConnectionController) extends Router {

  override def routes: Routes = {

    /** Re-routing Url's to its controller for Connection Check and connection to memsql */
    case GET(p"/intro") => controllers.index
    case GET(p"/listUsers") => controllers.listUsers

    /** Re-routing Utility urls */
    case POST(p"/config") => confController.provideconfig
    case POST(p"/json") => confController.saveconfig
    case POST(p"/deleteConfig") => confController.deleteConfig
    case POST(p"/manageconfig") => confController.saveconfig
    case POST(p"/updateconfig") => confController.updateGroup

    /** Re-routing Utility urls for Start Non Blocking cluster Spark Job */
    case POST(p"/submitJob") => dbController.createSparkJob

    /** Re-routing Utility urls for Saving Json and querying Json */
    case POST(p"/inputJson") => dbController.InputJson
    case POST(p"/inputJsonPf") => dbController.InputJsonPf
    case POST(p"/getJson") => dbController.statusQuery

  }

}
