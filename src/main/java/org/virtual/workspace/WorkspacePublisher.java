package org.virtual.workspace;

import static org.virtualrepository.utils.CommonProperties.*;

import java.io.InputStream;

import javax.inject.Provider;

import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtual.workspace.types.WorkspaceType;
import org.virtualrepository.Asset;
import org.virtualrepository.Properties;
import org.virtualrepository.Property;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Publisher;

public class WorkspacePublisher implements Publisher<Asset,InputStream> {

	private final WorkspaceType type;
	private final Provider<Workspace> ws;
	
	
	public WorkspacePublisher(Provider<Workspace> ws, WorkspaceType type) {
		this.type=type;
		this.ws=ws;
	}
	
	
	@Override
	public Type<? extends Asset> type() {
		return type.assetType();
	}

	@Override
	public Class<InputStream> api() {
		return InputStream.class;
	}

	@Override
	public void publish(Asset asset, InputStream content) throws Exception {
		
		Workspace workspace = ws.get();
		
		String folderId = workspace.getRoot().getId();
		
		Properties properties = asset.properties();
		String description = properties.contains(DESCRIPTION.name())?
								properties.lookup(DESCRIPTION.name()).value().toString()
								:""; 
				
		WorkspaceItem item = workspace.createExternalFile(asset.name(),description,type.mime(), content,folderId);
		
		copy(properties,item);
	}
	
	private void copy(Properties properties, WorkspaceItem item) throws Exception {
		
		for (Property prop : properties)
			if (prop.isDisplay())
				item.getProperties().addProperty(prop.name(),prop.value().toString());
		
		for (String tag : type.tags())
			item.getProperties().addProperty(tag,"true");
		
	}

}
