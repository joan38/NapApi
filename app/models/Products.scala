package models

/**
 * Created by Joan on 23/09/2014.
 */
case class Product(id: String,
                   name: String,
                   price: Int)

trait ProductComponent {

  object Products {

    def filter(query: String) = Products.all filter (_.name split "-| " contains query)

    val all: Seq[Product] = Seq(
      Product(
        "428236",
        "Stripe-trimmed open-knit cotton-blend sweater",
        515
      ),
      Product(
        "444634",
        "The Perfect cotton-chambray shirt",
        210
      )
    )
  }
}