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
public class SearchInOrganismBean implements Serializable {

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
	
	public static List<Organism> organisms = new ArrayList<>();
	
	public void preRender() {
		// TODO Verificar se Neo4j esta on. Se nao estiver, ligar remotamente. Como ????
		organisms = searchInOrganismServico.getAllOrganisms();
	}
	
	public SearchInOrganismBean() {
	}
	
	public void searchEnzyme() throws ServletException, IOException {
		request.getSession().setAttribute("organismSelected", "");
	    response.sendRedirect("/EnzymeGraph/SearchEnzyme.xhtml");
		facesContext.responseComplete();
	}
	
	public void searchPathway() throws ServletException, IOException {
		request.getSession().setAttribute("organismSelected", "");
	    response.sendRedirect("/EnzymeGraph/SearchPathway.xhtml");
		facesContext.responseComplete();
	}
	
	public void searchEnzymeInOrganism() throws ServletException, IOException {
		request.getSession().setAttribute("organismSelected", (String) organismSelected.getTaxName());
	    response.sendRedirect("/EnzymeGraph/SearchEnzyme.xhtml");
		
	    System.out.println("organismSelected: "+ organismSelected.getTaxName());
	    
		facesContext.responseComplete();
	}
	
	public void searchPathwayInOrganism() throws ServletException, IOException {
		System.out.println("organismSelected: "+ organismSelected);
		request.getSession().setAttribute("organismSelected", organismSelected.getTaxName());
	    response.sendRedirect("/EnzymeGraph/SearchPathway.xhtml");
		
		facesContext.responseComplete();
	}
	
	public List<Organism> getOrganisms() {
		return organisms;
	}

	public Organism getOrganismSelected() {
		return organismSelected;
	}

	public void setOrganismSelected(Organism organismSelected) {
		this.organismSelected = organismSelected;
	}
}
