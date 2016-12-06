package com.system.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.json.JSONObject;

@Named
@ManagedBean
@SessionScoped
public class ViewGraphBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private HttpServletRequest request;
	
	private JSONObject jsonGraph;
	
	private String jsonGraphString;
	
	public void preRender() {
		jsonGraph = (JSONObject) request.getSession().getAttribute("jsonGraph");
		jsonGraphString = jsonGraph.toString();
	}
	
	public ViewGraphBean() {
	}

	public JSONObject getJsonGraph() {
		return jsonGraph;
	}

	public void setJsonGraph(JSONObject jsonGraph) {
		this.jsonGraph = jsonGraph;
	}

	public String getJsonGraphString() {
		return jsonGraphString;
	}

	public void setJsonGraphString(String jsonGraphString) {
		this.jsonGraphString = jsonGraphString;
	}
	
}
