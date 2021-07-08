
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class BasvuruUcretiView implements ViewInterface {

	@Override
	public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
		System.out.println(this.toString());
		switch(operationName) {
		case "select": return selectOperation(modelData);	
		case "insert": return insertOperation(modelData);	
		case "update": return updateOperation(modelData);	
		
		case "select.gui": return selectGUI(modelData);
		case "insert.gui": return insertGUI(modelData);
		case "update.gui": return updateGUI(modelData);
		
		}
		return new ViewData("MainMenu", "");
	}
	
	
	ViewData selectOperation(ModelData modelData) throws Exception {
		ResultSet resultSet = modelData.resultSet;
		
		if (resultSet != null) {
			while (resultSet.next()) {
				// Retrieve by column name
				int Basvuru_No = resultSet.getInt("Basvuru_No");
				Date ucretTalepTarihi = resultSet.getDate("UcretTalepTarihi");
				String basvuruUcreti = resultSet.getString("BasvuruUcreti")+" TL";
				String odendimi = resultSet.getString("Odendimi");
				String fark = resultSet.getString("fark")+" Gün";
				// Display values
				
				System.out.print(Basvuru_No + "\t");
				System.out.print(ucretTalepTarihi + "\t");
				System.out.print(basvuruUcreti + "\t");
				System.out.print(odendimi + "\t");
				System.out.print(fark + "\t");
				System.out.println();
			}
			resultSet.close();	
		}
		
		return new ViewData("MainMenu", "");
	}
	
	ViewData insertOperation(ModelData modelData) throws Exception {
		System.out.println("Number of inserted rows is " + modelData.recordCount);
		
		return new ViewData("MainMenu", "");
	}

	ViewData updateOperation(ModelData modelData) throws Exception {
		System.out.println("Number of updated rows is " + modelData.recordCount);
		
		return new ViewData("MainMenu", "");
	}
	
	Map<String, Object> getWhereParameters() throws Exception {
		System.out.println("Filter conditions:");
		int Basvuru_No = getInteger("Baþvuru no : ", true);
		
		Map<String, Object> whereParameters = new HashMap<>();
		if (Basvuru_No != 0 ) whereParameters.put("Basvuru_No", Basvuru_No);
		
		return whereParameters;
	}
	
	ViewData selectGUI(ModelData modelData) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("whereParameters", getWhereParameters());
		
		return new ViewData("BasvuruUcreti", "select", parameters);
	}

	ViewData insertGUI(ModelData modelData) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("fieldNames", "Basvuru_No,UcretTalepTarihi,BasvuruUcreti,"
				+ "Odendimi");
		
		List<Object> rows = new ArrayList<>();
		int cikis=1;
		LocalDate ucretTalepTarihi=LocalDate.now();
		double basvuruUcreti;
		boolean odendimi=false;
		int Basvuru_No=0;
		
		do
		{
			
			Basvuru_No = getInteger("Baþvuru no : ", true);
			basvuruUcreti = getDouble("Baþvuru ücreti : ", true);
			//odendimi = getBoolean("Ödendimi : ", true);
			if (Basvuru_No != 0) {
				rows.add(new BasvuruUcreti(Basvuru_No,ucretTalepTarihi,basvuruUcreti,odendimi));
			}
					
			cikis=getInteger("Baþka bir iþlem yapmak istiyormusunuz(Evet->1, Hayýr->0):",true);
		}
		while (cikis!=0);
		
		parameters.put("rows", rows);
		
		return new ViewData("BasvuruUcreti", "insert", parameters);
	}
	ViewData updateGUI(ModelData modelData) throws Exception {
		System.out.println("Fields to update:");
		boolean odendimi = getBoolean("Ödendimi (Evet->true) : ", true);
		System.out.println();
		
		Map<String, Object> updateParameters = new HashMap<>();
		if (odendimi) updateParameters.put("Odendimi", odendimi);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("updateParameters", updateParameters);
		parameters.put("whereParameters", getWhereParameters());
		
		return new ViewData("BasvuruUcreti", "update", parameters);
	}
	
	@Override
	public String toString() {
		return "Baþvuru Ücret Ekraný";
	}		
}
