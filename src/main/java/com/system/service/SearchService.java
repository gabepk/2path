package com.system.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import com.system.repository.ConectionDB;

public class SearchService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ConectionDB db;
	
	public List<String> getAllOrganisms() {
		return db.getAllOrganisms();
	}
	
	public List<String> getAllCompounds () {
		return db.getAllCompounds();
	}
	
	public List<String> getAllEnzymes () {
		return db.getAllEnzymes();
	}
	
	public List<String> getJsonForEnzyme(String organism, String ec) {
		return db.getJsonForEnzyme(organism, ec);
	}
	
	public List<String> getJsonForPathway(String organism, String substract, String product) {
		return db.getJsonForPathway(organism, substract, product);
	}
	
}
