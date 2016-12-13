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
	
	public List<String> getJsonForEnzyme(String organism, String ec) {
		JSONObject jsonObj;
		JSONArray results, data;
		String query_1, query_2, query_3;
		String payload_1, payload_2, payload_3;
		List<String> data_set = new ArrayList<>();
		
		query_1 = "MATCH (e:Enzymes{ecNumber:\\\"" + ec + "\\\"})-[c:CATALYSE]->(r:Reactions)" +
					"-[p:PRODUCTOF]->(co:Compounds) RETURN e, c, r, p, co";
		payload_1 = "{\"statements\" : [ {\"statement\" : \"" + query_1 + "\", \"parameters\": null," +
				"\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true} ] }";
		String enzyme_to_substract = sendTransactionalCypherQuery(payload_1);
		
		
		query_2 = "MATCH (Enzymes{ecNumber:\\\"" + ec + "\\\"})-[CATALYSE]->(Reactions)" +
				"<-[s:SUBSTRATE_FOR]-(co:Compounds) RETURN s, co";
		payload_2 = "{\"statements\" : [ {\"statement\" : \"" + query_2 + "\", \"parameters\": null," +
				"\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true} ] }";
		String enzyme_to_product = sendTransactionalCypherQuery(payload_2);
		
		try {
			jsonObj = new JSONObject(enzyme_to_substract);
			results = jsonObj.getJSONArray("results");
			data = ((JSONObject) results.get(0)).getJSONArray("data");
			data_set.add(data.toString());
			
			jsonObj = new JSONObject(enzyme_to_product);
			results = jsonObj.getJSONArray("results");
			data = ((JSONObject) results.get(0)).getJSONArray("data");
			data_set.add(data.toString());
			
			if (data_set.size() == 0) return null; // Enzima nao catalisa nenhuma reacao com substrato e produto

			
			if (organism != null && !organism.isEmpty()) {
				/*
				 * Caminho entre organismo e reacoes (que fazem parte caminho que liga os dois compostos)
				 */
				query_3 = "MATCH (o:Organism {taxName:\\\""+ organism + "\\\"})" +
						"-[h:HAS]->(s:Sequences)-[m:MATCHES]->(Enzymes{ecNumber:\\\"" + ec + "\\\"})" + 
						" RETURN o, h, s, m";
				payload_3 = "{\"statements\" : [ {\"statement\" : \"" + query_3 + "\", \"parameters\": null," +
						"\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true} ] }";
				String organism_to_enzymes = sendTransactionalCypherQuery(payload_3);
				
				jsonObj = new JSONObject(organism_to_enzymes);
				results = jsonObj.getJSONArray("results");
				data = ((JSONObject) results.get(0)).getJSONArray("data");
				
				// Se pediu para encontrar no organismo, mas nao tem, entao nao existe
				if (data.isNull(0)) data_set.clear();
			}
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
			return null;
		}
		
		return data_set;
	}
	
	public List<String> getJsonForPathway(String organism, String substract, String product) {
		JSONObject jsonObj;
		JSONArray results, data;
		String query_1, query_2, payload;
		List<String> data_set = new ArrayList<>();
		
		/*
		 * Caminho entre dois compostos e as reacoes entre eles
		 */
		query_1 = "MATCH (c1:Compounds{compoundName:\\\"" + substract
				 +"\\\"})-[r*]->(c2:Compounds{compoundName:\\\""+ product +"\\\"}) " +
				"RETURN c1, r, c2";
		payload = "{\"statements\" : [ {\"statement\" : \"" + query_1 + "\", \"parameters\": null," +
				"\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true} ] }";
		String path_between_components = sendTransactionalCypherQuery(payload);
		
		
		try {
			jsonObj = new JSONObject(path_between_components);
			results = jsonObj.getJSONArray("results");
			data = ((JSONObject) results.get(0)).getJSONArray("data");
			
			if (data.isNull(0)) return null; // Nao existe caminho entre eles
			
			data_set.add(data.toString());
			
			if (organism != null && !organism.isEmpty()) {
				/*
				 * Caminho entre organismo e reacoes (que fazem parte caminho que liga os dois compostos)
				 */
				query_2 = "MATCH (Organism {taxName:\\\""+ organism + "\\\"})" +
					    "-[HAS]->(Sequences)-[MATCHES]->(Enzymes)-[CATALYSE]->(r:Reactions) "+
						"RETURN r";
				payload = "{\"statements\" : [ {\"statement\" : \"" + query_2 + "\", \"parameters\": null," +
						"\"resultDataContents\": [\"row\",\"graph\"],\"includeStats\": true} ] }";
				String organism_to_reactions = sendTransactionalCypherQuery(payload);
				
				jsonObj = new JSONObject(organism_to_reactions);
				results = jsonObj.getJSONArray("results");
				data = ((JSONObject) results.get(0)).getJSONArray("data");
				data_set.add(data.toString());
			}
		}
		catch (JSONException e) {
			System.out.println("unexpected JSON exception : " + e);
			return null;
		}
		
		return data_set;
	}
	
	
	private static String sendTransactionalCypherQuery(String payload) {
		final String txUri = SERVER_ROOT_URI + "transaction/commit";
		WebResource resource = Client.create().resource(txUri);
		resource.addFilter(new HTTPBasicAuthFilter("neo4j","213546")); 
		
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity(payload)
				.get(ClientResponse.class);
		
		// TODO: Usar a resposta para verificar se deu 200 Ok

		String responseJson = response.getEntity(String.class);
		response.close();
		return responseJson;
	}	
}
