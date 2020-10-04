
import com.google.gson.Gson;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Returns Rising topics data according the restrictions..*/
@WebServlet("/rising-topics")
public class RisingTopicsServlet extends HttpServlet {
  public static final String RESPONSE_JSON = "application/json";
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String country = request.getParameter("country");
    String term = request.getParameter("term");
    String interval = request.getParameter("interval");
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");

    LinkedHashMap<String, ArrayList<HistogramTopic>> results =
        HistogramyDataRetrieval.getDataForServlet(
            term, startDate, endDate, country, interval, TrendsFunctions.RISING_TOPICS);
    response.setContentType(RESPONSE_JSON);
    String json = new Gson().toJson(results);
    response.getWriter().println(json);
  }
}
