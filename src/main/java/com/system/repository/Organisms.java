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
	
	
	// TODO Iterar sobre data (tem 2 elementos se grafo tem duas partiçoes) e também sobre nodes (nos de cada particao)
	// Se buscar dados sem arestas, iterar sobre data (cada no é uma particao)
	// Se bucar com arestas, tratar das particoes
	
	

	private static final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";
	
	public static String SequenceNodeCypher = "CREATE (n:Sequences { ";
	
	private static final long serialVersionUID = 1L;
	
	public List<Organism> getAllOrganisms() {
		List<Organism> allOrganisms = new ArrayList<>();
		String payload = "{\"statements\" : [ {\"statement\" : \"MATCH (n:Organism) RETURN n\"} ]}";
		String responseJson = sendTransactionalCypherQuery(payload);
		
		try {
			JSONObject jsonObj = new JSONObject(responseJson);
			JSONArray results = jsonObj.getJSONArray("results");
			
			JSONArray data = ((JSONObject) results.get(0)).getJSONArray("data");
			JSONArray rows, meta;
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
	
	public List<String> getAllCompounds () {
		List<String> allCompounds = new ArrayList<>();
		String payload = "{\"statements\" : [ {\"statement\" : \"MATCH (n:Compounds) RETURN n\"} ]}";
		String responseJson = sendTransactionalCypherQuery(payload);
		
		try {
			JSONObject jsonObj = new JSONObject(responseJson);
			JSONArray results = jsonObj.getJSONArray("results");
			
			JSONArray data = ((JSONObject) results.get(0)).getJSONArray("data");
			JSONArray rows;
			for (int i = 0; i < data.length(); i++) {
				rows = ((JSONObject) data.get(i)).getJSONArray("row");
				allCompounds.add( (String) ((JSONObject) rows.get(0)).get("compoundName") );
			}
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
		}
		
		return allCompounds;
	}
	
	public List<String> getAllEnzymes () {
		List<String> allEnzymes = new ArrayList<>();
		String payload = "{\"statements\" : [ {\"statement\" : \"MATCH (n:Enzymes) RETURN n\"} ]}";
		String responseJson = sendTransactionalCypherQuery(payload);
		
		try {
			JSONObject jsonObj = new JSONObject(responseJson);
			JSONArray results = jsonObj.getJSONArray("results");
			
			JSONArray data = ((JSONObject) results.get(0)).getJSONArray("data");
			JSONArray rows;
			for (int i = 0; i < data.length(); i++) {
				rows = ((JSONObject) data.get(i)).getJSONArray("row");
				allEnzymes.add( (String) ((JSONObject) rows.get(0)).get("ecNumber") );
			}
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
		}
		
		return allEnzymes;
	}
	
	public String getJsonForEnzyme(String organism, String ec) {
		JSONObject jsonObj;
		JSONArray results, data;
		String query, payload;
		
		if (organism.isEmpty()) {
			query = "MATCH (e:Enzymes{ecNumber:\\\"" + ec + "\\\"})-[c:CATALYSE]->(r:Reactions)-[l:PRODUCTOF]->(p:Compounds) RETURN e, c, r, l, p";
		}
		else {
			query = "MATCH (o:Organism {taxName:\\\""+ organism + "\\\"})" +
					"-[h:HAS]->(s:Sequences)-[m:MATCHES]->(e:Enzymes{ecNumber:\\\"" + ec + "\\\"})-[c:CATALYSE]" + 
					"->(r:Reactions) RETURN o, s, e, r, h, m, c";
		}
		payload = "{\"statements\" : [ {\"statement\" : \"" + query + "\", \"parameters\": null," +
				"\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true} ] }";
		String responseNeo4j = sendTransactionalCypherQuery(payload);
		
		try {
			jsonObj = new JSONObject(responseNeo4j);
			results = jsonObj.getJSONArray("results");
			data = ((JSONObject) results.get(0)).getJSONArray("data");
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
			return "";
		}
		
		return data.toString();
	}
	
	public List<String> getJsonForPathway(String organism, String substract, String product) {
		JSONObject jsonObj;
		JSONArray results, data;
		String query_1, query_2, payload;
		List<String> two_datas = new ArrayList<>();
		
		query_1 = "MATCH (c1:Compounds{compoundName:\\\""+ 
				substract +"\\\"})-[r2*]->(c2:Compounds{compoundName:\\\""+ product +"\\\"}) " +
				"RETURN c1, r2, c2";
		payload = "{\"statements\" : [ {\"statement\" : \"" + query_1 + "\", \"parameters\": null," +
				"\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true} ] }";
		String path_between_components = sendTransactionalCypherQuery(payload);
		
		
		try {
			jsonObj = new JSONObject(path_between_components);
			results = jsonObj.getJSONArray("results");
			data = ((JSONObject) results.get(0)).getJSONArray("data");
			
			if (data.isNull(0)) return null; // Nao existe caminho entre eles
			
			two_datas.add(data.toString());
			
			if (!organism.isEmpty()) {
				query_2 = "MATCH (o:Organism {taxName:\\\""+ organism + "\\\"})" +
					    "-[h:HAS]->(s:Sequences)-[m:MATCHES]->(e:Enzymes)-[c:CATALYSE]->(r:Reactions) "+
						"RETURN o, h, s, m, e, c, r, co";
				payload = "{\"statements\" : [ {\"statement\" : \"" + query_1 + "\", \"parameters\": null," +
						"\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true} ] }";
				String organism_to_reactions = sendTransactionalCypherQuery(payload);
				
				jsonObj = new JSONObject(path_between_components);
				results = jsonObj.getJSONArray("results");
				data = ((JSONObject) results.get(0)).getJSONArray("data");
				two_datas.add(data.toString());
				
			}
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
			return null;
		}
		
		return two_datas;
	}
	
	/*public Boolean getPathwayInOrganism (String organism, String component_1, String component_2) {
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
	}*/
	
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
}
