package ar.com.lpa.samples.util;

public class SIDConverters 
{
	public static Byte[] convertStringSID(String stringSid) 
	{
	    if (stringSid == null || !stringSid.startsWith("S-")) {
	        throw new IllegalArgumentException("SID must begin with 'S-' and not NULL.");
	    }
	    // Dividir el SID en partes
	    String[] sidParts = stringSid.substring(2).split("-");
	    if (sidParts.length < 3) {
	        throw new IllegalArgumentException("SID format error.");
	    }
	    // SID Revision
	    int revision = Integer.parseInt(sidParts[0]);
	    // Identifier Authority (Big-Endian, ocupa 6 bytes)
	    long identifierAuthority = Long.parseLong(sidParts[1]);
	    // SubAuthorities (los números después de la autoridad)
	    int subAuthorityCount = sidParts.length - 2;
	    long[] subAuthorities = new long[subAuthorityCount];
	    for (int i = 0; i < subAuthorityCount; i++) {
	        subAuthorities[i] = Long.parseLong(sidParts[i + 2]);
	    }
	    // Calcular el tamaño total del array de bytes
	    int totalBytes = 8 + (subAuthorityCount * 4); // 1 byte para revision, 1 para subcount, 6 para authority, 4 por subauthority
	    Byte[] binarySid = new Byte[totalBytes];
	    // Asignar el revision
	    binarySid[0] = (byte) revision;
	    // Asignar el sub authority count
	    binarySid[1] = (byte) subAuthorityCount;
	    // Asignar la identifier authority (6 bytes, Big-Endian)
	    for (int i = 5; i >= 0; i--) {
	        binarySid[2 + i] = (byte) (identifierAuthority & 0xFF);
	        identifierAuthority >>= 8;
	    }
	    // Asignar las subauthorities (4 bytes cada una, Little-Endian)
	    int offset = 8;
	    for (long subAuthority : subAuthorities) {
	        for (int i = 0; i < 4; i++) {
	            binarySid[offset + i] = (byte) (subAuthority & 0xFF);
	            subAuthority >>= 8;
	        }
	        offset += 4;
	    }

	    return binarySid;
	}
	
	public static String convertBinarySID(Byte[] sidBytes) 
	{
        StringBuilder strSid = new StringBuilder("S-");
        // Add SID revision
        strSid.append(Byte.toUnsignedInt(sidBytes[0]));
        // Next six bytes are SID authority value
        if (sidBytes[6] != 0 || sidBytes[5] != 0) {
            String strAuth = String.format("0x%02x%02x%02x%02x%02x%02x",
                    Byte.toUnsignedInt(sidBytes[1]), Byte.toUnsignedInt(sidBytes[2]),
                    Byte.toUnsignedInt(sidBytes[3]), Byte.toUnsignedInt(sidBytes[4]),
                    Byte.toUnsignedInt(sidBytes[5]), Byte.toUnsignedInt(sidBytes[6]));
            strSid.append("-").append(strAuth);
        } else {
            long iVal = (Byte.toUnsignedInt(sidBytes[1]))
                    + (Byte.toUnsignedInt(sidBytes[2]) << 8)
                    + (Byte.toUnsignedInt(sidBytes[3]) << 16)
                    + ((long) Byte.toUnsignedInt(sidBytes[4]) << 24);
            strSid.append("-").append(iVal);
        }
        // Get sub authority count
        int iSubCount = Byte.toUnsignedInt(sidBytes[7]);
        for (int i = 0; i < iSubCount; i++) {
            int idxAuth = 8 + i * 4;
            if (idxAuth + 3 >= sidBytes.length) { // Check if index is out of bounds
                System.out.println("OK: old NT account");
            }
            long iSubAuth = ((long) Byte.toUnsignedInt(sidBytes[idxAuth])
                    | ((long) Byte.toUnsignedInt(sidBytes[idxAuth + 1]) << 8)
                    | ((long) Byte.toUnsignedInt(sidBytes[idxAuth + 2]) << 16)
                    | ((long) Byte.toUnsignedInt(sidBytes[idxAuth + 3]) << 24)) & 0xFFFFFFFFL;
            strSid.append("-").append(iSubAuth);
        }
        return strSid.toString();
	}
	
    public static Byte[] convertHexStringToByteArray(String hexString) 
    {
        int length = hexString.length();
        Byte[] byteArray = new Byte[length / 2];
        for (int i = 0; i < length; i += 2) 
        {
            byteArray[i / 2] = (byte) Integer.parseInt(hexString.substring(i, i + 2), 16);
        }
        return byteArray;
    }
    
    public static String convertByteArrayToHexString(Byte[] byteArray) 
    {
    	StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }

}
