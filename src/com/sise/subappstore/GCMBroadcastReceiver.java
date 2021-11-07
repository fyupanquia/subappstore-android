package com.sise.subappstore;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//Todo Auto-generated metjod Stub
		ComponentName cpm = new
				ComponentName(context.getPackageName(),
						GCMIntentService.class.getName());
		
		startWakefulService(context, (intent.setComponent(cpm)));
		setResultCode(Activity.RESULT_OK);
	}
}
