import org.junit.runner.RunWith
import org.specs2.matcher.JsonMatchers
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers._
import play.api.test._


@RunWith(classOf[JUnitRunner])
class RequirementsSpec extends Specification with JsonMatchers {

  object CheckJson {
    def apply(query: Option[String], expected: JsValue) = {
      val result = controllers.Application.queryProducts(query)(FakeRequest())
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      contentAsJson(result) mustEqual expected
    }
  }

  "Application" should {

    "return product with id 428236" in {
      val product428236 = Json.arr(
        Json.obj(
          "id" -> "428236",
          "name" -> "Stripe-trimmed open-knit cotton-blend sweater",
          "price" -> 515
        )
      )

      "query with 'sweater'" in CheckJson(
        query = Some("sweater"),
        expected = product428236
      )

      "query with 'Stripe'" in CheckJson(
        query = Some("Stripe"),
        expected = product428236
      )

      "query with 'knit'" in CheckJson(
        query = Some("knit"),
        expected = product428236
      )
    }

    "return all product" in CheckJson(
      query = None,
      expected = Json.arr(
        Json.obj(
          "id" -> "428236",
          "name" -> "Stripe-trimmed open-knit cotton-blend sweater",
          "price" -> 515
        ),
        Json.obj(
          "id" -> "444634",
          "name" -> "The Perfect cotton-chambray shirt",
          "price" -> 210
        )
      )
    )

    "return empty array" in {
      "query with a dash char" in CheckJson(
        query = Some("Stripe-trimmed"),
        expected = Json.arr()
      )

      "query with a space char" in CheckJson(
        query = Some("The Perfect"),
        expected = Json.arr()
      )
    }
  }
}
