package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
 
    public void rebuildIndexes() {

        Connection conn = null;
        String ItemID="";
		String Name="";
		String Description="";
		String Content="";
		StringBuilder Category;
        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    Statement stmt=conn.createStatement();
	ResultSet rs=stmt.executeQuery("SELECT ItemID,Name,Description FROM Item");
	PreparedStatement prepareCategory=conn.prepareStatement("SELECT Category FROM ItemCategory WHERE ItemID = ?");
	Directory dir=new FSDirectory(new File("var/lib/lucene/IndexofItemCategory"));
	IndexWriterConfig conf=new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
	conf.setOpenMode(IndexWriterConfig.openMode.CREATE);
	IndexWriter indexWriter = new IndexWriter(dir, conf);
    while (rs.next())
	{
		ItemID=rs.getString("ItemID");
		Name=rs.getString("Name");
		Description=rs.getString("Description");
		Category=new StringBuilder();
		if (Description==null) Description="";
		prepareCategory.set(1,ItemID);
		rs_category=prepareCategory.executeQuery();//get categories of an item;
		    while (rs_category.next())
			{
				Category.append(rs_category.getString("Category")+" ");
			}
		rs_category.close();
		Document doc=new Document();
		doc.add(new StringField("ItemID",ItemID,Field.Store.YES));
		doc.add(new StringField("Name",Name,Field.Store.YES));
		Content=ItemID+" "+Name+" "+Category.toString()+" "+Description;
		doc.add(new TextField("Content",Content,Field.Store.NO));
		indexWriter.addDocument(doc);
		
	}
	rs.close();
	prepareCategory.close();
	stmt.close();
	
	indexWriter.close();
	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
    

        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
