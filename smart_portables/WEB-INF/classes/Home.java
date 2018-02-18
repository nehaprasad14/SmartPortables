import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Home extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Utility utility = new Utility(request,out);
		utility.printHtml("header.html");
		utility.printHtml("content.html");
		utility.printHtml("left-nav.html");
		utility.printHtml("footer.html");

	}
}
