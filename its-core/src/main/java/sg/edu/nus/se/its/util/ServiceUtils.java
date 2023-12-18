package sg.edu.nus.se.its.util;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Utility class with helper methods to call the ITS web services.
 */
public class ServiceUtils {

  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  /**
   * Sends a post request.
   *
   * @param url - the URL of the post request
   * @param json - the JSON payload content
   * @return HTML response
   * @throws IOException - thrown
   */
  public static String post(String url, String json) throws IOException {
    final OkHttpClient client = new OkHttpClient();

    RequestBody body = RequestBody.create(JSON, json);
    Request request = new Request.Builder().url(url).post(body).build();
    Response response = client.newCall(request).execute();
    final String responseStr = response.body().string();

    client.dispatcher().executorService().shutdown();
    client.connectionPool().evictAll();
    if (client.cache() != null) {
      client.cache().close();
    }

    return responseStr;
  }

}
