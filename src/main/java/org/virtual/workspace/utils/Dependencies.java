package org.virtual.workspace.utils;

import static dagger.Provides.Type.*;
import static org.virtualrepository.CommonProperties.*;
import static org.virtualrepository.Context.*;

import javax.inject.Named;

import org.gcube.common.homelibrary.home.HomeLibrary;
import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.manager.output.StructureWriterManager;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.virtual.workspace.WorkspacePlugin;
import org.virtual.workspace.types.WorkspaceType;
import org.virtual.workspace.types.WsCsvCodelist;
import org.virtual.workspace.types.WsSdmxCodelist;
import org.virtualrepository.Properties;

import dagger.Module;
import dagger.Provides;

@Module(injects=WorkspacePlugin.class)
public class Dependencies {

	
	@Provides @Named("current")
	String user() {
		
		Properties contextual = properties();
		
		if (contextual.contains(USERNAME.name()))
			return contextual.lookup(USERNAME.name()).value(String.class);
		else
			throw new IllegalStateException("no current user");
	}
	
	@Provides
	Workspace workspace(@Named("current") String user) {
			
		try {
			return HomeLibrary.getUserWorkspace(user);
		}
		catch(Exception e) {
			throw new RuntimeException("cannot access workspace of "+user,e);
		}
	}
	
	@Provides(type=SET)
	WorkspaceType csvcodelist() {
		return new WsCsvCodelist();
	}
	
	@Provides(type=SET)
	WorkspaceType sdmxcodelist(WsSdmxCodelist list) {
		return list;
	}
	
	@Provides
	StructureParsingManager parser() {
		return SdmxServiceFactory.parser();
	}
	
	@Provides
	StructureWriterManager writer() {
		return SdmxServiceFactory.writer();
	}
}
