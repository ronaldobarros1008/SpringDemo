package com.example.demo.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Person;
import com.example.demo.repositories.PersonRepository;

@RestController
@RequestMapping(path="/persons")
public class PersonResource {
	
	private PersonRepository personRepository;
	
	public PersonResource(PersonRepository personRepository) {
		super();
		this.personRepository = personRepository;
	};
	
	@PostMapping
	public ResponseEntity<Person> save(Person person){
		personRepository.save(person);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<Person>> getAll(){
		List<Person> persons = new ArrayList<>();
		persons = personRepository.findAll();
		return new ResponseEntity<>(persons, HttpStatus.OK);
	}
	
	@GetMapping(path="/{id}")
	public ResponseEntity<Optional<Person>> getById(@PathVariable Integer id){
		Optional<Person> person;
		try {
			person = personRepository.findById(id);
			return new ResponseEntity<Optional<Person>>(person, HttpStatus.OK);
		}catch(NoSuchElementException nsee){
			return new ResponseEntity<Optional<Person>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping(path="/{id}")
	public ResponseEntity<Optional<Person>> deleteById(@PathVariable Integer id){
		try {
			personRepository.deleteById(id);
			return new ResponseEntity<Optional<Person>>(HttpStatus.OK);
		}catch(NoSuchElementException nsee) {
			return new ResponseEntity<Optional<Person>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Person> update(@PathVariable Integer id, Person newPerson){
		return personRepository.findById(id)
				.map(person -> {
					person.setName(newPerson.getName());
					person.setAge(newPerson.getAge());
					Person personoUpdated = personRepository.save(person);
					return ResponseEntity.ok().body(personoUpdated);
				}).orElse(ResponseEntity.notFound().build());
	}

}












