package org.virtual.workspace.types;

import static java.lang.String.*;
import static java.util.Arrays.*;
import static org.virtual.workspace.utils.Tags.*;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.gcube.common.homelibrary.home.workspace.Properties;
import org.gcube.common.homelibrary.home.workspace.WorkspaceItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.csv.CsvStream2Table;
import org.virtualrepository.csv.Table2CsvStream;
import org.virtualrepository.spi.Transform;
import org.virtualrepository.tabular.Table;

public class WsCsvCodelist extends AbstractWorkspaceType<CsvCodelist,Table> {

	private final static Logger log = LoggerFactory.getLogger(WsCsvCodelist.class);
	
	private final Transform<CsvCodelist, InputStream, Table> importTransform = new CsvStream2Table<CsvCodelist>();
	private final Transform<CsvCodelist, Table, InputStream> publishTransform = new Table2CsvStream<CsvCodelist>();
	
	@Inject
	public WsCsvCodelist() {
		super(CsvCodelist.type, "text/plain");
	}

	@Override
	public CsvCodelist getAsset(WorkspaceItem item) throws Exception {
		
		CsvCodelist list = new CsvCodelist(item.getId(), item.getName(), 0);
		
		try{

			Map<String,String>	props = item.getProperties().getProperties();

			if (props.containsKey(HEADER.name()))
				list.hasHeader(true);
			
			if (props.containsKey(DELIMITER.name())) {
				String delimiter = props.get(DELIMITER.name());
				if (!delimiter.isEmpty())
					list.setDelimiter(delimiter.charAt(0));
			}
			
			if (props.containsKey(QUOTE.name())) {
				String quote =props.get(QUOTE.name());
				if (!quote.isEmpty())
					list.setQuote(quote.charAt(0));
			}
			if (props.containsKey(ENCODING.name())) {
				String encoding = props.get(ENCODING.name());
				if (Charset.isSupported(encoding))
					list.setEncoding(Charset.forName(encoding));
			}

				
		}
		catch(Exception e) {
			
			log.error("error trasforming item properties into asset properties",e);
			//we are not failing import, client may still be able to parse
		}
		
		return list;
	}
	

	
	public void toItem(Asset asset, Properties props) throws Exception {
		
		CsvCodelist list = (CsvCodelist) asset;
		
		props.addProperty(HEADER.name(),valueOf(list.hasHeader()));
		props.addProperty(DELIMITER.name(),valueOf(list.delimiter()));
		props.addProperty(QUOTE.name(),valueOf(list.quote()));
		props.addProperty(ENCODING.name(),list.encoding().displayName());
		
	}
	
	@Override
	public Set<String> tags() {
		return new HashSet<>(asList(CODELIST.name(),CSV.name()));
	}

	@Override
	public Transform<CsvCodelist, InputStream, Table> fromStream() {
		return importTransform;
	}

	@Override
	public Transform<CsvCodelist, Table, InputStream> toStream() {
		return publishTransform;
	}
}
