
public class BasvuruKurum {
	
	private int Basvuru_No;
	private int YonlendirilenKurumid;
	BasvuruKurum() {
		
	}
	
	BasvuruKurum(int Basvuru_No,int YonlendirilenKurumid) {
		this.Basvuru_No=Basvuru_No;
		this.YonlendirilenKurumid=YonlendirilenKurumid;
		
	}

	
	
	public Object getByName(String attributeName) {
		switch (attributeName) {
		case "Basvuru_No": return Basvuru_No;
		case "YonlendirilenKurum_id": return YonlendirilenKurumid;
		
		default: return null;
		}
	}
	
	@Override
	public String toString() {
		return "basvuru kurum";
	}

}

