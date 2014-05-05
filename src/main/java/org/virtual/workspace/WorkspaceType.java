package org.virtual.workspace;

import java.io.InputStream;

import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.csv.CsvStream2Table;
import org.virtualrepository.csv.Table2CsvStream;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.MutableAsset;
import org.virtualrepository.spi.Transform;

public enum WorkspaceType {

	CSVCODELIST(CsvCodelist.type,"text/plain") {
		
		@Override
		MutableAsset toAsset(WorkspaceItem item) throws Exception {
			return new CsvCodelist(item.getId(), item.getName(),0);
		}
		
		@Override
		Transform<?,InputStream, ?> transformOnImport() {
			return new CsvStream2Table<CsvCodelist>();
		}
		
		@Override
		Transform<?,?, InputStream> transformOnPublih() {
			return new Table2CsvStream<CsvCodelist>();
		}
	};
	
	
	
	private final Type<?> type;
	private final String mime;
	
	private WorkspaceType(Type<?> type, String mime) {
		this.type = type;
		this.mime =mime;
	} 
	
	Type<?> assetType() {
		return type;
	}
	
	String mime() {
		return mime;
	}
	
	
	abstract Transform<?,InputStream,?>  transformOnImport();
	abstract Transform<?,?,InputStream>  transformOnPublih();
	
	abstract MutableAsset toAsset(WorkspaceItem item) throws Exception;
}
