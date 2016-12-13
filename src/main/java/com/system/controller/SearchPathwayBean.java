package com.system.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
public class SearchPathwayBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchService service; 
	
	@Inject
	private HttpServletRequest request;
	
	private String organism, substract, product;
	
	private String jsonGraphString;
	
	public static List<String> compounds = new ArrayList<>();
	
	public void preRender() {
		compounds = service.getAllCompounds();
		jsonGraphString = "";
		substract = "";
		product = "";
	}
	
	public SearchPathwayBean() {
	}
	
	public void searchPathwayInOrganism() throws ServletException, IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message;
		organism = (String) request.getSession().getAttribute("organismSelected");
		String resultValue = "";
		String json = buildGraph();
		
		if (json.replaceAll("\\s+","").equals("{\"nodes\":[],\"links\":[]}")) {
			resultValue = (organism.isEmpty()) ? "Pathway not found" : "Pathway not found in organism " + organism;
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  resultValue);
			
		}
		else {
			resultValue = (organism.isEmpty()) ? "Pathway found" : "Pathway found in organism " + organism;
			message = new FacesMessage("Successful",  resultValue);
		}
		
		context.addMessage(null, message);
		jsonGraphString = json;
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

	public String buildGraph() {
		System.out.println("Organism: "+ organism);
		System.out.println("Substract: "+ substract);
		System.out.println("Product: "+ product);
		
		List<String> neo4jResponse = null;
		if (substract != null && product != null) 
			neo4jResponse = service.getJsonForPathway(organism, substract, product);

		String allData;
		allData = parseNeo4jResult(neo4jResponse);

		return allData
				.replaceAll("\\s+","")
				.equals("{\"nodes\":[],\"links\":[]}") 
				? "{\"nodes\":[],\"links\":[]}" 
				: allData;
	}
	
	private String parseNeo4jResult(List<String> jsonObj) {
		String nodesD3 = "", linksD3 = "";
		String name, propertie;
		
		// Se procura-se caminho sem organismo ou resultado retornou lista vazia
		if (jsonObj == null)
			return "{ \"nodes\": [], \"links\": [] }";
		
		try {
			JSONArray data = new JSONArray(jsonObj.get(0));
			JSONArray nodeGroup, relationshipGroup, reactions;
			JSONObject graph, node, relationship;
			
			// Se foi procurado via no organismo, parser deve verificar se todas as reacoes da via ocorrem no organismo
			reactions = null;
			if (jsonObj.size() > 1) {
				reactions = new JSONArray(jsonObj.get(1));
			}
			
			for (int j = 0; j < data.length(); j++) {
				graph = ((JSONObject) data.get(j)).getJSONObject("graph");
				nodeGroup = graph.getJSONArray("nodes");
				relationshipGroup = graph.getJSONArray("relationships");
				
				
				for (int k = 0; k < nodeGroup.length(); k++) {
					node = ((JSONObject) nodeGroup.get(k));
					name = getName( ((String) (node.getJSONArray("labels")).get(0)) );
					propertie = getPropertie( ((String) (node.getJSONArray("labels")).get(0)) );
					
					// Verifica se todas as reações da via estao no conjunto de reacoes que o organismo pode catalisar
					if ((reactions != null) && 
							(name.equals("reactionName")) && 
							!(reactions.toString().contains(node.getString("id")))) {
						return "{ \"nodes\": [], \"links\": [] }";
					}
					
					// Nao inclui no's repetidos
					if (! nodesD3.contains("{\"id\":\"" + node.getString("id")))
						nodesD3 += "\n{\"id\":\"" + node.getString("id") +
								"\", \"name\":\"" + (node.getJSONObject("properties"))
														 .getString(name)
														 .replace('\n', ' ')
														 .replace('\t', ' ') +
								"\", \"propertie\":\"" + (node.getJSONObject("properties"))
															  .getString(propertie)
															  .replace('\n', ' ')
															  .replace('\t', ' ') +
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
	
	public String getJsonGraphString() {
		return jsonGraphString;
	}

	public void setJsonGraphString(String jsonGraphString) {
		this.jsonGraphString = jsonGraphString;
	}
}
