package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ucla.cs.cs144.AuctionSearch;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.w3c.dom.*;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
		try 
        {
        	response.setContentType("text/html");
        	PrintWriter out = response.getWriter();
        	
        	AuctionSearch se = new AuctionSearch();
        	String id = request.getParameter("ItemID");
		if (id == null || id.length()==0) 
            {
                request.getRequestDispatcher("/EmptyItemidInterface.jsp").forward(request, response);
		        out.flush();
		    	out.close();
                return;
            }
            String data =se.getXMLDataForItemId(id);
			int EmptyItem=0;
            if (data==null||data.length()==0) 
            {
            	//out.println("XML_Data is empty!");
            	EmptyItem = 1;
                request.setAttribute("IR",new ItemResult());
                request.setAttribute("EmptyItem", EmptyItem);
                request.setAttribute("Bid_Results",new BidResult[0]);
                request.getRequestDispatcher("/EmptyItemInterface.jsp").forward(request, response);
		        out.flush();
		    	out.close();
                return;
            }
		/*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           factory.setValidating(false);
           factory.setIgnoringElementContentWhitespace(true);      
           DocumentBuilder builder = factory.newDocumentBuilder();
		   StringReader reader = new StringReader(data);
           Document doc = builder.parse(new InputSource(reader));*/
		   
		    String Name=getChild(data,"Name");
		    ArrayList<String> Categories=new ArrayList<>();
		    String Currently=getChild(data, "Currently");
			String Buy_Price=getChild(data, "Buy_Price");
			String First_Bid=getChild(data, "First_Bid");
			String Number_of_Bids=getChild(data, "Number_of_Bids");
			
			String Started=getChild(data, "Started");
			String Ends=getChild(data, "Ends");
			
			out.println("<!DOCTYPE html>");
            out.println("<head><title>"+getChild(data,"Name")+"</title></head>");
			
            out.println("<body>");
            out.println("<h1>"+getChild(data, "Name")+"</h1>");
            while(data.indexOf("<Category>") > -1)
            {
				Categories.add(getChild(data, "Category"));
                out.println(getChild(data, "Category") + "<br>");
                data = data.substring(data.indexOf("</Category>") + 11);
				
            }
			String[] Category_List=new String[Categories.size()];
			Category_List=Categories.toArray(Category_List);
            if(data.indexOf("<Buy_Price>")>-1)
                out.println("<h3>Buy Price: "+getChild(data, "Buy_Price")+"</h3>");
            out.println("<h3>Current highest bid: " + getChild(data, "Currently") + "</h3>");
            out.println("<h3>First bid: "+ getChild(data, "First_Bid")+"</h3>");
            out.println("<h3>Auction started: "+ getChild(data, "Started")+"</h3>");
            out.println("<h3>Auction ends: "+ getChild(data, "Ends")+"</h3>");
            int numBids = Integer.parseInt(getChild(data, "Number_of_Bids"));
            out.println("<h3>Number of bids: "+ numBids +"</h3>");
            out.println("<h2>Bids</h2>");
			
			BidResult[] Bid_Results=new BidResult[numBids];
            if(numBids == 0)
                out.println("None yet");
            else
            {
                String bids = getChild(data, "Bids");
                data = data.substring(data.indexOf("</Bids>") + 7);
				
                for(int i = 0; i< numBids; i++)
                {
                    String bid = getChild(bids, "Bid");
                    String bidder = getTag(bid, "Bidder");
					String Bidder_id=getAttribute(bidder, "UserID");
			        String Bidder_Rating=getAttribute(bidder, "Rating");
					String Bidder_Location="";
					String Bidder_Country="";
                    out.println("<b>"+getAttribute(bidder, "UserID")+" ("+getAttribute(bidder, "Rating")+")</b><br>");
                    if(bid.indexOf("<Location>")>-1){
					out.println("Location: " + getChild(bid, "Location") + "<br>");
				    Bidder_Location=getChild(bid, "Location");
					}
                        
                    if(bid.indexOf("<Country>")>-1){
				    out.println("Country: " + getChild(bid, "Country") + "<br>");
					Bidder_Country=getChild(bid, "Country");
					}
					String Time=getChild(bid, "Time");
			        String Amount=getChild(bid, "Amount");                    
                    out.println("Time: " + getChild(bid, "Time") + "<br>");
                    out.println("Amount: " + getChild(bid, "Amount") + "<p>");
                    bids = bids.substring(bids.indexOf("</Bid>")+6);
					Bid_Results[i]=new BidResult(Bidder_id, Bidder_Rating, Time, Amount,  Bidder_Location, Bidder_Country);
			    }
				Arrays.sort(Bid_Results);
            }
			
			
			String Location=getChild(data, "Location");
			String Country=getChild(data, "Country");
            out.println("<h2>Item Information</h2>");
            out.println("<p>"+getChild(data, "Country") + " ("+getChild(data, "Location")+")<br>");
            String loc = getTag(data, "Location");
			
			String Latitude="";
			String Longitude="";
            if(loc.indexOf("Latitude")>-1){
				out.println("Latitude: "+getAttribute(loc, "Latitude") + "<br>");
				Latitude=getChild(data, "Latitude");
			}
                
            if(loc.indexOf("Longitude")>-1){
			    out.println("Longitude: "+getAttribute(loc, "Longitude") + "<br>");
				Longitude=getAttribute(loc, "Longitude");
			}
                
			
			
            String seller = getTag(data, "Seller");
			String Seller_id=getAttribute(seller, "UserID");
			String Seller_Rating=getAttribute(seller, "Rating");
			String Description=getChild(data, "Description");
            out.println("<h3>Seller: " + getAttribute(seller, "UserID") + " (" + getAttribute(seller, "Rating") + ")</h3>");
            out.println("<h3>Description</h3>" + getChild(data, "Description"));
            
			out.println("</body>");
            out.println("</html>");
			
			
			
			ItemResult IR = new ItemResult(  id, Name, Category_List, Currently,Buy_Price, First_Bid, Number_of_Bids, 
	    		          						 Location,  Latitude, Longitude, Country, 
	    		          						 Started, Ends,  Seller_id,  Seller_Rating,  Description) ;
        
		request.setAttribute("IR",IR);
		request.setAttribute("Bid_Results",Bid_Results);
		request.setAttribute("EmptyItem",EmptyItem);
		request.getRequestDispatcher("/itemInterface.jsp").forward(request, response);
		        
		out.flush();
        out.close();
        }
	     catch (Exception e)
		 {
			 System.exit(0);
		 }
	}
    
    //helper function: get stuff in between tags
    private static String getChild(String s, String tag)
    {
        
        int startpos = s.indexOf("<"+tag);
        s = s.substring(startpos);
        startpos = s.indexOf(">") + 1;
        int endpos = s.substring(startpos).indexOf("</"+tag+">")+startpos;
        String result = "";
        try{result = s.substring(startpos, endpos).trim();} 
        catch(StringIndexOutOfBoundsException err){result = "start:" + startpos + " end:" + endpos;}
        return result;
    }
    
    //helper function: get actual start tag with the attributes in it
    private static String getTag(String s, String tag)
    {
        int startpos = s.indexOf("<"+tag);
        String result = s.substring(startpos);
        return result.substring(0, result.indexOf('>'));
    }
    
    //helper function: get attribute inside tag
    private static String getAttribute(String s, String name)
    {
        int startpos = s.indexOf(name+"=\"") + (2+name.length());
        String result = s.substring(startpos);
        return result.substring(0, result.indexOf('"'));
    }
}
