import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class BasvuruView implements ViewInterface {
	
	public static boolean authorized = false;
	@Override
	public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
		//yetkili kullan�c�n�n login durumunu kontrol ediyor
		if(DatabaseUtilities.yetki_id!=0)
		{
			authorized=true;
		}
		else
		{
			authorized=false;
		}
			
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
			// kolon isimleri
			
			while (resultSet.next()) {
				// Retrieve by column name
				int basvuru_no = resultSet.getInt("Basvuru_No");
				String basvuruTipi = resultSet.getString("BasvuruTipi");
				String basvuruCevabi = resultSet.getString("BasvuruCevabi");
				String uyruk = resultSet.getString("Uyruk");
				String TCKimlikNo = resultSet.getString("TCKimlikNo");
				String ad = resultSet.getString("Adi");
				String soyad = resultSet.getString("Soyadi");
				String tuzelKisiUnvani = resultSet.getString("TuzelKisiUnvani");
				String durumu = resultSet.getString("Durumu");
				String cevaplama_Tipi = resultSet.getString("Cevaplama_Tipi");
				
				Date basvuruTarihi = resultSet.getDate("Basvuru_Tarihi");
				Date basvuruCevapTarihi = resultSet.getDate("BasvuruCevapSonTarihi");
				// Display values
				
				
				System.out.print(basvuru_no + "\t");
				System.out.print(basvuruCevabi + "\t");
				System.out.print(basvuruTipi + "\t");
				System.out.print(uyruk + "\t");
				System.out.print(TCKimlikNo + "\t");
				System.out.print(ad + "\t");
				System.out.print(soyad + "\t");
				System.out.print(tuzelKisiUnvani + "\t");
				System.out.print(durumu + "\t");
				System.out.print(cevaplama_Tipi + "\t");
				System.out.print(basvuruTarihi + "\t");
				System.out.print(basvuruCevapTarihi + "\t");
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
	
	
	
	Map<String, Object> getWhereParameters() throws Exception {
		System.out.println("Filter conditions:");
		Integer basvuru_no = getInteger("Ba�vuru No giriniz : ", true);
		boolean cevapBekleyenmi=false;
		String adi=null,soyadi=null;
		//yetkili kullan�c� giri� yapt� ise
		if(authorized)
		{
			adi = getString("Ba�vuru sahibinin ad� : ", true);
			soyadi = getString("Ba�vuru sahibinin soyad� : ", true);
			cevapBekleyenmi = getBoolean("Cevap bekleyen ba�vuru mu(Evet->true,Hay�r->false)? : ", true);
		}
			
		Map<String, Object> whereParameters = new HashMap<>();
		
		
		      
		if (basvuru_no != null) whereParameters.put("Basvuru_No", basvuru_no);
		if (adi != null) whereParameters.put("Adi", adi);
		if (soyadi != null) whereParameters.put("Soyadi", soyadi);
		// is null degeri g�nderiliyor
		if (cevapBekleyenmi)whereParameters.put("BasvuruCevabi is NULL", null);
		
		return whereParameters;
	}
	
	ViewData selectGUI(ModelData modelData) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("whereParameters", getWhereParameters());
		
		return new ViewData("Basvuru", "select", parameters);
	}
	
	ViewData insertGUI(ModelData modelData) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("fieldNames", "Adi,Soyadi,TurkVatandasimi,TCKimlikNo,"
				+ "TuzelKisimi,TuzelKisiUnvani,Adres,Adres_tipi,"
				+ "Telefon,TelefonTipi,Eposta,Aciklama,"
				+ "CevaplamaTipi_id,BasvuruDurumu_id,Basvuru_Tarihi,"
				+ "BasvuruCevapSonTarihi,BasvuruTipi_id,AdresGeographyID");
		//yetkili kullan�c� giri�ine g�re soru sonekleri de�i�iyor
		 String suffix1 = "";
	     String suffix2 = "";
	     String suffix3 = "";
	        
		List<Object> rows = new ArrayList<>();
		int cikis=1;
		LocalDate basvuruTarihi=LocalDate.now();
		LocalDate basvuruCevapSonTarihi=LocalDate.now().plusDays(15);
	
		boolean turkVatandasimi,tuzelKisimi;
		short BasvuruTipi_id=1;
		String adi,soyadi,tcKimlikNo,
					tuzelKisiUnvani,adres,adresTipi,
					telefon,telTipi,eposta,aciklama;
		int geographyid=0,CevaplamaTipi_id=1,basvuruDurumu_id=1;
		
		 if(!authorized) {
	            suffix1 = "niz";
	            suffix2 = "siniz";
	            suffix3 = "iniz";
	      }
		do
		{
			System.out.println("Kisisel bilgileri" + suffix1 + "i giriniz:");
			adi = getString("Adi" + suffix1 + " : ", true);
			soyadi = getString("Soyadi" + suffix1 + " : ", true);
			turkVatandasimi =getBoolean("Turk Vatandasimi" + suffix2 + "(Evet->true,Hay�r->false) : ", true);
			if(turkVatandasimi==true)
			{
				tcKimlikNo=getString("Kimlik Numaran�z : ", true);
			}
			else
			{
				tcKimlikNo=null;
			}
			tuzelKisimi = getBoolean("T�zel ki�i ba�vurusu(Evet->true,Hay�r->false) : ", true);
			if(tuzelKisimi==true)
			{
				tuzelKisiUnvani=getString("T�zel ki�i �nvan�n� giriniz : ", true);
			}
			else
			{
				tuzelKisiUnvani=null;
			}
			telefon=getString("Telefon Numaran�z : ", true);
			telTipi=getString("Telefon tipi (Ev,i�,mobil) : ", true);
			eposta=getString("E-posta adresiniz : ", true);
			System.out.println("******** Adres bilgisi *******");
			//adres listesi gelecek
			BasvuruModel.selectGeographys();
			geographyid= getInteger("�ehir/�l�e/Mahalle se�iniz : ", true);
			adres=getString("Adresinizi giriniz : ", true);
			adresTipi=getString("Adres tipi (Ev,i�) : ", true);
			if(authorized) {
				System.out.println("******** Ba�vuru Tipleri *******");
				BasvuruModel.selectBasvuruTipleri();
				BasvuruTipi_id = getShort("Basvuru tipi seciniz : ", true);
				basvuruTarihi=getDate("Basvuru tarihini giriniz : ", true);
				basvuruCevapSonTarihi=basvuruTarihi.plusDays(15);
			}
                
			aciklama=getString("A��klaman�z : ", true);
			CevaplamaTipi_id=getInteger("Cevaplama tipi se�iniz(1->Yaz�l�,2->eposta : ", true);
			
			if (adi != null && soyadi != null) {
				rows.add(new Basvuru(adi,soyadi,turkVatandasimi,tcKimlikNo,tuzelKisimi,
						tuzelKisiUnvani,adres,adresTipi,
						telefon,telTipi,eposta,aciklama,
						CevaplamaTipi_id,basvuruDurumu_id,basvuruTarihi,
						basvuruCevapSonTarihi,BasvuruTipi_id,geographyid));
			}
					
			cikis=getInteger("Ba�ka bir ba�vuru yapmak istiyormusunuz(Evet->1, Hay�r->0):",true);
		}
		while (cikis!=0);
		
		parameters.put("rows", rows);
		
		return new ViewData("Basvuru", "insert", parameters);
	}
	ViewData updateGUI(ModelData modelData) throws Exception {
		System.out.println("Fields to update:");
		boolean itirazmi=getBoolean("�tiraz edilen ba�vuru mu(Evet->true,Hay�r->False : ", true);
		System.out.println("******** Ba�vuru durum bilgisi *******");
		BasvuruModel.selectBasvuruDrumlari();
		Short basvuruDurumu_id = getShort("Ba�vuru Durumu id: ", true);
		System.out.println("******** Red sebebi bilgisi *******");
		BasvuruModel.selectRedSebebleri();
		Short RedSebebi_id = getShort("Red sebebi id: ", true);
		String BasvuruCevabi=getString("Ba�vuru cevab�: ", true);
		if(RedSebebi_id!=null)
			BasvuruCevabi+=" Red Sebebi:"+BasvuruModel.selectRedSebebi(RedSebebi_id);
		System.out.println();
		
		Map<String, Object> updateParameters = new HashMap<>();
		if (basvuruDurumu_id != null) updateParameters.put("BasvuruDurumu_id",basvuruDurumu_id);
		if (RedSebebi_id != null) updateParameters.put("RedSebebi_id",RedSebebi_id);
		if (BasvuruCevabi != null) updateParameters.put("BasvuruCevabi", BasvuruCevabi);
		if (itirazmi) updateParameters.put("Itirazmi", itirazmi);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("updateParameters", updateParameters);
		parameters.put("whereParameters", getWhereParameters());
		
		return new ViewData("Basvuru", "update", parameters);
	}
	
	@Override
	public String toString() {
		return "/////// Ba�vuru Ekran� //////";
	}		
}
