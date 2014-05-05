package org.virtual.workspace;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.Importer;
import org.virtualrepository.spi.Lifecycle;
import org.virtualrepository.spi.Publisher;
import org.virtualrepository.spi.ServiceProxy;
import org.virtualrepository.tabular.Table;


@Singleton
public class WorkspaceProxy implements ServiceProxy, Lifecycle {

	@Inject
	WorkspaceBrowser browser;
	
	private final List<Publisher<?,?>> publishers = new ArrayList<Publisher<?,?>>();
	private final List<Importer<?,?>> importers = new ArrayList<Importer<?,?>>();

	
	@Override
	public void init() throws Exception {
		
		Importer<CsvCodelist,Table> csvcodelists = new WorkspaceImporter<>(CsvCodelist.type, Table.class);
		
		importers.add(csvcodelists);
	}
	
	@Override
	public Browser browser() {
		return browser;
	}

	@Override
	public List<? extends Importer<?, ?>> importers() {
		
		return importers;
	}

	@Override
	public List<? extends Publisher<?, ?>> publishers() {
		return publishers;
	}

}
