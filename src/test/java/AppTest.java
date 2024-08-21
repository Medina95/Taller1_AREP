import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

/**
 * La clase AppTest contiene pruebas unitarias para una API RESTful que maneja dinosaurios.
 * Utiliza Mockito para simular las dependencias y verificar que las respuestas HTTP se generen
 * correctamente para los métodos GET, POST, PUT, y DELETE.
 */
public class AppTest {

    @Mock
    private OutputStream mockOutputStream;
    private RestServiceImpl restService;

    /**
     * MockitoAnnotations.initMocks(this) Inicializa los objetos anotados con @Mock en la clase de prueba.
     * Esto permite que Mockito cree instancias simuladas de BufferedReader, OutputStream, y Socket que se usan en las pruebas.
     * Crea una nueva instancia de la clase RestServiceImpl que se va a probar.
     * Crea un objeto simulado de OutputStream que se utilizará para capturar y verificar los datos escritos en el flujo de salida.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        restService = new RestServiceImpl();
        mockOutputStream = mock(OutputStream.class);
    }


    /**
     *Verifica que el método handleGet devuelve una respuesta JSON correcta para una solicitud GET.
     * Verifica que el servidor responda con un código de estado HTTP 200 OK
     * @throws IOException  si ocurre un error de entrada/salida durante la comunicación HTTP.
     */
    @Test
    public void test01HandleGet() throws IOException {
        // Simular la entrada para la solicitud GET
        BufferedReader bufferedReader = new BufferedReader(new StringReader(
                "GET /api/dinosaurio HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "\r\n"
        ));

        // Ejecutar el método
        restService.handleGet(new String[]{"GET", "/api/dinosaurio", "HTTP/1.1"}, bufferedReader, mockOutputStream, mock(Socket.class));

        // Capturar los argumentos que se pasaron a mockOutputStream.write()
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockOutputStream, times(2)).write(argumentCaptor.capture());

        // Obtener el cuerpo de la respuesta JSON (segunda llamada a write())
        byte[] responseBodyBytes = argumentCaptor.getAllValues().get(1);
        String jsonResponse = new String(responseBodyBytes);
        // Verificar que el código de estado es 200 OK
        String responseHeader = new String(argumentCaptor.getAllValues().get(0));
        assertTrue(responseHeader.contains("HTTP/1.1 200 OK"));
        // Verificar que el JSON contiene el formato específico
        assertTrue(jsonResponse.contains("[]"));
    }

    /**
     *Verifica que el método HandlePost agrege un dino y devuelva una respuesta JSON correcta para una solicitud POST.
     * Verifica que el servidor responda con un código de estado HTTP 201 Created
     * @throws IOException  Si ocurre un error de entrada/salida durante la comunicación HTTP.
     */
    @Test
 public void test02HandlePost() throws IOException {
    // Simular la entrada con un JSON válido
    String jsonInput = "{\"Dinosaurio\":\"Triceratops\"}";
    BufferedReader bufferedReader = new BufferedReader(new StringReader(
            "POST /api/dinosaurio HTTP/1.1\r\n" +
            "Host: localhost\r\n" +
            "Content-Length: " + jsonInput.length() + "\r\n" +
            "\r\n" +
            jsonInput
    ));

    // Ejecutar el método
    restService.handlePost(bufferedReader, mockOutputStream);


        // Capturar los argumentos que se pasaron a mockOutputStream.write()
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockOutputStream, times(2)).write(argumentCaptor.capture());
        // Verificar que el código de estado es 201 Created
        String responseHeader = new String(argumentCaptor.getAllValues().get(0));
        assertTrue(responseHeader.contains("HTTP/1.1 201 Created"));

        // Obtener la primera llamada, que es el encabezado de la respuesta
        byte[] responseBodyBytes = argumentCaptor.getAllValues().get(1);
        String jsonResponse = new String(responseBodyBytes);

        // Verificar el contenido del JSON
        assertTrue(jsonResponse.contains("[{ \"id\": 1, \"status\": \"Dino received\", \"Dinosaurio\": \"Triceratops\" }]"));

}
    /**
     *Verifica que el método HandlePut actualice un dino y devuelva una respuesta JSON correcta para una solicitud PUT.
     * Verifica que el servidor responda con un código de estado HTTP 200, pues devuelve los datos actualizados
     * @throws IOException  Si ocurre un error de entrada/salida durante la comunicación HTTP.
     */
    @Test
    public void test03HandlePut() throws IOException {


        // Simular la entrada con un JSON válido para PUT
        String jsonInputPut = "{\"Dinosaurio\":\"Brachiosaurus\"}";
        BufferedReader bufferedReaderPut = new BufferedReader(new StringReader(
                "PUT /api/dinosaurio/" + 2 + " HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "Content-Length: " + jsonInputPut.length() + "\r\n" +
                        "\r\n" +
                        jsonInputPut
        ));

        // Ejecutar el método para PUT
        restService.handlePut(bufferedReaderPut, mockOutputStream, 1);

        // Capturar los argumentos que se pasaron a mockOutputStream.write() después del PUT
        ArgumentCaptor<byte[]> argumentCaptorPut = ArgumentCaptor.forClass(byte[].class);
        verify(mockOutputStream, times(2)).write(argumentCaptorPut.capture());
        // Verificar que el código de estado es 200 OK
        String Codigo = new String(argumentCaptorPut.getAllValues().get(0));
        assertTrue(Codigo.contains("HTTP/1.1 200 OK"));

        // Obtener el cuerpo de la respuesta JSON (última llamada a write())
        byte[] responseBodyBytesPut = argumentCaptorPut.getAllValues().get(1);
        String jsonResponsePut = new String(responseBodyBytesPut);
        assertTrue(jsonResponsePut.contains("[{ \"id\": 1, \"status\": \"Dino received\", \"Dinosaurio\": \"Brachiosaurus\" }]"));


    }

    /**
     *Verifica que el método HandleDelete elimine un dino y devuelva una respuesta JSON correcta para una solicitud PUT.
     * Verifica que el servidor responda con un código de estado HTTP 200, pues devuelve los datos actualizados
     * @throws IOException  Si ocurre un error de entrada/salida durante la comunicación HTTP.
     */
    @Test
    public void test04HandleDelete() throws IOException {



        BufferedReader bufferedReaderDelete = new BufferedReader(new StringReader(
                "DELETE /api/dinosaurio/" + 1 + " HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "Content-Length: 0\r\n" +
                        "\r\n"
        ));

        // Ejecutar el método para DELETE
        restService.handleDelete(bufferedReaderDelete, mockOutputStream, 1);

        // Capturar los argumentos que se pasaron a mockOutputStream.write() después del DELETE
        ArgumentCaptor<byte[]> argumentCaptorDelete = ArgumentCaptor.forClass(byte[].class);
        verify(mockOutputStream, times(2)).write(argumentCaptorDelete.capture());

        // Verificar que el código de estado es 200 OK
        String Codigo = new String(argumentCaptorDelete.getAllValues().get(0));
        assertTrue(Codigo.contains("HTTP/1.1 200 OK"));

        // Obtener el cuerpo de la respuesta JSON (última llamada a write())
        byte[] responseBodyBytesDelete = argumentCaptorDelete.getAllValues().get(1);
        String jsonResponseDelete = new String(responseBodyBytesDelete);

        // Verificar que el JSON contiene el objeto actualizado (vacío después del DELETE)

        assertTrue(jsonResponseDelete.contains("[]"));

    }
}
