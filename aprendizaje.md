# Registro de aprendizaje del grupo

## Entrega 1: Echo text

- Comportamiento que añadimos o contrato que evolucionamos: Endpoint inicial en la ruta `/books` que devuelve una respuesta básica en texto plano.
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Fallo de comportamiento en `BookCheckpoint01Test` al no existir un controlador que mapease la petición HTTP GET en `/books` (error 404).
- Cambio mínimo y resultado verde: Creación de la clase `BookController` anotada con `@RestController` y un método `@GetMapping("/books")` que devuelve una cadena de texto plana.
- Refactorización: Configuración de la anotación `@RequestMapping("/books")` a nivel de clase para centralizar la ruta base.
- Decisiones sobre capas y contrato HTTP: Mantenimiento de la lógica directamente en el controlador en esta primera fase. Contrato: `GET /books` devuelve `text/plain` con código HTTP 200 OK.
- Contribuciones, revisión y siguiente paso: Implementado y validado por Aaron (`Entrega 1: echo text`). Siguiente paso: migrar la respuesta a formato JSON estructurado mediante una entidad y DTOs.

## Entrega 2: JSON

- Comportamiento que añadimos o contrato que evolucionamos: Introducción del modelo `Book` y los DTOs de petición y respuesta para serializar y deserializar datos en formato JSON.
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Fallo de aserción en `BookCheckpoint02Test` por incompatibilidad de cabeceras y tipos: el test esperaba `Content-Type: application/json` y un cuerpo estructurado con campos del libro, recibiendo texto plano.
- Cambio mínimo y resultado verde: Creación de `Book` y los records `BookRequest` y `BookResponse`. Implementación de endpoints GET y POST que reciben y devuelven estos objetos, almacenándolos temporalmente en una colección en memoria.
- Refactorización: Adopción de Java records para los DTOs, garantizando inmutabilidad y reduciendo código repetitivo.
- Decisiones sobre capas y contrato HTTP: Se mantiene el almacenamiento en memoria dentro del controlador. Contrato: peticiones y respuestas con `Content-Type: application/json` y código HTTP 200 por defecto.
- Contribuciones, revisión y siguiente paso: Implementado y validado por Pablo (`Entrega 2: JSON`). Siguiente paso: incorporar respuestas semánticas con códigos HTTP precisos mediante `ResponseEntity`.

## Entrega 3: ResponseEntity

- Comportamiento que añadimos o contrato que evolucionamos: Gestión explícita del protocolo HTTP con códigos de estado específicos (`201 Created`, `204 No Content`) y cabecera `Location` en la creación de recursos.
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Fallo de aserción en `BookCheckpoint03Test`: el POST devolvía `200 OK` sin cabecera `Location`, mientras la prueba exigía `201 Created` y `Location: /books/{id}`; las operaciones de borrado esperaban `204 No Content`.
- Cambio mínimo y resultado verde: Envolver los retornos en `ResponseEntity.created(location).body(...)`, `ResponseEntity.noContent().build()` y `ResponseEntity.notFound().build()` para recursos inexistentes.
- Refactorización: Estandarización de retornos `ResponseEntity<T>` en todos los métodos del controlador para unificar la gestión de estados HTTP.
- Decisiones sobre capas y contrato HTTP: El controlador asume el control formal de las cabeceras y estados HTTP sin alterar la lógica de almacenamiento en memoria.
- Contribuciones, revisión y siguiente paso: Implementado y validado por Aaron (`Entrega 3: ResponseEntity`). Siguiente paso: extraer la gestión de estado a una capa de servicio independiente.

## Entrega 4: Servicio

