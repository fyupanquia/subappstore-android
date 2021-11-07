package com.sise.subappstore;

import java.math.BigInteger;

import com.sise.subappstore.bean.BeanCompra;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.bean.UsuarioBean;
import com.sise.subappstore.http.Http_ComprarMonedas;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComprarMonedas extends ActionBarActivity implements OnClickListener {
	EditText cantdoradas,cantplateadas,numtarjeta,clavetarjeta;
	Button btnComprar;
	
	private SharedPreferences pref;
	private SharedPreferences prefsubappstore;
	private UsuarioBean u;
	
	BeanToast bt ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comprar_monedas);
		
		bt= new BeanToast(ComprarMonedas.this,"",
					getLayoutInflater(),findViewById(R.id.lyToast),R.id.tvMsgToast);
		
		
		pref=getSharedPreferences("subasta", Context.MODE_PRIVATE);  
		prefsubappstore=getSharedPreferences("prefsubappstore", Context.MODE_PRIVATE); 
		
        String usuario=prefsubappstore.getString("usuario","Desconocido");
	    u=(UsuarioBean) BeanMapper.fromJson(usuario,UsuarioBean.class);	 
		
		cantdoradas = (EditText) findViewById(R.id.etCompraMonedasDCompra);
		cantplateadas = (EditText) findViewById(R.id.etCantMonedasPCompra);
		numtarjeta = (EditText) findViewById(R.id.etNumTarCompra);
		clavetarjeta = (EditText) findViewById(R.id.etClaveTarjCompra);
		btnComprar = (Button) findViewById(R.id.btnComprarCompra);
		
		btnComprar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String plateadas = cantplateadas.getText().toString().trim();
				String doradas = cantdoradas.getText().toString().trim();
				String ntarj = numtarjeta.getText().toString().trim();
				String clavtarj = clavetarjeta.getText().toString().trim();
				
				BeanCompra bc = new BeanCompra();
				
				if(plateadas.equals("")){
					bc.setCantPlateadas( 0 );
					}else{
						bc.setCantPlateadas(Integer.parseInt(plateadas));
					}
				
				if(doradas.equals("")){
					bc.setCantDoraradas(0 );
					}else{
						bc.setCantDoraradas(Integer.parseInt(doradas));
					}
				
				if(!ntarj.equals("") && !clavtarj.equals("")){
					bc.setNumtarjeta(ntarj);
					bc.setClavTarjeta( Integer.parseInt(clavtarj));
					bc.setIdUsuario(u.getId());
					
					Http_ComprarMonedas cm = new Http_ComprarMonedas(ComprarMonedas.this,"Comprando",bc);
					cm.execute(getString(R.string.urlComprar));
				}else{
					//Toast.makeText(getApplicationContext(), ,Toast.LENGTH_SHORT).show();
					bt.setMensaje("Ingrese su número de tarjeta y su clave");
					bt.show();
				}

					
				
			}
		});
		
	}
	
	public void clear(){
		cantdoradas.setText("");
		cantplateadas.setText("");
		numtarjeta.setText("");
		clavetarjeta.setText("");
		
		//Toast.makeText(ComprarMonedas.this, "Su Compra fue registrada correctamente ...",Toast.LENGTH_LONG).show();
		bt.setMensaje("Su Compra fue registrada correctamente ...");
		bt.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comprar_monedas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
