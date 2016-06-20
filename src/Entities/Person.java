package Entities;

import java.util.List;

public class Person {
	private String id;
	private String nome;
	private String email;
	private  List<Availability> availabilities;
	
	public Person(String id, String nome, String email, List<Availability> availabilities){
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.availabilities = availabilities;
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

	public List<Availability> getAvailabilities() {
		return availabilities;
	}

	public void setAvailabilities(List<Availability> availabilities) {
		this.availabilities = availabilities;
	}
}
