package iersinyavas.bm.proje.konumbazlihatirlatici;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OtomatikKonumServisiBaslat extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent servisibaslat = new Intent(context, KonumServisi.class);
		Intent konumkontrolservisibaslat = new Intent(context, KonumKontrol.class);
		try {
			wait(40000);
		} catch (Exception e) {
			Log.e("Hata", "Beklemede Hata!",e);
		}
		context.startService(servisibaslat);
		context.startService(konumkontrolservisibaslat);
	}

}
