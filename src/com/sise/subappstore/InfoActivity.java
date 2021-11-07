package com.sise.subappstore;

import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.http.Http_Acerca_de;
import com.sise.subappstore.http.Http_RProducto;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends ActionBarActivity {

	private TextView lblName;
	private TextView lblDescripcion;
	private ImageView imageView;
	private Http_Acerca_de about;
	private Bundle extra;
	BeanToast bt ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		bt= new BeanToast(InfoActivity.this,"",
				getLayoutInflater(),findViewById(R.id.lyToast),R.id.tvMsgToast);
		
		lblName = (TextView) findViewById(R.id.lblName);
		imageView = (ImageView) findViewById(R.id.imageView);
		
		extra =  getIntent().getExtras();
		
		about = new Http_Acerca_de(this);
		about.execute(extra.getInt("posicion"));	
		

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void describir(String descripcion){
		if(descripcion==""){
			
			Bundle bundle = new Bundle();
			Intent intent = new Intent(InfoActivity.this,MenuActivity.class);
			bundle.putInt("posicion", 6);
			intent.putExtras(bundle);
			startActivity(intent);
			
			bt.setMensaje( "La descripción de '"+extra.getString("titulo")+"' aún no está disponible.");
			bt.show();
			
			//Toast.makeText(getApplicationContext(), "La descripción de '"+extra.getString("titulo")+"' aún no está disponible.",Toast.LENGTH_LONG).show();
		}else{
			ListView 	lvAboutSimple = (ListView) findViewById(R.id.lvAboutSimple);
			
            String[] values = new String[] { descripcion };
            
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);

            lvAboutSimple.setAdapter(adapter); 
  
		}
		
		lblName.setText(extra.getString("titulo"));
		imageView.setImageResource(extra.getInt("img"));
	}
	

	

	
}
