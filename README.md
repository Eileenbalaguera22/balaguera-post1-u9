# Recordatorios de Citas - Unidad 9

## Descripción del proyecto

Este proyecto permite gestionar el envío automático de recordatorios de citas médicas mediante diferentes canales de notificación. Se implementaron estrategias de envío por correo electrónico y SMS, pruebas unitarias con JUnit 5 y Mockito, pruebas de integración con Spring Boot y un proceso de integración continua mediante GitHub Actions.

El desarrollo se divide en dos partes: la implementación inicial con Test Doubles y el refactor de la arquitectura aplicando el principio de Inversión de Dependencias (DIP).

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.0.9
- Spring Data JPA
- H2 Database
- JUnit 5
- Mockito
- Maven
- Git y GitHub
- GitHub Actions

## Funcionalidades implementadas

- Consulta de citas pendientes de recordatorio.
- Búsqueda de citas programadas dentro de las próximas 24 horas.
- Selección del canal de notificación según la preferencia de cada cita.
- Envío simulado de recordatorios por correo electrónico o SMS.
- Actualización del estado de la cita después del envío.
- Envío de recordatorios urgentes mediante un canal independiente.
- Validación de citas para evitar notificaciones duplicadas o fuera del intervalo permitido.
- Pruebas unitarias y pruebas de integración.

---

# Parte 1: Mockito y elección justificada de Test Doubles

## Decisiones de diseño

### Patrón Strategy

Se creó la interfaz `ReminderChannel` para definir una operación común de envío de recordatorios. Las clases `EmailReminderChannel` y `SmsReminderChannel` implementan esta interfaz, permitiendo utilizar diferentes canales sin modificar la lógica principal del servicio.

Esta separación facilita las pruebas, ya que es posible simular los canales de notificación con Mockito y verificar su comportamiento sin realizar envíos reales.

### Patrón Factory

Se implementó `ReminderChannelFactory` para seleccionar el canal correspondiente según el valor de `CanalNotificacion`.

De esta forma, `CitaReminderServiceImpl` delega la selección del canal en lugar de encargarse directamente de crear las estrategias. Esto reduce el acoplamiento y facilita la incorporación de nuevos canales de notificación.

### Uso de Test Doubles

Se utilizaron objetos de prueba para aislar el servicio de sus dependencias y controlar su comportamiento durante las pruebas unitarias.

Las principales decisiones fueron:

- **Mock:** se utilizó Mockito para simular `CitaRepository`, `ReminderChannelFactory` y `ReminderChannel`. Esto permite controlar las respuestas de las dependencias y verificar interacciones como las consultas, los envíos y el guardado de las citas.
- **Stub:** se proporcionan respuestas controladas a las dependencias para probar diferentes escenarios, como la existencia o ausencia de citas pendientes.
- **Fake:** no se implementó una clase Fake personalizada para las pruebas unitarias. Para las pruebas de persistencia se utiliza H2 como base de datos en memoria, proporcionando un entorno de almacenamiento de prueba.

También se utiliza `Clock.fixed(...)` para controlar la fecha y hora de las pruebas. Es un reloj real configurado con una hora fija, no un Mock ni un Stub. Esto permite obtener resultados predecibles y repetibles sin depender de la hora del sistema.

### Separación de responsabilidades

El servicio coordina el proceso de envío, el repositorio administra el acceso a las citas y los canales se encargan de enviar las notificaciones.

Esta separación facilita el mantenimiento, las pruebas y la evolución del sistema.

## Ejecución de las pruebas

Desde la terminal, ubicada en la carpeta raíz del proyecto, ejecutar:

```powershell
mvn test
```

La ejecución se considera correcta cuando Maven finaliza con `BUILD SUCCESS` y las pruebas terminan sin errores.

---

# Parte 2: Inversión de Dependencias (DIP), pruebas de integración y CI

## Decisiones de diseño y arquitectura

### 1. Aplicación del principio de Inversión de Dependencias (DIP)

Se refactorizó la lógica de negocio para evitar que el servicio principal dependa directamente de implementaciones concretas de los canales de notificación.

Mediante la clase `CanalUrgenteConfig` se define la configuración del canal de notificación urgente, que se inyecta en `CitaReminderServiceImpl` mediante el constructor utilizando la interfaz `ReminderChannel`.

Esta implementación permite que el servicio dependa de una abstracción y no de una clase concreta, facilitando el mantenimiento, las pruebas y la sustitución de implementaciones.

### 2. Inyección de dependencias

Se utiliza la inyección de dependencias por constructor para proporcionar al servicio sus colaboradores principales:

