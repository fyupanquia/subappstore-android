package com.sise.subappstore.modelo;

public class ItemNavigation {

	private String titulo;
	private int icono;
	private String count = "0";
	private boolean isCounterVisible = false;
	
	public ItemNavigation(){}

	public ItemNavigation(String title, int icon){
		this.titulo = title;
		this.icono = icon;
	}
	
	public ItemNavigation(String title, int icon, boolean isCounterVisible, String count){
		this.titulo = title;
		this.icono = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getIcono() {
		return icono;
	}

	public void setIcono(int icono) {
		this.icono = icono;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public boolean isCounterVisible() {
		return isCounterVisible;
	}

	public void setCounterVisible(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}
	
}
