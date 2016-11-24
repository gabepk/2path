package com.system.controller;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.system.model.Organism;

 
@FacesConverter("organismConverter")
public class OrganismConverter implements Converter {
 
		// O retorn é um Objeto do tipo Organism
		@Override
		public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
			if (arg2 != null) {
				for (Organism organism : SearchInOrganismBean.organisms) {
					if (arg2.equals(organism.getLabel())) {
						return organism;
					}
				}
			}
			return null;
		}

		// O 3º Objeto é o tipo Interesse
		@Override
		public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
			if(arg2 == null) {
				return null;
			}
			
			Organism interesse = (Organism) arg2;
			return interesse.getLabel(); // Nome do Ícone de cada interesse é único
		}

	
}
