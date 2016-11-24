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
	
	public List<Organism> getAllOrganisms() throws JSONException {
		return organisms.getAllOrganisms();
	}
	
	//public List<String> getKeywordsInOrganism(String keyword, String organism) {
	//	return organisms.getKeywordsInOrganism(keyword, organism);
	//}
	
	public String getGraphDataInJson(String result) {
		return organisms.getGraphDataInJson(result);
	}
	
	public List<String> getPathwayInOrganism (String organism, String keyword_1, String keyword_2) {
		return organisms.getPathwayInOrganism(organism, keyword_1, keyword_2);
	}

}
