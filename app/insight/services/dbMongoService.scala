package insight.services

import javax.inject.Inject
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.collection.JSONCollection
import scala.concurrent.{ExecutionContext, Future}
import play.modules.reactivemongo.json._

trait PostRepo {

  def find()(implicit ec: ExecutionContext): Future[List[JsObject]]

  def update(selector: BSONDocument, update: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult]

  def remove(document: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult]

  def save(document: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult]

}

class dbMongoService @Inject()(reactiveMongoApi: ReactiveMongoApi) extends PostRepo {

  protected def collection = reactiveMongoApi.db.collection[JSONCollection]("jobs")

  def getDBobj(name: String): JSONCollection = {
    reactiveMongoApi.db.collection[JSONCollection](name)
  }

  def find()(implicit ec: ExecutionContext): Future[List[JsObject]] = {
    collection.find(Json.obj()).cursor[JsObject](ReadPreference.Primary).collect[List]()
  }

  def find(document: BSONDocument)(implicit ec: ExecutionContext): Future[List[JsObject]] = {
    collection.find(document).cursor[JsObject](ReadPreference.Primary).collect[List]()
  }

  def find(selector: BSONDocument, columnList: BSONDocument)(implicit ec: ExecutionContext): Future[List[JsObject]] = {
    collection.find(selector, columnList).cursor[JsObject](ReadPreference.Primary).collect[List]()
  }

  def find(document: BSONDocument, collectionOBJ: JSONCollection)(implicit ec: ExecutionContext): Future[List[JsObject]] = {
    collectionOBJ.find(BSONDocument(), document).cursor[JsObject](ReadPreference.Primary).collect[List]()
  }

  def find(filter: BSONDocument, columns: BSONDocument, collectionOBJ: JSONCollection)(implicit ec: ExecutionContext): Future[List[JsObject]] = {
    collectionOBJ.find(filter, columns).cursor[JsObject](ReadPreference.Primary).collect[List]()
  }

  def update(selector: BSONDocument, update: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collection.update(selector, update)
  }

  def remove(document: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collection.remove(document)
  }

  def remove(document: BSONDocument, collectionOBJ: JSONCollection)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collectionOBJ.remove(document)
  }

  def save(document: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collection.update(BSONDocument("_id" -> document.get("_id").getOrElse(BSONObjectID.generate)), document, upsert = true)
  }

  def save(document: BSONDocument, collectionOBJ: JSONCollection)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collectionOBJ.update(BSONDocument("_id" -> document.get("_id").getOrElse(BSONObjectID.generate)), document, upsert = true)
  }

  def searchfind(key: String, selectObj: BSONDocument, collectionOBJ: JSONCollection)(implicit ec: ExecutionContext): Future[List[JsObject]] = {
    collectionOBJ.find(BSONDocument("text" -> BSONDocument("$regex" -> key)), selectObj).cursor[JsObject](ReadPreference.Primary).collect[List](300)
  }

  def getAllCollection()(implicit ec: ExecutionContext) = reactiveMongoApi.db.collectionNames

}
