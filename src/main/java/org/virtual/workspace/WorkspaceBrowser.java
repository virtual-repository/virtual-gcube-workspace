package org.virtual.workspace;

import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.virtualrepository.AssetType;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;

@Singleton
public class WorkspaceBrowser implements Browser {

	@Inject
	public WorkspaceBrowser() {
		
	}
	
	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> types) throws Exception {
		return Collections.emptyList();
	}

}
