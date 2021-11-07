package com.sise.subappstore.bean;

public class ProductoBean {
	private String titulo="";
	private String Descripcion="";
	private double precio=0;
	private String usuario ;
	private int id=0;
	private int categoria;
	private int limitcat;
	
	public int getLimitcat() {
		return limitcat;
	}


	public void setLimitcat(int limitcat) {
		this.limitcat = limitcat;
	}


	public int getCategoria() {
		return categoria;
	}


	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public boolean validar(){
		boolean b=false;
		if(titulo!="" && titulo.length()<50 && Descripcion.length()<200 && Descripcion!="" && precio!=0 && id!=0 && usuario!="" && categoria>0 && categoria<=limitcat){
			b = true;
		}
		return b;
	}
	
	
	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo.trim();
	}
	public String getDescripcion() {
		return Descripcion;
	}
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion.trim();
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
}
