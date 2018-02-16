package iersinyavas.bm.proje.konumbazlihatirlatici;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class KonumServisi extends Service{

	LocationManager locationManager;
	LocationListener locationListener;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		
		Toast.makeText(getApplicationContext(), "Konum Servisi Baþlatýldý.Konum bilgisi alýnýyor...", Toast.LENGTH_LONG).show();
		
		locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);	
			}
			
			@Override
			public void onLocationChanged(Location location) {
				Intent intent = new Intent("konumu_guncelle");
				intent.putExtra("Koordinatlar", "Enlem : " + location.getLatitude()+" Boylam : "+location.getLongitude()+" Yükseklik :"+location.getAltitude());
				intent.putExtra("Enlem", location.getLatitude());
				intent.putExtra("Boylam", location.getLongitude());
				intent.putExtra("Yukseklik", location.getAltitude());
				KonumVeritabani konumVeritabani = new KonumVeritabani(KonumServisi.this);
				konumVeritabani.BaglantiyiAc();
				konumVeritabani.MesajGetir(location.getLatitude(), location.getLongitude(), location.getAltitude());
				konumVeritabani.BaglantiyiKapat();
				if(KonumVeritabani.kayitsayisi == 0)
					startService(new Intent(getApplicationContext(), KonumKontrol.class));
				sendBroadcast(intent);
				
			}
		};
		locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

		

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(locationManager != null)
			locationManager.removeUpdates(locationListener);
		Toast.makeText(getApplicationContext(), "Konum Servisi Durduruldu...", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
