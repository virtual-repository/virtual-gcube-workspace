package org.virtual.workspace.types;

import java.io.InputStream;
import java.util.Set;

import org.gcube.common.homelibrary.home.workspace.Properties;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.MutableAsset;
import org.virtualrepository.spi.Transform;

//a file type supported by this plugin, maps onto an asset type
public interface WorkspaceType {

	//a raw interface simplifies clients and DI. 
	//implementations are typed instead.

	//the corresponding asset type
	Type<?> assetType();
	
	//transforms stream->API, API->stream from which type-specific accessors are derived
	Transform<?,InputStream,?>  fromStream();
	Transform<?,?,InputStream>  toStream();
	
	//tags used to "recognise" workspace items of this type 
	Set<String> tags();
	
	
	//callbacks at discovery and publication time
	
	MutableAsset toAsset(WorkspaceItem item) throws Exception;

	void toItem(Asset asset, Properties propertie) throws Exception;
	
	
	String mime();
}