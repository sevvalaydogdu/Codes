

public class BasvuruKabul {
	private int VeriNo;
	private int Basvuru_No;
	private String Veri;
	private boolean Bilgimi;
	
	
	
	BasvuruKabul() {
		
	}
	
	BasvuruKabul(int Basvuru_No,String Veri,boolean Bilgimi) {
		
		this.Basvuru_No=Basvuru_No;
		this.Veri=Veri;
		
		this.Bilgimi=Bilgimi;
	
	}

	
	
	public Object getByName(String attributeName) {
		switch (attributeName) {
		case "Basvuru_No": return Basvuru_No;
		case "VeriNo": return VeriNo;
		case "Veri": return Veri;
		case "Bilgimi": return Bilgimi;
		
		default: return null;
		}
	}
	
	@Override
	public String toString() {
		return "Basvuru_No";
	}

}


