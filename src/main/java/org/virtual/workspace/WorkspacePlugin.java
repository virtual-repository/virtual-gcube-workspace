package org.virtual.workspace;

import static java.util.Collections.*;

import java.util.Collection;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.workspace.utils.Dependencies;
import org.virtualrepository.Property;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.spi.Lifecycle;
import org.virtualrepository.spi.Plugin;

import dagger.ObjectGraph;

public class WorkspacePlugin implements Plugin, Lifecycle {

	public static final QName name = new QName("http://www.gcube-system.org","gCube Workspace");
	
	private static final Logger log = LoggerFactory.getLogger(WorkspacePlugin.class);
	
	private static final String description = "Personal user space in a gCube Infrastructure.";

	@Inject
	WorkspaceProxy proxy;
	

	@Override
	public void init() throws Exception {

		ObjectGraph.create(new Dependencies()).inject(this);
		
	}
	
	@Override
	public Collection<RepositoryService> services() {
		
		log.info("initialising plugin...");
		
		Property desc = new Property("description", description);
		
		return singleton(new RepositoryService(name,proxy,desc));
	}
}
