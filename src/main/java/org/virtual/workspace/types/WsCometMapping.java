package org.virtual.workspace.types;

import static java.util.Arrays.*;
import static org.virtual.workspace.utils.Tags.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.fao.fi.comet.mapping.model.MappingData;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtualrepository.fmf.CometAsset;
import org.virtualrepository.spi.Transform;

@Singleton
public class WsCometMapping extends AbstractWorkspaceType<CometAsset,MappingData> {

	private final JAXBContext ctx;

	
	private final Transform<CometAsset, InputStream,MappingData> importTransform = new Transform<CometAsset, InputStream,MappingData>() {
		
		@Override
		public MappingData apply(CometAsset asset, InputStream input) throws Exception {
			
			return (MappingData) ctx.createUnmarshaller().unmarshal(input);
		}
		
		@SuppressWarnings("all")
		public Class<MappingData> outputAPI() {
			return MappingData.class;
		};
		
		@Override
		public Class<InputStream> inputAPI() {
			return InputStream.class;
		}
	};

	private final Transform<CometAsset,MappingData, InputStream> publishTransform = new Transform<CometAsset,MappingData, InputStream>() {
		
		public InputStream apply(CometAsset asset, MappingData input) throws Exception {
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream(2048);
			ctx.createMarshaller().marshal(input,stream);
			return new ByteArrayInputStream(stream.toByteArray());
		};
		
		public Class<InputStream> outputAPI() {
			return InputStream.class;
		};
		
		@Override
		public Class<MappingData> inputAPI() {
			return MappingData.class;
		}
	};
	
	
	@Inject
	public WsCometMapping(JAXBContext ctx) {
		super(CometAsset.type, "application/xml");
		this.ctx=ctx;
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
	public Transform<CometAsset, InputStream, MappingData> transformOnImport() {
		return importTransform;
	}

	@Override
	public Transform<CometAsset, MappingData, InputStream> transformOnPublih() {
		return publishTransform;
	}

}
