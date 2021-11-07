package com.sise.subappstore.bean;

public class BeanCategoria {
	
    private String name;
    
    private int icon;   
 
    public BeanCategoria(String nombre, int icono) 
    {
        super();
        this.name = nombre;
        this.icon = icono;
    }
 
    public String getNombre() 
    {
        return name;
    }
 
    public void setNombre(String nombre) 
    {
        this.name = nombre;
    }
 
    public int getIcono() 
    {
        return icon;
    }
 
    public void setIcono(int icono) 
    {
        this.icon = icono;
    } 
    
}
