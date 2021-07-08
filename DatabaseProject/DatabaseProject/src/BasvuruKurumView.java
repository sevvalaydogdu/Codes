import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class BasvuruKurumView implements ViewInterface {
	
	
	@Override
	public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
		
		System.out.println(this.toString());
		switch(operationName) {
		case "select": return selectOperation(modelData);	
		case "insert": return insertOperation(modelData);	
		case "update": return updateOperation(modelData);	
		case "delete": return deleteOperation(modelData);
		case "select.gui": return selectGUI(modelData);
		case "insert.gui": return insertGUI(modelData);
		case "update.gui": return updateGUI(modelData);
		
		}
		
		return new ViewData("MainMenu", "");
	}
	
	ViewData selectOperation(ModelData modelData) throws Exception {
		ResultSet resultSet = modelData.resultSet;
		
		if (resultSet != null) {
			// kolon isimleri
			System.out.print("No\t Baþvuru Tipi Uyruk\tT.C. Kimlik No\t"
					+ "Adý\tSoyadý\t"
					+ "Tüzel Kiþi Unvaný\tDurumu\tCevaplama Tipi\tTarihi\n");
			while (resultSet.next()) {
				// Retrieve by column name
				int basvuru_no = resultSet.getInt("Basvuru_No");
				String basvuruTipi = resultSet.getString("BasvuruTipi");
				String uyruk = resultSet.getString("Uyruk");
				String TCKimlikNo = resultSet.getString("TCKimlikNo");
				String ad = resultSet.getString("Adi");
				String soyad = resultSet.getString("Soyadi");
				String tuzelKisiUnvani = resultSet.getString("TuzelKisiUnvani");
				String durumu = resultSet.getString("Durumu");
				String cevaplama_Tipi = resultSet.getString("Cevaplama_Tipi");
				
				Date basvuruTarihi = resultSet.getDate("Basvuru_Tarihi");
				// Display values
				
				
				System.out.print(basvuru_no + "\t");
				System.out.print(basvuruTipi + "\t");
				System.out.print(uyruk + "\t");
				System.out.print(TCKimlikNo + "\t");
				System.out.print(ad + "\t");
				System.out.print(soyad + "\t");
				System.out.print(tuzelKisiUnvani + "\t");
				System.out.print(durumu + "\t");
				System.out.print(cevaplama_Tipi + "\t");
				System.out.print(basvuruTarihi + "\t");
				System.out.println("\n");
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
	
	ViewData deleteOperation(ModelData modelData) throws Exception {
		System.out.println("Number of deleted rows is " + modelData.recordCount);
		
		return new ViewData("MainMenu", "");
	}	
	
	Map<String, Object> getWhereParameters() throws Exception {
		System.out.println("Filter conditions:");
		Integer basvuru_no = getInteger("Baþvuru No giriniz : ", true);
		
		Map<String, Object> whereParameters = new HashMap<>();
		if (basvuru_no != null) whereParameters.put("Basvuru_No", basvuru_no);
		
		return whereParameters;
	}
	
	ViewData selectGUI(ModelData modelData) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("whereParameters", getWhereParameters());
		
		return new ViewData("Basvuru", "select", parameters);
	}
	
	ViewData insertGUI(ModelData modelData) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("fieldNames", "Basvuru_No,YonlendirilenKurum_id");
		
		List<Object> rows = new ArrayList<>();
		int cikis=1;
		
		int Basvuru_No=0,YonlendirilenKurum_id=0;
		
		do
		{
			
			Basvuru_No = getInteger("Baþvuru no : ", true);
			System.out.println("******** Kurum bilgisi *******");
			//kurum listesi gelecek
			BasvuruKurumModel.selectKurums();
			YonlendirilenKurum_id = getInteger("Yönlendirilecek kurum no : ", true);
			
			if (Basvuru_No != 0 && YonlendirilenKurum_id != 0) {
				rows.add(new BasvuruKurum(Basvuru_No,YonlendirilenKurum_id));
			}
					
			cikis=getInteger("Baþka bir yönlendirme yapmak istiyormusunuz(Evet->1, Hayýr->0):",true);
		}
		while (cikis!=0);
		
		parameters.put("rows", rows);
		
		return new ViewData("BasvuruKurum", "insert", parameters);
	}
	ViewData updateGUI(ModelData modelData) throws Exception {
		System.out.println("Fields to update:");
		String name = getString("Name : ", true);
		String groupName = getString("Group Name : ", true);
		System.out.println();
		
		Map<String, Object> updateParameters = new HashMap<>();
		if (name != null) updateParameters.put("Name", name);
		if (groupName != null) updateParameters.put("GroupName", groupName);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("updateParameters", updateParameters);
		parameters.put("whereParameters", getWhereParameters());
		
		return new ViewData("Department", "update", parameters);
	}
	ViewData deleteGUI(ModelData modelData) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("whereParameters", getWhereParameters());
		
		return new ViewData("Department", "delete", parameters);
	}
	
	@Override
	public String toString() {
		return "////// Baþvuru Kurum Yönlendirme Ekraný /////";
	}		
}
