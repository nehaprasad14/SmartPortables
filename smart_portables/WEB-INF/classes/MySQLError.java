import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class MySQLError extends HttpServlet {

      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          response.setContentType("text/html");
          PrintWriter out = response.getWriter();
          Utility utility = new Utility(request,out);

          utility.printHtml("header.html");
          out.print("<article><hr style='width: 95%'><h2 style='font-size: 35px;'> MySQL server is not up and running !!</h2><hr style='width: 95%'></article>");
          utility.printHtml("left-nav.html");
          utility.printHtml("footer.html");
      }

}
