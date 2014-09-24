
/**
 * Created by Joan on 24/09/2014.
 */
package object models {

  /**
   * Default DAO definition
   */
  private[models] class DAO
    extends ApiProductComponent {
  }

  /**
   * Instance of the current used DAO
   */
  val DAO = new DAO
}
