package org.acme;

import static java.lang.System.*;
import static org.virtual.workspace.utils.Context.*;

import java.io.InputStream;

import org.gcube.common.scope.api.ScopeProvider;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.workspace.WorkspacePlugin;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.csv.CsvStream2Table;
import org.virtualrepository.impl.Repository;
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
		
		repository.discover(1000000,CsvCodelist.type);
		
		for (Asset s : repository)
			log.info("name={}\nproperties{}\n",s.name(),s.properties());
	}
	
	
	@Test
	public void retrieveCodelist() throws Exception {
	
		VirtualRepository repository = new Repository();
		
		repository.discover(1000000,CsvCodelist.type);
		
		Asset codelist = repository.iterator().next();
		
		Table table = repository.retrieve(codelist,Table.class);
		
		for (Row row : table)
			System.out.println(row);
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
}
