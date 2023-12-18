package sg.edu.nus.se.its.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.util.constants.Constants;

/** Collection of Utility functions. */
public class UtilFunctions {


  /**
   * Get the time since Epoch in seconds.
   *
   * @return the time since Epoch in seconds.
   */
  public static double secondsSinceEpoch() {
    return System.currentTimeMillis() / (double) 1000;
  }

  /**
   * Check whether object is undefined.
   *
   * @param x the object to check.
   * @return whether x is undefined.
   */
  public static boolean isUndefined(Object x) {
    return Objects.equals(x, Constants.UNDEFINED);
  }

  /**
   * Assert an object can be cast to class {@code clazz}. Otherwise, throw {@code RuntimeError}.
   *
   * @param x object to check.
   * @param clazz class object to check.
   * @throws AssertionError if the types do not match.
   */
  public static void assertType(Object x, Class<?> clazz) {
    if (!clazz.isAssignableFrom(x.getClass())) {
      throw new AssertionError(
          String.format(
              "Type unmatched: expected %s, got %s",
              clazz.getSimpleName(), x.getClass().getSimpleName()));
    }
  }

  /**
   * Asserts the number of list elements by the given size restriction.
   *
   * @param xs -- the list object to check
   * @param size -- expected size of the list
   * @throws AssertionError for unmatched sizes.
   */
  public static void assertArgCount(List<Object> xs, long size) {
    if (xs.size() != size) {
      throw new AssertionError(
          String.format("function argument count unmatched: excepted %d, got %d", size, xs.size()));
    }
  }

  /**
   * Asserts the number of list elements by the given size restriction.
   *
   * @param xs -- the list object to check
   * @param size -- expected size of the list
   * @throws AssertionError for exceed size limits.
   */
  public static void assertArgCountMax(List<Object> xs, long size) {
    if (xs.size() > size) {
      throw new AssertionError(
          String.format("function argument count unmatched: max %d, got %d", size, xs.size()));
    }
  }

  private static final int EXECUTE_TIMEOUT = 1000; // milliseconds

  /**
   * Execute a program with given path.
   *
   * @param filePath -- the path of source code
   * @param fileInputs -- the input for source code in filePath
   * @throws AssertionError for exceed size limits.
   */
  public static String executeProgramForC(File filePath, String fileInputs) {

    if (filePath == null || !filePath.exists()) {
      throw new RuntimeException("Parsing Error: Provided file does not exist!");
    }

    StringBuilder outputs = new StringBuilder();
    try {

      Path tmpFilePath = Files.createTempFile("program", ".c");
      Files.write(
          tmpFilePath,
          FileUtils.readFileToString(filePath, StandardCharsets.UTF_8)
              .getBytes(StandardCharsets.UTF_8));

      File tmpFile = tmpFilePath.toFile();
      File tmpFileOutput = new File(tmpFile.getAbsolutePath().replace(".c", ".out"));

      String[] compileCommand = {"gcc", tmpFile.toString(), "-o", tmpFileOutput.toString()};
      final String[] executeCommand = {tmpFileOutput.toString()};

      System.out.println(Arrays.toString(compileCommand));

      ProcessBuilder compileProcBuilder = new ProcessBuilder(compileCommand);
      Process compileProc = compileProcBuilder.start();

      compileProc.waitFor(EXECUTE_TIMEOUT, TimeUnit.MILLISECONDS);
      int exitCode = compileProc.exitValue();
      if (exitCode != 0) {
        throw new RuntimeException("Compilation Error: unexpected c script exit code " + exitCode);
      }

      ProcessBuilder executeProcBuilder = new ProcessBuilder(executeCommand);
      Process executeProc = executeProcBuilder.start();
      OutputStream stdin = executeProc.getOutputStream();

      stdin.write((fileInputs + "\n").getBytes());
      stdin.flush();
      executeProc.waitFor(EXECUTE_TIMEOUT, TimeUnit.MILLISECONDS);
      exitCode = executeProc.exitValue();
      if (exitCode != 0) {
        throw new RuntimeException("Executing Error: unexpected c script exit code " + exitCode);
      }

      tmpFile.deleteOnExit();
      tmpFileOutput.deleteOnExit();
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(executeProc.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        outputs.append(line);
      }

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return outputs.toString();
  }

  /**
   * Execute a program with given path.
   *
   * @param filePath -- the path of source code
   * @param fileInputs -- the input for source code in filePath
   * @throws AssertionError for exceed size limits.
   */
  public static String executeProgramForPython(File filePath, String fileInputs) {

    if (filePath == null || !filePath.exists()) {
      throw new RuntimeException("Parsing Error: Provided file does not exist!");
    }

    StringBuilder outputs = new StringBuilder();
    try {

      Path tmpFilePath = Files.createTempFile("program", ".py");
      Files.write(
          tmpFilePath,
          (FileUtils.readFileToString(filePath, StandardCharsets.UTF_8) + fileInputs)
              .getBytes(StandardCharsets.UTF_8));
      File tmpFile = tmpFilePath.toFile();
      String command = "python3 " + tmpFile;

      Process proc = Runtime.getRuntime().exec(command);
      proc.waitFor(EXECUTE_TIMEOUT, TimeUnit.MILLISECONDS);

      int exitCode = proc.exitValue();
      if (exitCode != 0) {
        throw new RuntimeException(
            "Executing Error: unexpected python script exit code " + exitCode);
      }

      tmpFile.deleteOnExit();

      BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        outputs.append(line);
      }

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return outputs.toString();
  }

  /**
   * Deep equality check for two objects.
   *
   * @param o1 - any Object
   * @param o2 - any Object
   * @return equality check of the given objects
   */
  public static boolean deepEquals(Object o1, Object o2) {

    if (o1.getClass().isArray()) {
      if (o2.getClass().isArray()) {
        return Arrays.deepEquals((Object[]) o1, (Object[]) o2);
      } else {
        return false;
      }
    }

    if (o1 instanceof String) {
      if (o2 instanceof String) {
        return ((String) o1).equals((String) o2);
      } else {
        return false;
      }
    }

    if (o1.getClass().isPrimitive()) {
      if (o2.getClass().isPrimitive()) {
        return Objects.equals(o1, o2);
      } else {
        return false;
      }
    }

    // todo: might affect other tests other than python data strucutre
    // check for type difference between Pythontuple and list
    if (o1 instanceof ArrayList && o2 instanceof ArrayList) {
      if (!o1.getClass().equals(o2.getClass())) {
        return false;
      }
    }


    return Objects.deepEquals(o1, o2);
  }

  /**
   * Generates a deep copy of the provided program.
   *
   * @param program the program to be copied
   * @return a deep copy of the program
   */
  public static Program deepCopyProgram(Program program) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    builder.setPrettyPrinting();

    Gson gson = builder.create();
    return gson.fromJson(gson.toJson(program), Program.class);
  }

}
