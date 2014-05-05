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
import org.virtualrepository.spi.PublishAdapter;
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
		
		
		for (WorkspaceType type : WorkspaceType.values()) {
			
			Importer<?,?> baseImporter = new WorkspaceImporter(ws,type);
			
			Transform<?,InputStream,?> importTransform = type.transformOnImport();
			Importer<?,?> importer = ImportAdapter.adapt((Importer) baseImporter,importTransform);
			importers.add(importer);
		
			Publisher<?,?> basePublisher = new WorkspacePublisher(ws,type);
			Transform<?,?,InputStream> publishTransform = type.transformOnPublih();
			Publisher<?,?> publisher = PublishAdapter.adapt((Publisher) basePublisher,publishTransform);
			publishers.add(publisher);
			
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
