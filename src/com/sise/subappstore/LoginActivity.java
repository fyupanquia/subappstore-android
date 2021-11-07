package com.sise.subappstore;

import com.sise.subappstore.async.FunctionAsync;
import com.sise.subappstore.bean.BeanMapper;
import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.bean.UsuarioBean;
import com.sise.subappstore.http.Http_LoginActivity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity implements OnClickListener{
	
	private EditText txtUsuario,txtClave;
	private ImageButton btnIngresar; 
	private TextView txtRegistrar;
	private Http_LoginActivity login;
	private UsuarioBean usuario;
	private String msg;
	
	//SharePreferences
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	
	private  BeanToast bt ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//setTitle("SubAppStore Login");
		setTitle("LOGIN");
		
		bt= new BeanToast(LoginActivity.this,"",
				getLayoutInflater(),findViewById(R.id.lyToast),R.id.tvMsgToast);
		
		/* REFERENCIANDO ELEMENTO DEL ACTIVITY*/
		txtUsuario=(EditText)findViewById(R.id.txtUsuario);
		txtClave=(EditText)findViewById(R.id.txtClave);
		btnIngresar=(ImageButton)findViewById(R.id.btnIngresar);
		txtRegistrar=(TextView)findViewById(R.id.txtRegistrar);
		
		
		/*INDICANDO EVENTOS CLICK*/
		btnIngresar.setOnClickListener(this);
		txtRegistrar.setOnClickListener(this);
		
		
		/* OBTENIENDO VARIABLES DE SESIÓN*/
		pref=getSharedPreferences("prefsubappstore", Context.MODE_PRIVATE);
		editor=pref.edit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
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

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.txtRegistrar) { // IR A ACTIVITY REGISTRAR
			Intent intent = new Intent(this,RegistroActivity.class);
			startActivity(intent);
		} else if(v.getId() == R.id.btnIngresar){ // PROCEDER A AUTENTICAR
				
				usuario = new UsuarioBean();
				
				if(validaruser(txtUsuario.getText().toString().trim())){
					if(validarpass(txtClave.getText().toString().trim())){
						usuario.setUsuario(txtUsuario.getText().toString().trim());
						usuario.setPassword(txtClave.getText().toString().trim());
						
						new FunctionAsync(LoginActivity.this, usuario, "Autenticando ...")
						 .execute(this.getString(R.string.urlAutenticar),"login");
						
					}else{
						bt.setMensaje(this.msg);
						bt.show();
					}
					
				}else{
					bt.setMensaje(this.msg);
					bt.show();
				}

				
				
					
			}
		}
	
	private boolean validaruser(String u){
		Boolean b = false;
		
		if(u.length()>0){
			
			String uu = u;
			String tempu =  u.replace(" ", "");
			b = uu.equalsIgnoreCase(tempu);
			
			if(!b)
				this.msg="No deben existir espacios en blanco en el nombre de usuario...";
		}else
			this.msg="Debe indicar el nombre de usuario ...";

		
		return b;
	}
	
	
	private boolean validarpass(String p){
		Boolean b = false;
		
		if(p.length()>0){
			
			if(p.length()>=8){

				b = true;

			}else{
				this.msg="Las claves deben tener un mínimo de 8 caracteres...";
			}

		}else
			this.msg="Debe indicar la contraseña de su cuenta ...";

		
		return b;
	}
		
	
	
	public void subResultado(){
		
			try {
						if(login.usuario.getId() != 0 && login.usuario.getId() != -1){
							
							editor.putString("usuario", BeanMapper.toJson(login.usuario, false)); // ###### Almacenamos en memoria datos del usaurio
							editor.commit();
							
							Intent intent=new Intent(this,MenuActivity.class);
							startActivity(intent);
							
							
						}else{
							Toast.makeText(this, "Usuario Incorrecto",Toast.LENGTH_LONG).show();
						}
				} catch (Exception e) {
							Toast.makeText(this, "Usuario Incorrecto",Toast.LENGTH_LONG).show();
				}
	}
	
	public void getUser(){
		
		login = new Http_LoginActivity(this, "Accediendo ...", usuario);
		login.execute();
		
	}
	
	@Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return true;
    }
}
