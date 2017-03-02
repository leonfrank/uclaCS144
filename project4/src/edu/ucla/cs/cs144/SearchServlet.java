package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.Integer;

import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchResult;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        PrintWriter out = response.getWriter();
        int numSkip = 0;
		int numReturn=0;
        String q = request.getParameter("q");
		if (q==null || q.length() == 0)
		  {			  
			  request.getRequestDispatcher("/EmptyQueryInterface.jsp").forward(request, response);
			  
			  out.flush();
			  out.close();
			  return;  
		  }
        String skipstr = request.getParameter("numResultsToSkip");
        if(skipstr!=null )
        {
            try{numSkip = Integer.parseInt(skipstr);}
            catch(NumberFormatException n){
                out.println("("+skipstr+")");
            }
        }
        String returnstr = request.getParameter("numResultsToReturn");
		if(returnstr!=null )
        {
            try{numReturn = Integer.parseInt(returnstr);}
            catch(NumberFormatException n){
                out.println("("+returnstr+")");
            }
        }
            
        out.println("<!DOCTYPE html>");
        out.println("<head><title>eBay Search</title></head>");
        out.println("<body>");
        out.println("<form method=\"GET\" action=\"http://localhost:1448/eBay/search\">");
        out.println("<input name=\"q\" type=\"text\"><input type=\"submit\">");
        out.println("<input name=\"skip\" type=\"hidden\" value=\"0\">");
        out.println("</form>");
        
		int maxTotal =1000000;
		AuctionSearch se=new AuctionSearch();
        SearchResult[] res = se.basicSearch(q, numSkip, numReturn);
		SearchResult[] totalRes = se.basicSearch(q, 0, maxTotal);
        /*for(int i = 0; i < res.length; i++)
        {
            out.println("<p><a href=\"http://localhost:1448/eBay/item?id="+res[i].getItemId()+"\">"+res[i].getName()+"</a>");
        }
        if(q != null)
        {
            if(numSkip > 0)
            {
                out.println("<form method=\"GET\" action=\"http://localhost:1448/eBay/search\">");
                out.println("<input type=\"hidden\" name=\"skip\" value=\""+(numSkip-10)+"\">");
                out.println("<input type=\"hidden\" name=\"q\" value=\""+q+"\">");
                out.println("<input value=\"Previous 10\" type=\"submit\">");
                out.println("</form>");
            }            
            out.println("<form method=\"GET\" action=\"http://localhost:1448/eBay/search\">");
            out.println("<input type=\"hidden\" name=\"skip\" value=\""+(numSkip+10)+"\">");
            out.println("<input type=\"hidden\" name=\"q\" value=\""+q+"\">");
            out.println("<input value=\"Next 10\" type=\"submit\">");
            out.println("</form>");
        }
        out.println("</body>");
        out.println("</html>");
        out.close();*/
		if (totalRes.length==0)
		{
			request.getRequestDispatcher("/EmptySearchInterface.jsp").forward(request, response);
		    out.flush();
			out.close();
			return;
		}
		 while(totalRes.length == maxTotal)
		  {
			  maxTotal = maxTotal + 1000000;
		      totalRes = se.basicSearch(q, 0, maxTotal);
		  }
		  maxTotal = totalRes.length;
		  
		  request.setAttribute("SR",res);
		  request.setAttribute("maxTotal",maxTotal);
		  request.setAttribute("numResultsToSkip",numSkip);
		  request.setAttribute("query",q);
		  request.setAttribute("numResultsToReturn",numReturn);
		  request.getRequestDispatcher("/searchInterface.jsp").forward(request, response);
		out.flush();
		out.close();
    }
}


