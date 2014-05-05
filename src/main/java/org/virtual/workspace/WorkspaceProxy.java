package org.virtual.workspace;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.ImportAdapter;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Lifecycle;
import org.virtualrepository.spi.Publisher;
import org.virtualrepository.spi.ServiceProxy;
import org.virtualrepository.spi.Transform;


@Singleton
public class WorkspaceProxy implements ServiceProxy, Lifecycle {

	@Inject
	WorkspaceBrowser browser;
	
	@Inject
	Workspace ws;
	
	private final List<Publisher<?,?>> publishers = new ArrayList<Publisher<?,?>>();
	private final List<Importer<?,?>> importers = new ArrayList<Importer<?,?>>();

	
	@Override
	@SuppressWarnings("all")
	public void init() throws Exception {
		
		
		for (WorkspaceAssetType type : WorkspaceAssetType.values()) {
			
			Importer<?,?> base = new WorkspaceImporter(ws,type.assetType());
			Transform<?,InputStream,?> transform = type.transform();
			Importer<?,?> importer = ImportAdapter.adapt((Importer) base,transform);
			
			importers.add(importer);
		}
		
	}
	
	@Override
	public Browser browser() {
		return browser;
	}

	@Override
	public List<? extends Importer<?, ?>> importers() {
		
		return importers;
	}

	@Override
	public List<? extends Publisher<?, ?>> publishers() {
		return publishers;
	}

}
