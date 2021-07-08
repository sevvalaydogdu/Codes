import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Login iþlemini kontrol eder.UserModel ve User ý kullanýr.
class LoginView implements ViewInterface {

	@Override
	public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
		
		switch(operationName) {
		case "select": return selectOperation(modelData);	
		case "select.gui": return selectGUI(modelData);
		}
		
		return new ViewData("MainMenu", "");
	}
	public static void loginUser(String user,int yetki_id) {
		DatabaseUtilities.session=user;
		DatabaseUtilities.yetki_id=yetki_id;
		
	}
	
	ViewData selectOperation(ModelData modelData) throws Exception {
		ResultSet resultSet = modelData.resultSet;
		
		if (resultSet != null) {
			while (resultSet.next()) {
				// Retrieve by column name
				
				String Username = resultSet.getString("Username");
				String fullName = resultSet.getString("Fullname");
				short Yetki_id = resultSet.getShort("Yetki_id");
				String yetkiAdi = resultSet.getString("Yetki_Adý");
				
				// Display values
				
				System.out.print("Welcome "+fullName+"!\n");
				System.out.print(yetkiAdi+" Kullanýcý");
				loginUser(Username,Yetki_id);
				return new ViewData("MainMenu", "");
			}
			resultSet.close();	
		}
		
		System.out.print("Username or password is not correct!");
		return new ViewData("MainMenu", "");
	}
	
	
	
	Map<String, Object> getWhereParameters() throws Exception {
		System.out.println("Filter conditions:");
		String username = getString("Username giriniz : ", true);
		String password = getString("Password giriniz : ", true);
		
		Map<String, Object> whereParameters = new HashMap<>();
		whereParameters.put("Username", username);
		whereParameters.put("Password", password);
		return whereParameters;
	}
	
	ViewData selectGUI(ModelData modelData) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("whereParameters", getWhereParameters());
		
		return new ViewData("Login", "select", parameters);
	}

	
	
	@Override
	public String toString() {
		return "Login Ekraný";
	}		
}
