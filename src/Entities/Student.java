package Entities;

public class Student {
	private String id;
	private String nome;
	private String email;
	
	
	public Student(String string, String nome, String email){
		this.id = string;
		this.nome = nome;
		this.email = email;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	

}
