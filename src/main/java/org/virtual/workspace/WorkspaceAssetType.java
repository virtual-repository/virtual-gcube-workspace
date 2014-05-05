package org.virtual.workspace;

import java.io.InputStream;

import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtualrepository.Asset;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.csv.CsvStream2Table;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.MutableAsset;
import org.virtualrepository.spi.Transform;

public enum WorkspaceAssetType {

	CSVCODELIST(CsvCodelist.type,"text/plain") {
		
		@Override
		MutableAsset toAsset(WorkspaceItem item) throws Exception {
			return new CsvCodelist(item.getId(), item.getName(),0);
		}
		
		@Override
		Transform<? extends Asset, InputStream, ?> transform() {
			return new CsvStream2Table<CsvCodelist>();
		}
	};
	
	
	private final Type<?> type;
	private final String mime;
	
	private WorkspaceAssetType(Type<?> type, String mime) {
		this.type = type;
		this.mime =mime;
	} 
	
	Type<?> assetType() {
		return type;
	}
	
	String mime() {
		return mime;
	}
	
	
	abstract Transform<? extends Asset, InputStream, ?>  transform();
	
	abstract MutableAsset toAsset(WorkspaceItem item) throws Exception;
}
