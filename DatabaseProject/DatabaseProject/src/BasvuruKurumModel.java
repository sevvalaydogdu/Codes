import java.sql.*;
import java.util.*;


class BasvuruKurumModel implements ModelInterface {
	
	@Override
	public ResultSet select(Map<String, Object> whereParameters) throws Exception {
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("	Basvuru_No,BasvuruTipi.Aciklama As BasvuruTipi ,"
				+ "		(CASE"
				+ "		WHEN TurkVatandasimi=1 THEN 'T.C. Vatandaþý'"
				+ "		ELSE 'YABANCI KÝÞÝ'"
				+ "		END)As Uyruk, COALESCE(TCKimlikNo, 0) AS TCKimlikNo,"
				+ "		Adi,Soyadi,COALESCE(CAST(TuzelKisiUnvani AS VARCHAR(20)),'-') AS TuzelKisiUnvani,"
				+ "		BasvuruDurumu.Aciklama As Durumu,CevaplamaTipi.Aciklama As Cevaplama_Tipi"
				+ ",	Basvuru_Tarihi");
		sql.append(" FROM Basvuru ");
		sql.append(" INNER JOIN BasvuruDurumu on BasvuruDurumu.BasvuruDurumu_id=Basvuru.BasvuruDurumu_id ");
		sql.append(" INNER JOIN CevaplamaTipi on CevaplamaTipi.CevaplamaTipi_id=Basvuru.CevaplamaTipi_id ");
		sql.append(" INNER JOIN BasvuruTipi on BasvuruTipi.BaþvuruTipi_id=Basvuru.BasvuruTipi_id ");
		
		List<Map.Entry<String, Object>> whereParameterList = DatabaseUtilities.createWhereParameterList(whereParameters);		
		sql.append(DatabaseUtilities.prepareWhereStatement(whereParameterList));
		
		sql.append("ORDER BY Basvuru_No");		
		//System.out.println(sql.toString() + "\n");

		
		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		DatabaseUtilities.setWhereStatementParameters(preparedStatement, whereParameterList);
		System.out.print("sql-"+preparedStatement);
		ResultSet result = preparedStatement.executeQuery();
		
		return result;
	}
		
	@Override
	public int insert(String fieldNames, List<Object> rows) throws Exception
	{
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" SET XACT_ABORT ON ");
		sql.append(" begin transaction ");
		sql.append(" INSERT INTO BasvuruKurumYonlendirme (" + fieldNames + ") " );
		sql.append(" VALUES ");
		BasvuruKurum basvuruKurum=null;
		String[] fieldList = fieldNames.split(",");

		int rowCount = 0;
		for (int i=0; i<rows.size(); i++) {
			if (rows.get(i) instanceof BasvuruKurum) {
				rowCount++;
				
				basvuruKurum = (BasvuruKurum)rows.get(i); 
	
				sql.append("(");
				for (int j=0; j<fieldList.length; j++) {
					String fieldName = fieldList[j].trim();
					sql.append(DatabaseUtilities.formatField(basvuruKurum.getByName(fieldName)));
					if (j < fieldList.length - 1) {
						sql.append(", ");
					}
				}
				sql.append(")");
				//son cevaplama tarihine +15 gün daha eklenmesi
				sql.append(" UPDATE Basvuru SET ");
				sql.append(" BasvuruCevapSonTarihi=dateadd(day, 15, BasvuruCevapSonTarihi) ");
				sql.append(" ,BasvuruDurumu_id=2");
				sql.append(" WHERE Basvuru_No="+basvuruKurum.getByName("Basvuru_No"));
				
			}
		}	
		
		// execute constructed SQL statement
		if (rowCount > 0) {
			
			
			try {
				
				
				sql.append(" commit transaction");
				Connection connection = DatabaseUtilities.getConnection();
				
				System.out.println(sql.toString());
				PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

				rowCount = preparedStatement.executeUpdate();
				System.out.println("Cevaplama tarihine +15 gün eklendi.");
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
	//Adres listesini hiyerarþik bir þekilde getiriyor.
	public static String selectKurums() throws Exception {
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append("WITH AltKurumlar AS( ");
		sql.append("	SELECT \r\n"
				+ "		*, CAST(Kurum_Adý AS VARCHAR(100)) AS Kurum"
				+ "	FROM Kurumlar"
				+ "	WHERE Kurum_id = 1"
				
				+ "	UNION ALL"
			
				+ "	SELECT "
				+ "		K.*,"
				+ "		   CAST(RTRIM(K.Kurum_Adý)  + '/' + CAST(Kurum AS VARCHAR(100)) AS VARCHAR(100))"
				+ "	FROM Kurumlar AS K INNER JOIN AltKurumlar AS AK"
				+ "	ON K.Parent_Kurum_id = AK.Kurum_id"
				+ ") ");
		sql.append(" SELECT ");
		sql.append(" Kurum_id,Kurum ");
		sql.append(" FROM AltKurumlar ");
	
		//System.out.println(sql.toString() + "\n");

		
		// execute constructed SQL statement
		Connection connection = DatabaseUtilities.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
		ResultSet result = preparedStatement.executeQuery();
		String output="";
		if (result != null) {
			while (result.next()) {
				// Retrieve by column name
				int id = result.getInt("Kurum_id");
				String kurum = result.getString("Kurum");
				
				
				// Display values
				System.out.print(id + "\t");
				System.out.print(kurum + "\t");
				System.out.print("\n");
			}
			result.close();	
		}
		return output;
	}
	
		
	@Override
	public String toString() {
		return "Baþvuru Kurum Model";
	}

	
	
}


