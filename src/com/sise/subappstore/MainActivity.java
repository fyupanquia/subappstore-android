package com.sise.subappstore;

import com.sise.subappstore.MainActivity;
import com.sise.subappstore.async.FunctionAsync;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
      setTitle("SubAppStore");
        
        Thread hilo = new Thread(){
        	@Override
        	public void run()
        	{
        		try{
        	sleep(3000);
        	Intent intent = new Intent
        	(MainActivity.this,LoginActivity.class);
        	startActivity(intent);
        	finish();
        	}catch (InterruptedException e){
        		e.printStackTrace();
        	}
        }
    };
    hilo.start();
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    public boolean onKeyDown(int keyCode,KeyEvent event){
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		new AlertDialog.Builder(this)
    		.setIcon(android.R.drawable.ic_dialog_alert)
    		.setTitle("Cerrar Sesión")
    		.setMessage("¿Estas seguro de cerrar sesión?")
    		.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int whith) {
					Intent intent = new Intent(MainActivity.this,LoginActivity.class);
					startActivity(intent);
				}
			})
			.setNegativeButton("Cancelar", null)
			.show();
    		return true;
			
    	}else{
    		return super.onKeyDown(keyCode, event);
    	}
    }
	
}
