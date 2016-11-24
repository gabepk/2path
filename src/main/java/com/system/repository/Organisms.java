package com.system.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.system.model.Organism;

public class Organisms implements Serializable {

	private static final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";
	
	public static String SequenceNodeCypher = "CREATE (n:Sequences { ";
	
	private static final long serialVersionUID = 1L;
	
	public List<Organism> getAllOrganisms() throws JSONException {
		List<Organism> allOrganisms = new ArrayList<>();
		String responseJson = sendTransactionalCypherQuery("MATCH (n:Organism) RETURN n");
		
		try {
			JSONObject jsonObj = new JSONObject(responseJson);
			JSONArray results = jsonObj.getJSONArray("results");
			
			JSONArray data = ((JSONObject) results.get(0)).getJSONArray("data");
			JSONArray rows, meta, graph;
			String taxname;
			for (int i = 0; i < data.length(); i++) {
				rows = ((JSONObject) data.get(i)).getJSONArray("row");
				meta = ((JSONObject) data.get(i)).getJSONArray("meta");
				//graph = ((JSONObject) data.get(i)).getJSONObject("graph");
				
				taxname = (String) ((JSONObject) rows.get(0)).get("taxName");
				Organism organism = new Organism(taxname);
				organism.setTaxName(taxname);
				organism.setId((Long) ((JSONObject) meta.get(0)).get("id"));
				organism.setTaxId((String) ((JSONObject) rows.get(0)).get("taxId"));
				organism.setTaxRefs((String) ((JSONObject) rows.get(0)).get("taxRefs"));
				 	
				allOrganisms.add(organism);
			}
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
		}
		
		return allOrganisms;
	}
	
	//public List<String> getKeywordsInOrganism(String organism, String keyword) {
	//	List<String> results = new ArrayList<>();
	//	return results;
	//}
	
	public List<String> getPathwayInOrganism (String organism, String keyword_1, String keyword_2) {
		List<String> results = new ArrayList<>();
		String responseJson = sendTransactionalCypherQuery("MATCH (n:Organism) RETURN n");
		
		// Organismo ate reacoes
		// MATCH (o:Organism {name:"Arabidopsis thaliana"})-[:HAS]->(s:Sequences)-[:MATCHES]->(e:Enzymes)-[:CATALYSE]->(r:Reactions) RETURN o, s, e, r 
		
		
		return results;
	}
	
	public String getGraphDataInJson(String result) {
		String graphJson = "";
		return graphJson;
	}
	
	
	
	
	
	
	private static String sendTransactionalCypherQuery(String query) {
		final String txUri = SERVER_ROOT_URI + "transaction/commit";
		WebResource resource = Client.create().resource(txUri);
		resource.addFilter(new HTTPBasicAuthFilter("neo4j","213546")); 
		String payload = "{\"statements\" : [ {\"statement\" : \"" + query
				+ "\"} ]}";
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity(payload)
				.get(ClientResponse.class);
		
		// TODO: Usar a resposta para verificar se deu 200 Ok

		/*System.out.println(String.format(
				" POST \n [%s] \n to \n [%s] \n status code [%d], returned data: \n "
						+ System.lineSeparator() + "%s", payload, txUri,
				response.getStatus(), response.getEntity(String.class)));*/
		String responseJson = response.getEntity(String.class);
		response.close();
		return responseJson;
	}
	
	/*public static void buildCypherQueryToSearchPathway(
			SequenceNode sequenceNode, OrganismNode o1, OrganismNode o2) {
		SequenceNodeCypher += "submissionID : '"
				+ sequenceNode.getSubmissionID() + "'";

		SequenceNodeCypher += ", submitedSequenceName : '"
				+ sequenceNode.getSubmitedSequenceName() + "'";

		SequenceNodeCypher += ", submitedSequence : '"
				+ sequenceNode.getSubmitedSequence() + "'";

		SequenceNodeCypher += ", ecNumber : '" + sequenceNode.getEc() + "'";
		SequenceNodeCypher += " })";
	}*/
	
	/*public static Node getChildByNodeName(Node node, String nodeName) {
		for (Node childNode = node.getFirstChild(); childNode != null;) {
			Node nextChild = childNode.getNextSibling();
			if (childNode.getNodeName().equalsIgnoreCase(nodeName)) {
				return childNode;
			}
			childNode = nextChild;
		}
		return null;
	}*/
	
}
