package com.system.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.json.JSONException;

import com.system.model.Organism;
import com.system.service.SearchInOrganismServico;

@Named
@ManagedBean
@SessionScoped
public class SearchPathwayBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchInOrganismServico searchInOrganismServico; 
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private HttpServletResponse response;
	
	private Organism organismSelected;
	private String keyword_1, keyword_2;
	
	public static List<String> compounds = new ArrayList<>();
	
	public void preRender() {
		compounds = searchInOrganismServico.getAllCompounds();
	}
	
	public SearchPathwayBean() {
	}
	
	public void searchPathwayInOrganism() throws ServletException, IOException {
		System.out.println("organismSelected: "+ organismSelected);
		System.out.println("keyword 1: "+ keyword_1);
		System.out.println("keyword 2: "+ keyword_2);
		
		request.getSession().setAttribute("organismSelected", organismSelected.getTaxName());
		request.getSession().setAttribute("keyword_1", keyword_1);
		request.getSession().setAttribute("keyword_2", keyword_2);
	    //response.sendRedirect("/EnzymeGraph/SearchPathway.xhtml"); // AJAX TRUE + GRAFO ou FALSE
		
		facesContext.responseComplete();
	}

	public List<String> sugerirKeywords(String consulta) {
		List<String> keywordsSugested = new ArrayList<>();
		
		for (String key : compounds) {
            if (key.toLowerCase().startsWith(consulta.toLowerCase())) {
            	keywordsSugested.add(key);
            }
        }
		return keywordsSugested;
	}
	

	public Organism getOrganismSelected() {
		return organismSelected;
	}

	public String getKeyword_1() {
		return keyword_1;
	}

	public void setKeyword_1(String keyword_1) {
		this.keyword_1 = keyword_1;
	}
	public String getKeyword_2() {
		return keyword_2;
	}

	public void setKeyword_2(String keyword_2) {
		this.keyword_2 = keyword_2;
	}
	
	public List<String> getCompounds() {
		return compounds;
	}
	
}
