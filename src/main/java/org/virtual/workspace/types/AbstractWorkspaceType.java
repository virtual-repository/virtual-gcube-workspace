package org.virtual.workspace.types;

import org.virtualrepository.impl.Type;

public abstract class AbstractWorkspaceType implements WorkspaceType {

	private final Type<?> type;
	private final String mime;
	
	public AbstractWorkspaceType(Type<?> type, String mime) {
		this.type = type;
		this.mime =mime;
	} 
	
	public Type<?> assetType() {
		return type;
	}
	
	public String mime() {
		return mime;
	}
	
	@Override
	public String toString() {
		boolean read = transformOnImport()!=null;
		boolean write = transformOnPublih()!=null;
		return String.format("%s (%s|%s: %s)",type,read?"R":"-",write?"W":"-",tags());
	}
	
	
}
