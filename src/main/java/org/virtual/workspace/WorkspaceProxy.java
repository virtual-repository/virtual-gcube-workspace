package org.virtual.workspace;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.workspace.types.WorkspaceType;
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

	private static Logger log = LoggerFactory.getLogger(WorkspaceProxy.class);
	
	@Inject
	WorkspaceBrowser browser;
	
	@Inject
	Provider<Workspace> ws;
	
	@Inject
	Set<WorkspaceType> types;
	
	private final List<Publisher<?,?>> publishers = new ArrayList<Publisher<?,?>>();
	private final List<Importer<?,?>> importers = new ArrayList<Importer<?,?>>();

	
	@Override
	@SuppressWarnings("all")
	public void init() throws Exception {
		
		log.info("supported types {}",types);
		
		for (WorkspaceType type : types) {
			
			Importer<?,?> baseImporter = new WorkspaceImporter(ws,type);
			Publisher<?,?> basePublisher = new WorkspacePublisher(ws,type);
			
			//derived transforms
			
			Transform<?,InputStream,?> importTransform = type.transformOnImport();
			
			if (importTransform!=null) {
				Importer<?,?> importer = ImportAdapter.adapt((Importer) baseImporter,importTransform);
				importers.add(importer);
			}
		
			
			Transform<?,?,InputStream> publishTransform = type.transformOnPublih();
			
			if (publishTransform!=null) {
				Publisher<?,?> publisher = PublishAdapter.adapt((Publisher) basePublisher,publishTransform);
				publishers.add(publisher);
			}
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
