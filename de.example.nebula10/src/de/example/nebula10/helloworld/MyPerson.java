package de.example.nebula10.helloworld;

import java.util.Date;

public class MyPerson{
	
	public int id;
	public String name;
	public Date birthDate;
	public boolean animal;
	public int distance;

	 MyPerson(int id, String name, Date birthDate) {
		this.id= id;
		this.name = name;
		this.birthDate = birthDate;
	}
	
	MyPerson(int id, String name, Date birthDate, boolean isAnimal, int distance) {
		this.setAnimal(isAnimal);
		this.setDistance(distance);
	}

	public boolean isAnimal() {
		return animal;
	}

	public void setAnimal(boolean animal) {
		this.animal = animal;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
}
