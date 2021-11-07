package com.sise.subappstore;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;

import com.sise.subappstore.async.FunctionAsync;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.UsuarioBean;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.AdapterView;


import com.sise.subappstore.R;


public class MenuActivity extends ActionBarActivity implements OnClickListener{
	
	private SharedPreferences pref;	
	private SharedPreferences.Editor edit; // añadir o editar los datos
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitulo;
	private CharSequence mTitulo;
	private String[] navMenuTitulos;
	private TypedArray navMenuIconos;
	private UsuarioBean bean;
	private JSONObject[] array_datos_lista;
	private Fragment fragment;
	private int _position;
	
	 private ArrayList<com.sise.subappstore.modelo.ItemNavigation> navDrawerItems;
	 private com.sise.subappstore.adaptador.ListAdapter adapter;	
	 private Bundle savedInstanceState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		setContentView(R.layout.activity_menu);
		// ###### Captura de valores en sesión
		pref=getSharedPreferences("prefsubappstore", Context.MODE_PRIVATE);  
		edit = pref.edit();
		
		String usuario=pref.getString("usuario","Desconocido");
	    bean=(UsuarioBean) BeanMapper.fromJson(usuario,UsuarioBean.class);		      
	      
	    mTitulo = mDrawerTitulo = getTitle();
	    navMenuTitulos = getResources().getStringArray(R.array.nav_drawer_items); 
	    // ###### Títulos de Navigation Drawer
	      
