/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        //Create each file for each table
		try
		{
		String file1="Item.dat";
		String file2="ItemCategory.dat";
		String file3="Bid.dat";
		String file4="AuctionUser.dat";
		String file5="Seller.dat";
		BufferedWriter out1=new BufferedWriter(new FileWriter(file1,true));
		BufferedWriter out2=new BufferedWriter(new FileWriter(file2,true));
		BufferedWriter out3=new BufferedWriter(new FileWriter(file3,true));
		BufferedWriter out4=new BufferedWriter(new FileWriter(file4,true));
		BufferedWriter out5=new BufferedWriter(new FileWriter(file5,true));
		
		org.w3c.dom.NodeList list=doc.getDocumentElement().getChildNodes();
		Element []Item_List= new Element[list.getLength()];
		for (int i=0;i<list.getLength();i++)
		{
			Node node=list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
        Item_List[i]=(Element) node;
		}
        //System.out.println(Item_List.length);
		int count=0;
		final String OLD_FORMAT="MMM-dd-YY HH:mm:ss";
		final String NEW_FORMAT="YYYY-MM-DD HH:mm:ss";
		for (int i=0;i<Item_List.length;i++)
		{
			Element Item=Item_List[i];
		//Below construct table Item(ItemID,Name,Currently,Buy_Price,First_Bid,Number_of_Bids,Seller_Location,Seller_Latitude,Seller_Longitude,Seller_Country,Started,Ends,Seller_UserID,Description)
		    String ItemID="";
			String Name="";
			String Currently="";
			String Buy_Price="";
			String First_Bid="";
			String Number_of_Bids="";
			String Location="";
			String Latitude="";
			String Longitude="";
			String Country="";
			String Started="";
			String Ends="";
			String Seller_UserID="";
			String Seller_Rating="";
			String Description="";
			
			try
			{
			org.w3c.dom.NamedNodeMap ItemID_List=Item.getAttributes();
			if (ItemID_List != null && ItemID_List.getLength()==1)
        	{
        		ItemID=ItemID_List.item(0).getNodeValue();
        	}
			Name = getElementTextByTagNameNR(Item, "Name"); //Name=Name.replaceAll("\"", "`"); //replace " to ` to avoid mismatch in MySQL
        	if (Name=="") Name="\\N";
        	  //else Name=columnSeparator+Name+columnSeparator;
        	Currently = getElementTextByTagNameNR(Item, "Currently");
        	if (Currently=="") Currently="\\N";
        	  //else Currently="\""+Currently+"\"";
        	Buy_Price = getElementTextByTagNameNR(Item, "Buy_Price");
        	if (Buy_Price=="") Buy_Price="\\N";
        	 
        	  //else Buy_Price="\""+Buy_Price+"\"";
        	First_Bid = getElementTextByTagNameNR(Item, "First_Bid");
        	if (First_Bid=="") First_Bid="\\N";
        	  //else First_Bid="\""+First_Bid+"\"";
        	Number_of_Bids = getElementTextByTagNameNR(Item, "Number_of_Bids");
        	if (Number_of_Bids=="") Number_of_Bids="\\N";
			
			Location = getElementTextByTagNameNR(Item, "Location");
			if (Location=="") Location="\\N";
			
			Element Loc = getElementByTagNameNR(Item, "Location");
			org.w3c.dom.NamedNodeMap Loc_Attr = Loc.getAttributes();
			
			if (Loc_Attr.getLength()!=0)
			{
				Latitude = Loc_Attr.item(0).getNodeValue();
				Longitude = Loc_Attr.item(1).getNodeValue();
			}
			Country = getElementTextByTagNameNR(Item, "Country");
			if (Country=="") Country="\\N";
			
			Started = getElementTextByTagNameNR(Item, "Started");
        	  if (Started=="") Started="\\N";
			try {
                DateFormat formatter;
                formatter = new SimpleDateFormat(OLD_FORMAT);
                // you can change format of date
                Date date = formatter.parse(Started);
                ((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
                Started=formatter.format(date);
            
            } catch (ParseException e) {
            
            }
        	Ends = getElementTextByTagNameNR(Item, "Ends");
        	  if (Ends=="") Ends="\\N";
			try {
                DateFormat formatter;
                formatter = new SimpleDateFormat(OLD_FORMAT);
                // you can change format of date
                Date date = formatter.parse(Ends);
                ((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
                Ends=formatter.format(date);
            
            } catch (ParseException e) {
            
            }  
			Element Sel = getElementByTagNameNR(Item, "Seller");
			org.w3c.dom.NamedNodeMap Sel_Attr = Sel.getAttributes();
			if (Sel_Attr.getLength()!=0)
			{
				Seller_Rating = Sel_Attr.item(0).getNodeValue();
				Seller_UserID = Sel_Attr.item(1).getNodeValue();
			}
			
			Description = getElementTextByTagNameNR(Item, "Description"); //Description=Description.replaceAll("\"", "`"); Description=truncate(Description);
        	  if (Description=="") Description="\\N";
        	//else Description=columnSeparator+Description+columnSeparator;
			
			out1.write(ItemID+columnSeparator+Name+columnSeparator+strip(Currently)+columnSeparator+strip(Buy_Price)+columnSeparator
			           +strip(First_Bid)+columnSeparator+Number_of_Bids+columnSeparator+Location +columnSeparator+Latitude+columnSeparator
					   +Longitude+columnSeparator+Country+columnSeparator+Started+columnSeparator+Ends
					   +columnSeparator+Seller_UserID+columnSeparator+Description+columnSeparator);

        	out1.newLine();
			
			out5.write(Seller_UserID+columnSeparator+Seller_Rating+columnSeparator);
        	out5.newLine();
			
			
			//Below Construct ItemCategory(ItemID,Category)
			Element []Category_List=getElementsByTagNameNR(Item, "Category");
        	String Category="";
        	for (int j=0;j<Category_List.length;j++)
        	{
        		Category=Category_List[j].getFirstChild().getNodeValue(); 
        		  if (Category=="") Category="\\N";
        		  //else Category=columnSeparator+Category+columnSeparator;
        		out2.write(ItemID+columnSeparator+Category+columnSeparator);
        		out2.newLine();
        	}
			
			//Below Construct Bid(ItemID,UserID,Time,Amount) and AuctionUser(UserID,Location,Country,Bidder_Rating,Seller_Rating)
			String UserID="";
        	String Bidder_Rating="";
        	String Bidder_Location="";
        	String Bidder_Country="";
        	String Time="";
        	String Amount="";
        	Element Bids = getElementByTagNameNR(Item, "Bids");
        	Element []Bid_List = getElementsByTagNameNR(Bids, "Bid");
        	for (int i4=0;i4<Bid_List.length;i4++)
        	{
        		Element Bidder = getElementByTagNameNR(Bid_List[i4], "Bidder");
        		Time = getElementTextByTagNameNR(Bid_List[i4], "Time");
				if (Time=="") Time="\\N"; 
				try {
                DateFormat formatter;
                formatter = new SimpleDateFormat(OLD_FORMAT);
                // you can change format of date
                Date date = formatter.parse(Time);
                ((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
                Time=formatter.format(date);
            
            } catch (ParseException e) {
            
            }
        		  
        		Amount = getElementTextByTagNameNR(Bid_List[i4], "Amount"); 
        		  if (Amount=="") Amount="\\N";
        		org.w3c.dom.NamedNodeMap Bidder_Attr = Bidder.getAttributes(); 
        		for (int j4=0;j4<Bidder_Attr.getLength();j4++)
        		{
        			if (Bidder_Attr.item(j4).getNodeName().equals("UserID"))
        			    UserID = Bidder_Attr.item(j4).getNodeValue();
        			if (Bidder_Attr.item(j4).getNodeName().equals("Rating"))
        			    Bidder_Rating = Bidder_Attr.item(j4).getNodeValue();
        		}
        		//Bidder_UserID=Bidder_UserID.replaceAll("\"", "`");//replace " to \" to avoid mismatch in MySQL
        		  if (UserID=="") UserID="\\N";
        		  //else Bidder_UserID=columnSeparator+Bidder_UserID+columnSeparator;
        		  if (Bidder_Rating=="") Bidder_Rating="\\N";
        		  //else Bidder_Rating="\""+Bidder_Rating+"\"";
        		Location = getElementTextByTagNameNR(Bidder, "Location"); 
        		  if (Bidder_Location=="") Bidder_Location="\\N";
        		  //else Bidder_Location=columnSeparator+Bidder_Location+columnSeparator;
        		Country = getElementTextByTagNameNR(Bidder, "Country"); 
        		  if (Bidder_Country=="") Bidder_Country="\\N";
        		 // else Bidder_Country=columnSeparator+Bidder_Country+columnSeparator;
        		out3.write(ItemID+columnSeparator+UserID+columnSeparator+Time+columnSeparator+strip(Amount)+columnSeparator);
        		out3.newLine();
        		out4.write(UserID+columnSeparator+Bidder_Rating+columnSeparator+Bidder_Location+columnSeparator+Bidder_Country+columnSeparator);
        		out4.newLine();
        		
        	}
			}
			catch (NullPointerException e)
			{
				
			}
			
        	out1.flush();//Flush the memory.
            out1.close();//Close the stream
            out2.flush();
            out2.close();
            out3.flush();
            out3.close();
            out5.flush();
            out5.close();
            out4.flush();
            out4.close();
		}
		}
		catch (IOException e)
		
		{
			
		}
		
        /**************************************************************/
               
        //recursiveDescent(doc, 0);
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
