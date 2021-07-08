import java.time.LocalDate;
import java.util.Date;

public class BasvuruUcreti {
	
	private int Basvuru_No;
	private LocalDate UcretTalepTarihi;
	private double BasvuruUcreti;
	private boolean Odendimi;
	
	
	
	BasvuruUcreti() {
		
	}
	
	BasvuruUcreti(int Basvuru_No,LocalDate UcretTalepTarihi,double BasvuruUcreti,
			boolean Odendimi) {
		this.Basvuru_No=Basvuru_No;
		this.UcretTalepTarihi=UcretTalepTarihi;
		this.BasvuruUcreti=BasvuruUcreti;
		this.Odendimi=Odendimi;
	
	}

	
	
	public Object getByName(String attributeName) {
		switch (attributeName) {
		case "Basvuru_No": return Basvuru_No;
		case "UcretTalepTarihi": return UcretTalepTarihi;
		case "BasvuruUcreti": return BasvuruUcreti;
		case "Odendimi": return Odendimi;
		
		default: return null;
		}
	}
	
	@Override
	public String toString() {
		return "Basvuru_No";
	}

}

