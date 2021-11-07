package com.sise.subappstore;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sise.subappstore.bean.BeanToast;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class GPSActivity extends ActionBarActivity {

	private GoogleMap mapa=null;
	private LatLng sise = new LatLng(-12.07758,-77.0358351);
	private LatLng compupalace = new LatLng(-12.111213,-77.030392);
	BeanToast bt ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		bt= new BeanToast(GPSActivity.this,"",
				getLayoutInflater(),findViewById(R.id.lyToast),R.id.tvMsgToast);
		
		mapa = ((SupportMapFragment)
        		getSupportFragmentManager()
        		.findFragmentById(R.id.map)).getMap();
        
        CameraUpdate camUpd2=CameraUpdateFactory.newLatLngZoom(sise, 12F);
        mapa.animateCamera(camUpd2);
        agregarMarcador();
        mostrarLineas();
        
        Gps.startListening(GPSActivity.this);
    }
    
    private void agregarMarcador()
    {
    	mapa.addMarker(new MarkerOptions()
    	.position(sise)
    	.title("Instituto Sise Sede Central"));
    	
    	mapa.addMarker(new MarkerOptions()
    	.position(compupalace)
    	.title("Compu Palace"));
    	
    	mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
    		public boolean onMarkerClick(Marker marker){
    			
				bt.setMensaje("Marcador Pulsado:\n" + marker.getTitle());
				bt.show();
    			//Toast.makeText(GPSActivity.this, "Marcador Pulsado:\n" + marker.getTitle(), Toast.LENGTH_SHORT).show();
    			
    			return false;
  
			}
		});
    }
    
    private void mostrarLineas(){
	
    	PolylineOptions lineas =new PolylineOptions()
    	.add(sise)
    	.add(compupalace);
    	lineas.width(8);
    	lineas.color(Color.GREEN);
	
    	mapa.addPolyline(lineas);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.g, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.posicion){
        	Location lyc=Gps.getLocation();
        	
        	CameraUpdate camUpd2=
        CameraUpdateFactory.newLatLngZoom(new LatLng(lyc.getLatitude(), lyc.getLongitude()), 12F);
        mapa.animateCamera(camUpd2);
        
        mapa.addMarker(new MarkerOptions()
        .position(new LatLng(lyc.getLatitude(), lyc.getLongitude()))
        .title("posicion actual"));
        
        	return true;
        	
        }
        return super.onOptionsItemSelected(item);
    }
}