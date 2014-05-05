package org.virtual.workspace;

import java.io.InputStream;

import javax.inject.Provider;

import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.gcube.common.homelibrary.home.workspace.folder.items.ExternalFile;
import org.virtual.workspace.types.WorkspaceType;
import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Importer;

public class WorkspaceImporter implements Importer<Asset,InputStream> {

	private final WorkspaceType type;
	private final Provider<Workspace> ws;
	
	
	public WorkspaceImporter(Provider<Workspace> ws, WorkspaceType type) {
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
	public InputStream retrieve(Asset asset) throws Exception {
		
		Workspace workspace = ws.get();
		
		return ExternalFile.class.cast(workspace.getItem(asset.id())).getData();
	
	}

}
