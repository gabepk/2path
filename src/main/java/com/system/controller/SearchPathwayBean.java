package com.system.controller;

import java.io.File;
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

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.system.model.Organism;
import com.system.service.SearchInOrganismServico;

@Named
@ManagedBean
@SessionScoped
public class SearchPathwayBean implements Serializable {

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
	
	private String organism, substract, product;
	
	public static List<String> compounds = new ArrayList<>();
	
	public void preRender() {
		compounds = searchInOrganismServico.getAllCompounds();
	}
	
	public SearchPathwayBean() {
	}
	
	public Boolean searchPathwayInOrganism() throws ServletException, IOException {
		organism = (String) request.getSession().getAttribute("organismSelected");
		if (! buildGraph()) {
			return false; // Nao existe essa caminho no 2Path
		}
		response.sendRedirect("#");
		facesContext.responseComplete();
		
		return true;
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

	public Boolean buildGraph() {
		System.out.println("Organism: "+ organism);
		System.out.println("Substract: "+ substract);
		System.out.println("Product: "+ product);
		
		try {
			// Overwrites the file if it already exists
			File f = new File(GRAPH_JSON_FILE_PATH);
			PrintWriter jsonFile = null;
			if (f.exists()) {
				f.setWritable(true);
				f.setReadable(true);
				jsonFile = new PrintWriter(f);
				
				List<String> jsonNeo4j = searchInOrganismServico.getJsonForPathway(organism, substract, product);
				
				String jsonAllData;
				jsonAllData = parseNeo4jResult(jsonNeo4j);
				jsonFile.write(jsonAllData);
				jsonFile.write("");
				
				jsonFile.close();
				return jsonNeo4j != null ? true : false;
			}
		}
		catch (IOException e) {
			System.out.println("unexpected IO issue: " + e);
		}
		return false;
	}
	
	private String parseNeo4jResult(List<String> jsonObj) {
		String nodesD3 = "", linksD3 = "";
		String name, propertie;
		
		if (jsonObj == null)
			return "{\"links\": []}";
		
		try {
			for (int i = 0; i < jsonObj.size(); i ++) {
				JSONArray data = new JSONArray(jsonObj.get(i));
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
							"\", \"propertie\":\"" + (node.getJSONObject("properties")).getString(propertie) +
							"\", \"label\":\"" + ((String) (node.getJSONArray("labels")).get(0)) + "\"},";
				}
				
				for (int j = 0; j < relationshipGroup.length(); j++) {
					relationship = ((JSONObject) relationshipGroup.get(j));
					linksD3 += "\n{\"source\":\"" + relationship.getString("startNode") +
							"\", \"target\":\"" + relationship.getString("endNode") + 
							"\", \"type\":\"" + relationship.getString("type") +"\"},";
				}
			}
			
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
		}
		
		/*return "{ \"nodes\": [ " + nodesD3.substring(0, nodesD3.length()-1) +
				"],\n \"links\": [ " + linksD3.substring(0, linksD3.length()-1) + "]}";*/
		return "{ \"links\": [ " + linksD3.substring(0, linksD3.length()-1) + "]}";
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
				name = "enzymeName";
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
				name = "ecNumber";
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
