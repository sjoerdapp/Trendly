import static java.util.Map.entry;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;

/** A wrapper class to access Google Trends API through Java */
public class TrendsAPIWrapper {
  private static final String BASE_URL = "https://www.googleapis.com/trends/v1beta/";
  private static final String API_KEY = "";
  private static final Map<String, Class<? extends TrendsResult>> FUNC_TO_CLASS =
      Map.ofEntries(
          entry("topTopics", TrendsTopicsResult.class),
              entry("risingTopics", TrendsRisingTopicsResult.class),
          entry("topQueries", TrendsQueriesResult.class),
              entry("risingQueries", TrendsRisingQueriesResult.class));

  /**
   * Runs the requested function from Google Trends API with the given restrictions and returns
   * TrendsResult object with the returned object from the API.
   *
   * @param funcName - The requested function name on Trends API (topTopics, risingTopics,
   *     topQueries, risingQueries).
   * @param term - The search term wished to be explored.
   * @param location - The location restriction for the search, in ISO-3166-2 format.
   * @param startDate - The start of requested time range should be a month and a year in the format
   *     YYYY-MM.
   * @param endDate - The end of requested time range should be a month and a year in the format
   *     YYYY-MM.
   * @return TrendsResult (TrendsTopicsResult/ TrendsRisingTopicsResult/ TrendsQueriesResult/
   *     TrendsRisingQueriesResult) object that returned from Google Trends API
   * @throws IOException
   */
  public static TrendsResult fetchDataFromTrends(
      String funcName, String term, String location, String startDate, String endDate)
      throws IOException {
    HttpTransport httpTransport = new NetHttpTransport();
    GenericUrl url = new GenericUrl(TrendsAPIWrapper.BASE_URL + funcName);
    url.put(UrlParams.TERM, term);
    url.put(UrlParams.GEO, location);
    url.put(UrlParams.START_DATE, startDate);
    url.put(UrlParams.END_DATE, endDate);
    url.put(UrlParams.KEY, TrendsAPIWrapper.API_KEY);
    url.put(UrlParams.ALT, UrlParams.JSON);
    HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
    HttpRequest request = requestFactory.buildGetRequest(url);
    HttpResponse httpResponse = request.execute();
    Gson gson = new Gson();
    return gson.fromJson(httpResponse.parseAsString(), FUNC_TO_CLASS.get(funcName));
  }
}