import java.sql.*;
import java.util.*;


class BasvuruModel implements ModelInterface {
	
	@Override
	public ResultSet select(Map<String, Object> whereParameters) throws Exception {
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" Basvuru_No,COALESCE(CAST(BasvuruCevabi AS VARCHAR(100)),'-') AS BasvuruCevabi, ");
		sql.append("	BasvuruTipi.Aciklama As BasvuruTipi ,"
				+ "		(CASE"
				+ "		WHEN TurkVatandasimi=1 THEN 'T.C. vatandaþý'"
				+ "		ELSE 'TC vatandaþý deðil'"
				+ "		END)As Uyruk, COALESCE(TCKimlikNo, 0) AS TCKimlikNo,"
				+ "		Adi,Soyadi,COALESCE(CAST(TuzelKisiUnvani AS VARCHAR(20)),'-') AS TuzelKisiUnvani,"
				+ "		BasvuruDurumu.Aciklama As Durumu,CevaplamaTipi.Aciklama As Cevaplama_Tipi"
				+ ",Basvuru_Tarihi,BasvuruCevapSonTarihi");
		sql.append(" FROM Basvuru ");
		sql.append(" INNER JOIN BasvuruDurumu on BasvuruDurumu.BasvuruDurumu_id=Basvuru.BasvuruDurumu_id ");
		sql.append(" INNER JOIN CevaplamaTipi on CevaplamaTipi.CevaplamaTipi_id=Basvuru.CevaplamaTipi_id ");
		sql.append(" INNER JOIN BasvuruTipi on BasvuruTipi.BasvuruTipi_id=Basvuru.BasvuruTipi_id ");
		
		List<Map.Entry<String, Object>> whereParameterList = DatabaseUtilities.createWhereParameterList(whereParameters);		
		sql.append(DatabaseUtilities.prepareWhereStatement(whereParameterList));
		
		sql.append(" ORDER BY BasvuruCevapSonTarihi");		
		//System.out.println(sql.toString() + "\n");

		try {
		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		DatabaseUtilities.setWhereStatementParameters(preparedStatement, whereParameterList);
		
		ResultSet result = preparedStatement.executeQuery();
		return result;
		} 
		catch (Exception e) {
	    	
			System.out.println( "Failed to insert SQL query: ( " + e.toString() + ")");
	        
	    }
		
		return null;
	}
		
	@Override
	public int insert(String fieldNames, List<Object> rows) throws Exception
	{
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO Basvuru (" + fieldNames + ") " );
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
			
			
			try {
				//kayýt yapýlan baþvurunun id deðerini döndürüyor.
				sql.append(";SELECT @@IDENTITY");
				Connection connection = DatabaseUtilities.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
				rowCount = preparedStatement.executeUpdate();
				ResultSet rs = preparedStatement.getGeneratedKeys();
				
				//baþvuru eklendikten sonra oluþturulan baþvuru_no yu alýyoruz
				int basvuruNo=0;
				if (rs.next()) {
					basvuruNo = rs.getInt(1);
					}
				System.out.println("Oluþturulan baþvuru no:"+basvuruNo);
				preparedStatement.close();	
				
				} 
			catch (Exception e) {
		    	
				System.out.println( "Failed to insert SQL query: ( " + e.toString() + ")");
		        
		    }
			
			
		}
		
		return rowCount;
	}
	
	@Override
	public int update(Map<String,Object> updateParameters, Map<String,Object> whereParameters) throws Exception
	{
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE Basvuru SET ");
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
		try {
			
			// execute constructed SQL statement
			Connection connection = DatabaseUtilities.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
			DatabaseUtilities.setWhereStatementParameters(preparedStatement, whereParameterList);		
			int rowCount = preparedStatement.executeUpdate();
			preparedStatement.close();
			return rowCount;
		}
		catch (Exception e) 
		{
	    	
			System.out.println( "Failed to insert SQL query: ( " + e.toString() + ")");
	        
	    }
		return 0;
	}

	@Override
	public int delete(Map<String,Object> whereParameters) throws Exception
	{
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM Basvuru ");

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
	//Adres listesini hiyerarþik bir þekilde getiriyor.
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
	//Red sebepleri listesi
	public static void selectRedSebebleri() throws Exception {
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" RedSebebi_id,Aciklama ");
		sql.append(" FROM RedSebebi ");
	
		//System.out.println(sql.toString() + "\n");

		
		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		//DatabaseUtilities.setWhereStatementParameters(preparedStatement, null);
		ResultSet result = preparedStatement.executeQuery();
		
		if (result != null) {
			while (result.next()) {
				// Retrieve by column name
				int id = result.getInt("RedSebebi_id");
				String aciklama = result.getString("Aciklama");

				// Display values
				System.out.print(id + "\t");
				System.out.print(aciklama + "\t");
				System.out.print("\n");
			}
			result.close();	
		}
		
	}
	//red sebebi acýklamasý ögrenmek için kullanýldý
	public static String selectRedSebebi(int id) throws Exception {
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" RedSebebi_id,Aciklama ");
		sql.append(" FROM RedSebebi ");
		sql.append(" WHERE RedSebebi_id=? ");
		//System.out.println(sql.toString() + "\n");

		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		preparedStatement.setInt(1, id);
		
		ResultSet result = preparedStatement.executeQuery();
		String output="";
		if (result != null) {
			while (result.next()) {
				// Retrieve by column name
				output+= result.getString("Aciklama");
			}
			result.close();	
		}
		return output;
	}
	public static void selectBasvuruDrumlari() throws Exception {
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" BasvuruDurumu_id,Aciklama ");
		sql.append(" FROM BasvuruDurumu ");
	
		//System.out.println(sql.toString() + "\n");

		
		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		//DatabaseUtilities.setWhereStatementParameters(preparedStatement, null);
		ResultSet result = preparedStatement.executeQuery();
		
		if (result != null) {
			while (result.next()) {
				// Retrieve by column name
				int id = result.getInt("BasvuruDurumu_id");
				String aciklama = result.getString("Aciklama");
				
				
				// Display values
				System.out.print(id + "\t");
				System.out.print(aciklama + "\t");
				System.out.print("\n");
			}
			result.close();	
		}
		
	}
	//Basvuru tiplerini getirir
	public static void selectBasvuruTipleri() throws Exception {
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" BasvuruTipi_id,Aciklama ");
		sql.append(" FROM BasvuruTipi ");
	
		//System.out.println(sql.toString() + "\n");

		
		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		//DatabaseUtilities.setWhereStatementParameters(preparedStatement, null);
		ResultSet result = preparedStatement.executeQuery();
		
		if (result != null) {
			while (result.next()) {
				// Retrieve by column name
				int id = result.getInt("BasvuruTipi_id");
				String aciklama = result.getString("Aciklama");

				// Display values
				System.out.print(id + "\t");
				System.out.print(aciklama + "\t");
				System.out.print("\n");
			}
			result.close();	
		}
		
	}
	@Override
	public String toString() {
		return "Baþvuru Model";
	}

	
	
}

