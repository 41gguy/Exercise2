
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;

public class Ex2Client {

    public static void main(String[] args) {

    	System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "38102");

        try {
        	Socket socket = new Socket("localhost", 38102);
            InputStream is = socket.getInputStream();
            DataInputStream ds = new DataInputStream(is);
            
            char[] hexArray = new char[200];
           
            for (int i = 0; i < 200; i++) {
            	byte var1 = ds.readByte();
            	StringBuilder sb = new StringBuilder();
            	sb.append(String.format("%02X", var1));
            	hexArray[i] = sb.charAt(1);
            	System.out.print(hexArray[i]);
            	if ((i+1)%20 == 0) {
            		System.out.println();
            	}
            }
            
            CRC32 crc = new CRC32();
            
            String hexstr = new String(hexArray);
            
            crc.update(hexstr.getBytes(), 0, 200);
            Long crcval = new Long(crc.getValue());
            
            String crcstring = crcval.toHexString(crcval);
            
            System.out.println("Generated CRC32: " + crcstring);
            
            OutputStream os = socket.getOutputStream();
    		DataOutputStream dos = new DataOutputStream(os);
    		
    		int t = 0; 
    		for (int v = 0; v < 4; v++) {
    			
    			
    			int byteval = Character.digit(crcstring.charAt(t), 16)*16;
    			t++;
    			byteval += Character.digit(crcstring.charAt(t), 16);
    			t++;
    			
    			byte finalbyte = (byte) byteval;
    			dos.write(finalbyte);
    		}

    		try {
    			is.close();
    			socket.close();
    			System.out.println("Disconnected from server");
        		
    		}
    		catch (Exception y) {
    			System.out.println("Error disconnecting from server.");
    		}
            
            
        }
        catch (Exception e) {
        	System.out.print(e);
        }
    }
}
