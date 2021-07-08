
import java.sql.*;
import java.util.*;


class BasvuruUcretiModel implements ModelInterface {
	
	@Override
	public ResultSet select(Map<String, Object> whereParameters) throws Exception {
		
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("	Basvuru_No,UcretTalepTarihi,BasvuruUcreti,");
		sql.append("	(CASE WHEN Odendimi=1 THEN 'Ücreti ödendi'"
				+ "				ELSE 'Ücreti ödenmedi'"
				+ "				END)As Odendimi,");
		sql.append(" DATEDIFF(DAY,UcretTalepTarihi,GETDATE()) As fark ");
		sql.append(" FROM BasvuruUcreti ");
		
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
		sql.append(" SET XACT_ABORT ON ");
		sql.append(" begin transaction ");
		sql.append(" INSERT INTO BasvuruUcreti (" + fieldNames + ") " );
		sql.append(" VALUES ");

		String[] fieldList = fieldNames.split(",");

		int rowCount = 0;
		for (int i=0; i<rows.size(); i++) {
			if (rows.get(i) instanceof BasvuruUcreti) {
				rowCount++;
				
				BasvuruUcreti basvuruUcreti = (BasvuruUcreti)rows.get(i); 
	
				sql.append("(");
				for (int j=0; j<fieldList.length; j++) {
					String fieldName = fieldList[j].trim();
					
					sql.append(DatabaseUtilities.formatField(basvuruUcreti.getByName(fieldName)));
					if (j < fieldList.length - 1) {
						sql.append(", ");
					}
				}
				sql.append(")");
				
				//son cevaplama tarihine +15 gün daha eklenmesi
				sql.append(" UPDATE Basvuru SET ");
				sql.append(" BasvuruCevapSonTarihi=dateadd(day, 15, BasvuruCevapSonTarihi) ");
				
				sql.append(" WHERE Basvuru_No="+basvuruUcreti.getByName("Basvuru_No"));				
			}
		}	
		
		//System.out.println(sql.toString());
		
		
		// execute constructed SQL statement
		if (rowCount > 0) {
			sql.append(" commit transaction");
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
		sql.append(" SET XACT_ABORT ON ");
		sql.append(" begin transaction ");
		sql.append(" UPDATE BasvuruUcreti SET ");
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
		
		//son cevaplama tarihine +15 gün daha eklenmesi
		sql.append(" UPDATE Basvuru SET ");
		sql.append(" BasvuruCevapSonTarihi=dateadd(day, 15, BasvuruCevapSonTarihi) ");
		sql.append(" ,BasvuruDurumu_id=2");
		sql.append(" WHERE Basvuru_No="+whereParameters.get("Basvuru_No"));
		System.out.println(sql.toString());
		// execute constructed SQL statement
		sql.append(" commit transaction");
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
		sql.append(" DELETE FROM BasvuruUcreti ");

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
	public String toString() {
		return "Basvuru ücret Model";
	}

	
	
}



