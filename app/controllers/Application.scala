package controllers

import models.Product
import models.DAO._
import play.api.mvc._
import play.api.libs.json._

object Application extends Controller {

  // JSON read/write formatter
  implicit val productFormat = Json.format[Product]

  /**
   * Query products
   *
   * @param query
   * @return
   */
  def queryProducts(query: Option[String]) = Action {
    val queriedProducts = query map Products.filter
    Ok(Json.toJson(queriedProducts getOrElse Products.all))
  }
}