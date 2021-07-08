

import java.sql.*;
import java.util.*;


class BasvuruKabulModel implements ModelInterface {
	
	@Override
	public ResultSet select(Map<String, Object> whereParameters) throws Exception {
		
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("	VeriNo,Basvuru_No,");
		sql.append("	(CASE WHEN Bilgimi=1 THEN 'Bilgi'"
				+ "				ELSE 'Veri'"
				+ "				END)As Bilgimi,Veri");
		
		sql.append(" FROM BasvuruBilgiBelge ");
		
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
		sql.append(" INSERT INTO BasvuruBilgiBelge (" + fieldNames + ") " );
		sql.append(" VALUES ");

		String[] fieldList = fieldNames.split(",");

		int rowCount = 0;
		for (int i=0; i<rows.size(); i++) {
			if (rows.get(i) instanceof BasvuruKabul) {
				rowCount++;
				
				BasvuruKabul basvuruKabul = (BasvuruKabul)rows.get(i); 
	
				sql.append("(");
				for (int j=0; j<fieldList.length; j++) {
					String fieldName = fieldList[j].trim();
					
					sql.append(DatabaseUtilities.formatField(basvuruKabul.getByName(fieldName)));
					if (j < fieldList.length - 1) {
						sql.append(", ");
					}
				}
				sql.append(")");
				
				//baþvurunun güncellenmesi
				sql.append(" UPDATE Basvuru SET ");
				sql.append(" BasvuruDurumu_id=3 ");
				if(basvuruKabul.getByName("Veri")!=null)
				{
					sql.append(" ,BasvuruCevabi='Kabul edildi,Veri Adresi:"+
							basvuruKabul.getByName("Veri")+"'");
				}
				else
				{
					sql.append(" ,BasvuruCevabi='Kabul edildi'");
				}
					
				sql.append(" WHERE Basvuru_No="+basvuruKabul.getByName("Basvuru_No"));				
			}
		}	
		
		System.out.println(sql.toString());
		
		
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
				sql.append(" UPDATE BasvuruBilgiBelge SET ");
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
		sql.append(" DELETE FROM BasvuruBilgiBelge ");

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
		return "Basvuru kabul Model";
	}

	
	
}




