package org.acme;

import static java.lang.System.*;
import static org.virtual.workspace.utils.Context.*;

import java.io.InputStream;
import java.util.Set;

import org.gcube.common.scope.api.ScopeProvider;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.model.StructureWorkspace;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.sdmxbeans.model.SdmxStructureFormat;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.sdmxsource.util.io.ReadableDataLocationTmp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.workspace.WorkspacePlugin;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.csv.CsvStream2Table;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

public class IntegrationTests {

	private static final Logger log = LoggerFactory.getLogger(IntegrationTests.class);
	
	@BeforeClass
	public static void setup() {
		
		setProperty("org.slf4j.simpleLogger.log.org.virtual", "trace");
		properties().add(user("fabio.simeoni"));
		ScopeProvider.instance.set("/gcube/devsec");
	}
	
	@Test
	public void discover() {
		
	
		VirtualRepository repository = new Repository();
		
		repository.discover(1000000,CsvCodelist.type,SdmxCodelist.type);
		
		for (Asset s : repository)
			log.info("name={}\nproperties{}\n",s.name(),s.properties());
	}
	
	
	@Test
	public void retrieveCsvCodelist() throws Exception {
	
		VirtualRepository repository = new Repository();
		
		repository.discover(1000000,CsvCodelist.type);
		
		Asset codelist = repository.iterator().next();
		
		Table table = repository.retrieve(codelist,Table.class);
		
		for (Row row : table)
			System.out.println(row);
	}
	
	@Test
	public void retrieveSdmxCodelist() throws Exception {
	
		VirtualRepository repository = new Repository();
		
		repository.discover(1000000,SdmxCodelist.type);
		
		Asset codelist = repository.iterator().next();
		
		CodelistBean bean = repository.retrieve(codelist,CodelistBean.class);
		
		print(bean);
		
		
	}
	
	@Test
	public void publishCsvCodelist() throws Exception {

		InputStream stream = getClass().getResourceAsStream("/samplecsv.txt");
		
		if (stream==null)
			throw new RuntimeException("missing test resource");

		VirtualRepository repository = new Repository();
		
		RepositoryService service = repository.services().lookup(WorkspacePlugin.name);
		
		CsvCodelist codelist = new CsvCodelist("sample-csv-codelist",0, service);
		
		Table table = new CsvStream2Table<>().apply(codelist,stream);
		
		repository.publish(codelist,table);
	}
	
	@Test
	public void publishSdmxCodelist() throws Exception {

		InputStream stream = getClass().getResourceAsStream("/samplesdmx.xml");
		
		if (stream==null)
			throw new RuntimeException("missing test resource");

		VirtualRepository repository = new Repository();
		
		RepositoryService service = repository.services().lookup(WorkspacePlugin.name);
		
		SdmxCodelist codelist = new SdmxCodelist("sample-sdmx-codelist",service);
		
		repository.publish(codelist,bean(stream));
	}
	
	
	private CodelistBean bean(InputStream stream) {
		
		StructureWorkspace ws = SdmxServiceFactory.parser().parseStructures(new ReadableDataLocationTmp(stream));

		SdmxBeans beans = ws.getStructureBeans(false);

		Set<CodelistBean> listBeans = beans.getCodelists();

		if (listBeans.isEmpty() || listBeans.size() > 1)
			throw new IllegalArgumentException(
					"stream includes no codelists or is ambiguous, i.e. contains multiple codelists");

		return listBeans.iterator().next();
	}
	
	private void print(CodelistBean bean) {
		
		SdmxBeans beans = new SdmxBeansImpl(bean);
		
		STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
		
		
		SdmxServiceFactory.writer().writeStructures(beans,new SdmxStructureFormat(format),System.out);
	}
}
