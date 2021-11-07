package com.sise.subappstore.modelo;

public class CategoriaNetwork 
{
	private String name;
	
	private int icon;	
	
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CategoriaNetwork(Integer id ,String nombre, int icono) 
	{
		super();
		this.name = nombre;
		this.icon = icono;
		this.id=id;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public int getIcon() 
	{
		return icon;
	}

	public void setIcon(int icon) 
	{
		this.icon = icon;
	}	

}