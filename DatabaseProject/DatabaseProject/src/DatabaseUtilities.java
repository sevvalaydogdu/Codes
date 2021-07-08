import java.sql.*;
import java.time.LocalDate;
import java.util.*;


class DatabaseUtilities {
	public static String session=null;
	public static int yetki_id=0;
	private static Connection connection = null;
	public static String host;
	public static String databaseName;
	
	private DatabaseUtilities() {
		
	}

	// database connection
	public static Connection getConnection() throws SQLException {
		if (connection == null) {
			String conUrl =   "jdbc:sqlserver://" + host + ";"
							+ "databaseName=" + databaseName + ";"
							+ "integratedSecurity=true;";

			connection = DriverManager.getConnection(conUrl);
			
		}
		
		return connection;
	}
	
	public static void disconnect() throws SQLException
	{
		if (connection == null) {
			connection.close();
		}
	}
	
	// common functions
	public static List<Map.Entry<String, Object>> createWhereParameterList(Map<String, Object> parameters)
	{
		List<Map.Entry<String, Object>> whereParameters = new ArrayList<Map.Entry<String, Object>>();

		if (parameters != null) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) { 
				whereParameters.add(entry);
			}
		}
		
		return whereParameters;
	}

	public static String prepareWhereStatement(List<Map.Entry<String, Object>> whereParameters)
	{
		StringBuilder whereStatement = new StringBuilder();
		
		if (whereParameters != null) {
			for (int i=0; i<whereParameters.size(); i++) {
				Map.Entry<String, Object> entry = whereParameters.get(i);
				String key = entry.getKey();
				Object value = entry.getValue();	
				
				String pref = (i == 0 ? " WHERE " : " AND ");
				if (value instanceof String && value.toString().contains("%"))
					whereStatement.append(pref + key + " LIKE ? ");
				else if (value ==null)
					whereStatement.append(pref + key);
				else
					whereStatement.append(pref + key + " = ? ");				
			}
		}
		
		return whereStatement.toString();
	}
	
	public static void setWhereStatementParameters(PreparedStatement preparedStatement, List<Map.Entry<String, Object>> whereParameters) throws Exception 
	{
		//null degeri whereParameters listesinden çýkarýyor
		for (int i=0; i<whereParameters.size(); i++) {
			whereParameters.get(i);
			
			if(whereParameters.get(i).getValue()==null)
				whereParameters.remove(i);
		}
		
		if (whereParameters != null ) {
			for (int i=0; i<whereParameters.size(); i++) {
				Map.Entry<String, Object> entry = whereParameters.get(i);
				Object value = entry.getValue();	
				
				if (value instanceof Integer) {
					preparedStatement.setInt(i + 1, (Integer)value);
				}
				
				if (value instanceof String) {
					preparedStatement.setString(i + 1, (String)value);
				}
				
				
			}
		}
	}

	public static Object formatField(Object value) {
		if (value instanceof String
				|| value instanceof Boolean
				|| value instanceof LocalDate) {
			return "'" + value + "'"; 
		}
		
		return value;
	}
	
}
