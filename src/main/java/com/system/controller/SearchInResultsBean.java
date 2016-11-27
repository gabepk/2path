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

import com.system.service.SearchInOrganismServico;

@Named
@ManagedBean
@SessionScoped
public class SearchInResultsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchInOrganismServico searchInOrganismServico;
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private HttpServletResponse response;
	
	public static List<String> results = new ArrayList<>();
	
	private Boolean path_between_components;
	private String resultSelected;
	
	public void preRender() {
		System.out.println("Organismo pra procurar: " + request.getSession().getAttribute("organismSelected"));
		System.out.println("Keyword pra procurar: " + request.getSession().getAttribute("keyword"));
		
		System.out.println("Keyword_1: " + request.getSession().getAttribute("keyword_1"));
		System.out.println("Keyword_2: " + request.getSession().getAttribute("keyword_2"));
		
		/*if (request.getSession().getAttribute("organismSelected") != null &&
				request.getSession().getAttribute("keyword") != null)
		{
			results = searchInOrganismServico.getKeywordsInOrganism(
					request.getSession().getAttribute("organismSelected").toString(),
					request.getSession().getAttribute("keyword").toString());
		}*/
		
		if (request.getSession().getAttribute("organismSelected") != null &&
				request.getSession().getAttribute("keyword_1") != null &&
				request.getSession().getAttribute("keyword_2") != null)
		{
			path_between_components = searchInOrganismServico.getPathwayInOrganism(
					request.getSession().getAttribute("organismSelected").toString(),
					request.getSession().getAttribute("keyword_1").toString(),
					request.getSession().getAttribute("keyword_2").toString());
			results.add(path_between_components.toString());
		}
		
		
	}
	
	public SearchInResultsBean() {
		
	}
	
	public void searchResultDetails() throws ServletException, IOException {
		System.out.println("resultSelected: "+ resultSelected);
		request.getSession().setAttribute("resultSelected", resultSelected);
	    response.sendRedirect("/EnzymeGraph/ResultDetails.xhtml");
		
		facesContext.responseComplete();
	}
	
	public void voltar() throws IOException {
		request.getSession().setAttribute("organismSelected", null);
		request.getSession().setAttribute("keyword", null);
		response.sendRedirect("/EnzymeGraph/Search.xhtml");
		
		facesContext.responseComplete();
	}

	public List<String> getResults() {
		return results;
	}

	public void setResults(List<String> results) {
		SearchInResultsBean.results = results;
	}

	public String getResultSelected() {
		return resultSelected;
	}

	public void setResultSelected(String resultSelected) {
		this.resultSelected = resultSelected;
	}

}
