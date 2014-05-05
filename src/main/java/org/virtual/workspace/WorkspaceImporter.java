package org.virtual.workspace;

import java.io.InputStream;

import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.gcube.common.homelibrary.home.workspace.folder.items.ExternalFile;
import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Importer;

public class WorkspaceImporter implements Importer<Asset,InputStream> {

	private final WorkspaceType type;
	private final Workspace ws;
	
	
	public WorkspaceImporter(Workspace ws, WorkspaceType type) {
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
		
		return ExternalFile.class.cast(ws.getItem(asset.id())).getData();
	
	}

}
