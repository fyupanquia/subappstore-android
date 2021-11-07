package com.sise.subappstore;

import java.util.ArrayList;
import java.util.List;

import com.sise.subappstore.Fragment_Home.ViewHolder;
import com.sise.subappstore.Fragment_Home.lista_adaptador;
import com.sise.subappstore.modelo.ItemImage;
import com.sise.subappstore.modelo.ItemLista;
import com.sise.subappstore.util.ImageDownloader;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PerfilSubasta extends ActionBarActivity {

	private SharedPreferences pref;
	private SharedPreferences prefsubappstore;
	private SharedPreferences.Editor editor;
	
	private TextView etNombre,etPrecio,etCategoria;
	private ListView lvImagenes,lvDescripcion;
	
	private List<ItemImage> imagenes = new ArrayList<ItemImage>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil_subasta);
		pref=getSharedPreferences("prefsubappstore", Context.MODE_PRIVATE);  
		editor = pref.edit();
		
		
		/* START ASIGNAR ELEMENTOS*/
		etNombre = (TextView) findViewById(R.id.tvNombrePerfilSubasta);
		etPrecio= (TextView) findViewById(R.id.tvPrecioPerfilSubasta);
		etCategoria= (TextView) findViewById(R.id.tvCategoriaPerfilSubasta);
		
		lvImagenes = (ListView) findViewById(R.id.lvImagenesPerfilSubasta);
		lvDescripcion= (ListView) findViewById(R.id.lvDescripcionPerfiLSubasta);
		/* STOP ASIGNAR ELEMENTOS*/
		
		
		cargarDatos();
		
	}
	
	private void cargarDatos(){
		etNombre.setText(""+pref.getString("nombre_perfilSubasta",""));
		etPrecio.setText(""+pref.getString("precio_perfilsubasta",""));
		etCategoria.setText(""+pref.getString("categoria_perfilSubasta",""));
		
		String descripcion = pref.getString("descripcion_perfilSubasta","");
		Integer cantidad = pref.getInt("cant_imgs_perfilSubasta",0);
		Integer idusuario = pref.getInt("idusuario_perfilsubasta",0);
		String grupo = pref.getString("grupo_perfilsubasta","");
		String usuario = pref.getString("usuario_perfilSubasta","");
		
		ListView lvImagenes = (ListView) findViewById(R.id.lvImagenesPerfilSubasta);		
		
		

		
		for (int i = 0; i <cantidad; i++) {
			
			if(i==0){
				imagenes.add(new ItemImage(grupo+".png", usuario, grupo));
			}else{
				imagenes.add(new ItemImage(grupo+i+".png", usuario, grupo));
			}
			
			
		}
		
        lista_adaptador lsadapter = new lista_adaptador(PerfilSubasta.this,true);
        lvImagenes.setAdapter(lsadapter);
        
        
		String[] values = new String[] { descripcion };
		
		mostrardescripcion(values);
		
		

	}
	
	private void mostrardescripcion(String[] values){
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		lvDescripcion.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.perfil_subasta, menu);
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
	
	class lista_adaptador extends ArrayAdapter<ItemImage> {
		Context context;
		Activity activity ;
		Boolean b ;
		
		lista_adaptador(Context context2,Boolean b) {
			super(context2, R.layout.activity_item_list_image, imagenes);
			this.context = context2;
			this.b=b;
		}

		public View getView(int position, View item, ViewGroup parent) {

			ViewHolder holder;
			activity = (Activity) context;
			if (item == null) {
				LayoutInflater inflater = activity.getLayoutInflater();
				item = inflater.inflate(R.layout.activity_item_list_image, null);

				holder = new ViewHolder();
				holder.vista = (ImageView) item.findViewById(R.id.ivImageListPerfilSubasta);

				item.setTag(holder);
			} else {
				holder = (ViewHolder) item.getTag();
			}

			String urlFoto = getString(R.string.urlImagenProducto) + imagenes.get(position).getUsuario() +"/productos/"+imagenes.get(position).getGrupo()+"/"+imagenes.get(position).getImagen();
			 ImageDownloader imagend = new ImageDownloader();
			 imagend.download(urlFoto, holder.vista);

			 
			return (item);
		}
	}
	
	static class ViewHolder{
    	ImageView vista;
    }
	
}
