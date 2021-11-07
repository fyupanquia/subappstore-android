package com.sise.subappstore;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GCMIntentService extends IntentService{
	private static final int NOTIF_ALERTA = 1;
	public GCMIntentService(){
		super("GCMIntentService");
		//TODO Auto-generates constructor stub
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		GoogleCloudMessaging gcm =
				GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		Bundle extras = intent.getExtras();
		if(!extras.isEmpty())
		{
			if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType));
			{
				String mensaje = extras.getString("mensaje");
				//Escribir mi notificacion
				
				NotificationManager mnotificacion = (NotificationManager)
						getSystemService(Context.NOTIFICATION_SERVICE);
				
				NotificationCompat.Builder mBuilder
					= new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.icono2)
				.setContentTitle("SubAppStore")
				.setContentText(mensaje);
			Intent mintent = new Intent(this,MainActivity.class);
			PendingIntent pIntent = 
					PendingIntent.getActivity(this, 0, mintent, 0);
			mBuilder.setContentIntent(pIntent);
			mnotificacion.notify(NOTIF_ALERTA,mBuilder.build());
			};
		}
		GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

}
