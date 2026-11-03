# Recordatorios de Citas - Unidad 9

## Descripción del proyecto

Este proyecto permite gestionar el envío de recordatorios de citas médicas mediante diferentes canales de notificación. Para esta primera parte se implementaron estrategias de envío por correo electrónico y SMS, junto con pruebas unitarias utilizando JUnit 5 y Mockito.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database
- JUnit 5
- Mockitor
- Maven

## Funcionalidades implementadas

- Consulta de citas pendientes de recordatorio.
- Búsqueda de citas dentro de las próximas 24 horas.
- Selección del canal de notificación según la preferencia de cada cita.
- Envío simulado de recordatorios por correo electrónico o SMS.
- Actualización de la cita después del envío.
- Pruebas unitarias del servicio de recordatorios.

## Decisiones de diseño

### Patrón Strategy

Se creó la interfaz `ReminderChannel` para definir una operación común de envío de recordatorios. Las clases `EmailReminderChannel` y `SmsReminderChannel` implementan esta interfaz, permitiendo utilizar diferentes canales sin cambiar la lógica principal del servicio.

Esta separación también facilita las pruebas, porque se puede simular un canal de notificación con Mockito y verificar que se invoque correctamente.

### Patrón Factory

Se implementó `ReminderChannelFactory` para seleccionar el canal correspondiente según el valor de `CanalNotificacion`. De esta forma, `CitaReminderServiceImpl` delega la selección del canal en lugar de encargarse directamente de crear las estrategias.

En las pruebas se puede verificar la selección del canal mediante `verify(reminderChannelFactory).obtenerCanal(...)`.

### Uso de Test Doubles

Se utilizan objetos simulados con Mockito para aislar el servicio de sus dependencias durante las pruebas:

- `CitaRepository`: permite controlar las citas que devuelve la consulta y verificar si se guardan cambios.
- `ReminderChannelFactory`: permite controlar el canal que se obtiene.
- `ReminderChannel`: permite verificar que se envíe el recordatorio sin realizar una notificación real.

También se utiliza `Clock.fixed(...)` para controlar la fecha y hora de las pruebas. En lugar de simular el reloj, se emplea un reloj real con una hora fija, lo que hace que los resultados sean predecibles y repetibles.

### Separación de responsabilidades

El servicio coordina el proceso de envío, el repositorio administra el acceso a las citas y los canales se encargan de enviar las notificaciones. Esta separación facilita el mantenimiento y las pruebas del código.

## Ejecución de las pruebas

Desde la terminal, ubicada en la carpeta raíz del proyecto, ejecutar:

```powershell
.\mvnw.cmd test
```

La ejecución se considera correcta cuando Maven finaliza con `BUILD SUCCESS` y las pruebas terminan sin errores.

## Alcance de esta entrega

Esta entrega corresponde a la primera parte de la actividad. Las notificaciones se simulan y no se realizan envíos reales mediante servicios externos.
# Parte 2: Refactor por Inversión de Dependencias (DIP), Compatibilidad con Spring Boot 3.4+, Pruebas de Integración y CI

## 🛠️ Decisiones de Diseño y Arquitectura (Parte 2)

### 1. Aplicación del Principio de Inversión de Dependencias (DIP)
* **Desacoplamiento de Canales Urgentes:** Se refactorizó la lógica de negocio para que el servicio principal no dependa de implementaciones concretas. Mediante la clase de configuración `CanalUrgenteConfig`, se define un punto de composición (*composition root*) que abstrae y gestiona la inyección de la estrategia para recordatorios de alta prioridad a través del constructor, cumpliendo estrictamente con el DIP.

### 2. Estrategia de Pruebas Robustas y *Test Doubles*
* **Reloj Determinista (`Clock.fixed`):** Para evitar la dependencia del tiempo del sistema y hacer que las pruebas de la ventana de 24 horas sean totalmente predecibles y repetibles, se configuró un `Clock` estático utilizando `Clock.fixed(Instant.parse("2026-08-10T09:00:00Z"), ZoneId.of("UTC"))`.
* **Aislamiento con `@MockitoBean`:** Se adoptó la nueva API de Spring Boot 3.4+ reemplazando el antiguo `@MockBean` por `@MockitoBean` (`org.springframework.test.context.bean.override.mockito.MockitoBean`) para aislar los gateways externos de mensajería (como `NotificadorSmsGateway`) en el contexto completo de Spring.
* **Gestión de Sobrescritura de Beans:** Se solventaron los conflictos de beans duplicados entre el entorno de producción y el de pruebas (específicamente con el bean `clock`) incorporando `@Primary` en las configuraciones de test y habilitando explícitamente la propiedad `spring.main.allow-bean-definition-overriding=true` a nivel de anotación en las pruebas de integración.

---

## 🔄 Integración Continua (CI) y Automatización

* **Pipeline en GitHub Actions:** Se configuró un flujo de trabajo automatizado que se ejecuta en cada `push` o `pull request` hacia la rama principal. Este entorno limpia, compila y ejecuta todas las fases del ciclo de vida de Maven de forma aislada.
* **Badge de Estado:** El repositorio incluye el indicador visual que certifica el correcto funcionamiento del pipeline en entornos Linux limpios:
  
  [![CI Status](https://github.com/Eileenbalaguera22/balaguera-post1-u9/actions/workflows/ci.yml/badge.svg)](https://github.com/Eileenbalaguera22/balaguera-post1-u9/actions)

---

## 📋 Verificación Local y Comandos de Ejecución

Para validar de forma integral que la arquitectura, el refactor DIP, las pruebas unitarias y las pruebas de integración pasen sin errores, ejecuta el siguiente comando desde la raíz del proyecto en tu terminal:

```powershell
mvn clean verify