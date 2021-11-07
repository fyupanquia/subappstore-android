package com.sise.subappstore.modelo;

public class item_lista_about {
	
	private int idImagen;
	private String titulo;
	private String descripcion;
	
	public item_lista_about(int idImagen, String titulo) {
		super();
		this.idImagen = idImagen;
		this.titulo = titulo;
	}
	
	public int getIdImagen() {
		return idImagen;
	}
	public void setIdImagen(int idImagen) {
		this.idImagen = idImagen;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}

}
