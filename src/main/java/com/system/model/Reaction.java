package com.system.model;

import java.util.ArrayList;
import java.util.List;

public class Reaction {

	private Long id; // TODO: Mandatory
	private String reactionDescription;
	private String reactionKEGG;
	private String reactionBioCyc;
	private String reactionReversibility;
	private List<String> reactionRefs = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReactionDescription() {
		return reactionDescription;
	}

	public void setReactionDescription(String reactionDescription) {
		this.reactionDescription = reactionDescription;
	}

	public String getReactionKEGG() {
		return reactionKEGG;
	}

	public void setReactionKEGG(String reactionKEGG) {
		this.reactionKEGG = reactionKEGG;
	}

	public String getReactionBioCyc() {
		return reactionBioCyc;
	}

	public void setReactionBioCyc(String reactionBioCyc) {
		this.reactionBioCyc = reactionBioCyc;
	}

	public String getReactionReversibility() {
		return reactionReversibility;
	}

	public void setReactionReversibility(String reactionReversibility) {
		this.reactionReversibility = reactionReversibility;
	}

	public List<String> getReactionRefs() {
		return reactionRefs;
	}

	public void setReactionRefs(List<String> reactionRefs) {
		this.reactionRefs = reactionRefs;
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Organism[");
        builder.append("id=").append(getId()).append(",");
        builder.append("reactionDescription=").append(getReactionDescription()).append(",");
        builder.append("reactionKEGG=").append(getReactionKEGG()).append(",");
        builder.append("reactionBioCyc=").append(getReactionBioCyc()).append(",");
        builder.append("reactionReversibility=").append(getReactionReversibility()).append(",");
        builder.append("reactionRefs=[").append(getReactionRefs()).append("]");
        builder.append("]");

        return builder.toString();
    }

}
