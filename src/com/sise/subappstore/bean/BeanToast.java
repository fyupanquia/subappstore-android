package com.sise.subappstore.bean;

import com.sise.subappstore.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BeanToast {
	private Context cc;
	private LayoutInflater lii;
	private View vv;
	private Integer tvmssg;
	private String mensaje;
	private boolean duracion = true;
	
	public boolean isDuracion() {
		return duracion;
	}

	public void setDuracion(boolean duracion) {
		this.duracion = duracion;
	}

	public BeanToast(Context c,String msg,LayoutInflater li,View ly,Integer tvMsg){
		this.cc=c;
		this.lii = li;
		this.vv=ly;
		this.tvmssg = tvMsg;
		this.mensaje=msg;
	}
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public void  show(){
		Toast t2 =new Toast(cc);
		LayoutInflater li = lii;
		View layout = li.inflate(R.layout.toast_layout,(ViewGroup) vv);
		TextView tvMensaje = (TextView) layout.findViewById(tvmssg);
		tvMensaje.setText(mensaje);
		if(duracion)
			t2.setDuration(Toast.LENGTH_LONG);
		else
			t2.setDuration(Toast.LENGTH_SHORT);
		t2.setView(layout);
		t2.show();
	}

}