	    navMenuIconos = getResources()
					.obtainTypedArray(R.array.nav_drawer_icons); 					
	    // ###### Iconos de Navigation Drawer
	      
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        
        // ###### Ingresando elementos de Navigation Drawer
		navDrawerItems = new ArrayList<com.sise.subappstore.modelo.ItemNavigation>();
		navDrawerItems.add(new com.sise.subappstore.modelo.ItemNavigation(navMenuTitulos[0], navMenuIconos.getResourceId(0, -1)));
		navDrawerItems.add(new com.sise.subappstore.modelo.ItemNavigation(navMenuTitulos[1], navMenuIconos.getResourceId(1, -1), true, "+20"));
		navDrawerItems.add(new com.sise.subappstore.modelo.ItemNavigation(navMenuTitulos[2], navMenuIconos.getResourceId(2, -1), true, "+20"));
		navDrawerItems.add(new com.sise.subappstore.modelo.ItemNavigation(navMenuTitulos[3], navMenuIconos.getResourceId(3, -1)));
		navDrawerItems.add(new com.sise.subappstore.modelo.ItemNavigation(navMenuTitulos[4], navMenuIconos.getResourceId(4, -1)));
		navDrawerItems.add(new com.sise.subappstore.modelo.ItemNavigation(navMenuTitulos[5], navMenuIconos.getResourceId(5, -1)));
		navDrawerItems.add(new com.sise.subappstore.modelo.ItemNavigation(navMenuTitulos[6], navMenuIconos.getResourceId(6, -1)));
		navDrawerItems.add(new com.sise.subappstore.modelo.ItemNavigation(navMenuTitulos[7], navMenuIconos.getResourceId(7, -1)));
		navDrawerItems.add(new com.sise.subappstore.modelo.ItemNavigation(navMenuTitulos[8], navMenuIconos.getResourceId(8, -1)));
		
			
		navMenuIconos.recycle();
		//	mDrawerList.setOnItemClickListener(new slide)
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());	
		
		adapter = new com.sise.subappstore.adaptador.ListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
					R.drawable.ic_drawer,
					R.string.app_name, 
					R.string.app_name 
			) {
				public void onDrawerClosed(View view) {
					getSupportActionBar().setTitle(mTitulo);
					invalidateOptionsMenu();
				}
	
				public void onDrawerOpened(View drawerView) {
						getSupportActionBar().setTitle(mDrawerTitulo);
						invalidateOptionsMenu();
				}
				};
				mDrawerLayout.setDrawerListener(mDrawerToggle);

				if (savedInstanceState == null) {
					
					
					
					Bundle extras = getIntent().getExtras(); 
					Integer _posicion=0;

					if (extras != null) {
						_posicion = extras.getInt("posicion");
					}
					
					if(_posicion!=0){
						displayView(_posicion);
					}else{
						displayView(0);
					}
					
					
				}else{
					Integer num = pref.getInt("posicion", 0);
					displayView(num);
				}
	}
	

	

	 private void displayView(int position) {
			 
			 _position=position;
			switch (position) {
			case 0:
				// TODAS LAS SUBASTAS
				array_datos_lista=null;
				FunctionAsync fa =  new FunctionAsync(MenuActivity.this, new UsuarioBean() , "Listando ...");
				fa.execute(this.getString(R.string.urllistarProductos),"xSubastar","all");

				break;
			case 1:
				// MIS SUBASTAS
				array_datos_lista=null;
				FunctionAsync faMis =  new FunctionAsync(MenuActivity.this, bean , "Listando ...");
				faMis.execute(this.getString(R.string.urllistarProductos),"xSubastarMios","this");
				break;
			case 2:
				// MIS SUBASTAS GANADAS
				array_datos_lista=null;
				FunctionAsync faMisG =  new FunctionAsync(MenuActivity.this, bean , "Listando ...");
				faMisG.execute(this.getString(R.string.urllistarProductos),"xSubastarGanar","thisWin");
				break;
			case 3:
				fragment = new FragmentSubastar();
				crearfragmento();
				break;
			case 4:
				fragment = new FragmentMaps();
				crearfragmento();
				break;
			case 5:
				Intent intent = new Intent(MenuActivity.this, ComprarMonedas.class);
				startActivity(intent);
				break;
			case 6:
				Intent intent2 = new Intent(MenuActivity.this, ModificarPerfil.class);
				startActivity(intent2);
				break;
			case 7:
				fragment = new FragmentAcercaDe();
				crearfragmento();
				break;
			case 8:
				new FunctionAsync(MenuActivity.this, edit, "Saliendo ...")
				 .execute("","logout");
				break;

			default:
				break;
			}
		}

	   private class SlideMenuClickListener implements
				ListView.OnItemClickListener {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						displayView(position);
					}
			}
	   
	   
		@Override
		protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		edit.putInt("posicion",0);
		edit.commit(); 
		}
		
	   
	   
	   public void instanciar(JSONObject[] response_array_json_data,String filtro){
		   
		  array_datos_lista = response_array_json_data;
		  fragment = new Fragment_Home(bean,response_array_json_data,filtro,edit); 

		   
		   crearfragmento();
	   }
	   
	   public void crearfragmento(){
		   if (fragment != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
				mDrawerList.setItemChecked(_position, true);
				mDrawerList.setSelection(_position);
				setTitle(navMenuTitulos[_position]);
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				Log.e("MainActivity", "Error al crear el fragmento");
			}
	   }
	   
	   public void close(){
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
	   }
	   
		@Override
	    public boolean onKeyDown(int keyCode,KeyEvent event){
	    	if(keyCode==KeyEvent.KEYCODE_BACK){
	    		new AlertDialog.Builder(this)
	    		.setIcon(R.drawable.ic_launcher)
	    		.setTitle("Salir de la Sala")
	    		.setMessage("¿Desea Cerrar su sesión?")
	    		.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int whith) {
						
						new FunctionAsync(MenuActivity.this, edit, "Saliendo ...")
						 .execute("","logout");
					}
				})
				.setNegativeButton("Cancelar", null)
				.show();
	    		return true;
				
	    	}else{
	    		return super.onKeyDown(keyCode, event);
	    	}
	    }
	   
	   
	   @Override
		public void setTitle(CharSequence title) {
			mTitulo = title;
			getSupportActionBar().setTitle(mTitulo);
		}

	    @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
	    
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}
			switch (item.getItemId()) {
			case R.id.action_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		
		 @Override
			public boolean onPrepareOptionsMenu(Menu menu) {
				boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
				menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
				return super.onPrepareOptionsMenu(menu);
			}
		    
		    @Override
			protected void onPostCreate(Bundle savedInstanceState) {
				super.onPostCreate(savedInstanceState);
				mDrawerToggle.syncState();
			}

			@Override
			public void onConfigurationChanged(Configuration newConfig) {
				super.onConfigurationChanged(newConfig);
				mDrawerToggle.onConfigurationChanged(newConfig);
			}

			@Override
			public void onClick(View arg0) {}
}
