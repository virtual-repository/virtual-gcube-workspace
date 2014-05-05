package org.virtual.workspace;

import static org.gcube.common.homelibrary.home.workspace.WorkspaceItemType.*;
import static org.gcube.common.homelibrary.home.workspace.folder.FolderItemType.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.gcube.common.homelibrary.home.workspace.folder.FolderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.workspace.utils.Context;
import org.virtualrepository.AssetType;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;

@Singleton
public class WorkspaceBrowser implements Browser {
	
	private static final Logger log = LoggerFactory.getLogger(WorkspaceBrowser.class);
	
	@Inject
	Provider<Workspace> workspaces;

	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> types) throws Exception {
		
		log.info("discovering assets in workspace of "+Context.properties().lookup(Context.username).value(String.class));
		
		return assetsIn(workspaces.get());

	}
	
	private Iterable<? extends MutableAsset> assetsIn(Workspace ws) throws Exception {
		
		//dummy logic for now
		
		List<MutableAsset> items = new ArrayList<>();
		
		for (WorkspaceItem item : ws.getRoot().getChildren())
			if (item.getType()==FOLDER_ITEM) {
				FolderItem fi = (FolderItem) item;
				if (fi.getFolderItemType()==EXTERNAL_FILE)
					for (WorkspaceType type : WorkspaceType.values())
						if (fi.getMimeType().equals(type.mime()))
							items.add(type.toAsset(fi));
			}
		
		return items;
	}

}
