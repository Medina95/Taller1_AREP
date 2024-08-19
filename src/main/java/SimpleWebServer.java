
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SimpleWebServer {

    private static final int PORT = 8080;
    private static final String WEB_ROOT = "src/webroot";
    private static final Map<String, RESTService> services = new HashMap<>();

    public static void main(String[] args) {
        // Add REST service mappings here
        addServices();


        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor escuchando en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addServices() {
        RestServiceImpl services = new RestServiceImpl();
        SimpleWebServer.services.put("GET" , services);
        SimpleWebServer.services.put("POST" , services);
        SimpleWebServer.services.put("PUT" , services);
        SimpleWebServer.services.put("DELETE" , services);
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 OutputStream out = clientSocket.getOutputStream()) {

                String requestLine = in.readLine();
                if (requestLine == null) return;

                String[] tokens = requestLine.split(" ");
                if (tokens.length < 3) return;

                String method = tokens[0];
                String requestedResource = tokens[1];
                String[] parts = requestedResource.split("/");
                           
                String idString = null;

                if (parts.length > 3) {
                    idString = parts[3]; // El ID debería estar en la cuarta parte de la ruta
                }       

                if (services.containsKey(method) && requestedResource.startsWith("/api")) {
                    RESTService service = services.get(method);
                    switch (method) {
                        case "GET":
                            service.handleGet(tokens, in, out, clientSocket);
                            break;
                        case "POST":
                            service.handlePost( in, out);
                            break;
                        case "PUT":
                            int id1 = Integer.parseInt(idString);

                            service.handlePut(in, out,id1);
                            break;
                        case "DELETE":
                            
                            int id = Integer.parseInt(idString);
                             service.handleDelete(in, out,id);
                            break;
                           
                        default:
                            send404(out);
                    }
                } else {
                    serveStaticFile(requestedResource, out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

     

        private void serveStaticFile(String resource, OutputStream out) throws IOException {
            Path filePath = Paths.get(WEB_ROOT, resource);
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                // Detectar el tipo MIME
                String contentType = Files.probeContentType(filePath);
                
           
                
                byte[] fileContent = Files.readAllBytes(filePath);
        
                // Crear el encabezado de la respuesta HTTP
                String responseHeader = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + fileContent.length + "\r\n" +
                        "\r\n";
                out.write(responseHeader.getBytes());
                out.write(fileContent);
            } else {
                send404(out);
            }
        }
        

        private void send404(OutputStream out) throws IOException {
            String response = "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    "{\"error\": \"Not Found\"}";
            out.write(response.getBytes());
        }
    }
}