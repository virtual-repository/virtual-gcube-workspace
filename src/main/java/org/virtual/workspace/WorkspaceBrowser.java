package org.virtual.workspace;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.workspace.types.WorkspaceType;
import org.virtualrepository.AssetType;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;

@Singleton
public class WorkspaceBrowser implements Browser {
	
	private static final Logger log = LoggerFactory.getLogger(WorkspaceBrowser.class);
	
	private final Provider<Workspace> ws;
	
	private final Provider<CurrentUser> currentUser;
	
	private final Map<AssetType,WorkspaceType> mapping = new HashMap<>(); 
	
	@Inject
	public WorkspaceBrowser(Provider<Workspace> ws,  Provider<CurrentUser> currentUser, Set<WorkspaceType> types) {
		
		this.ws=ws;
		
		this.currentUser=currentUser;
		
		for (WorkspaceType type : types)
			mapping.put(type.assetType(),type);
	}

	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> types) throws Exception {
		
		CurrentUser user = currentUser.get();
		
		if (user==null) {
			log.warn("aborting discovery as there is no current user to identify target workspace");
			return emptyList();
		}
		
		log.info("discovering assets in workspace of "+user.name());
		
		return assetsIn(ws.get(),invert(types));

	}
	
	private Iterable<? extends MutableAsset> assetsIn(Workspace ws,Iterable<WorkspaceType> types) throws Exception {
		
		//TODO dummy logic for now, will send a query.
		
		List<MutableAsset> items = new ArrayList<>();
		
		for (WorkspaceItem item : ws.getRoot().getChildren()) { 
			Set<String> keys = item.getProperties().getProperties().keySet();
			for (WorkspaceType type : types)
				if (keys.containsAll(type.tags()))
					items.add(type.toAsset(item));
	
		}
			
		return items;
	}

	private Collection<WorkspaceType> invert(Collection<? extends AssetType> types) {
		
		Set<WorkspaceType> wtypes = new HashSet<>();
		
		for(AssetType atype : types)
			if (mapping.containsKey(atype))
				wtypes.add(mapping.get(atype));
		
		return wtypes;
	}
}
