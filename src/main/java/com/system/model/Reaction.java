package com.system.model;

import java.util.ArrayList;
import java.util.List;

public class Reaction {

	private Long id; // TODO: Mandatory
	private String keggLink_Reaction;
	private String reactionName;
	private String reactionDescription;
	private String keggID_Reaction;
	private String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getKeggLink_Reaction() {
		return keggLink_Reaction;
	}

	public void setKeggLink_Reaction(String keggLink_Reaction) {
		this.keggLink_Reaction = keggLink_Reaction;
	}

	public String getReactionName() {
		return reactionName;
	}

	public void setReactionName(String reactionName) {
		this.reactionName = reactionName;
	}

	public String getReactionDescription() {
		return reactionDescription;
	}

	public void setReactionDescription(String reactionDescription) {
		this.reactionDescription = reactionDescription;
	}

	public String getKeggID_Reaction() {
		return keggID_Reaction;
	}

	public void setKeggID_Reaction(String keggID_Reaction) {
		this.keggID_Reaction = keggID_Reaction;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Reaction[");
        builder.append("id=").append(getId().toString()).append(",");
        builder.append("keggLink_Reaction=").append(getKeggLink_Reaction()).append(",");
        builder.append("reactionName=").append(getReactionName()).append(",");
        builder.append("reactionDescription=").append(getReactionDescription()).append(",");
        builder.append("keggID_Reaction=").append(getKeggID_Reaction()).append(",");
        builder.append("type=").append(getType()).append("]");

        return builder.toString();
    }

}