- Comportamiento que añadimos o contrato que evolucionamos: Separación de la capa web y la lógica de almacenamiento, introduciendo `BookService` e inyección de dependencias por constructor.
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Error de compilación inicial en `BookCheckpoint04Test` por falta de la clase `BookService` y por la expectativa de tipos con sufijo DTO (`BookRequestDTO` y `BookResponseDTO`).
- Cambio mínimo y resultado verde: Creación de `@Service BookService`, traslado de `ConcurrentHashMap` y `AtomicLong` desde el controlador al servicio, e inyección de `BookService` en `BookController` para delegar todas las operaciones.
- Refactorización: Limpieza completa del controlador, eliminando cualquier referencia a colecciones o estado interno.
- Decisiones sobre capas y contrato HTTP: Aplicación del criterio RA5.b: `BookController` se limita a traducir peticiones HTTP y devolver respuestas semánticas, mientras `BookService` centraliza la gestión del catálogo sin acoplamiento a tipos web.
- Contribuciones, revisión y siguiente paso: Implementado y validado por Pablo (`Entrega 4: servicio`). Siguiente paso: modelar situaciones anómalas de negocio con excepciones propias desde el servicio.

## Entrega 5: Excepciones

- Comportamiento que añadimos o contrato que evolucionamos: Creación de excepciones de dominio (`BookNotFoundException`, `InvalidBookException`) y su lanzamiento desde el servicio ante operaciones inválidas o IDs inexistentes.
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Fallo de comportamiento en `BookCheckpoint05Test` al consultar o eliminar libros inexistentes, esperando que el servicio lanzase `BookNotFoundException` en lugar de resolver la ausencia con valores nulos.
- Cambio mínimo y resultado verde: Definición de las clases de excepción heredando de `RuntimeException` y modificación de los métodos `get`, `update` y `delete` de `BookService` para lanzar la excepción correspondiente si el libro no existe en el mapa.
- Refactorización: Eliminación de retornos `null` en la capa de servicio para consolidar el control de flujo basado en excepciones.
- Decisiones sobre capas y contrato HTTP: El servicio no debe conocer códigos HTTP (como 404 o 400); únicamente señala la violación de la regla de negocio mediante la excepción correspondiente.
- Contribuciones, revisión y siguiente paso: Implementado y validado por Aaron (`Entrega 5: excepciones`). Siguiente paso: interceptar estas excepciones de forma centralizada para generar respuestas JSON homogéneas.

## Entrega 6: Advice global

- Comportamiento que añadimos o contrato que evolucionamos: Captura centralizada de excepciones mediante `@RestControllerAdvice` y validación de entrada en los DTOs con Jakarta Validation (`@Valid`, `@NotBlank`).
- Test y rojo observado (distinguir compilación/configuración de comportamiento): Error inicial de compilación por ausencia de `GlobalExceptionHandler`. Posteriormente, fallo de comportamiento en el test `validationHasErrorPayloadWithoutCallingService`: devolvía HTTP 500 por `NullPointerException` (al invocar el servicio sin datos válidos) en lugar del HTTP 400 esperado por validación temprana.
- Cambio mínimo y resultado verde: Reubicación de las excepciones de `src/test` a `src/main`. Implementación de `GlobalExceptionHandler` con `@ExceptionHandler` para mapear `BookNotFoundException` a 404, `InvalidBookException` a 400, `MethodArgumentNotValidException` a 400 (`VALIDATION_ERROR`), `HttpMessageNotReadableException` a 400 y errores no controlados a 500. Incorporación de `@Valid` en los métodos `create` y `update` de `BookController`.
- Refactorización: Creación de un `record ErrorResponse` interno para estructurar la carga útil del error (`error`, `message`, `timestamp`) sin exponer detalles internos de la aplicación en errores 500.
- Decisiones sobre capas y contrato HTTP: Desacoplamiento total del tratamiento de errores respecto a los controladores, eliminando bloques `try-catch` dispersos y garantizando un contrato uniforme de error en toda la API.
- Contribuciones, revisión y siguiente paso: Implementado y validado por Pablo (`Entrega 6: advice global`). Práctica completada con las 6 entregas validadas mediante `./mvnw test` y suite de tests en verde.
