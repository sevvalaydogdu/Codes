import java.sql.*;
import java.util.*;


class UserModel implements ModelInterface {
	
	@Override
	public ResultSet select(Map<String, Object> whereParameters) throws Exception {
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("	Username,Kullanýcýlar.Yetki_id,Yetki.Yetki_Adý, Personel_Adý+' '+Personel_Soyadý As Fullname");
		sql.append(" FROM Kullanýcýlar ");
		sql.append(" INNER JOIN Personel on Personel.Personel_id=Kullanýcýlar.Personel_id ");
		sql.append(" INNER JOIN Yetki on Yetki.Yetki_id=Kullanýcýlar.Yetki_id ");
		
		List<Map.Entry<String, Object>> whereParameterList = DatabaseUtilities.createWhereParameterList(whereParameters);		
		sql.append(DatabaseUtilities.prepareWhereStatement(whereParameterList));
		
				
		//System.out.println(sql.toString() + "\n");

		
		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		DatabaseUtilities.setWhereStatementParameters(preparedStatement, whereParameterList);
		ResultSet result = preparedStatement.executeQuery();
		
		return result;
	}
		
	@Override
	public int insert(String fieldNames, List<Object> rows) throws Exception
	{
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO Baþvuru (" + fieldNames + ") " );
		sql.append(" VALUES ");

		String[] fieldList = fieldNames.split(",");

		int rowCount = 0;
		for (int i=0; i<rows.size(); i++) {
			if (rows.get(i) instanceof Basvuru) {
				rowCount++;
				
				Basvuru basvuru = (Basvuru)rows.get(i); 
	
				sql.append("(");
				for (int j=0; j<fieldList.length; j++) {
					String fieldName = fieldList[j].trim();
					
					sql.append(DatabaseUtilities.formatField(basvuru.getByName(fieldName)));
					if (j < fieldList.length - 1) {
						sql.append(", ");
					}
				}
				sql.append(")");
				
				if (i < rows.size() - 1) {
					sql.append(", ");
				}				
			}
		}	
		
		//System.out.println(sql.toString());
		
		
		// execute constructed SQL statement
		if (rowCount > 0) {
			Connection connection = DatabaseUtilities.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
			rowCount = preparedStatement.executeUpdate();
			preparedStatement.close();
		}
		
		return rowCount;
	}
	
	@Override
	public int update(Map<String,Object> updateParameters, Map<String,Object> whereParameters) throws Exception
	{
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE Baþvuru SET ");
		int appendCount = 0;
		for (Map.Entry<String, Object> entry : updateParameters.entrySet()) {
			sql.append(entry.getKey() + " = " + DatabaseUtilities.formatField(entry.getValue()));
			if (++appendCount < updateParameters.size()) {
				sql.append(", ");
			}
		}
		List<Map.Entry<String, Object>> whereParameterList = DatabaseUtilities.createWhereParameterList(whereParameters);		
		sql.append(DatabaseUtilities.prepareWhereStatement(whereParameterList));
		//System.out.println(sql.toString());
		
	
		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		DatabaseUtilities.setWhereStatementParameters(preparedStatement, whereParameterList);		
		int rowCount = preparedStatement.executeUpdate();
		preparedStatement.close();
		
		return rowCount;
	}

	@Override
	public int delete(Map<String,Object> whereParameters) throws Exception
	{
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM Baþvuru ");

		List<Map.Entry<String, Object>> whereParameterList = DatabaseUtilities.createWhereParameterList(whereParameters);		
		sql.append(DatabaseUtilities.prepareWhereStatement(whereParameterList));
		//System.out.println(sql.toString());

	
		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		DatabaseUtilities.setWhereStatementParameters(preparedStatement, whereParameterList);		
		int rowCount = preparedStatement.executeUpdate();
		preparedStatement.close();
		
		return rowCount;
	}
	public static String selectGeographys() throws Exception {
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append("WITH Regions AS( ");
		sql.append("	SELECT \r\n"
				+ "		*, CAST(Name AS VARCHAR(100)) AS Location"
				+ "	FROM Geography"
				+ "	WHERE GeographyID = 1"
				
				+ "	UNION ALL"
			
				+ "	SELECT "
				+ "		GL.*,"
				+ "		      CAST(RTRIM(GL.Name)  + '/' + CAST(Location AS VARCHAR(100)) AS VARCHAR(100))"
				+ "	FROM Geography AS GL INNER JOIN Regions AS R"
				+ "	ON GL.ParentGeographyID = R.GeographyID"
				+ ") ");
		sql.append(" SELECT ");
		sql.append(" GeographyID,Location ");
		sql.append(" FROM Regions ");
	
		//System.out.println(sql.toString() + "\n");

		
		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		//DatabaseUtilities.setWhereStatementParameters(preparedStatement, null);
		ResultSet result = preparedStatement.executeQuery();
		String output="";
		if (result != null) {
			while (result.next()) {
				// Retrieve by column name
				int id = result.getInt("GeographyID");
				String aciklama = result.getString("Location");
				
				
				// Display values
				System.out.print(id + "\t");
				System.out.print(aciklama + "\t");
				System.out.print("\n");
			}
			result.close();	
		}
		return output;
	}
	@Override
	public String toString() {
		return "User Model";
	}

	
	
}


