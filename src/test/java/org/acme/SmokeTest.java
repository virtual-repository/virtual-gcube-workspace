package org.acme;

import static org.acme.Utils.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;
import org.virtual.workspace.WorkspacePlugin;
import org.virtual.workspace.utils.Dependencies;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;

import dagger.Module;

@Module(injects=SmokeTest.class,includes=Dependencies.class)
public class SmokeTest {

	@Inject
	WorkspacePlugin plugin;
	
	@Test
	public void dependenciesAreInjected() {
		
		inject(this);
		
		assertNotNull(plugin);
		
	}
	
	@Test
	public void pluginCanBeActivated() {
		
		VirtualRepository repo = new Repository();
		
		assertNotNull(repo.services());
		
	}
	
	
	
}


