package com.system.repository;

import java.io.Serializable;
import java.util.ArrayList;
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
		String payload = "{\"statements\" : [ {\"statement\" : \"MATCH (n:Organism) RETURN n\"} ]}";
		String responseJson = sendTransactionalCypherQuery(payload);
		
		try {
			JSONObject jsonObj = new JSONObject(responseJson);
			JSONArray results = jsonObj.getJSONArray("results");
			
			JSONArray data = ((JSONObject) results.get(0)).getJSONArray("data");
			JSONArray rows, meta, graph;
			String taxname;
			for (int i = 0; i < data.length(); i++) {
				rows = ((JSONObject) data.get(i)).getJSONArray("row");
				meta = ((JSONObject) data.get(i)).getJSONArray("meta");
				
				taxname = (String) ((JSONObject) rows.get(0)).get("taxName");
				Organism organism = new Organism(taxname);
				organism.setTaxName(taxname);
				organism.setId((Integer)     ((JSONObject) meta.get(0)).get("id"));
				organism.setTaxId((String)   ((JSONObject) rows.get(0)).get("taxId"));
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
	
	public Boolean getPathwayInOrganism (String organism, String component_1, String component_2) {
		List<String> Matched_reactions_ids = new ArrayList<>();
		JSONObject jsonObj, graph, node;
		JSONArray results, data, meta, nodeGroup;
		String query, payload;
		// Organismo ate reacoes
		query = "MATCH (o:Organism {taxName:\\\""+ organism + "\\\"})" +
				"-[:HAS]->(s:Sequences)-[:MATCHES]->(e:Enzymes)-[:CATALYSE]->(r1:Reactions) RETURN r1";
		payload = "{\"statements\" : [ {\"statement\" : \"" + query + "\", \"parameters\": null," +
				"\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true} ] }";
		String organism_to_reactions = sendTransactionalCypherQuery(payload);
		
		// Caminho entre dois compostos
		query = "MATCH (c1:Compounds{compoundName:\\\""+ 
				component_1 +"\\\"})-[r2*]->(c2:Compounds{compoundName:\\\""+ component_2 +"\\\"}) RETURN r2";
		payload = "{\"statements\" : [ {\"statement\" : \"" + query + "\", \"parameters\": null," +
				"\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true} ] }";
		String path_between_components = sendTransactionalCypherQuery(payload);
				
		try {
			// Adiciona em Matched_reactions_ids as 
			// reacoes que sao catalizadas por enzimas 
			// cuja sequencia foi encontrada no organismo
			
			jsonObj = new JSONObject(organism_to_reactions);
			results = jsonObj.getJSONArray("results");
			
			data = ((JSONObject) results.get(0)).getJSONArray("data");
			
			for (int i = 0; i < data.length(); i++) {
				meta = ((JSONObject) data.get(i)).getJSONArray("meta");
				
				Matched_reactions_ids.add(((JSONObject) meta.get(0)).get("id").toString());
			}
			
			// Verifica se todas as reações do caminho
			// entre os compostos estao em Matched_reactions_ids
			
			jsonObj = new JSONObject(path_between_components);
			results = jsonObj.getJSONArray("results");
			
			data = ((JSONObject) results.get(0)).getJSONArray("data");
			//meta = ((JSONObject) data.get(0)).getJSONArray("meta");
			graph = ((JSONObject) data.get(0)).getJSONObject("graph");
			nodeGroup = graph.getJSONArray("nodes");
			
			for (int i = 0; i < nodeGroup.length(); i++) {
				node = ((JSONObject) nodeGroup.get(i));
				
				if ( ((String) (node.getJSONArray("labels")).get(0)) . equals("Reactions") ) {
					if ( !Matched_reactions_ids.contains(node.get("id").toString()) ) {
						return false;
					}
				}
			}
			
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
			return false;
		}
		
		return true;
	}
	
	public String getGraphDataInJson(String result) {
		String graphJson = "";
		return graphJson;
	}
	
	
	
	
	
	
	private static String sendTransactionalCypherQuery(String payload) {
		final String txUri = SERVER_ROOT_URI + "transaction/commit";
		WebResource resource = Client.create().resource(txUri);
		resource.addFilter(new HTTPBasicAuthFilter("neo4j","213546")); 
		//String payload = "{\"statements\" : [ {\"statement\" : \"" + query
		//		+ "\"} ]}";
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
