package com.sise.subappstore.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class UsuarioBean {

	@JsonProperty
	private Integer id = 0;
	@JsonProperty
	private String codigo ="";
	@JsonProperty
	private String nombres ="";
	@JsonProperty
	private String apellidos ="";
	@JsonProperty
	private String email ="";
	@JsonProperty
	private String direccion ="";
	@JsonProperty
	private String telefono ="";
	@JsonProperty
	private String usuario ="";
	@JsonProperty
	private Integer level =0;
	@JsonProperty
	private String password ="";
	@JsonProperty
	private String created_at ="";
	@JsonProperty
	private String updated_at ="";
	
	
	
	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public UsuarioBean() {
		super();
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getNombres() {
		return nombres;
	}


	public void setNombres(String nombres) {
		this.nombres = nombres;
	}


	public String getApellidos() {
		return apellidos;
	}


	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public Integer getLevel() {
		return level;
	}


	public void setLevel(Integer level) {
		this.level = level;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getCreated_at() {
		return created_at;
	}


	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}


	public String getUpdated_at() {
		return updated_at;
	}


	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	

	
	
	
	
}
