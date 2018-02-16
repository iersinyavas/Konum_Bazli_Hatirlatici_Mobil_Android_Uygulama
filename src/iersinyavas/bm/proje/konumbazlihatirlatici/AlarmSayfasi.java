package iersinyavas.bm.proje.konumbazlihatirlatici;

import iersinyavas.bm.proje.konumbazlihatirlatici.R;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AlarmSayfasi extends Activity{
	Button alarmikapat, mesajisil;
	MediaPlayer mediaPlayer;
	TextView mesajigoster;
	KonumVeritabani konumVeritabani = new KonumVeritabani(AlarmSayfasi.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_sayfasi);
		mesajigoster = (TextView) findViewById(R.id.mesaji_goster);
		alarmikapat = (Button) findViewById(R.id.alarmi_kapat);
		mesajisil = (Button) findViewById(R.id.mesaji_sil);
		
		final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		mesajigoster.setText(getIntent().getExtras().getString("Mesaj"));
		mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.oroborus);
		mediaPlayer.start();
		vibrator.vibrate(15000);
		
		alarmikapat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mediaPlayer.stop();
				vibrator.cancel();
				onDestroy();
			}
		});	

		mesajisil.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				konumVeritabani.BaglantiyiAc();
			    konumVeritabani.VeriSil(mesajigoster.getText().toString());
				konumVeritabani.BaglantiyiKapat();
				mediaPlayer.stop();
				vibrator.cancel();
				onDestroy();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
}
