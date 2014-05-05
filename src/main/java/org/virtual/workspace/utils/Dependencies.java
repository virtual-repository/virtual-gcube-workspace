package org.virtual.workspace.utils;

import static org.virtual.workspace.utils.Context.*;

import javax.inject.Named;

import org.gcube.common.homelibrary.home.HomeLibrary;
import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.virtual.workspace.WorkspacePlugin;

import dagger.Module;
import dagger.Provides;

@Module(injects=WorkspacePlugin.class)
public class Dependencies {

	
	@Provides @Named("current")
	String user() {
		return properties().lookup(username).value(String.class);
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
}
