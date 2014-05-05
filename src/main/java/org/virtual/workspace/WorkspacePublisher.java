package org.virtual.workspace;

import java.io.InputStream;

import javax.inject.Provider;

import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.virtual.workspace.types.WorkspaceType;
import org.virtualrepository.Asset;
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
		
		workspace.createExternalFile(asset.name(),"",type.mime(), content,folderId);
		
	}

}
