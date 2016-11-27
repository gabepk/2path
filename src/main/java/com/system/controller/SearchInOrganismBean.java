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
	
	private static final List<String> KEYWORDS = new ArrayList<>();
	private Organism organismSelected;
	private String keyword, keyword_1, keyword_2;
	
	public static List<Organism> organisms = new ArrayList<>();
	
	static {
    	KEYWORDS.add("1-Deoxy-D-xylulose 5-phosphate");
    	KEYWORDS.add("2-Phospho-4-(cytidine 5'-diphospho)-2-C-methyl-D-erythritol");
    	KEYWORDS.add("Water");
		KEYWORDS.add("Biology");
		KEYWORDS.add("Melanine");
		KEYWORDS.add("2Path");
		KEYWORDS.add("Teste");
		KEYWORDS.add("Hydro");
		KEYWORDS.add("Water with muscle");
	}
	
	public void preRender() throws JSONException {
		organisms = searchInOrganismServico.getAllOrganisms();
	}
	
	/*public String preRender() {
		String x = "oi";
		return x;
	}*/
	
	public SearchInOrganismBean() {
	}
	
	public void searchKeyword() throws ServletException, IOException {
		System.out.println("organismSelected: "+ organismSelected);
		System.out.println("keyword 1: "+ keyword_1);
		System.out.println("keyword 2: "+ keyword_2);
		
		request.getSession().setAttribute("organismSelected", organismSelected.getTaxName());
		request.getSession().setAttribute("keyword", keyword_1);
		request.getSession().setAttribute("keyword_1", keyword_1);
		request.getSession().setAttribute("keyword_2", keyword_2);
	    response.sendRedirect("/EnzymeGraph/Results-bs.xhtml");
		
		facesContext.responseComplete();
	}

	public List<String> sugerirKeywords(String consulta) {
		List<String> keywordsSugested = new ArrayList<>();
		
		for (String key : KEYWORDS) {
            if (key.toLowerCase().startsWith(consulta.toLowerCase())) {
            	keywordsSugested.add(key);
            }
        }
		return keywordsSugested;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	
	public List<String> getKeywords() {
		return KEYWORDS;
	}
	
}
