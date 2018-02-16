package iersinyavas.bm.proje.konumbazlihatirlatici;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class KonumKontrol extends Service{

	
	BroadcastReceiver broadcastReceiver;
	String mesaj;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(getApplicationContext(), "KonumKontrol Servisi Baþlatýldý...", Toast.LENGTH_LONG).show();
		
		if(broadcastReceiver == null)
			broadcastReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					
					double enlem = Double.parseDouble(intent.getExtras().get("Enlem").toString());
					double boylam = Double.parseDouble(intent.getExtras().get("Boylam").toString());
					double yukseklik = Double.parseDouble(intent.getExtras().get("Yukseklik").toString());
					
					KonumVeritabani konumVeritabani = new KonumVeritabani(KonumKontrol.this);	
					konumVeritabani.BaglantiyiAc();
					mesaj = konumVeritabani.MesajGetir(enlem, boylam, yukseklik);
					konumVeritabani.BaglantiyiKapat();

					if(mesaj != null)
					{	
						Thread thread = new Thread(new Runnable() {
							
							Intent alarmintenti = new Intent();
							@Override
							public void run() {
								alarmintenti = new Intent(getBaseContext(), AlarmSayfasi.class);
								alarmintenti.putExtra("Mesaj", mesaj);
								alarmintenti.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								getApplication().startActivity(alarmintenti);
								stopService(new Intent(getApplicationContext(), KonumKontrol.class));	
							}
						});
						thread.run();			
					}
				}
			};
			
			registerReceiver(broadcastReceiver, new IntentFilter("konumu_guncelle"));	
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(broadcastReceiver != null)
			unregisterReceiver(broadcastReceiver);
		
		Toast.makeText(getApplicationContext(), "KonumKontrol Servisi Durduruldu...", Toast.LENGTH_LONG).show();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
