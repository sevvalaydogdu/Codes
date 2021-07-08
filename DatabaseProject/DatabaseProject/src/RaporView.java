//Raporlama i�lemi yap�l�r
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
		
		System.out.println("1. Yap�lan bilgi edinme ba�vurular�n�n say�s�n� g�ster");
		System.out.println("2. Olumlu sonu�lanan ba�vuru say�s�");
		System.out.println("3. Reddedilen ba�vuru say�s� ve da��l�m�");
		System.out.println("4. Bilgi ve belgelere eri�im sa�lanan ba�vuru say�s�");
		System.out.println("5. itiraz edilen ba�vuru say�s� ile bunlar�n sonu�lar�n�");
		System.out.println("6. Back to Main Men�");
		choice= getInteger("Se�iminiz : ", true);
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
		return "Rapor Ekran�";
	}		
}

