package org.virtual.workspace.types;

import static java.util.Arrays.*;
import static org.virtual.workspace.utils.Tags.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWriterManager;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.sdmxsource.sdmx.api.model.StructureWorkspace;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.sdmxbeans.model.SdmxStructureFormat;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.sdmxsource.util.io.ReadableDataLocationTmp;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.MutableAsset;
import org.virtualrepository.spi.Transform;

@Singleton
public class WsSdmxCodelist extends AbstractWorkspaceType {

	private final StructureParsingManager parser;
	private final StructureWriterManager writer;

	
	private final Transform<?, InputStream, ?> importTransform = new Transform<SdmxCodelist, InputStream,CodelistBean>() {
		
		@Override
		public CodelistBean apply(SdmxCodelist asset, InputStream input) throws Exception {
			
			StructureWorkspace ws = parser.parseStructures(new ReadableDataLocationTmp(input));

			SdmxBeans beans = ws.getStructureBeans(false);

			Set<CodelistBean> listBeans = beans.getCodelists();

			if (listBeans.isEmpty() || listBeans.size() > 1)
				throw new IllegalArgumentException(
						"stream includes no codelists or is ambiguous, i.e. contains multiple codelists");

			return listBeans.iterator().next();
		}
		
		public java.lang.Class<CodelistBean> outputAPI() {
			return CodelistBean.class;
		};
		
		@Override
		public Class<InputStream> inputAPI() {
			return InputStream.class;
		}
	};

	private final Transform<?, ?, InputStream> publishTransform = new Transform<SdmxCodelist,CodelistBean, InputStream>() {
		
		public InputStream apply(SdmxCodelist asset, CodelistBean input) throws Exception {
			
			SdmxBeans beans = new SdmxBeansImpl(input);
			
			STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			
			writer.writeStructures(beans,new SdmxStructureFormat(format),stream);
			
			return new ByteArrayInputStream(stream.toByteArray());
		};
		
		public Class<InputStream> outputAPI() {
			return InputStream.class;
		};
		
		@Override
		public Class<CodelistBean> inputAPI() {
			return CodelistBean.class;
		}
	};
	
	
	@Inject
	public WsSdmxCodelist(StructureParsingManager parser,StructureWriterManager writer) {
		super(SdmxCodelist.type, "application/xml");
		this.parser=parser;
		this.writer=writer;
	}

	@Override
	public MutableAsset getAsset(WorkspaceItem item) throws Exception {
		return new SdmxCodelist(item.getId(),item.getId(),"n/a", item.getName());
	}
	
	@Override
	public Set<String> tags() {
		return new HashSet<>(asList(CODELIST.name(),SDMX.name()));
	}

	@Override
	public Transform<?, InputStream, ?> transformOnImport() {
		return importTransform;
	}

	@Override
	public Transform<?, ?, InputStream> transformOnPublih() {
		return publishTransform;
	}

}
