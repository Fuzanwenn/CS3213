package sg.edu.nus.se.its.parser;

import java.io.File;
import java.io.IOException;
import sg.edu.nus.se.its.model.Program;

/**
 * Interface for parsing a source code file into the internal data structure representation.
 */
public interface Parser {

  /**
   * Parses the program source code and returns the program in form of the internal object
   * representation.
   *
   * @param filePath - the path to the program text file.
   * @return the internal representation of the program source code.
   * @throws IOException if the file does not exist or could not be parsed.
   */
  public Program parse(File filePath) throws IOException;

}
