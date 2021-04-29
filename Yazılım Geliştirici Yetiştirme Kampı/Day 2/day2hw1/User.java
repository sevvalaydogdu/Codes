package day2hw1;

public class User {
	private String name;
	private int userId;
	private String username;
	private String password;
	private String eposta;
	
	public User(String name,int userId,String username,String password,String eposta) {
		this.name = name;
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.eposta = eposta;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEposta() {
		return eposta;
	}

	public void setEposta(String eposta) {
		this.eposta = eposta;
	}
	
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	

}

