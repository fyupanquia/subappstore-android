package com.sise.subappstore.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ImagePrepare {
	
		private String rutafoto;
		private Bitmap bImagen;
		private String name;
	


		public Bitmap getbImagen() {
			return bImagen;
		}

		public void setbImagen(Bitmap bImagen) {
			this.bImagen = bImagen;
		}

				public File crearImagen(){
					//Asigna un nombre único a la imagen
					String fecha = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
					name = "img_" + fecha + ".png";
					//Va a crear si no existe la carpeta imagen Android
					//en SDCard
					File archivo = new File
							(Environment.getExternalStorageDirectory().getAbsolutePath()+
									"/ImagenAndroid/");
					archivo.mkdirs();
					File archivoimagen = new File(archivo,name);
					rutafoto = archivoimagen.getAbsolutePath();
					return archivoimagen;
			
				}

				
				public ImagePrepare decodeArchivo(String ruta){
					bImagen = BitmapFactory.decodeFile(ruta);
					return this;
				} 
				
				public ImagePrepare createScaledBitmap(int width,int heigth,boolean b){
					Bitmap _bImagen = Bitmap.createScaledBitmap(bImagen, 400, 400, false);
					bImagen.recycle();
					bImagen = _bImagen;
					return this;
				}
				

				public String getRutafoto() {
					return rutafoto;
				}

				public void setRutafoto(String rutafoto) {
					this.rutafoto = rutafoto;
				}
				
				public String getName() {
					return name;
				}
		
				public void setName(String name) {
					this.name = name;
				}
	
	
	

}
