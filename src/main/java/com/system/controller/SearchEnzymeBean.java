package com.system.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.ws.rs.core.Response;

import org.primefaces.json.JSONException;

import com.system.model.Organism;
import com.system.service.SearchInOrganismServico;

@Named
@ManagedBean
@SessionScoped
public class SearchEnzymeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String JSON_FILE_PATH = "curso-primefaces/EnzymeGraph/src/main/webapp/pathway.json";
	
	@Inject
	private SearchInOrganismServico searchInOrganismServico; 
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private HttpServletResponse response;
	
	private String organism, ec;
	
	public static List<String> enzymes = new ArrayList<>();
	
	public void preRender() {
		enzymes = searchInOrganismServico.getAllEnzymes();
	}
	
	public SearchEnzymeBean() {
	}
	
	public Boolean searchEnzymeInOrganism() throws ServletException, IOException {
		organism = (String) request.getSession().getAttribute("organismSelected");
		if (! buildGraph()) {
			return false;
		}
		response.sendRedirect("#");
		facesContext.responseComplete();
		
		parseJsonFile();
		return true;
	}

	public List<String> suggestKeywords(String consulta) {
		List<String> keywordsSugested = new ArrayList<>();
		
		for (String key : enzymes) {
            if (key.toLowerCase().startsWith(consulta.toLowerCase())) {
            	keywordsSugested.add(key);
            }
        }
		return keywordsSugested;
	}

	public Boolean buildGraph() {
		System.out.println("Organism: "+ organism);
		System.out.println("Enzyme: "+ ec);
		
		try {
			// Overwrites the file if it already exists
			File f = new File(JSON_FILE_PATH);
			PrintWriter jsonFile = new PrintWriter(f);
			String jsonNeo4j = searchInOrganismServico.getJsonForEnzyme(organism, ec);
			if (!jsonNeo4j.isEmpty()) {
				jsonFile.write(jsonNeo4j);
				jsonFile.close();
				return true;
			}
			jsonFile.close();
		}
		catch (IOException e) {
			System.out.println("unexpected IO issue: " + e);
		}
		return false;
	}
	
	public void parseJsonFile() {
		try {
		File f = new File(JSON_FILE_PATH);
		PrintWriter jsonFile = new PrintWriter(f);
		}
		catch (IOException e) {
			System.out.println("unexpected IO issue: " + e);
			
		}
		
	}
	
	public String getEc() {
		return ec;
	}

	public void setEc(String ec) {
		this.ec = ec;
	}
	
	public List<String> getEnzymes() {
		return enzymes;
	}
	
}
