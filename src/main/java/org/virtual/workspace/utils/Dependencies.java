package org.virtual.workspace.utils;

import static dagger.Provides.Type.*;
import static org.virtualrepository.CommonProperties.*;
import static org.virtualrepository.Context.*;

import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;

import org.fao.fi.comet.mapping.model.MappingData;
import org.gcube.common.homelibrary.home.HomeLibrary;
import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.manager.output.StructureWriterManager;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.virtual.workspace.CurrentUser;
import org.virtual.workspace.WorkspacePlugin;
import org.virtual.workspace.types.WorkspaceType;
import org.virtual.workspace.types.WsCometMapping;
import org.virtual.workspace.types.WsCsvCodelist;
import org.virtual.workspace.types.WsSdmxCodelist;
import org.virtualrepository.Properties;

import dagger.Module;
import dagger.Provides;

@Module(injects=WorkspacePlugin.class)
public class Dependencies {

	//provides current user, if it exists.
	//can inject a Provider<CurrentUser> if want to check and tolerate absence (discovery)
	//can inject one directly if current user is required (retrieval, publication)
	@Provides
	CurrentUser user() {
		
		Properties contextual = properties();
		
		return contextual.contains(USERNAME.name())?
			new CurrentUser(contextual.lookup(USERNAME.name()).value(String.class))
		:
			null;
	}
	
	//provides workspace of current user, if one is set.
	//meant to be injected as Provider<Workspace> and obtained lazily just before use. 
	@Provides
	Workspace workspace(CurrentUser user) {
			
		if (user==null)
				throw new IllegalStateException("no current user");
		try {
			return HomeLibrary.getUserWorkspace(user.name());
		}
		catch(Exception e) {
			throw new RuntimeException("cannot access workspace of "+user,e);
		}
	}
	
	
	//suported types: gathered and injected in WorkspaceTypes
	
	@Provides(type=SET) @Singleton
	WorkspaceType csvcodelist(WsCsvCodelist list) {
		return list;
	}
	
	@Provides(type=SET) @Singleton
	WorkspaceType sdmxcodelist(WsSdmxCodelist list) {
		return list;	
	}
	
	@Provides(type=SET) @Singleton
	WorkspaceType cometmapping(WsCometMapping mapping) {
		return mapping;
	}
	
	
	
	//sdmx I/O facilities for testing
	
	@Provides
	StructureParsingManager parser() {
		return SdmxServiceFactory.parser();
	}
	
	@Provides
	StructureWriterManager writer() {
		return SdmxServiceFactory.writer();
	}
	
	@Provides @Singleton
	JAXBContext jaxb() {
		
		try {
			return JAXBContext.newInstance(MappingData.class);
		}
		catch(Exception e) {
			throw new RuntimeException("configuration error: cannot instantiate JAXB context",e);
		}
	}
}
