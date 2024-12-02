package ar.com.lpa.samples.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import com.fasterxml.jackson.databind.ObjectMapper;
import ar.com.lpa.samples.model.Principal;


public class JsonExporter {
	
    public static String exportToJson(Collection<Principal> principals) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(principals);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void exportJsonToFile(Collection<Principal> principals, String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), principals);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
