package com.example.first;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class First extends HardDisk{
	
	private int id;
	private String name;
	@Autowired
	private HardDisk hardDisk;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public void print() {
		// TODO Auto-generated method stub
		super.model = "First";
		hardDisk.model = "First";
		hardDisk.print();
	}
	

}
