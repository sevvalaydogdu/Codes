
import java.time.LocalDate;
import java.util.Date;

public class Basvuru {
	
	//private int Baþvuru_No;
	private String adi;
	private String soyadi;
	private boolean turkVatandasimi;
	private Long tcKimlikNo;
	private boolean tuzelKisimi;
	private String tuzelKisiUnvani;
	private String adres;
	private String adresTipi;
	private String telefon;
	private String telTipi;
	private String eposta;
	private short BasvuruDurumu_id;
	private LocalDate basvuruTarihi;
	private LocalDate basvuruCevapSonTarihi;
	private String Aciklama;	
	private short CevaplamaTipi_id;
	private short BasvuruTipi_id;
	private int AdresGeographyID;
	Basvuru() {
		
	}
	
	Basvuru(String adi,String soyadi,boolean turkVatandasimi,String tcKimlikNo,boolean tuzelKisimi,
			String tuzelKisiUnvani,String adres,String adresTipi,
			String telefon,String telTipi,String eposta,String aciklama,
			int CevaplamaTipi_id,int basvuruDurumu_id,LocalDate basvuruTarihi,
			LocalDate basvuruCevapSonTarihi,short BasvuruTipi_id,int AdresGeographyID) {
		this.adi=adi;
		this.soyadi=soyadi;
		this.turkVatandasimi=turkVatandasimi;
		if(tcKimlikNo!=null)
		{
			this.tcKimlikNo=Long.parseLong(tcKimlikNo);
		}
		
		this.tuzelKisimi=tuzelKisimi;
		this.tuzelKisiUnvani=tuzelKisiUnvani;
		this.adres=adres;
		this.adresTipi=adresTipi;
		this.telefon=telefon;
		this.telTipi=telTipi;
		this.eposta=eposta;
		this.BasvuruDurumu_id=(short)basvuruDurumu_id;
		this.basvuruTarihi=LocalDate.now();
		this.basvuruCevapSonTarihi=LocalDate.now().plusDays(15);
		this.basvuruTarihi=basvuruTarihi;
		this.basvuruCevapSonTarihi=basvuruCevapSonTarihi;
		this.Aciklama=aciklama;	
		this.CevaplamaTipi_id=(short)CevaplamaTipi_id;
		this.BasvuruTipi_id=BasvuruTipi_id;
		this.AdresGeographyID=AdresGeographyID;
	}

	
	
	public Object getByName(String attributeName) {
		switch (attributeName) {
		case "Adi": return adi;
		case "Soyadi": return soyadi;
		case "TurkVatandasimi": return turkVatandasimi;
		case "TCKimlikNo": return tcKimlikNo;
		case "TuzelKisimi": return tuzelKisimi;
		case "TuzelKisiUnvani": return tuzelKisiUnvani;
		case "Adres": return adres;
		case "Adres_tipi": return adresTipi;
		case "Telefon": return telefon;
		case "TelefonTipi": return telTipi;
		case "Eposta": return eposta;
		case "BasvuruDurumu_id": return BasvuruDurumu_id;
		case "Basvuru_Tarihi": return basvuruTarihi;
		case "BasvuruCevapSonTarihi": return basvuruCevapSonTarihi;
		case "Aciklama": return Aciklama;
		case "CevaplamaTipi_id": return CevaplamaTipi_id;
		case "BasvuruTipi_id": return BasvuruTipi_id;
		case "AdresGeographyID": return AdresGeographyID;
		default: return null;
		}
	}
	
	@Override
	public String toString() {
		return turkVatandasimi + ", " + Aciklama + ", "+ basvuruTarihi + ", ";
	}

}
