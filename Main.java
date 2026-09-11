import java.util.Arrays;
import java.util.List;

interface Document {
    void process(String format) throws Exception;
}

class ColombiaInvoice implements Document {
    public void process(String format) throws Exception {
        if (!Arrays.asList(".pdf", ".xml").contains(format)) throw new Exception("Invalid format for Colombia Invoice: " + format);
        System.out.println("Processing DIAN Invoice (Colombia) in " + format);
    }
}
class ColombiaContract implements Document {
    public void process(String format) throws Exception {
        if (!Arrays.asList(".pdf", ".doc").contains(format)) throw new Exception("Invalid format for Colombia Contract: " + format);
        System.out.println("Processing Legal Contract (Colombia) in " + format);
    }
}
class ColombiaCertificate implements Document {
    public void process(String format) throws Exception {
        if (!format.equals(".pdf")) throw new Exception("Certificates in Colombia must be .pdf");
        System.out.println("Processing Digital Certificate (Colombia) in " + format);
    }
}
// Mexico products
class MexicoInvoice implements Document {
    public void process(String format) throws Exception {
        if (!Arrays.asList(".xml", ".pdf").contains(format)) throw new Exception("Invalid format for SAT Invoice (Mexico): " + format);
        System.out.println("Processing SAT Invoice (Mexico) in " + format);
    }
}
class MexicoReport implements Document {
    public void process(String format) throws Exception {
        if (!Arrays.asList(".xlsx", ".csv").contains(format)) throw new Exception("Invalid format for Mexico Report: " + format);
        System.out.println("Processing Financial Report (Mexico) in " + format);
    }
}
class MexicoTaxDeclaration implements Document {
    public void process(String format) throws Exception {
        if (!format.equals(".pdf")) throw new Exception("Tax Declarations in Mexico must be .pdf");
        System.out.println("Processing Tax Declaration (Mexico) in " + format);
    }
}
// Argentina products
class ArgentinaInvoice implements Document {
    public void process(String format) throws Exception {
        if (!Arrays.asList(".txt", ".pdf").contains(format)) throw new Exception("Invalid format for AFIP Invoice (Argentina): " + format);
        System.out.println("Processing AFIP Invoice (Argentina) in " + format);
    }
}
class ArgentinaContract implements Document {
    public void process(String format) throws Exception {
        if (!Arrays.asList(".docx", ".pdf").contains(format)) throw new Exception("Invalid format for Argentina Contract: " + format);
        System.out.println("Processing Legal Contract (Argentina) in " + format);
    }
}
class ArgentinaCertificate implements Document {
    public void process(String format) throws Exception {
        if (!format.equals(".pdf")) throw new Exception("Certificates in Argentina must be .pdf");
        System.out.println("Processing Digital Certificate (Argentina) in " + format);
    }
}

//chile products
class ChileInvoice implements Document {
    public void process(String format) throws Exception {
        if (!Arrays.asList(".xml", ".pdf").contains(format)) throw new Exception("Invalid format for SII Invoice (Chile): " + format);
        System.out.println("Processing SII Invoice (Chile) in " + format);
    }
}
class ChileReport implements Document {
    public void process(String format) throws Exception {
        if (!Arrays.asList(".csv", ".pdf").contains(format)) throw new Exception("Invalid format for Chile Report: " + format);
        System.out.println("Processing Financial Report (Chile) in " + format);
    }
}
class ChileTaxDeclaration implements Document {
    public void process(String format) throws Exception {
        if (!format.equals(".pdf")) throw new Exception("Tax Declarations in Chile must be .pdf");
        System.out.println("Processing Tax Declaration (Chile) in " + format);
    }
}

/
abstract class CountryDocumentFactory {
   
    public abstract Document createDocument(String docType) throws Exception;


    public void processBatch(String docType, List<String> formats) {
        System.out.println("\n--- Starting Batch Processing for " + this.getClass().getSimpleName() + " ---");
        
        for (String format : formats) {
            try {
                Document doc = createDocument(docType);
                doc.process(format);
                System.out.println("[SUCCESS] Document processed.");
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }
}


// ==========================================
class ColombiaFactory extends CountryDocumentFactory {
    @Override
    public Document createDocument(String docType) throws Exception {
        if (docType.equalsIgnoreCase("Invoice")) return new ColombiaInvoice();
        if (docType.equalsIgnoreCase("Contract")) return new ColombiaContract();
        if (docType.equalsIgnoreCase("Certificate")) return new ColombiaCertificate();
        throw new Exception("Document type " + docType + " not supported in Colombia.");
    }
}

class MexicoFactory extends CountryDocumentFactory {
    @Override
    public Document createDocument(String docType) throws Exception {
        if (docType.equalsIgnoreCase("Invoice")) return new MexicoInvoice();
        if (docType.equalsIgnoreCase("Report")) return new MexicoReport();
        if (docType.equalsIgnoreCase("Tax")) return new MexicoTaxDeclaration();
        throw new Exception("Document type " + docType + " not supported in Mexico.");
    }
}

class ArgentinaFactory extends CountryDocumentFactory {
    @Override
    public Document createDocument(String docType) throws Exception {
        if (docType.equalsIgnoreCase("Invoice")) return new ArgentinaInvoice();
        if (docType.equalsIgnoreCase("Contract")) return new ArgentinaContract();
        if (docType.equalsIgnoreCase("Certificate")) return new ArgentinaCertificate();
        throw new Exception("Document type " + docType + " not supported in Argentina.");
    }
}

class ChileFactory extends CountryDocumentFactory {
    @Override
    public Document createDocument(String docType) throws Exception {
        if (docType.equalsIgnoreCase("Invoice")) return new ChileInvoice();
        if (docType.equalsIgnoreCase("Report")) return new ChileReport();
        if (docType.equalsIgnoreCase("Tax")) return new ChileTaxDeclaration();
        throw new Exception("Document type " + docType + " not supported in Chile.");
    }
}

public class Main {
    public static void main(String[] args) {
        List<String> batchFormats = Arrays.asList(".pdf", ".xml", ".docx", ".csv");

    
        CountryDocumentFactory colombiaSystem = new ColombiaFactory();
        colombiaSystem.processBatch("Invoice", batchFormats);

        CountryDocumentFactory mexicoSystem = new MexicoFactory();
        mexicoSystem.processBatch("Report", batchFormats);
        
        CountryDocumentFactory chileSystem = new ChileFactory();
        chileSystem.processBatch("Tax", batchFormats); // 
        
        System.out.println("\n--- Testing Unsupported Document ---");
        colombiaSystem.processBatch("Report", batchFormats);
    }
}