package org.virtual.workspace.types;

import static java.util.Arrays.*;
import static org.virtual.workspace.utils.Tags.*;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.csv.CsvStream2Table;
import org.virtualrepository.csv.Table2CsvStream;
import org.virtualrepository.spi.MutableAsset;
import org.virtualrepository.spi.Transform;

public class WsCsvCodelist extends AbstractWorkspaceType {

	private final Transform<?, InputStream, ?> importTransform = new CsvStream2Table<CsvCodelist>();
	private final Transform<?, ?, InputStream> publishTransform = new Table2CsvStream<CsvCodelist>();
	
	public WsCsvCodelist() {
		super(CsvCodelist.type, "text/plain");
	}

	@Override
	public MutableAsset toAsset(WorkspaceItem item) throws Exception {
		return new CsvCodelist(item.getId(), item.getName(), 0);
	}
	
	@Override
	public Set<String> tags() {
		return new HashSet<>(asList(CODELIST.name(),CSV.name()));
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
