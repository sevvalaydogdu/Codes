//Raporlama iþlemi yapýlýr
class RaporView implements ViewInterface {

	@Override
	public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
		
		
		switch(operationName) {
		
		case "rapor.gui": return raporGUI(modelData);
		}
		
		return new ViewData("MainMenu", "");
	}
	
	
	ViewData raporGUI(ModelData modelData) throws Exception {
		Integer choice=0;
		
		System.out.println("1. Yapýlan bilgi edinme baþvurularýnýn sayýsýný göster");
		System.out.println("2. Olumlu sonuçlanan baþvuru sayýsý");
		System.out.println("3. Reddedilen baþvuru sayýsý ve daðýlýmý");
		System.out.println("4. Bilgi ve belgelere eriþim saðlanan baþvuru sayýsý");
		System.out.println("5. itiraz edilen baþvuru sayýsý ile bunlarýn sonuçlarýný");
		System.out.println("6. Back to Main Menü");
		choice= getInteger("Seçiminiz : ", true);
		switch (choice) {
		
		case 1: 
			System.out.println(RaporModel.selectBasvurular(1));
			break;
		case 2: 
			System.out.println(RaporModel.selectBasvurular(2));
			break;
		case 3:
			System.out.println(RaporModel.selectBasvurular(3));
			break;
		case 4:
			System.out.println(RaporModel.selectBasvurular(4));
			break;
		case 5:
			System.out.println(RaporModel.selectBasvurular(5));
			break;
		case 6:
			return new ViewData("MainMenu", "");
			
		
		}
		return new ViewData("Rapor", "rapor.gui");
	}
	
	@Override
	public String toString() {
		return "Rapor Ekraný";
	}		
}

