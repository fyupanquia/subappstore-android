package com.sise.subappstore.modelo;

public class ItemLista {
	private String nombre;
	private Double precio;
	private String imagen;
	private String usuario;
    private int idusuario;
    private int id;
    private int level;
    private int oro;
    private int plata;
    private int bronce;
    private int cant_imgs;
    private String descripcion;
    private String categoria;
    
    
    public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getCant_imgs() {
		return cant_imgs;
	}
	public void setCant_imgs(int cant_imgs) {
		this.cant_imgs = cant_imgs;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOro() {
		return oro;
	}
	public void setOro(int oro) {
		this.oro = oro;
	}
	public int getPlata() {
		return plata;
	}
	public void setPlata(int plata) {
		this.plata = plata;
	}
	public int getBronce() {
		return bronce;
	}
	public void setBronce(int bronce) {
		this.bronce = bronce;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public ItemLista(String usuario,String nombre , Double precio,String imagen ){
    		this.nombre = nombre;
    		this.precio = precio ;
    		this.imagen = imagen ;
    		this.usuario = usuario;
    }
	
	public ItemLista(String nombre){
		this.nombre = nombre;
}
	
	public ItemLista(Integer id,Integer idusuario,String usuario,String nombre,String descripcion ,String categoria, Double precio,String imagen,Integer level,Integer cant_imgs){
		this.nombre = nombre;
		this.precio = precio ;
		this.imagen = imagen ;
		this.usuario = usuario;
		this.idusuario = idusuario;
		this.id = id;
		this.level = level;
		this.cant_imgs = cant_imgs;
		this.descripcion = descripcion;
		this.categoria = categoria;
}
	
    public ItemLista(String nombre , String imagen,int oro ,int plata,int bronce){
		this.nombre = nombre;
		this.imagen = imagen ;
		this.oro = oro ;
		this.plata = plata ;
		this.bronce = bronce ;
}
    
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public int getIdusuario() {
		return idusuario;
	}
	public void setIdusuario(int idusuario) {
		this.idusuario = idusuario;
	}
    
    
	
}
