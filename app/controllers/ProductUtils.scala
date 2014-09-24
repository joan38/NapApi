package controllers

import models.{Product, ApiProduct}

/**
 * Created by Joan on 24/09/2014.
 */
object ProductUtils {
  def toProduct(apiProduct: ApiProduct) =
    Product(apiProduct.id.toString, apiProduct.name, apiProduct.gross / apiProduct.divisor)
}
