

public class User {
	
	private int User_id;
	private int Personel_id;
	private String Username;
	private String Password;
	private short Yetki_id;
	
	
	User() {
		
	}
	
	User(int user_id,String username,String password,int personel_id,
			short yetki_id
			) {
		this.User_id=user_id;
		this.Personel_id=personel_id;
		this.Username=username;
		
		this.Yetki_id=yetki_id;
		this.Password=password;
		
	}

	
	
	public Object getByName(String attributeName) {
		switch (attributeName) {
		case "User_id": return User_id;
		case "Personel_id": return Personel_id;
		case "Username": return Username;
		case "Yetki_id": return Yetki_id;
		case "Password": return Password;
		
		default: return null;
		}
	}
	
	@Override
	public String toString() {
		return "username";
	}

}

