# Declaración de uso de IA

Completar por todos los integrantes, también si no se ha usado IA. No pegar
credenciales ni datos personales de terceros.

| Integrante | Herramienta/modelo o «no utilizada» | Uso concreto | Archivos/commits afectados | Cómo se verificó y qué se aprendió |
|---|---|---|---|---|
| Pablo Reyes Leroy | Gemini Flash (alto) | Ayuda orientativa para resolver trazas de error cuando estábamos bloqueados: depuración del fallo 500 en tests de validación MockMvc, reubicación de excepciones a producción y diseño del payload de error. | `Entrega 4: servicio`, `Entrega 6: advice global` (`BookService.java`, `BookController.java`, `BookRequestDTO.java`, `GlobalExceptionHandler.java`) | Se verificó ejecutando `./mvnw test` hasta obtener BUILD SUCCESS con los 7 tests en verde. Se aprendió la separación estricta Controller-Service, validación con `@Valid` y centralización de excepciones con `@RestControllerAdvice`. |
| Aaron Naranjo | Gemini Flash (alto) | Apoyo orientativo conjunto en momentos de bloqueo técnico para resolver dudas de sintaxis en cabeceras HTTP y estructura inicial de excepciones de negocio. | `Entrega 1: echo text`, `Entrega 3: ResponseEntity`, `Entrega 5: excepciones` (`BookController.java`, `BookNotFoundException.java`, `InvalidBookException.java`) | Se verificó pasando los tests de cada entrega con `./mvnw test`. Se aprendió el uso de códigos semánticos (`201 Created` con `Location`, `204 No Content`) y el lanzamiento de excepciones desde la capa de servicio. |

Cada integrante debe comprender y poder defender sus cambios. La IA puede
ayudar a interpretar errores y conceptos; no sustituye la autoría ni justifica
copiar una solución completa sin entenderla.