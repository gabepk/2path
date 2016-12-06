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
import javax.faces.component.html.HtmlOutputText;
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
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLObjectElement;

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
	
	private HtmlOutputText result;
	
	
	public static List<String> enzymes = new ArrayList<>();
	
	public void preRender() {
		enzymes = searchInOrganismServico.getAllEnzymes();
	}
	
	public SearchEnzymeBean() {
	}
	
	public void searchEnzymeInOrganism() throws ServletException, IOException, InterruptedException {
		organism = (String) request.getSession().getAttribute("organismSelected");
		String resultValue = "";
		String resultStyle = "";
		String json = buildGraph();
		
		if (json.replaceAll("\\s+","").equals("{\"nodes\":[],\"links\":[]}")) {
			resultStyle = "font-size:18px;color:red";
			resultValue = (organism.isEmpty()) ? "Enzyme not found" : "Enzyme not found in organism " + organism;
		}
		else {
			resultStyle = "font-size:18px;color:green";
			resultValue = (organism.isEmpty()) ? "Enzyme found" : "Enzyme found in organism " + organism;
		}
				
		result.setStyle(resultStyle);
		result.setValue(resultValue);
		
		try {
			JSONObject jsonGraph;
			jsonGraph = new JSONObject(json);
			request.getSession().setAttribute("jsonGraph", jsonGraph);
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
	
	public String buildGraph() {
		System.out.println("Organism: "+ organism);
		System.out.println("Enzyme: "+ ec);
		
		List<String> neo4jResponse = null;
		if (ec != null) 
			neo4jResponse = searchInOrganismServico.getJsonForEnzyme(organism, ec);
		
		String allData;
		allData = parseNeo4jResult(neo4jResponse);

		return allData
				.replaceAll("\\s+","")
				.equals("{\"nodes\":[],\"links\":[]}") 
				? "{\"nodes\":[],\"links\":[]}" 
				: allData; // Retorna "" se grafo eh vazio
	}
	
	private String parseNeo4jResult(List<String> jsonObj) {
		String nodesD3 = "", linksD3 = "";
		String name, propertie;
		
		if (jsonObj == null)
			return "{ \"nodes\": [], \"links\": [] }";
		
		try {
			for (int i = 0; i < jsonObj.size(); i ++) {
				JSONArray data = new JSONArray(jsonObj.get(i));
				JSONArray nodeGroup, relationshipGroup;
				JSONObject graph, node, relationship;
				
				for (int j = 0; j < data.length(); j++) {
					graph = ((JSONObject) data.get(j)).getJSONObject("graph");
					nodeGroup = graph.getJSONArray("nodes");
					relationshipGroup = graph.getJSONArray("relationships");
					
					for (int k = 0; k < nodeGroup.length(); k++) {
						node = ((JSONObject) nodeGroup.get(k));
						name = getName( ((String) (node.getJSONArray("labels")).get(0)) );
						propertie = getPropertie( ((String) (node.getJSONArray("labels")).get(0)) );
						
						// Nao inclui no's repetidos
						if (! nodesD3.contains("{\"id\":\"" + node.getString("id")))
							nodesD3 += "\n{\"id\":\"" + node.getString("id") +
									"\", \"name\":\"" + (node.getJSONObject("properties"))
														.getString(name)
														.replaceAll("\\n+", "")
														.replaceAll("\\t+", "") +
									"\", \"propertie\":\"" + (node.getJSONObject("properties"))
															.getString(propertie)
															.replaceAll("\\n+", "").
															replaceAll("\\t+", "") +
									"\", \"label\":\"" + ((String) (node.getJSONArray("labels")).get(0)) + "\"},";
					}
					
					for (int k = 0; k < relationshipGroup.length(); k++) {
						relationship = ((JSONObject) relationshipGroup.get(k));
						linksD3 += "\n{\"source\":\"" + relationship.getString("startNode") +
								"\", \"target\":\"" + relationship.getString("endNode") + 
								"\", \"type\":\"" + relationship.getString("type") +"\"},";
					}
				}
			}
			
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
		}
		
		int nodeLenght = nodesD3.length();
		int linkLenght = linksD3.length();
		
		if (!nodesD3.isEmpty()) nodeLenght = nodesD3.length()-1;
		if (!linksD3.isEmpty()) linkLenght = linksD3.length()-1;
		
		return "{ \"nodes\": [ " + nodesD3.substring(0, nodeLenght) +
			"], \"links\": [ " + linksD3.substring(0, linkLenght) + "]}";
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

	public HtmlOutputText getResult() {
		return result;
	}

	public void setResult(HtmlOutputText result) {
		this.result = result;
	}
	
}
