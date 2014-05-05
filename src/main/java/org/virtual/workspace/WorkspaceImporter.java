package org.virtual.workspace;

import org.virtualrepository.Asset;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.Importer;

public class WorkspaceImporter<T extends Asset,A> implements Importer<T, A> {

	private final Type<T> type;
	private final Class<A> api;
	
	public WorkspaceImporter(Type<T> type, Class<A> api) {
		this.type=type;
		this.api=api;
	}
	
	
	@Override
	public Type<T> type() {
		return type;
	}

	@Override
	public Class<A> api() {
		return api;
	}

	@Override
	public A retrieve(Asset asset) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
