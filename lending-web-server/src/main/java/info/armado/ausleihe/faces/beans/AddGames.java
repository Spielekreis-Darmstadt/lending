package info.armado.ausleihe.faces.beans;

import java.io.Serializable;

import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;

public class AddGames implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Produces
	@FlowDefinition
	public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {

		String flowId = "addGames";
		flowBuilder.id("", flowId);
		flowBuilder.viewNode(flowId, "/addGames.xhtml").markAsStartNode();
		flowBuilder.viewNode("addGames2", "/addGames-page2.xhtml");
		flowBuilder.viewNode("addGames3", "/addGames-page3.xhtml");
		flowBuilder.viewNode("addGames4", "/addGames-page4.xhtml");
		flowBuilder.returnNode("returnAddGames")
			.fromOutcome("index");
		
		return flowBuilder.getFlow();
	}

}
