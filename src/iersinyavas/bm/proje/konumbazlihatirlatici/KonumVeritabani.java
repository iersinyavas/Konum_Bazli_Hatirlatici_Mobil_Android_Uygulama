package iersinyavas.bm.proje.konumbazlihatirlatici;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KonumVeritabani{
	
	private static final String VERITABANI_ISMI = "KonumVeritabani";
	private static final String VERITABANI_TABLO = "KonumMesaj";
	private static final int VERITABANI_VERSIYON = 2;
	
	public static final String KONUM_ID = "KonumId";
	public static final String ENLEM = "Enlem";
	public static final String BOYLAM = "Boylam";
	public static final String YUKSEKLIK = "Yukseklik";
	public static final String KULLANICI_MESAJI = "KullaniciMesaji";
	
	private final Context context;
	private VeritabaniHelper veritabaniHelper;
	private SQLiteDatabase konumDatabase;
	public static int kayitsayisi = 0;
	
	public KonumVeritabani(Context context){
		this.context = context;
	}
	
	public KonumVeritabani BaglantiyiAc() throws SQLException{
		veritabaniHelper = new VeritabaniHelper(context);
		konumDatabase = veritabaniHelper.getWritableDatabase();
		return this;
	}
	
	public KonumVeritabani OkunabilirBaglantiyiAc() throws SQLException{
		veritabaniHelper = new VeritabaniHelper(context);
		konumDatabase = veritabaniHelper.getReadableDatabase();
		return this;
	}
	
	public void BaglantiyiKapat(){
		veritabaniHelper.close();
	}
	
	
	private static class VeritabaniHelper extends SQLiteOpenHelper{

		public VeritabaniHelper(Context context) {
			super(context, VERITABANI_ISMI, null, VERITABANI_VERSIYON);

		}
		
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "+VERITABANI_TABLO+"("+KONUM_ID+" INTEGER PRIMARY_KEY AUTO INCREMENT, "
														   +ENLEM+" DOUBLE NOT NULL, "
														   +BOYLAM+" DOUBLE NOT NULL, "
														   +YUKSEKLIK+" DOUBLE NOT NULL, "
														   +KULLANICI_MESAJI+" TEXT NOT NULL)");
			
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+VERITABANI_TABLO);
			onCreate(db);
		}
	}
	
	public void KonumEkle(double enlem, double boylam, double yukseklik, String kullanicimesaji)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(ENLEM, enlem);
		contentValues.put(BOYLAM, boylam);
		contentValues.put(YUKSEKLIK, yukseklik);
		contentValues.put(KULLANICI_MESAJI, kullanicimesaji);
		konumDatabase.insert(VERITABANI_TABLO, null, contentValues);
	}
	
	public ArrayList<String> KayitlariListele()
	{
		ArrayList<String> kayitlar = new ArrayList<String>();
		String[] sutunlar = new String[]{ENLEM, BOYLAM, YUKSEKLIK, KULLANICI_MESAJI};
		Cursor cursor = konumDatabase.query(VERITABANI_TABLO, sutunlar, null, null, null, null, null);
		while(cursor.moveToNext())
		{
			kayitlar.add("Enlem: "+cursor.getDouble(0)+"\n"+
						 "Boylam: "+cursor.getDouble(1)+"\n"+
						 "Yükseklik: "+cursor.getDouble(2)+"\n"+
						 "Kullanýcý Mesajý: "+cursor.getString(3));
		}
		return kayitlar;	
	}
	
	
	public String MesajGetir(double enlem, double boylam, double yukseklik)
	{
		//Buradaki deðerler kapsama alanýný belirler 
		double enlemP = enlem + 0.0001;
		double enlemN = enlem - 0.0001;
		double boylamP = boylam + 0.005;
		double boylamN = boylam - 0.005;
		double yukseklikP = yukseklik + 150.0;
		double yukseklikN = yukseklik - 150.0;

		String sql = "select KullaniciMesaji from KonumMesaj where (Enlem<"+enlemP+" and Enlem>"+enlemN+") and (Boylam<"+boylamP+" and Boylam>"+boylamN+") and "
																+ "(Yukseklik<"+yukseklikP+" and Yukseklik>"+yukseklikN+")";
		Cursor cursor = konumDatabase.rawQuery(sql, null);
		
		if(cursor.moveToFirst())
		{		
			String mesaj = cursor.getString(cursor.getColumnIndex("KullaniciMesaji"));
			kayitsayisi = cursor.getCount();
			return mesaj;
		}
		else 
		{	
			kayitsayisi = 0; 
			return null;
		}
	}
	
	public void VeriSil(String mesaj)
	{	
		konumDatabase.delete(VERITABANI_TABLO, "KullaniciMesaji='"+mesaj+"'", null);
	}
}




