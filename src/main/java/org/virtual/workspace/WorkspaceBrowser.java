package org.virtual.workspace;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.workspace.types.WorkspaceType;
import org.virtual.workspace.types.WorkspaceTypes;
import org.virtualrepository.AssetType;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;

@Singleton
public class WorkspaceBrowser implements Browser {
	
	private static final Logger log = LoggerFactory.getLogger(WorkspaceBrowser.class);
	
	private final Provider<Workspace> ws;
	
	private final Provider<CurrentUser> currentUser;
	
	private final WorkspaceTypes types;
	
	@Inject
	public WorkspaceBrowser(Provider<Workspace> ws,  Provider<CurrentUser> currentUser, WorkspaceTypes types) {
		
		this.ws=ws;
		this.currentUser=currentUser;
		this.types=types;
		
	}

	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> assetTypes) throws Exception {
		
		CurrentUser user = currentUser.get();
		
		if (user==null) {
			log.warn("aborting discovery: no current user identifies target workspace");
			return emptyList();
		}
		
		log.info("discovering assets in {}'s workspace",user.name());
		
		return assetsIn(ws.get(),types.thatCorrespondTo(assetTypes));

	}
	
	private Iterable<? extends MutableAsset> assetsIn(Workspace ws,Iterable<WorkspaceType> types) throws Exception {
		
		List<MutableAsset> items = new ArrayList<>();
		
		for (WorkspaceType type : types)
			for (WorkspaceItem item : ws.searchByProperties(new ArrayList<>(type.tags())))
				items.add(type.toAsset(item));
		
		return items;
	}
}
