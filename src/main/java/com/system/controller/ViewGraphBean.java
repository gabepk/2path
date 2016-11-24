package com.system.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.system.service.SearchInOrganismServico;

@Named
@ManagedBean
@SessionScoped
public class ViewGraphBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchInOrganismServico searchInOrganismServico;
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private HttpServletResponse response;
	
	public static String graphData;
	
	public void preRender() {
		System.out.println("Resultado selecionado para montar grafo: " + request.getSession().getAttribute("resultSelected"));
		
		
		if (request.getSession().getAttribute("resultSelected") != null)
		{
			graphData = searchInOrganismServico.getGraphDataInJson(
					request.getSession().getAttribute("resultSelected").toString());
			System.out.println("Dados do grafo em json: " + graphData + "\n");
		}
	
	}
	
	public void voltar() throws IOException {
		request.getSession().setAttribute("resultSelected", null);
		response.sendRedirect("/EnzymeGraph/Results.xhtml");
		
		facesContext.responseComplete();
	}
	
	public ViewGraphBean() {
		
	}
	
	public String getResults() {
		return graphData;
	}

	public void setResults(String graphData) {
		ViewGraphBean.graphData = graphData;
	}
	
}
