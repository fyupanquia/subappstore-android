package com.sise.subappstore;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.sise.subappstore.R;
import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.bean.UsuarioBean;
import com.sise.subappstore.modelo.ItemLista;
import com.sise.subappstore.util.ImageDownloader;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_Home extends Fragment {

	private UsuarioBean u;
	private List<ItemLista> productos = new ArrayList<ItemLista>();
	private ListView ElementosImagenes;
	View rootView;
	private JSONObject[] response_array_json_data;
	private TextView tvuserhome;
	private ImageView imgperfil;
	private SharedPreferences.Editor editor;
	private String filtro;
	BeanToast bt ;
	
	public Fragment_Home(){
	}

	public Fragment_Home(UsuarioBean _u,JSONObject[] response_array_json_data,String filtro,SharedPreferences.Editor editor){
		this.u = _u;
		this.response_array_json_data = response_array_json_data;
		this.filtro = filtro;
		this.editor = editor;
	}
	
	
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.activity_fragment__home, container, false);
        
        
        bt =  new BeanToast(rootView.getContext(),"",
        		getActivity().getLayoutInflater(),rootView.findViewById(R.id.lyToast),R.id.tvMsgToast);
        
        tvuserhome =(TextView) rootView.findViewById(R.id.tvuserhome);
        imgperfil =(ImageView) rootView.findViewById(R.id.img_perfil_user);

	  	  if(u!=null){
	  		  
	            // ###### Mostrando Imagen de Perfil
	     		String urlFoto=getString(R.string.urlImagen)+ u.getUsuario()+"/"+u.getUsuario()+".png";
	     		ImageDownloader imagend=new ImageDownloader();
	     	    imagend.download(urlFoto, imgperfil);
	     	    
			      try {
	
			        tvuserhome.setText(u.getUsuario());
			        
			        if(response_array_json_data!=null)
			        	listar(response_array_json_data,true);
			        else
			        	listar(response_array_json_data,false);
			        
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  	  	}

        return rootView;
	}
	
	public void listar(JSONObject[] losproductos,final Boolean b) throws JSONException{
		
		if(b){
			//int cantidad = losproductos[0].getInt("cantidadtotal");
			int cantidad = losproductos.length;
			
			for (int i = 0; i <cantidad; i++) {
				productos.add(new ItemLista(
						losproductos[i].getInt("id"),
						losproductos[i].getInt("idusuario"),
						losproductos[i].getString("usuario"),
						losproductos[i].getString("nombre"),
						losproductos[i].getString("descripcion"),
						losproductos[i].getString("categoria"),
						losproductos[i].getDouble("precio"),
						losproductos[i].getString("imagen"),
						losproductos[i].getInt("level"),
						losproductos[i].getInt("imagenes")
						));
			}

			bt.setMensaje("Se han encontrado "+cantidad+"  productos!!!");
			bt.show();
			
			//Toast.makeText(rootView.getContext(), "Se han encontrado "+cantidad+"  productos!!!" ,Toast.LENGTH_LONG).show();
			
			 ElementosImagenes = (ListView) rootView.findViewById(R.id.lvProductsxsub);
	         lista_adaptador lsadapter = new lista_adaptador(rootView.getContext(),true);
	         ElementosImagenes.setAdapter(lsadapter);

	         
		}else{
			String nombre = "";
			
			if(filtro.equals("this")){
				nombre = "Usted no tiene registrado productos para subastar.";
			}else if(filtro.equals("thisWin")){
				nombre = "Usted no tiene subastas ganadas .";
			}
			
			productos.add(new ItemLista(
					nombre
					));
			productos.add(new ItemLista(
					"Vamos ... ! Registra un nuevo producto sin costos de publicación"
					));
			productos.add(new ItemLista(
					"¿ Aún no sabes como funciona SubAppStore ?"
					));
			productos.add(new ItemLista(
					"Compra monedas para poder subastar los productos de SubAppStore."
					));
			
			
			 ElementosImagenes = (ListView) rootView.findViewById(R.id.lvProductsxsub);
	         lista_adaptador lsadapter = new lista_adaptador(rootView.getContext(),false);
	         ElementosImagenes.setAdapter(lsadapter);
	          
			
		}
		
        ElementosImagenes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(b){
					
					if(filtro.equals("all")){
						
						 String propietario = productos.get(position).getUsuario();
						 String nombre = productos.get(position).getNombre();
						 String imagen =  productos.get(position).getImagen();
						 Integer idpropietario =  productos.get(position).getIdusuario();
						 Integer id =  productos.get(position).getId();
						 Integer level =  productos.get(position).getLevel();
						 
						 
						 
						 editor.putString("detproducto_propietario", propietario);
						 editor.putString("detproducto_nombre", nombre);
						 editor.putString("detproducto_imagen", imagen);
						 editor.putInt("detproducto_level", level);
						 editor.putInt("detproducto_idproducto", id);
						 editor.putInt("detproducto_idpropietario", idpropietario);
						 
						 editor.commit();
						 
						Intent intent = new Intent(rootView.getContext(), Subappstore.class);
						startActivity(intent);
						
					}else if(filtro.equals("this") || filtro.equals("thisWin")){
						
						String nombre = productos.get(position).getNombre();
						String descripcion = productos.get(position).getDescripcion();
						Double precio = productos.get(position).getPrecio();
						Integer cant_imgs = productos.get(position).getCant_imgs();
						Integer idusuario = productos.get(position).getIdusuario();
						String categoria = productos.get(position).getCategoria();
						String grupo =  productos.get(position).getImagen();
						String usuario =  productos.get(position).getUsuario();
						
						 editor.putString("nombre_perfilSubasta", nombre);
						 editor.putString("descripcion_perfilSubasta", descripcion);
						 editor.putInt("cant_imgs_perfilSubasta", cant_imgs);
						 editor.putInt("idusuario_perfilsubasta", idusuario);
						 editor.putString("precio_perfilsubasta",""+precio);
						 editor.putString("categoria_perfilSubasta", categoria);
						 editor.putString("grupo_perfilsubasta", grupo);
						 editor.putString("usuario_perfilSubasta", usuario);
						 editor.commit();
						 
						Intent intent = new Intent(rootView.getContext(), PerfilSubasta.class);
						startActivity(intent);
						
					}

					
					
				}else{
					switch (position) {
					case 0:
						
						break;
					case 1:
						Intent intent = new Intent(rootView.getContext(), RegistrarSubasta.class);
						startActivity(intent);
						break;
					case 2:
						Bundle bundle = new Bundle();
						Intent intent2 = new Intent(rootView.getContext(),MenuActivity.class);
						bundle.putInt("posicion", 6);
						intent2.putExtras(bundle);
						startActivity(intent2);
						break;
					case 3:
						Bundle bundle2 = new Bundle();
						Intent intent22 = new Intent(rootView.getContext(),MenuActivity.class);
						bundle2.putInt("posicion", 5);
						intent22.putExtras(bundle2);
						startActivity(intent22);
						break;

					default:
						break;
					}
				}

			}
		});
		

    }
	
	class lista_adaptador extends ArrayAdapter<ItemLista> {
		Context context;
		Activity activity ;
		Boolean b ;
		
		lista_adaptador(Context context2,Boolean b) {
			super(context2, R.layout.activity_item_list_view, productos);
			this.context = context2;
			this.b=b;
		}

		public View getView(int position, View item, ViewGroup parent) {

			ViewHolder holder;
			activity = (Activity) context;
			if (item == null) {
				LayoutInflater inflater = activity.getLayoutInflater();
				item = inflater.inflate(R.layout.activity_item_list_view, null);

				holder = new ViewHolder();
				holder.vista = (ImageView) item.findViewById(R.id.ivImageListPerfilSubasta);
				holder.nombre = (TextView) item.findViewById(R.id.tvnombre);
				holder.precio = (TextView) item.findViewById(R.id.tvprecio);
				holder.moneda = (ImageView) item.findViewById(R.id.ivMonedaItemLista);

				item.setTag(holder);
			} else {
				holder = (ViewHolder) item.getTag();
			}

			
			holder.nombre.setText(productos.get(position).getNombre());
			if(b){
			if(productos.get(position).getLevel()==1)
				holder.moneda.setImageResource(R.drawable.monedita_dorada);
				
			holder.precio.setText(productos.get(position).getPrecio()+"");
			String urlFoto = getString(R.string.urlImagenProducto) + productos.get(position).getUsuario()+"/productos/"+productos.get(position).getImagen()+"/"+ productos.get(position).getImagen()+".png";
			 ImageDownloader imagend = new ImageDownloader();
			 imagend.download(urlFoto, holder.vista);
			}else{
				holder.precio.setText("");
				holder.vista.setImageResource(R.drawable.ic_launcher);
			}
			 
			return (item);
		}
	}
	
	static class ViewHolder{
    	ImageView vista;
    	TextView nombre;
    	TextView precio;
    	ImageView moneda;
    }
	
	
}
