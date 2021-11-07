package com.sise.subappstore.bean;

import com.sise.subappstore.LoginActivity;
import com.sise.subappstore.R;
import com.sise.subappstore.async.FunctionAsync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

public class SesionBean {
	private static ProgressDialog pDialog;
	private SharedPreferences.Editor edit;
	public SesionBean(SharedPreferences.Editor edit){
		this.edit=edit;
	}
	
	public boolean  logout(){
		edit.clear();
		edit.commit();
		SystemClock.sleep(1500);
		return true;
	}
	
	public void exit(Context cc){
		Intent intent = new Intent(cc,LoginActivity.class);
		

		
		mostrar(cc);
		
		pDialog.dismiss();
		cc.startActivity(intent);
	}
	
	static  void mostrar(Context cc){
        pDialog = new ProgressDialog(cc);
        pDialog.setMessage("Saliendo ... ");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
	}

}
