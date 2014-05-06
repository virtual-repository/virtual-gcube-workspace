package org.virtual.workspace.types;

import static java.util.Arrays.*;
import static org.virtual.workspace.utils.Tags.*;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.csv.CsvStream2Table;
import org.virtualrepository.csv.Table2CsvStream;
import org.virtualrepository.spi.Transform;
import org.virtualrepository.tabular.Table;

public class WsCsvCodelist extends AbstractWorkspaceType<CsvCodelist,Table> {

	private final Transform<CsvCodelist, InputStream, Table> importTransform = new CsvStream2Table<CsvCodelist>();
	private final Transform<CsvCodelist, Table, InputStream> publishTransform = new Table2CsvStream<CsvCodelist>();
	
	@Inject
	public WsCsvCodelist() {
		super(CsvCodelist.type, "text/plain");
	}

	@Override
	public CsvCodelist getAsset(WorkspaceItem item) throws Exception {
		return new CsvCodelist(item.getId(), item.getName(), 0);
	}
	
	@Override
	public Set<String> tags() {
		return new HashSet<>(asList(CODELIST.name(),CSV.name()));
	}

	@Override
	public Transform<CsvCodelist, InputStream, Table> transformOnImport() {
		return importTransform;
	}

	@Override
	public Transform<CsvCodelist, Table, InputStream> transformOnPublih() {
		return publishTransform;
	}

}
