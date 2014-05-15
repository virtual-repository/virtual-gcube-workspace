package org.virtual.workspace;

import static org.virtualrepository.spi.ImportAdapter.*;
import static org.virtualrepository.spi.PublishAdapter.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.workspace.types.WorkspaceType;
import org.virtual.workspace.types.WorkspaceTypes;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Lifecycle;
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
	Provider<CurrentUser> currentUser;
	
	@Inject
	WorkspaceTypes types;
	
	private final List<Publisher<?,?>> publishers = new ArrayList<Publisher<?,?>>();
	private final List<Importer<?,?>> importers = new ArrayList<Importer<?,?>>();

	
	@Override
	public void init() throws Exception {
		
		log.info("supported types {}",types);
		
		for (WorkspaceType type : types)
			
			addAccessorsFor(type);
		
	}
	
	@SuppressWarnings("all")
	private void addAccessorsFor(WorkspaceType type) {
		
		//type contributes importers and/or publishers based on type-specific APIs

		//they are all derived from stream-based base versions
		StreamImporter importer = new StreamImporter(ws,type);
		StreamPublisher publisher = new StreamPublisher(ws,type);
		
		
		//first derivation is no-op
		importers.add(importer);
		publishers.add(publisher);
		
		//add derived importer via transform stream->type
		
		Transform<?,?,?> fromStream = type.fromStream();
		
		if (fromStream!=null)
			importers.add(
					adapt((Importer) importer,fromStream)
			);
		
		//add derived publisher via transform type->stream
		
		Transform<?,?,?> toStream = type.toStream();
		
		if (toStream!=null)
			publishers.add(
					adapt((Publisher) publisher,toStream)
			);
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
