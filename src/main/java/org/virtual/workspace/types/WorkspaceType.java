package org.virtual.workspace.types;

import java.io.InputStream;
import java.util.Set;

import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.MutableAsset;
import org.virtualrepository.spi.Transform;

public interface WorkspaceType {

	MutableAsset toAsset(WorkspaceItem item) throws Exception;
	
	Transform<?,InputStream,?>  transformOnImport();
	
	Transform<?,?,InputStream>  transformOnPublih();
	
	Type<?> assetType();
	
	Set<String> tags();
	
	String mime();
}