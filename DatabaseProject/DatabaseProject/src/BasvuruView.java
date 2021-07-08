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
		//yetkili kullanýcýnýn login durumunu kontrol ediyor
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
		Integer basvuru_no = getInteger("Baþvuru No giriniz : ", true);
		boolean cevapBekleyenmi=false;
		String adi=null,soyadi=null;
		//yetkili kullanýcý giriþ yaptý ise
		if(authorized)
		{
			adi = getString("Baþvuru sahibinin adý : ", true);
			soyadi = getString("Baþvuru sahibinin soyadý : ", true);
			cevapBekleyenmi = getBoolean("Cevap bekleyen baþvuru mu(Evet->true,Hayýr->false)? : ", true);
		}
			
		Map<String, Object> whereParameters = new HashMap<>();
		
		
		      
		if (basvuru_no != null) whereParameters.put("Basvuru_No", basvuru_no);
		if (adi != null) whereParameters.put("Adi", adi);
		if (soyadi != null) whereParameters.put("Soyadi", soyadi);
		// is null degeri gönderiliyor
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
		//yetkili kullanýcý giriþine göre soru sonekleri deðiþiyor
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
			turkVatandasimi =getBoolean("Turk Vatandasimi" + suffix2 + "(Evet->true,Hayýr->false) : ", true);
			if(turkVatandasimi==true)
			{
				tcKimlikNo=getString("Kimlik Numaranýz : ", true);
			}
			else
			{
				tcKimlikNo=null;
			}
			tuzelKisimi = getBoolean("Tüzel kiþi baþvurusu(Evet->true,Hayýr->false) : ", true);
			if(tuzelKisimi==true)
			{
				tuzelKisiUnvani=getString("Tüzel kiþi ünvanýný giriniz : ", true);
			}
			else
			{
				tuzelKisiUnvani=null;
			}
			telefon=getString("Telefon Numaranýz : ", true);
			telTipi=getString("Telefon tipi (Ev,iþ,mobil) : ", true);
			eposta=getString("E-posta adresiniz : ", true);
			System.out.println("******** Adres bilgisi *******");
			//adres listesi gelecek
			BasvuruModel.selectGeographys();
			geographyid= getInteger("Þehir/Ýlçe/Mahalle seçiniz : ", true);
			adres=getString("Adresinizi giriniz : ", true);
			adresTipi=getString("Adres tipi (Ev,iþ) : ", true);
			if(authorized) {
				System.out.println("******** Baþvuru Tipleri *******");
				BasvuruModel.selectBasvuruTipleri();
				BasvuruTipi_id = getShort("Basvuru tipi seciniz : ", true);
				basvuruTarihi=getDate("Basvuru tarihini giriniz : ", true);
				basvuruCevapSonTarihi=basvuruTarihi.plusDays(15);
			}
                
			aciklama=getString("Açýklamanýz : ", true);
			CevaplamaTipi_id=getInteger("Cevaplama tipi seçiniz(1->Yazýlý,2->eposta : ", true);
			
			if (adi != null && soyadi != null) {
				rows.add(new Basvuru(adi,soyadi,turkVatandasimi,tcKimlikNo,tuzelKisimi,
						tuzelKisiUnvani,adres,adresTipi,
						telefon,telTipi,eposta,aciklama,
						CevaplamaTipi_id,basvuruDurumu_id,basvuruTarihi,
						basvuruCevapSonTarihi,BasvuruTipi_id,geographyid));
			}
					
			cikis=getInteger("Baþka bir baþvuru yapmak istiyormusunuz(Evet->1, Hayýr->0):",true);
		}
		while (cikis!=0);
		
		parameters.put("rows", rows);
		
		return new ViewData("Basvuru", "insert", parameters);
	}
	ViewData updateGUI(ModelData modelData) throws Exception {
		System.out.println("Fields to update:");
		boolean itirazmi=getBoolean("Ýtiraz edilen baþvuru mu(Evet->true,Hayýr->False : ", true);
		System.out.println("******** Baþvuru durum bilgisi *******");
		BasvuruModel.selectBasvuruDrumlari();
		Short basvuruDurumu_id = getShort("Baþvuru Durumu id: ", true);
		System.out.println("******** Red sebebi bilgisi *******");
		BasvuruModel.selectRedSebebleri();
		Short RedSebebi_id = getShort("Red sebebi id: ", true);
		String BasvuruCevabi=getString("Baþvuru cevabý: ", true);
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
		return "/////// Baþvuru Ekraný //////";
	}		
}
