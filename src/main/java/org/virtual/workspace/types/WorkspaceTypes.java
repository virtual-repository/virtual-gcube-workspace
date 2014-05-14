package org.virtual.workspace.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.virtualrepository.AssetType;

@Singleton
public class WorkspaceTypes {

	private final Map<AssetType,WorkspaceType> mapping = new HashMap<>(); 
	
	@Inject
	public WorkspaceTypes(Set<WorkspaceType> types) {
		
		for (WorkspaceType type : types)
			mapping.put(type.assetType(),type);
	}
	
	public WorkspaceType map(AssetType type) {
		
		return mapping.get(type);

	}

	public Collection<WorkspaceType> map(Collection<? extends AssetType> types) {
		
		Set<WorkspaceType> wtypes = new HashSet<>();
		
		for(AssetType atype : types)
			if (mapping.containsKey(atype))
				wtypes.add(mapping.get(atype));
		
		return wtypes;
	}
	
	public Iterable<WorkspaceType> all() {
		return mapping.values();
	}
	
}
