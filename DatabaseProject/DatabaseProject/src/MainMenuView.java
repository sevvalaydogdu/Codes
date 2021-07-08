import java.util.*;

class MainMenuView implements ViewInterface {

	@Override
	public ViewData create(ModelData modelData, String functionName, String operationName) throws Exception {
		System.out.println(this.toString());
		Integer choice;
		do {
			
			System.out.println("1. Baþvuru Sorgula");
			//Bireysel baþvuru
			System.out.println("2. Baþvuru Yap(internet)");
			if(DatabaseUtilities.yetki_id==0)
				System.out.println("3. Login As Authorized User");
			System.out.println("4. Quit");
			if(DatabaseUtilities.yetki_id!=0)
			{
				System.out.println("5. Logout");
				System.out.println("6. Tüm Baþvurular");
				System.out.println("7. Baþvuru Ekle(Posta,Fax,Dilekçe)");
				System.out.println("8. Baþvuru Yönlendir");
				System.out.println("9. Cevap bekleyen baþvurular");
				System.out.println("10. RAPORLAR");
			}
			if(DatabaseUtilities.yetki_id==1)
			{
				System.out.println("11. Baþvuru Güncelle");
				System.out.println("12. Baþvuru Ücret talebi oluþtur");
				System.out.println("13. Baþvuru Kabul edilmesi");
				System.out.println("14. Baþvuru Red edilmesi");
				System.out.println("15. Baþvuru Ücreti Al");
				System.out.println("16. Ücret istenen baþvurular");
				
			}
			
			System.out.println();
			System.out.println(DatabaseUtilities.session);
			choice = getInteger("Enter your choice : ", false);
			//login olmadan yetkili ekranlara ulaþmayý engelliyor
			if(DatabaseUtilities.session==null && choice>4)
			{
				choice=null;
			}
		} 
		while (choice == null || choice < 1 || choice > 16);
		
		
		Map<String, Object> userInput = new HashMap<>();
		userInput.put("mainMenuChoice", choice);
		
		switch (choice.intValue()) {
		
		case 1: functionName="Basvuru";operationName = "select.gui";break;
		//normal basvuru
		case 2: functionName="Basvuru";operationName = "insert.gui";break;
		case 3: functionName="Login";operationName = "select.gui";break;
		case 5: DatabaseUtilities.session=null;DatabaseUtilities.yetki_id=0;break;
		case 6: functionName="Basvuru";operationName = "select";break;
		//yetkili kiþi baþvurusu
		case 7: functionName="Basvuru";operationName = "insert.gui";break;
		case 8: functionName="BasvuruKurum";operationName = "insert.gui";break;
		case 9: functionName="Basvuru";operationName = "select.gui";break;
		case 10: functionName="Rapor";operationName = "rapor.gui";break;
		case 11: functionName="Basvuru";operationName = "update.gui";break;
		case 12: functionName="BasvuruUcreti";operationName = "insert.gui";break;
		case 13: functionName="BasvuruKabul";operationName = "insert.gui";break;
		case 14: functionName="Basvuru";operationName = "update.gui";break;
		case 15: functionName="BasvuruUcreti";operationName = "update.gui";break;
		case 16: functionName="BasvuruUcreti";operationName = "select";break;
		default: return new ViewData(null, null);
		}
		
		return new ViewData(functionName, operationName, new HashMap<>());
	}

	@Override
	public String toString() {
		return "/// Main Menu View ///";
	}
	
	
	
}
