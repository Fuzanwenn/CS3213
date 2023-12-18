package sg.edu.nus.se.its.util;

/**
 * Interface for classes that shall be de-/serializable in the custom JSON format.
 */
public interface JsonSerializable {

  /**
   * Returns the type of the class.
   *
   * @return type as String object
   */
  public String getType();

}
