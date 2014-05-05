package org.virtual.workspace.types;

import java.util.Map.Entry;

import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtualrepository.Property;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.MutableAsset;

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
	public MutableAsset toAsset(WorkspaceItem item) throws Exception {
		MutableAsset asset = getAsset(item);
		for (Entry<String,String> entry : item.getProperties().getProperties().entrySet())
			asset.properties().add(new Property(entry.getKey(),entry.getValue()));
		return asset;
	}
	
	protected abstract MutableAsset getAsset(WorkspaceItem item) throws Exception;
	
	@Override
	public String toString() {
		boolean read = transformOnImport()!=null;
		boolean write = transformOnPublih()!=null;
		return String.format("%s (%s|%s: %s)",type,read?"R":"-",write?"W":"-",tags());
	}
	
	
}