- `C# Recordatorios de Citas - Unidad 9

## Descripción del proyecto

Este proyecto permite gestionar el envío automático de recordatorios de citas médicas mediante diferentes canales de notificación. Se implementaron estrategias de envío por correo electrónico y SMS, pruebas unitarias con JUnit 5 y Mockito, pruebas de integración con Spring Boot y un proceso de integración continua mediante GitHub Actions.

El desarrollo se divide en dos partes: la implementación inicial con Test Doubles y el refactor de la arquitectura aplicando el principio de Inversión de Dependencias (DIP).

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.0.9
- Spring Data JPA
- H2 Database
- JUnit 5
- Mockito
- Maven
- Git y GitHub
- GitHub Actions

## Funcionalidades implementadas

- Consulta de citas pendientes de recordatorio.
- Búsqueda de citas programadas dentro de las próximas 24 horas.
- Selección del canal de notificación según la preferencia de cada cita.
- Envío simulado de recordatorios por correo electrónico o SMS.
- Actualización del estado de la cita después del envío.
- Envío de recordatorios urgentes mediante un canal independiente.
- Validación de citas para evitar notificaciones duplicadas o fuera del intervalo permitido.
- Pruebas unitarias y pruebas de integración.

---

# Parte 1: Mockito y elección justificada de Test Doubles

## Decisiones de diseño

### Patrón Strategy

Se creó la interfaz `ReminderChannel` para definir una operación común de envío de recordatorios. Las clases `EmailReminderChannel` y `SmsReminderChannel` implementan esta interfaz, permitiendo utilizar diferentes canales sin modificar la lógica principal del servicio.

Esta separación facilita las pruebas, ya que es posible simular los canales de notificación con Mockito y verificar su comportamiento sin realizar envíos reales.

### Patrón Factory

Se implementó `ReminderChannelFactory` para seleccionar el canal correspondiente según el valor de `CanalNotificacion`.

De esta forma, `CitaReminderServiceImpl` delega la selección del canal en lugar de encargarse directamente de crear las estrategias. Esto reduce el acoplamiento y facilita la incorporación de nuevos canales de notificación.

### Uso de Test Doubles

Se utilizaron objetos de prueba para aislar el servicio de sus dependencias y controlar su comportamiento durante las pruebas unitarias.

Las principales decisiones fueron:

- **Mock:** se utilizó Mockito para simular `CitaRepository`, `ReminderChannelFactory` y `ReminderChannel`. Esto permite controlar las respuestas de las dependencias y verificar interacciones como las consultas, los envíos y el guardado de las citas.
- **Stub:** se proporcionan respuestas controladas a las dependencias para probar diferentes escenarios, como la existencia o ausencia de citas pendientes.
- **Fake:** no se implementó una clase Fake personalizada para las pruebas unitarias. Para las pruebas de persistencia se utiliza H2 como base de datos en memoria, proporcionando un entorno de almacenamiento de prueba.

También se utiliza `Clock.fixed(...)` para controlar la fecha y hora de las pruebas. Es un reloj real configurado con una hora fija, no un Mock ni un Stub. Esto permite obtener resultados predecibles y repetibles sin depender de la hora del sistema.

### Separación de responsabilidades

El servicio coordina el proceso de envío, el repositorio administra el acceso a las citas y los canales se encargan de enviar las notificaciones.

Esta separación facilita el mantenimiento, las pruebas y la evolución del sistema.

## Ejecución de las pruebas

Desde la terminal, ubicada en la carpeta raíz del proyecto, ejecutar:

```powershell
mvn test
```

La ejecución se considera correcta cuando Maven finaliza con `BUILD SUCCESS` y las pruebas terminan sin errores.

---

# Parte 2: Inversión de Dependencias (DIP), pruebas de integración y CI

## Decisiones de diseño y arquitectura

### 1. Aplicación del principio de Inversión de Dependencias (DIP)

Se refactorizó la lógica de negocio para evitar que el servicio principal dependa directamente de implementaciones concretas de los canales de notificación.

Mediante la clase `CanalUrgenteConfig` se define la configuración del canal de notificación urgente, que se inyecta en `CitaReminderServiceImpl` mediante el constructor utilizando la interfaz `ReminderChannel`.

Esta implementación permite que el servicio dependa de una abstracción y no de una clase concreta, facilitando el mantenimiento, las pruebas y la sustitución de implementaciones.

### 2. Inyección de dependencias

Se utiliza la inyección de dependencias por constructor para proporcionar al servicio sus colaboradores principales:

- `C