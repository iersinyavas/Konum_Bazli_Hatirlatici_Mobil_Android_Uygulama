package iersinyavas.bm.proje.konumbazlihatirlatici;

import iersinyavas.bm.proje.konumbazlihatirlatici.R;
import java.util.ArrayList;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MenuSayfasi extends Activity{

	Button servisibaslat, servisidurdur, konumekle, kayitlarigetir, konumkontrolservisinibaslat;
	KonumServisi konumservisi;
	TextView textView; 
	BroadcastReceiver broadcastReceiver;
    static String S_enlem, S_boylam, S_yukseklik;
	static double enlem=0.0; 
	static double boylam=0.0;
	static double yukseklik=0.0;

	ListView lvKayitlar;
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(broadcastReceiver == null)
			broadcastReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					textView.setText(" Enlem       :"+intent.getExtras().get("Enlem")+"\n"+
									 " Boylam     :"+intent.getExtras().get("Boylam")+"\n"+
									 " Yükseklik  :"+intent.getExtras().get("Yukseklik"));
					MenuSayfasi.S_enlem = intent.getExtras().get("Enlem").toString();
					MenuSayfasi.S_boylam = intent.getExtras().get("Boylam").toString();
					MenuSayfasi.S_yukseklik = intent.getExtras().get("Yukseklik").toString();
					MenuSayfasi.enlem = Double.parseDouble(MenuSayfasi.S_enlem);
					MenuSayfasi.boylam = Double.parseDouble(MenuSayfasi.S_boylam);
					MenuSayfasi.yukseklik = Double.parseDouble(MenuSayfasi.S_yukseklik);
				}
			};
			
			registerReceiver(broadcastReceiver, new IntentFilter("konumu_guncelle"));
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(broadcastReceiver != null)
			unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_sayfasi);
		
		servisibaslat = (Button) findViewById(R.id.servisi_baslat);
		servisidurdur = (Button) findViewById(R.id.servisi_durdur);
		konumkontrolservisinibaslat = (Button) findViewById(R.id.konum_kontrol_servisini_baslat);
		textView = (TextView) findViewById(R.id.konum_bilgisi);
		konumekle = (Button) findViewById(R.id.konum_ekle);
		kayitlarigetir = (Button) findViewById(R.id.bKayitlariGetir);
		lvKayitlar = (ListView) findViewById(R.id.lvKayitlar);
		final EditText kullanicimesaji = (EditText) findViewById(R.id.kullanici_mesaji);
		
		servisibaslat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startService(new Intent(getApplicationContext(), KonumServisi.class));
			}
		});
		
		servisidurdur.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopService(new Intent(getApplicationContext(), KonumServisi.class));	
			}
		});
		
		konumkontrolservisinibaslat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startService(new Intent(getApplicationContext(), KonumKontrol.class));
			}
		});
		
		konumekle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					KonumVeritabani konumVeritabani = new KonumVeritabani(MenuSayfasi.this);
					konumVeritabani.BaglantiyiAc();
					konumVeritabani.KonumEkle(enlem, boylam, yukseklik, kullanicimesaji.getText().toString());
					konumVeritabani.BaglantiyiKapat();
				}catch(Exception e){
					Dialog hata = new Dialog(MenuSayfasi.this);
					hata.setTitle("Ekleme Ýþlemi");
					TextView tvHata = new TextView(MenuSayfasi.this);
					tvHata.setText(e.toString());
					hata.setContentView(tvHata);
					hata.show();
				}finally{
					Dialog dialog = new Dialog(MenuSayfasi.this);
					dialog.setTitle("Ekleme Ýþlemi");
					TextView tvSonuc = new TextView(MenuSayfasi.this);
					tvSonuc.setText("Baþarýlý");
					dialog.setContentView(tvSonuc);
					dialog.show();
				}
			}
		});
		
		kayitlarigetir.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				KonumVeritabani konumVeritabani = new KonumVeritabani(MenuSayfasi.this);
				konumVeritabani.BaglantiyiAc();
				ArrayList<String> kayitlar = konumVeritabani.KayitlariListele();
				konumVeritabani.BaglantiyiKapat();

				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MenuSayfasi.this, android.R.layout.simple_list_item_1, android.R.id.text1, kayitlar);
				lvKayitlar.setAdapter(arrayAdapter);
				
			}
		});
	}
}
