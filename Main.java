import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


interface Document {
    void process(String format) throws Exception;
}


class ColombiaInvoice implements Document {
    @Override
    public void process(String format) throws Exception {
        if (!Arrays.asList(".pdf", ".xml").contains(format)) throw new Exception("Invalid format for Colombia Invoice: " + format);
        System.out.println("Processing DIAN Invoice (Colombia) in " + format);
    }
}
class ColombiaContract implements Document {
    @Override
    public void process(String format) throws Exception {
        if (!Arrays.asList(".pdf", ".doc").contains(format)) throw new Exception("Invalid format for Colombia Contract: " + format);
        System.out.println("Processing Legal Contract (Colombia) in " + format);
    }
}
class ColombiaCertificate implements Document {
    @Override
    public void process(String format) throws Exception {
        if (!format.equals(".pdf")) throw new Exception("Certificates in Colombia must be .pdf");
        System.out.println("Processing Digital Certificate (Colombia) in " + format);
    }
}

class MexicoInvoice implements Document {
    @Override
    public void process(String format) throws Exception {
        if (!Arrays.asList(".xml", ".pdf").contains(format)) throw new Exception("Invalid format for SAT Invoice (Mexico): " + format);
        System.out.println("Processing SAT Invoice (Mexico) in " + format);
    }
}
class MexicoReport implements Document {
    @Override
    public void process(String format) throws Exception {
        if (!Arrays.asList(".xlsx", ".csv").contains(format)) throw new Exception("Invalid format for Mexico Report: " + format);
        System.out.println("Processing Financial Report (Mexico) in " + format);
    }
}
class MexicoTaxDeclaration implements Document {
    @Override
    public void process(String format) throws Exception {
       
        if (!format.equals(".pdf")) throw new Exception("Tax Declarations in Mexico must be .pdf");
        System.out.println("Processing Tax Declaration (Mexico) in " + format);
    }
}

class ArgentinaInvoice implements Document {
    @Override
    public void process(String format) throws Exception {
        if (!Arrays.asList(".txt", ".pdf").contains(format)) throw new Exception("Invalid format for AFIP Invoice (Argentina): " + format);
        System.out.println("Processing AFIP Invoice (Argentina) in " + format);
    }
}
class ArgentinaContract implements Document {
    @Override
    public void process(String format) throws Exception {
        if (!Arrays.asList(".docx", ".pdf").contains(format)) throw new Exception("Invalid format for Argentina Contract: " + format);
        System.out.println("Processing Legal Contract (Argentina) in " + format);
    }
}
class ArgentinaCertificate implements Document {
    @Override
    public void process(String format) throws Exception {
        
        if (!format.equals(".pdf")) throw new Exception("Certificates in Argentina must be .pdf");
        System.out.println("Processing Digital Certificate (Argentina) in " + format);
    }
}

class ChileInvoice implements Document {
    @Override
    public void process(String format) throws Exception {
        if (!Arrays.asList(".xml", ".pdf").contains(format)) throw new Exception("Invalid format for SII Invoice (Chile): " + format);
        System.out.println("Processing SII Invoice (Chile) in " + format);
    }
}
class ChileReport implements Document {
    @Override
    public void process(String format) throws Exception {
        if (!Arrays.asList(".csv", ".pdf").contains(format)) throw new Exception("Invalid format for Chile Report: " + format);
        System.out.println("Processing Financial Report (Chile) in " + format);
    }
}
class ChileTaxDeclaration implements Document {
    @Override
    public void process(String format) throws Exception {
        // CORREGIDO
        if (!format.equals(".pdf")) throw new Exception("Tax Declarations in Chile must be .pdf");
        System.out.println("Processing Tax Declaration (Chile) in " + format);
    }
}

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
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", Main::serveInterface);
            server.createContext("/api/process", Main::processRequest);
            server.setExecutor(null);
            server.start();
            System.out.println("Interfaz disponible en http://localhost:8080");
            openInterface();
        } catch (IOException e) {
            System.out.println("No se pudo iniciar el servidor: " + e.getMessage());
        }
    }

    private static void openInterface() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(java.net.URI.create("http://localhost:8080"));
            } else {
                System.out.println("No se puede abrir el navegador automáticamente.");
            }
        } catch (Exception e) {
            System.out.println("No se pudo abrir la interfaz: " + e.getMessage());
        }
    }

    private static void serveInterface(HttpExchange exchange) throws IOException {
        Path index = Paths.get("index.html").toAbsolutePath();
        byte[] content = Files.readAllBytes(index);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, content.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(content);
        }
    }

    private static void processRequest(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"Metodo no permitido\"}");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> data = parseForm(body);
        String country = data.getOrDefault("country", "");
        String type = data.getOrDefault("type", "");
        String formatsValue = data.getOrDefault("formats", "");
        String[] formats = formatsValue.isEmpty() ? new String[0] : formatsValue.split(",");
        CountryDocumentFactory factory = createFactory(country);
        StringBuilder response = new StringBuilder("{\"country\":\"").append(json(country)).append("\",\"type\":\"").append(json(type)).append("\",\"results\":[");

        for (int index = 0; index < formats.length; index++) {
            String format = formats[index];
            boolean success = false;
            String message;
            try {
                if (factory == null) throw new Exception("Pais no soportado: " + country);
                factory.createDocument(type).process(format);
                success = true;
                message = "Documento procesado correctamente";
            } catch (Exception error) {
                message = error.getMessage();
            }
            if (index > 0) response.append(',');
            response.append("{\"format\":\"").append(json(format)).append("\",\"success\":").append(success).append(",\"message\":\"").append(json(message)).append("\"}");
        }
        response.append("]}");
        sendJson(exchange, 200, response.toString());
    }

    private static CountryDocumentFactory createFactory(String country) {
        if ("Colombia".equalsIgnoreCase(country)) return new ColombiaFactory();
        if ("Mexico".equalsIgnoreCase(country)) return new MexicoFactory();
        if ("Argentina".equalsIgnoreCase(country)) return new ArgentinaFactory();
        if ("Chile".equalsIgnoreCase(country)) return new ChileFactory();
        return null;
    }

    private static Map<String, String> parseForm(String body) {
        Map<String, String> values = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) values.put(decode(parts[0]), decode(parts[1]));
        }
        return values;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String json(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static void sendJson(HttpExchange exchange, int status, String body) throws IOException {
        byte[] content = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, content.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(content);
        }
    }
}