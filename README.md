# TALLER DISEÑO Y ESTRUCTURACIÓN DE APLICACIONES DISTRIBUIDAS EN INTERNET


Este proyecto implementa un servidor web básico en Java que maneja solicitudes RESTful (GET, POST, PUT, DELETE) y sirve archivos estáticos. El servidor está diseñado para manejar múltiples conexiones de clientes simultáneamente y realizar operaciones CRUD en objetos JSON en memoria. Cada solicitud es manejada por un hilo separado (ClientHandler), permitiendo la concurrencia. El tema que se escogio para la pagina web es de dinosaurios :D. 
 ![Pagina](src/ReadmeImages/paginita.png)

 ## Arquitectura
 Este proyecto sigue la arquitectura cliente-servidor. Los clientes envían solicitudes HTTP al servidor, que maneja la lógica de los serviciso REST y devuelve respuestas en formato JSON. El servidor también puede servir archivos estáticos, como HTML, CSS e imágenes.

 ### componentes  
   - **Servidor** : SimpleWebServer maneja las conexiones y enruta las solicitudes a los servicios correspondientes. ClientHandler Gestiona la comunicación con el cliente, procesa la solicitud y decide si debe servir un archivo estático o delegar la solicitud a un servicio REST.
   - **Servicios REST**: Implementados en la clase RestServiceImpl, manejan operaciones CRUD en objetos JSON.
   - **Archivos estáticos**: Almacenados en el directorio webroot


## Primeros Pasos
Estas instrucciones le permitirán obtener una copia del proyecto en funcionamiento en su máquina local para fines de desarrollo y prueba. 


### Requisitos Previos
Para ejecutar este proyecto, necesitarás tener instalado:

- Java JDK 8 o superior.
- Un IDE de Java como IntelliJ IDEA, Eclipse, o NetBeans.
- Maven para manejar las dependencias 
- Un navegador web para interactuar con el servidor.

### Instalación 

1. Tener instalado Git en tu maquina local 
2. Elegir una carpeta en donde guardes tu proyecto
3. abrir la terminal de GIT --> mediante el clik derecho seleccionas Git bash here
4. Clona el repositorio en tu máquina local:
   ```bash
   git clone https://github.com/Medina95/Taller1_AREP.git
   ```
5. Abre el proyecto con tu IDE favorito o navega hasta el directorio del proyecto 
6. Desde la terminal  para compilar el proyecto ejecuta:

   ```bash
   mvn clean install
   ```
7. compila el proyecto  que contiene el metodo MAIN: SimpleWebServer.java o ejecuta desde la terminal

   ```bash
    java -cp target/miprimera-app-1.0-SNAPSHOT.jar SimpleWebServer
   ```
   Vera que el servidor esta listo y corriendo sobre el puerto 8080
8. Puedes Ingresar desde el navegador a  la pagina:
    http://localhost:8080/index.html
9. Puedes interactuar con los endpoints RESTful (/api):
   - GET = http://localhost:8080/api/dinosaurios
   - POST= http://localhost:8080/api/dinosaurio
   - PUT=  http://localhost:8080/api/dinosaurio/1
   - DELETE= http://localhost:8080/api/dinosaurio/1

    Utilizando herramientas como Postman, para el POST/PUT/DELETE te vas a la parte de body -> raw -> verificas el formato JSON y agregas un dino asi 
    - {"Dinosaurio":"Brachiosaurus"}
    ![Dinosaurio](src/ReadmeImages/image.png)
## Ejecutar las pruebas

Se implementaron pruebas unitarias para los métodos de manejo de solicitudes HTTP (GET, POST, PUT, DELETE) en el servidor. Estas pruebas se realizaron utilizando JUnit y Mockito para simular las solicitudes y validar las respuestas.

Para ejecutar las pruebas:  
1. Desde tu IDE, ejecuta las clase AppTest.java o desde la terminal ejecutas:
   ```bash
   mvn test
   ```
### Desglosar en pruebas de extremo a extremo

- **Test01HandleGet**: Verifica que el método handleGet devuelve una respuesta JSON correcta para una solicitud GET y Verifica que el servidor responda con un código de estado HTTP 200 OK.
- **Test02HandlePost**: Verifica que el método HandlePost agrege un dino y devuelva una respuesta JSON correcta para una solicitud POST y Verifica que el servidor responda con un código de estado HTTP 201 Created
- **Test03HandlePut**: Verifica que el método HandlePut actualice un dino y devuelva una respuesta JSON correcta para una solicitud  PUT y Verifica que el servidor responda con un código de estado HTTP 200, pues devuelve los datos actualizados.
- **Test04HandleDelete**: Verifica que el método HandleDelete elimine un dino y devuelva una respuesta JSON correcta para una solicitud  DELETE y Verifica que el servidor responda con un código de estado HTTP 200, pues devuelve los datos actualizados.

    ![Pagina](src/ReadmeImages/test.png)



## Built With
* [Maven](https://maven.apache.org/) - Dependency Management



## Authors

* **Carolina Medina Acero** -  [Medina95](https://github.com/Medina95)
