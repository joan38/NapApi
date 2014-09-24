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
      running(FakeApplication()) {
        val result = controllers.Application.queryProducts(query)(FakeRequest())
        status(result) mustEqual OK
        contentType(result) must beSome("application/json")
        contentAsJson(result) mustEqual expected
      }
    }
  }

  "Application" should {

    "return 2 product when query with 'Muscle'" in CheckJson(
      query = Some("Muscle"),
      expected = Json.arr(
        Json.obj(
          "product-id" -> "175092",
          "name" -> "Classic Muscle jersey T-shirt",
          "price" -> 65.0
        ),
        Json.obj(
          "product-id" -> "175091",
          "name" -> "Muscle jersey T-shirt",
          "price" -> 65.0

        )
      )
    )

    "return all products" in {

      "query with no parameter" in {
        running(FakeApplication()) {
          val result = controllers.Application.queryProducts(None)(FakeRequest())
          status(result) mustEqual OK
          contentType(result) must beSome("application/json")
          contentAsJson(result).as[Array[JsValue]] must haveSize(1000)
        }
      }
    }

    "return empty array" in {

      "query with a dash char" in CheckJson(
        query = Some("leather-paneled"),
        expected = Json.arr()
      )

      "query with a space char" in CheckJson(
        query = Some("Markus Lupfer"),
        expected = Json.arr()
      )

      "query with 'Stripe'" in CheckJson(
        query = Some("Stripe"),
        expected = Json.arr()
      )
    }
  }
}
