package com.sise.subappstore.modelo;

public class ItemImage {
	private String imagen;
	private String usuario;
	private String grupo;
	
	public ItemImage(String imagen,String usuario,String grupo){
		this.imagen = imagen;
		this.usuario = usuario;
		this.grupo = grupo;
		
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	
	

}
