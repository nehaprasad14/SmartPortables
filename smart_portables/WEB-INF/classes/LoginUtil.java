import java.io.*;
import javax.servlet.http.*;

public class LoginUtil {
	HttpServletRequest req;
	HttpSession session;

	public LoginUtil(HttpServletRequest req) {
		this.req = req;
		this.session = req.getSession(true);
	}
    
    public boolean validation(String username, String password){
        if((username==null|| username=="" || username==" ")&&(password==null||password=="" || password==" ")){
			session.setAttribute("missing", "PLEASE ENTER USERNAME and PASSWORD");
            return false;
		} else if(username==null|| username=="" || username==" "){
			session.setAttribute("missing", "PLEASE ENTER USERNAME");
            return false;
		} else if(password==null|| password=="" || password==" "){
			session.setAttribute("missing", "PLEASE ENTER PASSWORD");
            return false;
		} else{
            return true;
        }
    }
    
    public boolean isLoggedin(){
		if (session.getAttribute("username")==null)
			return false;
		return true;
	}

    public void logout(){
		session.removeAttribute("logged");
		session.removeAttribute("username");
        session.removeAttribute("user");
	}
	
}
