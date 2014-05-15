package org.acme;

import static java.lang.String.*;
import static java.lang.System.*;
import static org.fao.fi.comet.mapping.dsl.DataProviderDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingContributionDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDataDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDetailDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingElementDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingElementIdentifierDSL.*;
import static org.fao.fi.comet.mapping.dsl.MatcherConfigurationDSL.*;
import static org.fao.fi.comet.mapping.dsl.MatcherConfigurationPropertyDSL.*;
import static org.virtualrepository.CommonProperties.*;
import static org.virtualrepository.Context.*;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.fao.fi.comet.mapping.model.DataProvider;
import org.fao.fi.comet.mapping.model.Mapping;
import org.fao.fi.comet.mapping.model.MappingData;
import org.gcube.common.homelibrary.home.HomeLibrary;
import org.gcube.common.homelibrary.home.workspace.Workspace;
import org.gcube.common.homelibrary.home.workspace.WorkspaceFolder;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
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
import org.virtualrepository.comet.CometAsset;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.csv.CsvStream2Table;
import org.virtualrepository.csv.CsvTable;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

public class IntegrationTests {

	private static final Logger log = LoggerFactory.getLogger(IntegrationTests.class);
	
	@BeforeClass
	public static void setup() {
		
		setProperty("org.slf4j.simpleLogger.log.org.virtual", "trace");
		properties().add(USERNAME.property("fabio.simeoni"));
		ScopeProvider.instance.set("/gcube/devsec");
	}
	
