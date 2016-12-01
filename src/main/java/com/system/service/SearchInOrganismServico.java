package com.system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.json.JSONException;

import com.system.model.Organism;
import com.system.repository.Organisms;

public class SearchInOrganismServico implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Organisms organisms;
	
	public List<Organism> getAllOrganisms() {
		return organisms.getAllOrganisms();
	}
	
	public List<String> getAllCompounds () {
		return organisms.getAllCompounds();
	}
	
	public List<String> getAllEnzymes () {
		return organisms.getAllEnzymes();
	}
	
	public String getJsonForEnzyme(String organism, String ec) {
		return organisms.getJsonForEnzyme(organism, ec);
	}
	
	public List<String> getJsonForPathway(String organism, String substract, String product) {
		return organisms.getJsonForPathway(organism, substract, product);
	}
	
	public Boolean getPathwayInOrganism (String organism, String keyword_1, String keyword_2) {
		return organisms.getPathwayInOrganism(organism, keyword_1, keyword_2);
	}
	
}
