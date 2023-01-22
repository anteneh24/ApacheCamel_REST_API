package com.example.first;

import org.springframework.stereotype.Component;

@Component
public class HardDisk {

	protected String model;
	public HardDisk() {
		super();
	}
	
	public void print() {
		System.out.println("System Hard Disk "+model);
	}
	
}
