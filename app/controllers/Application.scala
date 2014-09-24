package controllers

import models.DAO._
import models.{ApiProduct, Product}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  // JSON read/write formatter
  implicit val apiProductReads: Reads[ApiProduct] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name" \ "en").read[String] and
      (JsPath \ "price" \ "gross").read[Int] and
      (JsPath \ "price" \ "divisor").read[Int]
    )(ApiProduct.apply _)

  implicit val productFormat = Json.format[Product]

  /**
   * Query products by name
   *
   * @param query
   * @return
   */
  def queryProducts(query: Option[String]) = Action.async {
    val queriedApiProducts = query map ApiProducts.filterByName getOrElse ApiProducts.all
    val queriedProducts = queriedApiProducts map (_ map ProductUtils.toProduct)
    queriedProducts map (Json.toJson(_)) map (Ok(_))
  }
}