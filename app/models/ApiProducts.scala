package models

import controllers.Application._
import play.api.Play.current
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Product from the API service
 *
 * Created by Joan on 23/09/2014.
 */
case class ApiProduct(id: Int,
                      name: String,
                      gross: Int,
                      divisor: Int) {
  assert(divisor != 0, "Divisor can't be 0")
}

trait ApiProductComponent {

  object ApiProducts {
    def filterByName(query: String) = all map (_ filter (_.name split "-| " contains query))

    def all = WS.url(current.configuration.getString("application.api.url").get).get() map { response =>
      (response.json \ "data").as[Array[ApiProduct]]
    }
  }

}