	@Test
	public void accessTest() throws Exception {
		
		ScopeProvider.instance.set("/gcube/devsec");
		
		long start = currentTimeMillis(),
			 last =start;
		
		Workspace ws = HomeLibrary.getUserWorkspace("fabio.simeoni");
		
		System.out.println(format("workspace in %s ms.",currentTimeMillis() - last));
		
		last = currentTimeMillis();
		
		WorkspaceFolder root = ws.getRoot();
		
		System.out.println(format("root in %s ms.",currentTimeMillis() - last));
		
		last = currentTimeMillis();
		
		List<WorkspaceItem> children = root.getChildren();

		System.out.println(format("children in %s ms.",currentTimeMillis() - last));

		last = currentTimeMillis();
		
		for (WorkspaceItem item : children) {
			
			System.out.println(format("item in %s ms.",currentTimeMillis() - last));
		
			last = currentTimeMillis();
			
			item.getProperties().getProperties().keySet();
			
			System.out.println(format("item properties in %s ms.",currentTimeMillis() - last));
			
			last = currentTimeMillis();

		}
		
		System.out.println(format("total is %s ms.",currentTimeMillis() - start));
	}
		
	
	@Test
	public void discover() {
		
	
		VirtualRepository repository = new Repository();
		
		repository.discover(1000000,CsvCodelist.type,SdmxCodelist.type);
		
		for (Asset s : repository)
			log.info("name={}\nproperties{}\n",s.name(),s.properties());
	}
	
	
	@Test
	public void usersAreKeptApart() {
		
		VirtualRepository repository = new Repository();
		
		properties().add(USERNAME.property("fabio.simeoni"));
		
		repository.discover(1000000,CsvCodelist.type);
		
		properties().add(USERNAME.property("someone.else"));
		
		repository.discover(1000000,CsvCodelist.type);
		
		
	}
	
	
	@Test
	public void retrieveCsvTable() throws Exception {
	
		VirtualRepository repository = new Repository();
		
		repository.discover(1000000,CsvCodelist.type);
		
		Asset codelist = repository.iterator().next();
		
		System.out.println(codelist.getClass());
		System.out.println("asset:"+codelist);
		System.out.println("properties"+codelist.properties());
		
		Table table = repository.retrieve(codelist,Table.class);
		
		for (Row row : table)
			System.out.println(row);
	}
	
	
	@Test
	public void retrieveCsvStream() throws Exception {
	
		VirtualRepository repository = new Repository();
		
		repository.discover(1000000,CsvCodelist.type);
		
		Asset codelist = repository.iterator().next();
		
		System.out.println(codelist.getClass());
		System.out.println("asset:"+codelist);
		System.out.println("properties"+codelist.properties());
		
		InputStream stream = repository.retrieve(codelist,InputStream.class);
		
		Table table = new CsvTable((CsvCodelist)codelist, stream);
		
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
	public void retrieveSdmxStream() throws Exception {
		
		VirtualRepository repository = new Repository();
		
		repository.discover(1000000,SdmxCodelist.type);
		
		Asset codelist = repository.iterator().next();
		
		InputStream stream = repository.retrieve(codelist,InputStream.class);
		
		StructureWorkspace ws = SdmxServiceFactory.parser().parseStructures(new ReadableDataLocationTmp(stream));

		ws.getStructureBeans(false);
		
	}
	
	@Test
	public void retrieveCometMapping() throws Exception {
	
		VirtualRepository repository = new Repository();
		
		repository.discover(1000000,CometAsset.type);
		
		Asset mapping = repository.iterator().next();
		
		MappingData data = repository.retrieve(mapping,MappingData.class);
		
		for (Mapping m : data.getMappings())
			System.out.println(m.toString()+"`n");
		
		
	}
	
	@Test
	public void publishCsvCodelist() throws Exception {

		InputStream stream = getClass().getResourceAsStream("/samplecsv.txt");
		
		if (stream==null)
			throw new RuntimeException("missing test resource");

		VirtualRepository repository = new Repository();
		
		RepositoryService service = repository.services().lookup(WorkspacePlugin.name);
		
		CsvCodelist codelist = new CsvCodelist("another-sample-csv-codelist.txt",0, service);
		
		codelist.hasHeader(true);
		codelist.setDelimiter('\t');
		codelist.setQuote('"');
		
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
		
		SdmxCodelist codelist = new SdmxCodelist("another-sample-sdmx-codelist.xml",service);
		
		codelist.setVersion("2.0");
		
		repository.publish(codelist,bean(stream));
	}
	
	
	
	@Test
    public void publishMapping() throws Exception {

        VirtualRepository repo = new Repository();

        RepositoryService service = repo.services().lookup(WorkspacePlugin.name);

        CometAsset asset = new CometAsset("sample-comet-mapping.xml", service);
        
        repo.publish(asset, amapping());
    }

	@XmlRootElement
	private static class Dummy {}

    private MappingData amapping() throws URISyntaxException {
    	
    	Dummy d = new Dummy();
    	
    	DataProvider sourceDataProvider = provider("foo","foo","foo");
		DataProvider targetDataProvider = provider("foo","foo","foo");
		
		MappingData mappingData = new MappingData().
			id(new URI("urn:foo:bar")).
			version("0.01").
			producedBy("Foo Bazzi").
			on(new Date()).
			linking(sourceDataProvider).to(targetDataProvider).
			through(
				configuredMatcher(new URI("urn:matcher:foo")).
					ofType("org.fao.fi.comet.common.matchers.LexicalMatcher").
					weighting(10).
					withMinimumScore(0.1).
					having(
						configurationProperty("stripSymbols", Boolean.FALSE)
					)
			).
			with(minimumWeightedScore(0.3), maximumCandidates(5)).
			including(
				map(wrap(d).with(identifierFor(new URI("urn:2")))).
					to(
						target(wrap(d).with(identifierFor(new URI("urn:70")))).
							asContributedBy(matcher(new URI("urn:matcher:foo")).scoring(0.49), 
											matcher(new URI("urn:matcher:bar")).scoring(0.59),
											matcher(new URI("urn:matcher:baz")).nonPerformed()
							).withWeightedScore(0.39)
					)
		);
        
        return mappingData;
    }

    
	// helpers
	
	
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
