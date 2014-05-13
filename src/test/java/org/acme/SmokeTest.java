package org.acme;

import static org.acme.Utils.*;
import static org.junit.Assert.*;

import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;
import org.virtual.workspace.WorkspacePlugin;
import org.virtual.workspace.types.WorkspaceType;
import org.virtual.workspace.utils.Dependencies;
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

	
	@Test
	public void dependenciesAreInjected() {
		
		inject(this);
		
		assertNotNull(plugin);
		
		assertFalse(types.isEmpty());
		
	}
	
	@Test
	public void pluginCanBeActivated() {
		
		VirtualRepository repo = new Repository();
		
		assertNotNull(repo.services());
		
	}
	
	
	@Test
	public void discoverySilentlyAbortsWithoutCurrentUser() {
		
		VirtualRepository repo = new Repository();
		
		assertNotNull(repo.services());
		
		repo.discover(CsvCodelist.type);
		
		
	}
	
	
}


