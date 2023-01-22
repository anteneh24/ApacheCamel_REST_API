package com.example.first.Model;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name",nullable = false)
	private String name;
	@Column(name = "desciption",length = 45 )
	private String description;  
	
	@Column(name="birthDate",length=45)
	private String birthDate;
	
	@ManyToOne
	@JoinColumn(name = "class_id",nullable = false)
	private Classes classNumber;

}
