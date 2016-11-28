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
	
	/*@Inject
	private HttpServletResponse response;*/
	
	private String organism, substract, product;
	
	public static List<String> compounds = new ArrayList<>();
	
	public void preRender() {
		compounds = searchInOrganismServico.getAllCompounds();
	}
	
	public SearchPathwayBean() {
	}
	
	public void searchPathwayInOrganism() throws ServletException, IOException {
		organism = (String) request.getSession().getAttribute("organismSelected");
		buildGraph();
		facesContext.responseComplete();
	}

	public List<String> suggestKeywords(String consulta) {
		List<String> keywordsSugested = new ArrayList<>();
		
		for (String key : compounds) {
            if (key.toLowerCase().startsWith(consulta.toLowerCase())) {
            	keywordsSugested.add(key);
            }
        }
		return keywordsSugested;
	}

	public void buildGraph() {
		System.out.println("Organism: "+ organism);
		System.out.println("Substract: "+ substract);
		System.out.println("Product: "+ product);
	}
	
	public String getSubstract() {
		return substract;
	}

	public void setSubstract(String substract) {
		this.substract = substract;
	}
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	
	public List<String> getCompounds() {
		return compounds;
	}
	
}
