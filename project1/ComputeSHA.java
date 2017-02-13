import java.security.*;
import java.io.*;
import java.util.Formatter;

public class computeSHA{
	private static String readFile(String fileName) throws IOException
	{
		FileInputStream in=null;
		
		try 
		{
			in=new FileInputStream(fileName);
			BufferedReader br=new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder sb=new StringBuilder();
			String line;
			line=br.readLine();
			sb.append(line);
			while ((line=br.readLine())!=null)
			{
				sb.append('\n');
				sb.append(line);			
			}
			return sb.toString();
		}
		finally
		{
			if (in!=null) in.close();				
		}
	}
    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
	public static void main(String[] args) throws NoSuchAlgorithmException,
	UnsupportedEncodingException,IOException
	{
		String read=readFile(args[0]);
        MessageDigest md=MessageDigest.getInstance("SHA-1");
        byte[] sha1hash=new byte[40];
        md.update(read.getBytes("iso-8859-1"),0,read.length());		
        sha1hash=md.digest();
		System.out.println(byteArray2Hex(sha1hash));
	
	}

}
