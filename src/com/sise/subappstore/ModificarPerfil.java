package com.sise.subappstore;

import com.sise.subappstore.async.FunctionAsync;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.bean.UsuarioBean;
import com.sise.subappstore.http.Http_LoginActivity;
import com.sise.subappstore.http.Http_RegistrarActivity;
import com.sise.subappstore.http.Http_UpdateUserActivity;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ModificarPerfil extends ActionBarActivity implements OnClickListener{
	
	private EditText etNombre,etApellido,etEmail,etDireccion,etTelefono,etClave;
	private Button btnGuardar;
	private UsuarioBean bean ;
	private String msg="";
	private BeanToast bt ;
	private Http_UpdateUserActivity update;
	private SharedPreferences prefsubappstore;
	private UsuarioBean u;
	private Http_LoginActivity login;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modificar_perfil);
		
		prefsubappstore=getSharedPreferences("prefsubappstore", Context.MODE_PRIVATE); 
		String usuario=prefsubappstore.getString("usuario","Desconocido");
		
		editor=prefsubappstore.edit();
	    u=(UsuarioBean) BeanMapper.fromJson(usuario,UsuarioBean.class);	 
		
		bt= new BeanToast(ModificarPerfil.this,"",
				getLayoutInflater(),findViewById(R.id.lyToast),R.id.tvMsgToast);
		
		asignarCampos();
		cargarCampos();
		eventos();
	}
	
	private void asignarCampos(){
		etNombre = (EditText) findViewById(R.id.etModificarPerfilNombre);
		etApellido = (EditText) findViewById(R.id.etModificarPerfilApellido);
		etEmail = (EditText) findViewById(R.id.etModificalEmail);
		etDireccion = (EditText) findViewById(R.id.etModificalPerfilDireccion);
		etTelefono = (EditText) findViewById(R.id.etModificarPerfilTelefono);
		etClave = (EditText) findViewById(R.id.etMOdificarPerfilPass);
		btnGuardar = (Button) findViewById(R.id.btnModificarPerfilSalvar);

	}
	
	private void cargarCampos(){
		etNombre.setText(u.getNombres());
		etApellido.setText(u.getApellidos());
		etEmail.setText(u.getEmail());
		etDireccion.setText(u.getDireccion());
		etTelefono.setText(u.getTelefono());
	}
	
	public void eventos(){
		
		btnGuardar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(etClave.getText().length()>0){
					
		    		new AlertDialog.Builder(ModificarPerfil.this)
		    		.setIcon(R.drawable.ic_launcher)
		    		.setTitle("Advertencia")
		    		.setMessage("Su clave esta siendo modificada.Si no desea hacerlo deje el campo vacío. ¿Confirmas los cambios?")
		    		.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int whith) {
							
							bean = new UsuarioBean();
							bean.setId(u.getId());
							bean.setUsuario(u.getUsuario());
							bean.setNombres(etNombre.getText().toString().trim());
							bean.setApellidos(etApellido.getText().toString().trim());
							bean.setEmail(etEmail.getText().toString().trim());
							bean.setDireccion(etDireccion.getText().toString().trim());
							bean.setTelefono(etTelefono.getText().toString().trim());
							bean.setPassword(etClave.getText().toString().trim());
							if(validarcampos(bean)){
								if(validarpass(bean)){
									new FunctionAsync(ModificarPerfil.this, bean, "Guardando ...")
									.execute(ModificarPerfil.this.getString(R.string.urlVerificar),"encrypt","0");
									
								}else{
									bt.setMensaje(msg);
									bt.show();
								}
							}else{
								bt.setMensaje(msg);
								bt.show();
							}
						}
					})
					.setNegativeButton("Cancelar", null)
					.show();
					
				}else{
					
					new AlertDialog.Builder(ModificarPerfil.this)
		    		.setIcon(R.drawable.ic_launcher)
		    		.setTitle("Advertencia")
		    		.setMessage("Usted esta conservando su clave anterior. ¿Confirmas los cambios?")
		    		.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int whith) {
					
					bean = new UsuarioBean();
					bean.setId(u.getId());
					bean.setUsuario(u.getUsuario());
					bean.setNombres(etNombre.getText().toString().trim());
					bean.setApellidos(etApellido.getText().toString().trim());
					bean.setEmail(etEmail.getText().toString().trim());
					bean.setDireccion(etDireccion.getText().toString().trim());
					bean.setTelefono(etTelefono.getText().toString().trim());
					
					if(validarcampos(bean)){
						update = new Http_UpdateUserActivity(ModificarPerfil.this, "Guardando ...", bean);
						update.execute("0");
					}else{
						bt.setMensaje(msg);
						bt.show();
					}
					
						}
					})
					.setNegativeButton("Cancelar", null)
					.show();
					
					
					
					
				}	
				
			}
		});
	}
	
/* INICIO METODOS DE VALIDACION */
	
	private boolean validarcampos(UsuarioBean u){
		boolean r=false;
		if(u.getNombres().length()>0 &&
				u.getApellidos().length()>0 &&
					u.getDireccion().length()>0 &&
						u.getEmail().length()>0 &&
							u.getTelefono().length()>0 ){
			
			r=true;
		}else{
			this.msg = "Todos los campos son requeridos  ...";
		}
	return r;
	}
	
	
	private boolean validarpass(UsuarioBean u){
		boolean r=false;

			 r = (u.getPassword().length()>=8);
			 
			 	if(!r)
				 this.msg = "Su clave debe tener al menos 8 caracteres";
			 return r;

	}
	
	/* FIN METODOS DE VALIDACION */


	
	/* INICIO METODOS ASYNC*/
			public void updateUser(String pass){
				bean.setPassword(pass);
				update = new Http_UpdateUserActivity(this, "Confirmando cambios ...", bean);
				update.execute("1");
			}
	/* FIN METODOS ASYNC*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modificar_perfil, menu);
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
	
	//Se ejecuta al fallar un Registro de Usuario
	public void subError(){
		//Toast.makeText(this, "El servicio de registro no esta disponible en estos instantes ...", Toast.LENGTH_LONG).show();
		bt.setMensaje("El servicio de actualización de datos no esta disponible en estos instantes ...");
		bt.show();
	}
	public void subResultado(){
		
		login = new Http_LoginActivity(this, "Cargando datos ...", bean);
		login.setB("1");
		login.execute();
		
	}
	
	public void subResultado2(){
		
		editor.putString("usuario", BeanMapper.toJson(login.usuario, false)); // ###### Almacenamos en memoria datos del usaurio
		editor.commit();
		
		Bundle bundle = new Bundle();
		Intent intent = new Intent(ModificarPerfil.this,MenuActivity.class);
		bundle.putInt("posicion", 0 );
		intent.putExtras(bundle);
		startActivity(intent);
		bt.setMensaje("Datos actualizados correctamente .");
		bt.show();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
