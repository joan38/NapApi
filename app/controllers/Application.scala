package controllers

import models.DAO._
import models.{ApiProduct, Product}
import play.api.Logger
import play.api.Play.current
import play.api.libs.functional.syntax._
import play.api.libs.iteratee.Enumeratee
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  private val ApiUrl = "http://lad-api.net-a-porter.com/product/summaries?business=NAP&categoryIds=2&country=GB"

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

  /**
   * Work in progress
   *
   * @param query
   * @return
   */
  def queryProductsStream(query: Option[String]) = Action.async {
    val a = WS.url(ApiUrl).getStream().map(_._2.through(Enumeratee.map { bytes =>
      Logger.debug(new String(bytes))
      Json.parse(bytes)
    }))
    //Logger.debug(a.)
    a map { enumerator =>
      enumerator map (e => Logger.debug(Json.prettyPrint(e)))
      Ok.feed(enumerator)
    }
  }
}