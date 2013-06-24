package de.example.nebula.nattable;

import java.util.Date;

public class Person {
	private int id;
	private String name;
	private Date birthDate;

	public Person(int id, String name, Date birthDate) {
		this.id = id; 
		this.name = name;    
		this.birthDate = birthDate;
	}

	public int getId() {return id;}
	public String getName() {return name;}
	public Date getBirthDate() {return birthDate;}
}