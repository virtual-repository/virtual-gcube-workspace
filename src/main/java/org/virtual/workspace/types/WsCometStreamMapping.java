package org.virtual.workspace.types;

import static java.util.Arrays.*;
import static org.virtual.workspace.utils.Tags.*;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtualrepository.comet.CometAsset;
import org.virtualrepository.spi.Transform;

@Singleton
public class WsCometStreamMapping extends AbstractWorkspaceType<CometAsset,InputStream> {

	private final Transform<CometAsset, InputStream,InputStream> identity = new Transform<CometAsset, InputStream,InputStream>() {
		
		@Override
		public InputStream apply(CometAsset asset, InputStream input) throws Exception {
			
			return input;
		}
		
		@Override
		public Class<InputStream> outputAPI() {
			return InputStream.class;
		};
		
		@Override
		public Class<InputStream> inputAPI() {
			return InputStream.class;
		}
	};

	@Inject
	public WsCometStreamMapping() {
		super(CometAsset.type, "application/xml");
	}

	@Override
	public CometAsset getAsset(WorkspaceItem item) throws Exception {
		return new CometAsset(item.getId(),item.getName());
	}
	
	@Override
	public Set<String> tags() {
		return new HashSet<>(asList(MAPPING.name(),COMET.name()));
	}

	@Override
	public Transform<CometAsset, InputStream, InputStream> transformOnImport() {
		return identity;
	}

	@Override
	public Transform<CometAsset, InputStream, InputStream> transformOnPublih() {
		return identity;
	}

}
