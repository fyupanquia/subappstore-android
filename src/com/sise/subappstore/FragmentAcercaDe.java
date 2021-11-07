package com.sise.subappstore;

import java.util.ArrayList;
import java.util.List;
import com.sise.subappstore.R;
import com.sise.subappstore.modelo.item_lista_about;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

public class FragmentAcercaDe extends Fragment{
	View rootView;
	private ListView lstImagenes;
	private TextView lblTitulo;
	public List<item_lista_about> datos = new ArrayList<item_lista_about>();
	public FragmentAcercaDe(){}
	
	public void LlenarDatos() {
		datos.add(new item_lista_about(R.drawable.quienesomos,"¿QUIENES SOMOS?"));
		datos.add(new item_lista_about(R.drawable.quienesomos,"MISION"));
		datos.add(new item_lista_about(R.drawable.quienesomos,"VISION"));
		datos.add(new item_lista_about(R.drawable.quienesomos,"¿COMO FUNCIONA SUBAPPSTORE?"));
		datos.add(new item_lista_about(R.drawable.quienesomos,"OBSERVACIONES"));
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
       rootView = inflater.inflate(R.layout.activity_fragment_acerca_de, container, false);
       LlenarDatos();
       
       lblTitulo = (TextView) rootView.findViewById(R.id.lblTitulo);
       lstImagenes = (ListView) rootView.findViewById(R.id.lstImagenes); 
       lista_adaptador adaptador = new lista_adaptador(rootView.getContext());
       lstImagenes.setAdapter(adaptador);
       lstImagenes.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				
				Intent intent = new Intent(rootView.getContext(), InfoActivity.class);
				intent.putExtra("titulo", datos.get(pos).getTitulo());
				intent.putExtra("posicion", pos);
				intent.putExtra("img", datos.get(pos).getIdImagen());
				startActivity(intent);
			}
		});
        return rootView;
    }
    
    	class lista_adaptador extends ArrayAdapter<item_lista_about> {
    		Activity context;
    	
    	lista_adaptador(Context context2) {
			super(context2, R.layout.itemlistview, datos);
			this.context = (Activity) context2;
		}
    	
    	public View getView(int position, View item, ViewGroup parent) {
    		ViewHolder holder;
    		if (item == null){
    			LayoutInflater inflater = context.getLayoutInflater();
    			item = inflater.inflate(R.layout.itemlistview, null);
    			
    			holder = new ViewHolder();
    			holder.imagen = (ImageView) item.findViewById(R.id.imagenList);
    			holder.titulo = (TextView) item.findViewById(R.id.lblTitulo);
    			
    			item.setTag(holder);
    		}else{
    			holder = (ViewHolder) item.getTag();
    		}
    		holder.titulo.setText(datos.get(position).getTitulo());
    		holder.imagen.setImageResource(datos.get(position).getIdImagen());
    		return (item);
    	}
	}
    	
        static class ViewHolder {
    		ImageView imagen;
    		TextView titulo;
    	}
}
