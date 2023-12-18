package sg.edu.nus.se.its.util.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Constants specific to c language.
 */
public class ClangConstants {
  /**
   * Custom label to represent pointer operation for c.
   */
  public static final String ADDRESS_OF = "AddressOf";

  /**
   * Custom label to represent casing of types.
   */
  public static final String CAST = "Cast";

  /**
   * Custom label to represent void function calls.
   */
  public static final String VOID_FUNCTION_CALLS = "_";

  public static final List<String> SUPPORTED_TYPES = new ArrayList<>(Arrays.asList(
      "char",
      "signed char",
      "unsigned char",
      "short",
      "short int",
      "signed short",
      "signed short int",
      "unsigned short",
      "unsigned short int",
      "int",
      "signed",
      "signed int",
      "unsigned",
      "unsigned int",
      "long",
      "long int",
      "signed long",
      "signed long int",
      "unsigned long",
      "unsigned long int",
      "long long",
      "long long int",
      "signed long long",
      "signed long long int",
      "unsigned long long",
      "unsigned long long int",
      "float",
      "double",
      "long double"
      ));


}
