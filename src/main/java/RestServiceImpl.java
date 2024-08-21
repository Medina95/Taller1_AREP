

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de los servicios REST para el servidor web.
 * Maneja solicitudes HTTP GET/POST/PUT/DELETE.
 */
public class RestServiceImpl implements RESTService {
    private static List<String> jsonResponses = new ArrayList<>();
    private static int currentId = 1;
      /**
     * Maneja una solicitud HTTP GET.
     *
     * @param requestLine Una matriz de cadenas que contiene la línea de solicitud, dividida por espacios.
     * @param in BufferedReader para leer datos de la solicitud.
     * @param out  OutputStream para enviar la respuesta al cliente.
     * @param clientSocket El Socket del cliente, utilizado para obtener información adicional si es necesario.
     * @throws IOException Si ocurre un error al leer la solicitud o al escribir la respuesta.
     */
    @Override
    public void handleGet(String[] requestLine, BufferedReader in, OutputStream out, Socket clientSocket) throws IOException {
             // Combinar todas las respuestas JSON en una sola
             String combinedJsonResponse = String.format("[%s]", String.join(",", jsonResponses));
    
             // Enviar la respuesta combinada
             sendJsonResponse(out,200, combinedJsonResponse);
    }

 /**
     * Maneja una solicitud HTTP POST.
     *
     * @param in Un flujo de entrada de {@link BufferedReader} para leer datos del cuerpo de la solicitud.
     * @param out Un flujo de salida {@link OutputStream} para enviar la respuesta al cliente.
     * @throws IOException Si ocurre un error al leer la solicitud o al escribir la respuesta.
     */
    public void handlePost(BufferedReader in, OutputStream out) throws IOException {
        // Leer los encabezados adicionales
        String line;
        int contentLength = -1;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }
    
        // Leer el cuerpo
        StringBuilder body = new StringBuilder();
        if (contentLength > 0) {
            char[] buffer = new char[contentLength];
            in.read(buffer, 0, contentLength);
            body.append(buffer);
        }
    
        // Extraer el valor del campo "dino"
        String Dinosaurio = extractValue(body, "Dinosaurio");
        if (Dinosaurio == null) return;
    
        // Crear una respuesta JSON con un ID único
        String jsonResponse = String.format("{ \"id\": %d, \"status\": \"Dino received\", \"Dinosaurio\": \"%s\" }", currentId++, Dinosaurio);
        jsonResponses.add(jsonResponse);
    
        // Combinar todas las respuestas JSON en una sola
        String combinedJsonResponse = String.format("[%s]", String.join(",", jsonResponses));
    
        // Enviar la respuesta combinada
        sendJsonResponse(out,201, combinedJsonResponse);
    }
      /**
     * Maneja una solicitud HTTP DELETE a traves de un ID 
     *
     * @param in Un flujo de entrada de {@link BufferedReader} para leer datos de la solicitud.
     * @param out Un flujo de salida {@link OutputStream} para enviar la respuesta al cliente.
     * @param id El ID del Dinosaurio que se está eliminando.
     * @throws IOException Si ocurre un error al leer la solicitud o al escribir la respuesta.
     */
    @Override
    public void handleDelete(BufferedReader in, OutputStream out, int id) throws IOException {
        // Implementación similar a GET o POST
        List<String> updatedResponses = new ArrayList<>();
        for (String response : jsonResponses) {
            String json = response.trim();
            
            // Verifica si el objeto tiene el id que queremos eliminar
            if (!json.contains("\"id\": " + id)) {
                
                updatedResponses.add(json);
            }
        }
        // Actualiza la lista original
        jsonResponses = updatedResponses;    
        String combinedJsonResponse = String.format("[%s]", String.join(",", jsonResponses));

        sendJsonResponse(out,200, combinedJsonResponse);   
    }

     /**
     * Maneja una solicitud HTTP PUT atraves de un ID
     *
     * @param in Un flujo de entrada de {@link BufferedReader} para leer datos del cuerpo de la solicitud.
     * @param out Un flujo de salida {@link OutputStream} para enviar la respuesta al cliente.
     * @param id El ID del Dinosaurio que se está actualizando.
     * @throws IOException Si ocurre un error al leer la solicitud o al escribir la respuesta.
     */
    @Override
    public void handlePut(BufferedReader in, OutputStream out,int id) throws IOException {
          // Leer los encabezados adicionales
          String line;
          int contentLength = -1;
          while ((line = in.readLine()) != null && !line.isEmpty()) {
              if (line.startsWith("Content-Length:")) {
                  contentLength = Integer.parseInt(line.split(":")[1].trim());
              }
          }
      
          // Leer el cuerpo
          StringBuilder body = new StringBuilder();
          if (contentLength > 0) {
              char[] buffer = new char[contentLength];
              in.read(buffer, 0, contentLength);
              body.append(buffer);
          }
      
          // Extraer el valor del campo "dino"
          String Dinosaurio = extractValue(body, "Dinosaurio");
          if (Dinosaurio == null) return;


          // Actualizar la lista de respuestas JSON
        List<String> updatedResponses = new ArrayList<>();


         for (String response : jsonResponses) {
            String json = response.trim();
        
        // Verifica si el objeto tiene el id que queremos actualizar
            if (json.contains("\"id\": " + id)) {
                // Actualizar el nombre del dinosaurio
                String updatedJson = String.format("{ \"id\": %d, \"status\": \"Dino received\", \"Dinosaurio\": \"%s\" }",id, Dinosaurio);
                updatedResponses.add(updatedJson);
            
            } else {
                updatedResponses.add(json);
            }
        }
        jsonResponses = updatedResponses;    
        String combinedJsonResponse = String.format("[%s]", String.join(",", jsonResponses));

        sendJsonResponse(out,200, combinedJsonResponse);   

    }
     /**
     * Envía una respuesta JSON al cliente.
     *
     * @param out El flujo de salida {@link OutputStream} para enviar la respuesta.
     * @param statusCode El código de estado HTTP para la respuesta.
     * @param jsonResponse El cuerpo de la respuesta en formato JSON.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */ 

    private void sendJsonResponse(OutputStream out, int statusCode, String jsonResponse) throws IOException {
        String statusText;
        switch (statusCode) {
            case 200:
                statusText = "OK";
                break;
            case 201:
                statusText = "Created";
                break;
            case 204:
                statusText = "No Content";
                break;
            case 404:
                statusText = "Not Found";
                break;
            case 400:
                statusText = "Bad Request";
                break;
            default:
                statusText = "OK";
                break;
        }
    
        String responseHeader = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + jsonResponse.length() + "\r\n" +
                "\r\n";
        out.write(responseHeader.getBytes());
        out.write(jsonResponse.getBytes());
    }


      /**
     * Extrae el valor asociado con una clave específica de un objeto JSON en formato de cadena.
     *
     * @param jsonString El objeto JSON como una cadena de texto.
     * @param key La clave cuyo valor se desea extraer.
     * @return El valor asociado con la clave, o {@code null} si la clave no se encuentra.
     */
    public static String extractValue(StringBuilder jsonString, String key) {
        // Encontrar la posicion de la clave
        int startIndex = jsonString.indexOf("\"" + key + "\":\"") + key.length() + 4;
        int endIndex = jsonString.indexOf("\"", startIndex);

        // Extraer el valor entre las comillas
        return jsonString.substring(startIndex, endIndex);
    }
}
