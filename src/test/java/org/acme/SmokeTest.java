package org.acme;

import static org.acme.Utils.*;
import static org.junit.Assert.*;
import static org.virtualrepository.CommonProperties.*;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.junit.Test;
import org.virtual.workspace.CurrentUser;
import org.virtual.workspace.WorkspacePlugin;
import org.virtual.workspace.types.WorkspaceType;
import org.virtual.workspace.utils.Dependencies;
import org.virtualrepository.Context;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.impl.Repository;

import dagger.Module;

@Module(injects=SmokeTest.class,includes=Dependencies.class)
public class SmokeTest {

	@Inject
	WorkspacePlugin plugin;
	
	@Inject
	Set<WorkspaceType> types;
	
	@Inject
	Provider<CurrentUser> user;

	
	@Test
	public void dependenciesAreInjected() {
		
		inject(this);
		
		assertNotNull(plugin);
		
		assertNotNull(user);
		
		assertFalse(types.isEmpty());
		
	}
	
	@Test
	public void pluginCanBeActivated() {
		
		VirtualRepository repo = new Repository();
		
		assertNotNull(repo.services());
		
	}
	
	
	@Test
	public void usersIsProvidedIfOneExists() {
		
		inject(this);
		
		assertNull(user.get());
		
		Context.properties().add(USERNAME.property("me"));
		
		assertEquals("me",  user.get().name());
		
		Context.reset();
	}
	
	@Test
	public void discoverySilentlyAbortsWithoutCurrentUser() {
		
		VirtualRepository repo = new Repository();
		
		assertNotNull(repo.services());
		
		repo.discover(CsvCodelist.type);
		
		
	}
	
	
}


