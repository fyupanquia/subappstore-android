package com.sise.subappstore;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.sise.subappstore.async.FunctionAsync;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.bean.UsuarioBean;
import com.sise.subappstore.modelo.ItemLista;
import com.sise.subappstore.util.ImageDownloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Subappstore extends Activity implements OnClickListener{

	
	private ImageView ivMoneda,ivMoneda1;
	private List<ItemLista> conectados;
	private ListView ElementosConectados;
	private TextView tvTRestante,tvUserGanador,tvMejorOferta,tvPropietario,tvNombreProducto,tvCantMonedas;
	private EditText etNewOferta;
	private SharedPreferences pref;
	private SharedPreferences prefsubappstore;
	private SharedPreferences.Editor editor;
	private lista_adaptador lsadapter;
	private Button btnSalir,btnOfrecer;
	private Boolean refresh=true,ofrecer=true;
	private UsuarioBean u;
	private Integer temptrestante,horas=0,minutos=0,segundos=0;
	
	private String propietario,nombreProducto,imagenproducto,result_tempganador;
	private Integer idpropietario,nuevaoferta,mismonedas,mejorOferta,idproducto,result_mejorOferta,level;
	private ImageView ivImagenProducto;
	BeanToast bt ;
	String cadenaconectados="",tempcadenaconectados="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subappstore);
		
		pref=getSharedPreferences("subasta", Context.MODE_PRIVATE);  
		prefsubappstore=getSharedPreferences("prefsubappstore", Context.MODE_PRIVATE); 
		
		bt= new BeanToast(Subappstore.this,"",
				getLayoutInflater(),findViewById(R.id.lyToast),R.id.tvMsgToast);
		
		/* INICIO CARGA ELEMENTOS */
		ElementosConectados = (ListView) findViewById(R.id.lvconectados);
		tvUserGanador = (TextView) findViewById(R.id.tvUserGanador);
		tvMejorOferta= (TextView) findViewById(R.id.tvMejorOferta);
		tvTRestante  = (TextView) findViewById(R.id.tvTRestante);
		tvCantMonedas = (TextView) findViewById(R.id.tvNewCantMonedasS);
		tvPropietario = (TextView) findViewById(R.id.tvPropietario);
		tvNombreProducto= (TextView) findViewById(R.id.tvNombreProducto);
		ivImagenProducto = (ImageView) findViewById(R.id.ivImagenProducto);
		etNewOferta  = (EditText) findViewById(R.id.etNewOferta);
		ivMoneda = (ImageView) findViewById(R.id.ivMonedaSubasta);
		ivMoneda1 = (ImageView) findViewById(R.id.ivMonedaSubasta1);
		/* FIN CARGA ELEMENTOS */
		
		
		/* INICIO DATOS EN MEMORIA DE LA SUBASTA*/
		btnSalir = (Button) findViewById(R.id.btnSalir);
		btnOfrecer = (Button) findViewById(R.id.btnOfrecer);
        editor=pref.edit();
        String usuario=prefsubappstore.getString("usuario","Desconocido");
	    u=(UsuarioBean) BeanMapper.fromJson(usuario,UsuarioBean.class);	    
        editor.putString("tempcadenaconectados", "default");
		editor.commit();
		/* FIN DATOS EN MEMORIA DE LA SUBASTA*/
		
		/* INICIO Datos Propietario*/
		 propietario = prefsubappstore.getString("detproducto_propietario","Desconocido");
		 nombreProducto = prefsubappstore.getString("detproducto_nombre","Desconocido");
		 imagenproducto = prefsubappstore.getString("detproducto_imagen","Desconocido");
		 idpropietario = prefsubappstore.getInt("detproducto_idpropietario",0);
		 idproducto = prefsubappstore.getInt("detproducto_idproducto",0);
		 level = prefsubappstore.getInt("detproducto_level",0);
	    /* FIN Datos Propietario*/
		 
		 
			FunctionAsync fa =  new FunctionAsync(Subappstore.this, u , "Ingresando a la sala ...");
			fa.execute(getString(R.string.urlSubasta),"ConectarSubasta",imagenproducto);
		
		// Si el producto es de un administrador , pues sólo se juega con monedas doradas . Por lo contrario con plateadas
		 if(level==1){
			 ivMoneda.setImageResource(R.drawable.monedita_dorada);
			 ivMoneda1.setImageResource(R.drawable.monedita_dorada);
		 }else{
			 ivMoneda.setImageResource(R.drawable.monedita_plateada);
			 ivMoneda1.setImageResource(R.drawable.monedita_plateada);
		 }
		 
		 /*
		 if(idpropietario==u.getId()){
			 etNewOferta.setVisibility(View.GONE);
			 btnOfrecer.setVisibility(View.GONE);
		 }*/
			

			btnSalir.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					refresh=false;
					FunctionAsync fa =  new FunctionAsync(Subappstore.this, u,"Saliendo de sala ..." );
					fa.execute(Subappstore.this.getString(R.string.urlSubasta),"RetirarSubasta",imagenproducto,"0");
				}
			});
			
			btnOfrecer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if(!etNewOferta.getText().toString().trim().equals("")){
						nuevaoferta	= Integer.parseInt(etNewOferta.getText().toString());
					
					
					if(nuevaoferta!=null){
						
						//mismonedas = Integer.parseInt(tvCantMonedas.getText().toString());
						if(compararMontos(mejorOferta,nuevaoferta)){
							if(ofrecer==true){
								/*CORREGIR A PARTIR DE AQUI*/
								refresh=false;
								FunctionAsync fa =  new FunctionAsync(Subappstore.this, u,"Ofreciendo ..." );
								fa.execute(Subappstore.this.getString(R.string.urlSubasta),"OfrecerMonto",imagenproducto,idproducto+"",nuevaoferta+"");
							}else{
								bt.setMensaje("Ya no se permiten mas ofertas , la Subasta ha finalizado ...");
								bt.show();
								
							}
						}else{
							bt.setMensaje("Aumente su oferta ...");
							bt.show();
						}
						
					}else{
						bt.setMensaje("Usted debe asignar un monto ..."+nuevaoferta);
						bt.show();
					}

					
					
					
					
					
					}else{
						bt.setMensaje("Usted debe asignar un monto ...");
						bt.show();
					}
						
					
					
					
					
				}
			});
	}
	
	
	/* FIN METODOS RESPUESTAS ASINCRONICAS*/
	public void salir(Integer num){
		Log.e("Estado Subasta","Saliendo de la subasta actual ....");
	
		Bundle bundle = new Bundle();
		Intent intent = new Intent(Subappstore.this,MenuActivity.class);
		bundle.putInt("posicion", num);
		intent.putExtras(bundle);
		startActivity(intent);
		
	}
	
	public void continuarSubasta(){
		refresh = true;
		generarlistado();
	}
	
	
	public void cargarSubasta(){
		Log.e("Estado Subasta","Cargando imagen producto ....");
	    String urlFoto=getString(R.string.urlImagen)+ propietario+"/productos/"+imagenproducto+"/"+imagenproducto+".png";
		Log.e("URLIMG SALA SUBASTA ",urlFoto);
  		ImageDownloader imagend=new ImageDownloader();
  	    imagend.download(urlFoto, ivImagenProducto);
		tvPropietario.setText(propietario);
		tvNombreProducto.setText(nombreProducto);
		generarlistado();
	}
	

	/* FIN METODOS RESPUESTAS ASINCRONICAS*/
	
	public void generarlistado(){
		Log.e("Estado Subasta","Obteniendo lista de Conectados ....");
		FunctionAsync fa =  new FunctionAsync(Subappstore.this, new UsuarioBean() , "");
		fa.execute(this.getString(R.string.urlSubasta),"Subastando",imagenproducto);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subappstore, menu);
		return true;
	}
	
	public void listar(JSONObject[] response_array_json_data)throws JSONException{
		
			conectados = new ArrayList<ItemLista>();
		
			pref=getSharedPreferences("subasta", Context.MODE_PRIVATE);  
	        editor=pref.edit();
	        cadenaconectados = pref.getString("tempcadenaconectados", "default");
	        temptrestante = response_array_json_data[0].getInt("trestante");
	        
	        if(temptrestante!=0){
	        	Log.e("Estado Subasta","Aun pueden seguir subastando ....");
	        	generarTiempo(temptrestante);
	        }else{
	        	Log.e("Estado Subasta","El tiempo de la subasta ha acabado ....");
	        	ofrecer=false;
	        	refresh=false;
	        	finalizarSubasta();
	        	
	        }
	        
	        /*INICIO DATOS USUARIO GANADOR TEMPORAL*/
	        Log.e("Estado Subasta","Listando datos del usuario ganador temporal ....");
	       String d=response_array_json_data[0].getString("precio");
	       if(d!=null){
	    	   mejorOferta =Integer.parseInt(d);
	    	   tvMejorOferta.setText(mejorOferta+"");
	       }else{
	    	   Log.e("Estado Subasta","Al parecer nadie ha apostado ....");
	    	   mejorOferta=0;
	    	   tvMejorOferta.setText(mejorOferta+"");
	    	   }
	       if(response_array_json_data[0].getString("uusuario")==null){
	    	   Log.e("Estado Subasta","No tenemos usuario temporal ganador ....");
	        	tvUserGanador.setText("No se ha ofrecido aún ...");
	        }else{
	        	   Log.e("Estado Subasta","Si tenemos usuario temporal ganador ....");
	        	result_tempganador =response_array_json_data[0].getString("uusuario");
	        	tvUserGanador.setText(result_tempganador);
	        }
	       /*FIN DATOS USUARIO GANADOR TEMPORAL*/
	        
		Log.e("Estado","listando conectados ...");
		int cantidad = response_array_json_data[0].getInt("cantidadconectados");
		tempcadenaconectados="";
		
			for (int i = 0; i <cantidad; i++) {
				if(u.getUsuario().equals(response_array_json_data[i].getString("usuario"))){
					String oro = response_array_json_data[i].getString("oro");
					tvCantMonedas.setText(oro+"");
				}
				conectados.add(new ItemLista(
						response_array_json_data[i].getString("usuario"),
						response_array_json_data[i].getString("usuario")+".png",
						response_array_json_data[i].getInt("oro"),
						response_array_json_data[i].getInt("plata"),
						response_array_json_data[i].getInt("bronce")
						));
				tempcadenaconectados += response_array_json_data[i].getString("usuario")+",";
			}
		 lsadapter = new lista_adaptador(Subappstore.this);		
	         Log.e("Estado","creando adaptador");
	         if(cadenaconectados=="default"){
	        	    ElementosConectados.setAdapter(lsadapter); 
	         }if(cadenaconectados.equals(tempcadenaconectados)){
	        	    lsadapter.notifyDataSetChanged(); 
	         }else{
	        	 	ElementosConectados.setAdapter(lsadapter); 
	        	    lsadapter.notifyDataSetChanged();
	        	    ElementosConectados.invalidateViews();
				    ElementosConectados.refreshDrawableState();
	         }
			         ElementosConectados.setOnItemClickListener(new OnItemClickListener() {
			
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
						// Acción a realizar al seleccionar a un conectado
						}
					});
		Log.e("Mensaje","listado generado...");
		editor.putString("tempcadenaconectados", tempcadenaconectados);
		editor.commit();
		if(refresh==true)
         generarlistado();
    }
	
	
	// INICIO METODOS UTILES SUBASTA
	public void generarTiempo(Integer secs){
	      Integer hr=(int) Math.round(temptrestante / 3600);
	      Integer min=(int) Math.round((temptrestante - (hr * 3600)) / 60);
	      Integer sec=(int) Math.round(temptrestante - (hr * 3600) - (min * 60));
	      String hhr="",mmin="",ssec="";
	        if(hr<10){ hhr="0"+hr; }else{hhr=""+hr;}
	        if(min<10){ mmin="0"+min; }else{mmin=""+min;}
	        if(sec<10){ ssec="0"+sec; }else{ssec=""+sec;}
	        if(hr==0){ hhr="00"; }
	        
	        tvTRestante.setText(hhr+":"+mmin+":"+ssec);
	}
	
	public boolean compararMontos(Integer m1,Integer m2){
		boolean b = false;
		if( m2!=null  && m2!=0){
			b=compararOfrecidos(m1,m2,false);
			
			if(level==1){
				b=compararOfrecidos(m2,Integer.parseInt(tvCantMonedas.getText().toString()),true);
			}
		}else{
			//Toast.makeText(Subappstore.this, "Usted debe asignar un monto ...",Toast.LENGTH_SHORT).show();
			bt.setMensaje("Usted debe asignar un monto ...");
			bt.show();
		}
		return b;
	}
	
	public boolean compararOfrecidos(Integer m1,Integer m2,boolean ban ){
		boolean b = false;
		if(ban==false){
				b = (m2>m1) ;
		}else if(ban==true){
				b = (m1<=m2) ;
		}
		return b;
	}
	
	public void finalizarSubasta(){
		Log.e("Estado Subasta ","evaluando ganador");
		if(tvUserGanador.getText().equals(u.getUsuario().toString())){

			
				FunctionAsync fa =  new FunctionAsync(Subappstore.this, u,"La subasta ha finalizado ..." );
				if(level==1){
					
					if(compararOfrecidos( Integer.parseInt(tvMejorOferta.getText().toString()),Integer.parseInt(tvCantMonedas.getText().toString()),true)){
						fa.execute(Subappstore.this.getString(R.string.urlSubasta),"EvaluarGanador",imagenproducto,idproducto+"",tvMejorOferta.getText().toString(),"1");
					}else{
						Log.e("Estado Subasta ","Error presentado al comparar montos");
						bt.setMensaje("INCOHERENCIAS EN MONTOS ASIGNADOS ...");
						bt.show();
					}	
					
				}else
					fa.execute(Subappstore.this.getString(R.string.urlSubasta),"EvaluarGanador",imagenproducto,idproducto+"",tvMejorOferta.getText().toString(),"0");
			
			
		}else{
			Toast.makeText(Subappstore.this, "Lo lamentamos , usted acaba de perder la subasta ... ",Toast.LENGTH_SHORT).show();
		}
	}
	
	public void finSubasta(boolean b){
		String band ="";
		if(b){
			//Toast.makeText(getApplicationContext(), "LA SUBASTA HA ACABADO Y USTED LO HA GANADO , SÍRVASE A CONTACTAR CON EL VENDEDOR",Toast.LENGTH_SHORT).show();
			bt.setMensaje("LA SUBASTA HA ACABADO Y USTED LO HA GANADO , SÍRVASE A CONTACTAR CON EL VENDEDOR");
			bt.show();
			band="2";
		}else if(!b){
			//Toast.makeText(getApplicationContext(), "LA SUBASTA HA ACABADO Y USTED NO LO HA GANADO , INTÉNTELO NUEVAMENTE EN OTRA SUBASTA",Toast.LENGTH_SHORT).show();
			bt.setMensaje("LA SUBASTA HA ACABADO Y USTED NO LO HA GANADO , INTÉNTELO NUEVAMENTE EN OTRA SUBASTA");
			bt.show();
			band="0";
		}
		FunctionAsync fa =  new FunctionAsync(Subappstore.this, u,"");
		fa.execute(Subappstore.this.getString(R.string.urlSubasta),"RetirarSubasta",imagenproducto,band);
	}
	
	// FIN METODOS UTILES SUBASTA 
	class lista_adaptador extends ArrayAdapter<ItemLista> {

		Activity activity ;
		
		lista_adaptador(Activity activity) {
			super(activity, R.layout.activity_item_list_view, conectados);
			this.activity = activity;
		}

		public View getView(int position, View item, ViewGroup parent) {

			ViewHolder holder;
			
			if (item == null) {
				LayoutInflater inflater = activity.getLayoutInflater();
				item = inflater.inflate(R.layout.activity_item_list_conectado, null);

				holder = new ViewHolder();
				holder.vista = (ImageView) item.findViewById(R.id.imgContacto);
				holder.nombre = (TextView) item.findViewById(R.id.tvUsuario);

				item.setTag(holder);
			} else {
				holder = (ViewHolder) item.getTag();
			}

			holder.nombre.setText(conectados.get(position).getNombre());
		
			String urlFoto = getString(R.string.urlImagenUsuario)+"/"+ conectados.get(position).getNombre()+"/" + conectados.get(position).getImagen();
			 ImageDownloader imagend = new ImageDownloader();
			 imagend.download(urlFoto, holder.vista);
			return (item);
		}
	}
	
	static class ViewHolder{
    	ImageView vista;
    	TextView nombre;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btnSalir) {
			Intent intent = new Intent(this,RegistroActivity.class);
			startActivity(intent);
		}
	}
	
	
	@Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		new AlertDialog.Builder(this)
    		.setIcon(R.drawable.ic_launcher)
    		.setTitle("Salir de la Sala")
    		.setMessage("¿Estas seguro de salir de la sala?")
    		.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int whith) {
					// TODO Auto-generated method stub
					refresh=false;
					FunctionAsync fa =  new FunctionAsync(Subappstore.this, u,"Saliendo de sala ..." );
					fa.execute(Subappstore.this.getString(R.string.urlSubasta),"RetirarSubasta",imagenproducto,"0");
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
