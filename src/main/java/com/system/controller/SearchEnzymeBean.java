package com.system.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.system.model.Organism;
import com.system.service.SearchInOrganismServico;

@Named
@ManagedBean
@SessionScoped
public class SearchEnzymeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String GRAPH_JSON_FILE_PATH = "curso-primefaces/EnzymeGraph/src/main/webapp/resources/json/graph.json";
	
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
		buildGraph();
	}
	
	public SearchEnzymeBean() {
	}
	
	public Boolean searchEnzymeInOrganism() throws ServletException, IOException {
		organism = (String) request.getSession().getAttribute("organismSelected");
		if (! buildGraph()) {
			return false; // Nao existe essa enzima no 2Path
		}
		response.sendRedirect("#");
		facesContext.responseComplete();
		
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

	private Boolean buildGraph() {
		System.out.println("Organism: "+ organism);
		System.out.println("Enzyme: "+ ec);
		
		String neo4jResponse = null;
		if (ec != null) 
			neo4jResponse = searchInOrganismServico.getJsonForEnzyme(organism, ec);
		
		try {
			// Overwrites the file if it already exists
			File f = new File(GRAPH_JSON_FILE_PATH);
			PrintWriter jsonFile = null;
			if (f.exists()) {
				f.setWritable(true);
				f.setReadable(true);
				jsonFile = new PrintWriter(f);
				neo4jResponse = parseNeo4jResult(neo4jResponse);
				
				jsonFile.write(neo4jResponse);
				jsonFile.write("");
				jsonFile.close();
			}
		}
		catch (IOException e) {
			System.out.println("unexpected IO issue: " + e);
		}
		return false;
	}
	
	private String parseNeo4jResult(String jsonObj) {
		String nodesD3 = "", linksD3 = "";
		String name, propertie;
		
		// Se procura-se enzima sem organismo ou resultado retornou string vazia
		if ((jsonObj == null) || (jsonObj.equals("[]")))
			return "{ \"nodes\": [], \"links\": [] }";
		
		try {
			JSONArray data = new JSONArray(jsonObj);
			JSONArray nodeGroup, relationshipGroup;
			JSONObject graph, node, relationship;
			graph = ((JSONObject) data.get(0)).getJSONObject("graph");
			nodeGroup = graph.getJSONArray("nodes");
			relationshipGroup = graph.getJSONArray("relationships");
			
			for (int j = 0; j < nodeGroup.length(); j++) {
				node = ((JSONObject) nodeGroup.get(j));
				name = getName( ((String) (node.getJSONArray("labels")).get(0)) );
				propertie = getPropertie( ((String) (node.getJSONArray("labels")).get(0)) );
				
				nodesD3 += "\n{\"id\":\"" + node.getString("id") +
						"\", \"name\":\"" + (node.getJSONObject("properties")).getString(name).replace('\n', ' ').replace('\t', ' ') +
						"\", \"propertie\":\"" + (node.getJSONObject("properties")).getString(propertie).replace('\n', ' ').replace('\t', ' ') +
						"\", \"label\":\"" + ((String) (node.getJSONArray("labels")).get(0)) + "\"},";
			}
			
			for (int j = 0; j < relationshipGroup.length(); j++) {
				relationship = ((JSONObject) relationshipGroup.get(j));
				linksD3 += "\n{\"source\":\"" + relationship.getString("startNode") +
						"\", \"target\":\"" + relationship.getString("endNode") + 
						"\", \"type\":\"" + relationship.getString("type") +"\"},";
			}
			
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
		}
		
		return "{ \"nodes\": [ " + nodesD3.substring(0, nodesD3.length()-1) +
			"],\n \"links\": [ " + linksD3.substring(0, linksD3.length()-1) + "]}";
	}
	
	private String getName(String label) {
		String name = "";
		switch (label) {
			case "Organism":
				name = "taxName";
				break;
			case "Compounds":
				name = "compoundName";
				break;
			case "Enzymes":
				name = "ecNumber";
				break;
			case "Reactions":
				name = "reactionName";
				break;
			case "Sequences":
				name = "ecNumber";
				break;
		}
		return name;
	}
	
	private String getPropertie (String label) {
		String name = "";
		switch (label) {
			case "Organism":
				name = "taxRefs";
				break;
			case "Compounds":
				name = "keggID_compound";
				break;
			case "Enzymes":
				name = "enzymeName";
				break;
			case "Reactions":
				name = "keggID_Reaction";
				break;
			case "Sequences":
				name = "seqFasta";
				break;
		}
		return name;
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
