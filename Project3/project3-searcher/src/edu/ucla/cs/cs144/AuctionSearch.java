package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	/*private IndexSearcher searcher=null;
	private QueryParser parser=null;
	public AuctionSearch() throws IOException{
		searcher=new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("var/lib/lucene/IndexofItemCategory"))));
		parser=new QueryParser("Content",new StandardAnalyzer());
	}
	public Topdocs performSearch(String queryString, int n) throws IOException,ParseException
	{
		Query query=parser.parse(queryString);
		return searcher.search(query,n);
	}
	public Document getDocument(int docId)
    throws IOException {
        return searcher.doc(docId);
    }*/
	public SearchResult[] basicSearch(String queryString, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
		SearchResult[] ret=new SearchResult[0];
		try {
		String filePath="var/lib/lucene/IndexofItemCategory";
		String searchField="Content";
		SearchEngine se=new SearchEngine(filePath,searchField);
		TopDocs topDocs=se.performSearch(queryString,se.performSearch(queryString,1).totalHits);
		ScoreDoc[] hits=topDocs.scoreDocs;
		if (numResultsToSkip>=hits.length) return new SearchResult[0];
		int maxResultIndex=math.min(hits.length,numResultsToReturn+numResultsToSkip);
		ret=new SearchResult[maxResultIndex-numResultsToSkip];
		for (int i=0;i<ret.length;i++){
			Document doc=se.getDocuemt(hits[i+numResultsToSkip].doc);
			String ItemID=doc.get("ItemID");
			String Name=doc.get("Name");
			ret[i]=new SearchResult(ItemID,Name);
		}
		}
		catch (Exception e)
		{
			System.out.println("Exception caught");
		}
		return ret;
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}
	
	public String echo(String message) {
		return message;
	}

}
