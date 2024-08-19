

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import junit.framework.TestCase;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/**
 * La clase AppTest contiene pruebas unitarias para una API RESTful que maneja dinosaurios. 
 * Estas pruebas se realizan utilizando JUnit y verifican los métodos HTTP GET, POST, PUT y DELETE de la API.
 *  La clase extiende TestCase de JUnit y utiliza anotaciones para ordenar los métodos de prueba.
 */
public class AppTest 
    extends TestCase{
    private static final String BASE_URL = "http://localhost:8080/api/dinosaurio";


    /**
     * Verifica el estado de la solicitud HTTP GET a la URL de la API para asegurarse 
     * de que el servidor responda con un código de estado HTTP 200
     * @throws IOException IOException Si ocurre un error de entrada/salida durante la comunicación HTTP.
     */
    public void test01Get() throws IOException {
        HttpURLConnection connection = createConnection(BASE_URL + "s", "GET");
        
        int status = connection.getResponseCode();
        assertEquals(200, status);
        connection.disconnect();
    }
    /**
     * Realiza una solicitud HTTP GET al archivo HTML especificado y verifica que el
     * código de estado de la respuesta sea 200.
     *
     * @throws IOException Si ocurre un error de entrada/salida durante la comunicación HTTP.
     */
    public void test01GetIMG() throws IOException {
         String BASE_URL = "http://localhost:8080/index.html";

        HttpURLConnection connection = createConnection(BASE_URL , "GET");
        
        int status = connection.getResponseCode();
        assertEquals(200, status);
        connection.disconnect();
    }

    /**
     * Realiza una solicitud HTTP POST al endpoint especificado con un cuerpo JSON
     * el nombre de un dinosaurio que se quiere agregar. Verifica que el código de estado
     * de la respuesta sea 201 y que el dinosaurio enviado esté presente en la respuesta.
     *
     * @throws IOException Si ocurre un error de entrada/salida durante la comunicación HTTP.
     */
    public void test02Post() throws IOException {
        String dinosaurioValue = "dilophosaurus";  // El valor de tu variable
        String jsonInputString = "{\"Dinosaurio\":\"" + dinosaurioValue + "\"}";

        HttpURLConnection connection = createConnection(BASE_URL, "POST");
        sendRequest(connection, jsonInputString);

        int status = connection.getResponseCode();
        assertEquals(201, status);

        String response = readResponse(connection);
      
        boolean  Dinoexist=verificadatodelalista(StringToList(response), dinosaurioValue);

        assertTrue(Dinoexist);

        connection.disconnect();
    }

    /**
     * Realiza una solicitud HTTP PUT al endpoint que contiene el ID que se quiere eliminar
     * Se Verifica que el código de estado de la respuesta sea 200 ( se que es 204 pero al actualizar se actualizara la respuesta)
     * Se verifica que el dinosaurio enviado esté presente en larespuesta después de verificar que el ID en la respuesta es correcto.
     * @throws IOException Si ocurre un error de entrada/salida durante la comunicación HTTP.
     */
    public void test03Put() throws IOException {
        String dinosaurioValue = "Velociraptor";  
        String jsonInputString = "{\"Dinosaurio\":\"" + dinosaurioValue + "\"}";
        HttpURLConnection connection = createConnection(BASE_URL + "/1", "PUT");
        sendRequest(connection, jsonInputString);

        int status = connection.getResponseCode();
        assertEquals(200, status);


        String response = readResponse(connection);


        if (VerificaIdDeLaLista(StringToList(response), 1)==true){
            boolean  Dinoexist1=verificadatodelalista(StringToList(response), dinosaurioValue);
            assertTrue(Dinoexist1);
        }


        connection.disconnect();
    }

    /**
     * Realiza una solicitud HTTP DELETE al endpoint especificado para eliminar un dinosaurio.
     * Verifica que el código de estado de la respuesta sea 200 ( se que es 204 pero al actualizar se actualizara la respuesta)
     *  y que el dinosaurio especificado ya no esté presente en la respuesta después de la eliminación.
     * @throws IOException Si ocurre un error de entrada/salida durante la comunicación HTTP.
     */
    public void test04Delete() throws IOException {
        String dinosaurioValue = "Velociraptor";  
        String jsonInputString = "{\"Dinosaurio\":\"" + dinosaurioValue + "\"}";
        HttpURLConnection connection = createConnection(BASE_URL + "/1", "DELETE");
        sendRequest(connection, jsonInputString);

        int status = connection.getResponseCode();
        assertEquals(200, status);

        String response = readResponse(connection);
        boolean  Dinoexist1=verificadatodelalista(StringToList(response), dinosaurioValue);
        assertFalse(Dinoexist1);

        connection.disconnect();
    }

    /**
     * Crea y configura una conexión HTTP para una solicitud a la URL especificada.
     * @param url La URL a la que se realizará la solicitud HTTP.
     * @param method El método HTTP que se utilizará para la solicitud (por ejemplo, "GET","POST", "PUT", "DELETE").
     * @return Una instancia de  HttpURLConnection configurada para la solicitud.
     * @throws IOException IOException Si ocurre un error al abrir la conexión o al configurar la conexión HTTP.
     */
    private HttpURLConnection createConnection(String url, String method) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        return connection;
    }

    /**
     * Lee la respuesta del servidor desde la conexión HTTP proporcionada.
     * 
     * Este método utiliza un {@link BufferedReader} para leer el flujo de entrada de la conexión HTTP
     * línea por línea, acumulando el contenido en un {@link StringBuilder}. El contenido de la respuesta
     * se convierte a una cadena de texto y se devuelve.
     * 
     * @param connection La conexión HTTP desde la cual se leerá la respuesta. Debe ser una instancia
     *                   de {@link HttpURLConnection} que esté abierta y que haya recibido una respuesta
     *                   del servidor.
     * @return El contenido de la respuesta del servidor como una cadena de texto.
     * @throws IOException Si ocurre un error al leer el flujo de entrada de la conexión HTTP.
     */

    private String readResponse(HttpURLConnection connection) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
    /**
     * Envía una solicitud HTTP con un cuerpo de datos a través de la conexión proporcionada.
     * 
     * Este método habilita el envío de datos a través de la conexión HTTP, convierte el cuerpo de la solicitud
     * (en formato JSON) en bytes y lo escribe en el flujo de salida de la conexión.
     * 
     * @param connection La conexión HTTP a la que se enviarán los datos. Debe ser una instancia de
     *                   {@link HttpURLConnection} configurada para aceptar datos en el cuerpo de la solicitud.
     * @param jsonInputString El cuerpo de la solicitud en formato JSON como una cadena de texto. Este será
     *                        convertido en bytes y enviado en la solicitud.
     * @throws IOException Si ocurre un error al obtener el flujo de salida de la conexión o al escribir en él.
     */
    private void sendRequest(HttpURLConnection connection, String jsonInputString) throws IOException {
        connection.setDoOutput(true);
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    /**
     * Convierte una cadena JSON que representa un array de objetos JSON en una lista de objetos JSON
    *  representados como cadenas de texto.
     * @param jsonString la cadena Json que queremos convertir a lista 
     * @returnUna  retorna una lista de cadenas de texto, donde cada cadena es un objeto JSON independiente.
     */
    private  List<String> StringToList(String jsonString){
        List<String> updatedResponses = new ArrayList<>();
                
                // Quitar los corchetes del array JSON
        String content = jsonString.substring(1, jsonString.length() - 1);
                
                // Eliminar las llaves de cada objeto JSON 
        content = content.replaceAll("\\{", "").replaceAll("}", "");
                
                // Dividir en objetos JSON basados en comas 
        String[] objects = content.split("(?<=\\})\\s*(?=\\{)");
                
        for (String obj : objects) {
            updatedResponses.add("{" + obj + "}");
        }
                
        return updatedResponses;
    }

    /**
     * Verifica si un dinosaurio específico está presente en una lista de objetos JSON representados como cadenas de texto.
    * @param lista La lista de cadenas de texto, donde cada cadena representa un objeto JSON.
     * @param dato El valor que se busca en los objetos JSON. Debe ser una cadena que represente el valor de la clave "Dinosaurio".
     * @return retorna el booleano si es verdadero existe el dato en la lista, si no, el dato no existe en la lista 
     */
    private boolean verificadatodelalista(List<String> lista,String dato){
        boolean b1=false;
        for (String response : lista) {
            String json = response.trim();
            
            // Verifica si el objeto tiene el id que queremos eliminar
            if (json.contains("\"Dinosaurio\": " + "\"" + dato + "\"")) {
                
                b1 =true;

            }
        }
        return b1;
    }

    /**
     * Verifica si un objeto JSON con un identificador específico está presente en una lista de objetos JSON.
     * 
     * Este método recorre una lista de cadenas de texto, donde cada cadena representa un objeto JSON, y busca
     * el identificador (`id`) especificado en el campo "id" de cada objeto JSON. Si encuentra una coincidencia,
     * devuelve {@code true}; de lo contrario, devuelve {@code false}.
     * 
     * @param lista La lista de cadenas de texto, donde cada cadena representa un objeto JSON en formato de texto.
     * @param id El identificador que se busca en los objetos JSON. Debe ser un valor entero que representa el
     *           valor del campo "id".
     * @return Devuelve un booleano, True si encontro el id especificado de lo contrario no. 
     */
    private boolean VerificaIdDeLaLista(List<String> lista,int id){
        boolean b1=false;
        for (String response : lista) {
            String json = response.trim();
            
            // Verifica si el objeto tiene el id que queremos eliminar
            if (json.contains("\"id\": " + id)) {
                
                b1 =true;

            }
        }
        return b1;
    }
}




