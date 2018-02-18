
public class User implements java.io.Serializable {
    
    private String username = null;
  	private String email = null;
  	private String password = null;
  	private String role = null;
    private int uid = 0;
  	
  	public User() {

  	}

  	public User(String username, String email, String password, String role) {
  		super();
        this.username = username;
  		this.email = email;
  		this.password=password;
  		this.role = role;
  	}
    
    public int getUid() {
  		return uid;
  	}

  	public void setUid(int uid) {
  		this.uid = uid;
  	}
    
    public String getUsername() {
  		return username;
  	}

  	public void setUsername(String username) {
  		this.username = username;
  	}

  	public String getEmail() {
  		return email;
  	}

  	public void setEmail(String email) {
  		this.email = email;
  	}

  	public String getPassword() {
  		return password;
  	}

  	public void setPassword(String password) {
  		this.password = password;
  	}

  	public String getRole() {
  		return role;
  	}

  	public void setRole(String role) {
  		this.role = role;
  	}
}
