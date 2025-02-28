package ar.com.lpa.samples.testing;

import java.io.IOException;

import ar.com.lpa.samples.util.SIDConverters;

public class TestSIDConverters {

	public static void main(String[] args) throws IOException 
	{
		System.out.println("1) Convert Binary to String");
		String hexString = "0105000000000005150000009B5617C9403009CB37AB0FAEF4010000";
		System.out.println("Binary SID; " + hexString);
		Byte[] sidBytes = SIDConverters.convertHexStringToByteArray(hexString);
		String result1 = SIDConverters.convertBinarySID(sidBytes);
		System.out.println("String SID: " + result1);
		System.out.println("\n2) Convert String to Binary");
		String siD = "S-1-5-21-2592625401-1541527055-4017201578-1287";
		System.out.println("String SID: " + siD);
		System.out.println("Binary SID: " + SIDConverters.convertStringSIdToHexSId(siD));
	}

}
