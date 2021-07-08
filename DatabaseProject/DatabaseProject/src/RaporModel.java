import java.sql.*;
import java.util.*;


class RaporModel implements ModelInterface{
	
	
	
	public static String selectBasvurular(int whereType) throws Exception {
		
		String output="";
		// construct SQL statement
		StringBuilder sql = new StringBuilder();
		switch (whereType) {
		
		case 1: 
			output="Ba�vuru say�s�:";
			sql.append(" SELECT ");
			sql.append(" Count(*) As BasvuruSayisi ");
			sql.append(" FROM Basvuru ");
			break;
		case 2: 
			output="Olumlu Sonu�lanan Ba�vuru say�s�:";
			sql.append(" SELECT ");
			sql.append(" Count(*) As BasvuruSayisi ");
			sql.append(" FROM Basvuru ");
			sql.append(" WHERE BasvuruDurumu_id = 3 ");
			break;
		case 3:
			output="/// Reddedilen ba�vuru say�s� ve da��l�m� //\n";
			sql.append(" SELECT ");
			sql.append(" RedSebebi.Aciklama,Count(*) As BasvuruSayisi ");
			sql.append(" FROM Basvuru INNER JOIN RedSebebi on RedSebebi.RedSebebi_id=Basvuru.RedSebebi_id ");
			sql.append(" WHERE BasvuruDurumu_id = 4  ");
			sql.append(" Group by RedSebebi.Aciklama ");
			break;
		case 4:
			output="Bilgi ve belgelere eri�im sa�lanan ba�vuru say�s�:";
			sql.append(" SELECT ");
			sql.append(" Count(*) As BasvuruSayisi ");
			sql.append(" FROM Basvuru ");
			sql.append(" WHERE BasvuruDurumu_id = 3 ");
			break;
		case 5:
			output="�tiraz edilen Ba�vuru say�s�:";
			sql.append(" SELECT ");
			sql.append(" Count(*) As BasvuruSayisi ");
			sql.append(" FROM Basvuru ");
			sql.append(" WHERE Itirazmi = 1 ");
			break;
		
		}
			try {
				// execute constructed SQL statement
				Connection connection = DatabaseUtilities.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
				//DatabaseUtilities.setWhereStatementParameters(preparedStatement, null);
				ResultSet result = preparedStatement.executeQuery();
			
			
				if (result != null) {
					while (result.next()) {
						// Retrieve by column name
						int count = result.getInt("BasvuruSayisi");
						if(whereType==3)
						{
							String aciklama = result.getString("Aciklama");
							output+=aciklama+"\t";
						}
							
						output+=count;
					}
					result.close();	
					}
				}
			catch (Exception e) {
		    	
				System.out.println( "Failed to insert SQL query: ( " + e.toString() + ")");
		        
		    }
				
		return output;
	}
	@Override
	public String toString() {
		return "Rapor Model";
	}
	@Override
	public ResultSet select(Map<String, Object> whereParameters) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int insert(String fieldNames, List<Object> rows) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int update(Map<String, Object> updateParameters, Map<String, Object> whereParameters) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int delete(Map<String, Object> whereParameters) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}

