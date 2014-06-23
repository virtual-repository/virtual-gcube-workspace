package org.virtual.workspace.types;

import java.io.InputStream;
import java.util.Map.Entry;

import org.gcube.common.homelibrary.home.workspace.Properties;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.virtual.workspace.utils.Tags;
import org.virtualrepository.Asset;
import org.virtualrepository.Property;
import org.virtualrepository.impl.Type;
import org.virtualrepository.spi.MutableAsset;
import org.virtualrepository.spi.Transform;

public abstract class AbstractWorkspaceType<T extends MutableAsset, A> implements WorkspaceType {

	private final Type<T> type;
	private final String mime;

	public AbstractWorkspaceType(Type<T> type, String mime) {
		this.type = type;
		this.mime = mime;
	}

	public Type<T> assetType() {
		return type;
	}

	public String mime() {
		return mime;
	}

	@Override
	public T toAsset(WorkspaceItem item) throws Exception {

		T asset = getAsset(item);

		// add version
		asset.setVersion(item.getProperties().getProperties().get(Tags.VERSION));

		// copy tags as properties
		for (Entry<String, String> entry : item.getProperties().getProperties().entrySet())
			asset.properties().add(new Property(entry.getKey(), entry.getValue()));

		return asset;
	}
	
	@Override
	public void toItem(Asset asset, Properties props) throws Exception {
	 //optionally overridden in classes	
	}

	protected abstract T getAsset(WorkspaceItem item) throws Exception;
	
	@Override
	public abstract Transform<T, InputStream, A> fromStream();

	@Override
	public abstract Transform<T, A, InputStream> toStream();

	@Override
	public String toString() {
		boolean read = fromStream() != null;
		boolean write = toStream() != null;
		return String.format("%s (%s|%s: %s)", type, read ? "R" : "-", write ? "W" : "-", tags());
	}

}
