package com.sise.subappstore.bean;

import java.math.BigInteger;

public class BeanCompra {
	private String numtarjeta;
	private Integer clavTarjeta;
	private Integer idUsuario;
	private Integer cantDoraradas;
	private Integer cantPlateadas;
	
	
	public BeanCompra(){
		
	}
	public BeanCompra(String numTarjeta,Integer clavTarjeta,Integer idUsuario,Integer cantDoradas,Integer cantPlateadas){
		this.numtarjeta = numTarjeta;
		this.clavTarjeta = clavTarjeta;
		this.idUsuario = idUsuario;
		this.cantDoraradas = cantDoradas;
		this.cantPlateadas = cantPlateadas;
	}
	
	public String getNumtarjeta() {
		return numtarjeta;
	}
	public void setNumtarjeta(String numtarjeta) {
		this.numtarjeta = numtarjeta;
	}
	public Integer getClavTarjeta() {
		return clavTarjeta;
	}
	public void setClavTarjeta(Integer clavTarjeta) {
		this.clavTarjeta = clavTarjeta;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getCantDoraradas() {
		return cantDoraradas;
	}
	public void setCantDoraradas(Integer cantDoraradas) {
		this.cantDoraradas = cantDoraradas;
	}
	public Integer getCantPlateadas() {
		return cantPlateadas;
	}
	public void setCantPlateadas(Integer cantPlateadas) {
		this.cantPlateadas = cantPlateadas;
	}
	
	
}
