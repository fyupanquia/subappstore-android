package com.sise.subappstore;

//import com.sise.appcinema.http.Http_RegistrarActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.sise.subappstore.async.FunctionAsync;
import com.sise.subappstore.bean.BeanToast;
import com.sise.subappstore.bean.UsuarioBean;
import com.sise.subappstore.http.Http_RegistrarActivity;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class RegistroActivity extends ActionBarActivity implements OnClickListener{
	
	// ###### Variables de acceso a datos
	private ImageView imgSubir;
	private EditText txtUsuario, txtClave, txtNombre,txtApellido,txtEmail,txtDireccion,txtTelefono;

	// ###### Variables de funciones
    private Button btnAceptar;
    private ImageButton btnTomarFoto, btnElegirGaleria;
    
    // ###### Variables de control de imágenes
    private Bitmap imagenasubir;
    private String rutafoto;
	private static final int METODO_CAMARA = 1000;
	private static final int METODO_GALERIA = 2000;
	private Http_RegistrarActivity registro;
	private UsuarioBean bean ;
	private String msg;
	BeanToast bt ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);		
		setTitle("SubAppStore Registro");
		
		bt= new BeanToast(RegistroActivity.this,"",
				getLayoutInflater(),findViewById(R.id.lyToast),R.id.tvMsgToast);
		
		imgSubir = (ImageView) findViewById(R.id.imgSubir);
		txtUsuario=(EditText)findViewById(R.id.etUsuarioRegistroUser);
		txtClave=(EditText)findViewById(R.id.etClaveRegistroUser);
		txtNombre=(EditText)findViewById(R.id.etNombreRegistroUser);
		txtApellido=(EditText)findViewById(R.id.etApellidoRegistroUser);
		txtEmail=(EditText)findViewById(R.id.etEmailRegistroUser);
		txtDireccion=(EditText)findViewById(R.id.etDireccionRegistroUser);
		txtTelefono=(EditText)findViewById(R.id.etTelefonoRegistroUser);
			
		btnAceptar=(Button) findViewById(R.id.btnAceptar);
		btnAceptar.setOnClickListener(this);
		btnTomarFoto=(ImageButton) findViewById(R.id.btnTomarFoto);
		btnTomarFoto.setOnClickListener(this);
		btnElegirGaleria=(ImageButton) findViewById(R.id.btnElegirGaleria);
		btnElegirGaleria.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.registro, menu);
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

		switch(v.getId()){
		case R.id.btnTomarFoto:
			try {
				Intent intentcamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
				File f = crearImagen();
				intentcamara.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				startActivityForResult(intentcamara, METODO_CAMARA);
			} catch (Exception ex) {
				
				bt.setMensaje("Asegúrese que si dispositivo este en correcto funcionamiento");
				bt.show();
			}
			break;
		case R.id.btnElegirGaleria:
			Intent elegirgaleria = new Intent(Intent.ACTION_PICK);
			elegirgaleria.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					MediaStore.Images.Media.CONTENT_TYPE);
			startActivityForResult(elegirgaleria, METODO_GALERIA);
			break;
		case R.id.btnAceptar:
			if(imagenasubir!=null){
				bean = new UsuarioBean();
				bean.setNombres(txtNombre.getText().toString().trim());
				bean.setApellidos(txtApellido.getText().toString().trim());
				bean.setDireccion(txtDireccion.getText().toString());
				bean.setEmail(txtEmail.getText().toString().trim());
				bean.setTelefono(txtTelefono.getText().toString().trim());
				bean.setUsuario(txtUsuario.getText().toString().trim());
				bean.setPassword(txtClave.getText().toString().trim());
				if(validarcampos(bean)){
				
					if(validarpass(bean)){
						
						if(validaruser(bean)){
							new FunctionAsync(RegistroActivity.this, bean, "Verificando ...")
							.execute(this.getString(R.string.urlVerificar),"encrypt","1");
						}else{
							//Toast.makeText(RegistroActivity.this,this.msg ,Toast.LENGTH_SHORT).show();
							bt.setMensaje(this.msg);
							bt.show();
						}
						
					}else{
						//Toast.makeText(RegistroActivity.this,this.msg ,Toast.LENGTH_SHORT).show();
						bt.setMensaje(this.msg);
						bt.show();
					}

				
				}else{
					//Toast.makeText(RegistroActivity.this,this.msg ,Toast.LENGTH_SHORT).show();
					bt.setMensaje(this.msg);
					bt.show();
				}
			}else{ //Toast.makeText(RegistroActivity.this, "Asigne una imagen para su perfil por favor ...",Toast.LENGTH_SHORT).show();
			bt.setMensaje("Asigne una imagen para su perfil por favor ...");
			bt.show();
			}
			break;
		}
	}
	
	private File crearImagen(){
				//Asigna un nombre único a la imagen
				String fecha = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
				String nombreimagen = "img_" + fecha + ".png";
				//Va a crear si no existe la carpeta imagen Android
				//en SDCard
				File archivo = new File
						(Environment.getExternalStorageDirectory().getAbsolutePath()+
								"/ImagenAndroid/");
				archivo.mkdirs();
				File archivoimagen = new File(archivo,nombreimagen);
				rutafoto = archivoimagen.getAbsolutePath();
	 return archivoimagen;
		
	}
	
	private boolean validarcampos(UsuarioBean u){
		boolean r=false;
		if(u.getNombres().length()>0 &&
				u.getApellidos().length()>0 &&
					u.getDireccion().length()>0 &&
						u.getEmail().length()>0 &&
							u.getTelefono().length()>0 &&
								u.getUsuario().length()>0 &&
									u.getPassword().length()>0 ){
			
			r=true;
		}else{
			this.msg = "Todos los campos son requeridos para su registro ...";
		}
	return r;
	}
	
	private boolean validaruser(UsuarioBean u){
		Boolean b = false;
		String uu = u.getUsuario();
		String tempu =  u.getUsuario().replace(" ", "");
		b = uu.equalsIgnoreCase(tempu);
		
		if(!b)
			this.msg="No deben existir espacios en blanco en su usuario asignado";
		
		return b;
	}
	
	private boolean validarpass(UsuarioBean u){
		boolean r=false;

			 r = (u.getPassword().length()>=8);
			 
			 	if(!r)
				 this.msg = "Su clave debe tener al menos 8 caracteres";
			 return r;

	}

		@Override
		protected void onActivityResult(int requestCode,int resultCode,Intent data){
			if(resultCode == RESULT_OK){
				if(requestCode == METODO_CAMARA){
					Bitmap imagenCompleta = BitmapFactory.decodeFile(rutafoto);
					//Resize redimencionar la imagen
					Bitmap imagenResize = Bitmap.createScaledBitmap
							(imagenCompleta, 400, 400, false);
					imagenasubir = imagenResize;
					
					imagenCompleta.recycle();
					imgSubir.setImageBitmap(imagenasubir);
				}else if (requestCode == METODO_GALERIA) {
					Uri imagenselecciona = data.getData();
					try {
						Bitmap imagenselec = MediaStore.Images.Media.getBitmap
								(getContentResolver(), imagenselecciona);
						Bitmap imagenResize = Bitmap.createScaledBitmap
								(imagenselec, 400, 400, false);
						imagenasubir = imagenResize;
						imagenselec.recycle();
						imgSubir.setImageBitmap(imagenasubir);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						//Toast.makeText(this, "Hubo error", Toast.LENGTH_SHORT).show();
						bt.setMensaje("Hubo error : "+e.getMessage());
						bt.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//Toast.makeText(this, "Hubo error", Toast.LENGTH_SHORT).show();
						bt.setMensaje("Hubo error : "+e.getMessage());
						bt.show();
						
					}
				}
			}
		}


		//Se ejecuta al realizar un Registro de Usuario correcto
		public void subResultado(){
			//new FunctionAsync(RegistroActivity.this, bean, "Suscribiendo ...",this).execute(this.getString(R.string.urlSuscribirse),"Suscribir");
			//Toast.makeText(RegistroActivity.this, "Gracias por registrarte ....",Toast.LENGTH_SHORT).show();
			bt.setMensaje("Gracias por registrarte ....");
			bt.show();
			
			//Toast.makeText(RegistroActivity.this, "Pase a Loguearse ....",Toast.LENGTH_LONG).show();
			Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
			startActivity(intent);
			bt.setMensaje("Pase a Loguearse ....");
			bt.show();
		}
		
		public void redirect(){
			//Toast.makeText(this, "Se registró correctamente ...", Toast.LENGTH_LONG).show();
			bt.setMensaje("Se registró correctamente ...");
			bt.show();
			Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
			startActivity(intent);
		}
		
		//Se ejecuta al fallar un Registro de Usuario
		public void subError(){
			//Toast.makeText(this, "El servicio de registro no esta disponible en estos instantes ...", Toast.LENGTH_LONG).show();
			bt.setMensaje("El servicio de registro no esta disponible en estos instantes ...");
			bt.show();
		}

		public void registroUser(String pass){
			bean.setPassword(pass);
			registro = new Http_RegistrarActivity(this, "Registrando ...", bean);
			registro.execute(imagenasubir);
		}
}
