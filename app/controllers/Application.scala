package controllers

import java.util.Date
import javax.inject.Inject

import play.api._
import play.api.libs.json.{JsArray, JsObject, Json}
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.Cursor
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject()(
  val reactiveMongoApi: ReactiveMongoApi
) extends Controller with MongoController with ReactiveMongoComponents {

  def collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection]("persons")

  def index = Action {
    Ok("works")
  }

  def create(name: String, age: Int) = Action.async {
    val json = Json.obj(
      "name" -> name,
      "age" -> age,
      "created" -> new Date().getTime()
    )
    collection.insert(json).map(lastError =>
      Ok("Mongo LastError: %s".format(lastError)))
  }

  def findByName(name: String) = Action.async {
    val cursor: Cursor[JsObject] = collection.
      find(Json.obj("name" -> name)).
      sort(Json.obj("created" -> -1)).
      cursor[JsObject]

    val futurePersonsList: Future[List[JsObject]] = cursor.collect[List]()

    val futurePersonsJsonArray: Future[JsArray] =
      futurePersonsList.map { persons => Json.arr(persons) }

    futurePersonsJsonArray.map { persons =>
      Ok(persons)
    }

  }

}