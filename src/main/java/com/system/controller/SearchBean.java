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

import com.system.service.SearchService;

@Named
@ManagedBean
@SessionScoped
public class SearchBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchService service; 
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private HttpServletResponse response;
	
	private String organismSelected;
	
	public static List<String> organisms = new ArrayList<>();
	
	public void preRender() {
		organisms = service.getAllOrganisms();
	}
	
	public SearchBean() {
	}
	
	public void searchEnzyme() throws ServletException, IOException {
		request.getSession().setAttribute("organismSelected", organismSelected);
	    response.sendRedirect("/EnzymeGraph/SearchEnzyme.xhtml");
		
	    System.out.println("organismSelected: "+ organismSelected);
		facesContext.responseComplete();
	}
	
	public void searchPathway() throws ServletException, IOException {
		request.getSession().setAttribute("organismSelected", organismSelected);
	    response.sendRedirect("/EnzymeGraph/SearchPathway.xhtml");
	    
	    System.out.println("organismSelected: "+ organismSelected);
		facesContext.responseComplete();
	}
	
	public List<String> getOrganisms() {
		return organisms;
	}

	public String getOrganismSelected() {
		return organismSelected;
	}

	public void setOrganismSelected(String organismSelected) {
		this.organismSelected = organismSelected;
	}
}
