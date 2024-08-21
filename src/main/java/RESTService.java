

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
/**
 * Esta interfaz define los métodos necesarios para manejar solicitudes HTTP RESTful en un servicio web.
 * Cada método corresponde a un tipo de solicitud HTTP (GET, POST, PUT, DELETE).
 */
public interface RESTService {
    void handleGet(String[] requestLine, BufferedReader in, OutputStream out, Socket clientSocket) throws IOException;
    void handlePost( BufferedReader in, OutputStream out) throws IOException;
    void handlePut(BufferedReader in, OutputStream out,int id) throws IOException;
    void handleDelete(BufferedReader in, OutputStream out, int id) throws IOException;
}

