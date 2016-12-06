package com.system.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.system.controller.SearchEnzymeBean;

@Path("/graph")
public class GraphApi {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	private String getGraphjson() {
		//SearchEnzymeBean.buildGraph();
		
		//JSONObject 
		String s = "{\"nodes\":[],\"links\":[]}";
		return s;
	}
}
