package org.virtual.workspace.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.virtualrepository.AssetType;

@Singleton
public class WorkspaceTypes implements Iterable<WorkspaceType> {

	private final Map<AssetType,WorkspaceType> mapping = new HashMap<>(); 
	
	@Inject
	public WorkspaceTypes(Set<WorkspaceType> types) {
		
		for (WorkspaceType type : types)
			mapping.put(type.assetType(),type);
	}
	
	public WorkspaceType map(AssetType type) {
		
		return mapping.get(type);

	}

	public Collection<WorkspaceType> map(Collection<? extends AssetType> assetTypes) {
		
		Set<WorkspaceType> types = new HashSet<>();
		
		for(AssetType assetType : assetTypes)
			if (mapping.containsKey(assetType))
				types.add(mapping.get(assetType));
		
		return types;
	}
	
	public Iterator<WorkspaceType> iterator() {
		return mapping.values().iterator();
	}
	
	@Override
	public String toString() {
		return mapping.values().toString();
	}
	
}
