package com.poplicus.crawler.codegen;

public class RHSTask {
	
	public enum RHSTaskType {
		ReturnAVariable,
		ReturnNothing
	}
	
	private RHSTaskType rhsTaskType = null;
	
	private Variable returnVariable = null;
	
	public RHSTask(RHSTaskType rhsTaskType) throws RHSTaskException {
		if(rhsTaskType == null) {
			throw new RHSTaskException("RHS Task Type cannot be null.");
		}
		this.rhsTaskType = rhsTaskType;
	}
	
	public void setReturnVariable(Variable returnVariable) {
		this.returnVariable = returnVariable;
	}
	
	public Variable getReturnVariable() {
		return this.returnVariable;
	}
	
	public void validateTaskDefinition() throws RHSTaskException {
		switch (rhsTaskType) {
			case ReturnAVariable:
				if(returnVariable == null) {
					throw new RHSTaskException("Return Variable cannot be null when RHS Task Type is ReturnAVariable.");
				} else {
					if(!(returnVariable.getName() != null && returnVariable.getName().length() > 0)) {
						throw new RHSTaskException("Return Variable has null name.");
					}
				}
			break;
			
			case ReturnNothing:
				
			break;
	
			default:
			
			break;
		}
	}
	
	public StringBuffer toStringBuffer() throws RHSTaskException {
		validateTaskDefinition();
		StringBuffer code = new StringBuffer();
		switch (rhsTaskType) {
			case ReturnAVariable:
				code.append(Constants.RETURN).append(Constants.SPACE).append(returnVariable.getName());
			break;
			
			case ReturnNothing:
				code.append(Constants.RETURN);
			break;
		
			default:
			break;
		}
		return code;
	}
	
	public RHSTaskType getRHSTaskType() {
		return this.rhsTaskType;
	}

}
