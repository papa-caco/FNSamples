package ar.com.lpa.samples.util;

public class Utilities {
	
    public static String extractShortName(String uPN)
    {
        String[] parts = uPN.split("@");
        return parts[0]; 
    }
    
    public static String extractCN(String dN)
    {
        String[] parts = dN.split(",");
        for (String part : parts) {
            if (part.startsWith("CN=")) {
                return part.split("=")[1];
            }
        }
        return null; 
    }

}
