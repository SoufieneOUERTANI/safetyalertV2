package com.ouertani.safetyalertV2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ouertani.safetyalertV2.model.Person;
import com.ouertani.safetyalertV2.service.IPersonService;

import lombok.Data;

@Data
@Service

public class PersonService implements IPersonService{
	
	

	@Override
	public Person addPerson(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person putPerson(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deletePerson(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Person> getPersonAdress(String Adress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person getPerson(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> getPersonCity(String city) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
