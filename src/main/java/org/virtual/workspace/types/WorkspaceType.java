package org.virtual.workspace.types;

import java.io.InputStream;
import java.util.Set;

import org.gcube.common.homelibrary.home.workspace.Properties;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.MutableAsset;
import org.virtualrepository.spi.Transform;

public interface WorkspaceType {

	MutableAsset toAsset(WorkspaceItem item) throws Exception;
	
	void toItem(Asset asset, Properties propertie) throws Exception;
	
	Transform<?,InputStream,?>  transformOnImport();
	
	Transform<?,?,InputStream>  transformOnPublih();
	
	Type<?> assetType();
	
	Set<String> tags();
	
	String mime();
}