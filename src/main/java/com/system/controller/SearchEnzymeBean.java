package com.system.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.system.service.SearchService;

@Named
@ManagedBean
@SessionScoped
public class SearchEnzymeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchService service; 
	
	@Inject
	private HttpServletRequest request;
	
	private String organism, ec;
	
	private String jsonGraphString;

	public void preRender() {
		jsonGraphString = "";
		ec = "";
	}
	
	public SearchEnzymeBean() {
	}
	
	public void searchEnzymeInOrganism() throws ServletException, IOException, InterruptedException {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message;
		organism = (String) request.getSession().getAttribute("organismSelected");
		String resultValue = "";
		String json = buildGraph();
		
		if (json.replaceAll("\\s+","").equals("{\"nodes\":[],\"links\":[]}")) {
			resultValue = (organism.isEmpty()) ? "Enzyme not found" : "Enzyme not found in organism " + organism;
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  resultValue);
		}
		else {
			resultValue = (organism.isEmpty()) ? "Enzyme found" : "Enzyme found in organism " + organism;
			message = new FacesMessage("Successful",  resultValue);
		}
				
		context.addMessage(null, message);
		jsonGraphString = json;
	}
	
	public String buildGraph() {
		System.out.println("Organism: "+ organism);
		System.out.println("Enzyme: "+ ec);
		
		List<String> neo4jResponse = null;
		if (ec != null) 
			neo4jResponse = service.getJsonForEnzyme(organism, ec);
		
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
			// Armazena primeiro todas as arestas, exceto a CATALYSE
			for (int i = 0; i < jsonObj.size(); i ++) {
				JSONArray data = new JSONArray(jsonObj.get(i));
				JSONArray nodeGroup, relationshipGroup, reactions;
				JSONObject graph, node, relationship;
				
				for (int j = 0; j < data.length(); j++) {
					graph = ((JSONObject) data.get(j)).getJSONObject("graph");
					nodeGroup = graph.getJSONArray("nodes");
					relationshipGroup = graph.getJSONArray("relationships");
					
					for (int k = 0; k < relationshipGroup.length(); k++) {
						relationship = ((JSONObject) relationshipGroup.get(k));
						linksD3 += "\n{\"source\":\"" + relationship.getString("startNode") +
								"\", \"target\":\"" + relationship.getString("endNode") + 
								"\", \"type\":\"" + relationship.getString("type") +"\"},";					
					}
					
					for (int k = 0; k < nodeGroup.length(); k++) {
						node = ((JSONObject) nodeGroup.get(k));
						name = getName( ((String) (node.getJSONArray("labels")).get(0)) );
						propertie = getPropertie( ((String) (node.getJSONArray("labels")).get(0)) );
						
						// Nao inclui no's repetidos
						if (!nodesD3.contains("{\"id\":\"" + node.getString("id"))) {
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
				name = "taxId";
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
	
	public String getJsonGraphString() {
		return jsonGraphString;
	}

	public void setJsonGraphString(String jsonGraphString) {
		this.jsonGraphString = jsonGraphString;
	}
	
}
