package org.virtual.workspace;

import static org.virtual.workspace.utils.Tags.*;
import static org.virtualrepository.CommonProperties.*;

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

public class StreamPublisher implements Publisher<Asset,InputStream> {

	private final WorkspaceType type;
	private final Provider<Workspace> ws;
	
	
	public StreamPublisher(Provider<Workspace> ws, WorkspaceType type) {
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
		
		Properties properties = asset.properties();
		
		Workspace workspace = ws.get();
		
		//if there is a description property, lift it up
		String description = properties.contains(DESCRIPTION.name())?
								properties.lookup(DESCRIPTION.name()).value().toString()
								:""; 

		String folderId = workspace.getRoot().getId();
														
		WorkspaceItem item = workspace.createExternalFile(asset.name(),description,type.mime(), content,folderId);
		
		//note: we cannot add properties to items at create time and we cannot add them all at once..
		
		org.gcube.common.homelibrary.home.workspace.Properties props = item.getProperties();
		
		//add version as property (if it exists)
		if (asset.version()!=null)
			props.addProperty(VERSION.name(),asset.version());
		
		//adds assets properties
		for (Property prop : properties)
			if (prop.isDisplay())
				props.addProperty(prop.name(),prop.value().toString());
		
		//adds "type tags"
		for (String tag : type.tags())
			props.addProperty(tag,"true");
		
		//callback for type-specicif additions
		type.toItem(asset,props);
	}

}
