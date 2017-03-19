# Diferencias de punto de ebullición
## Hackaton IoT - Microsoft 2017
### Integrantes
- Fabio Andrés Pertuz Umaña
- Juan Sebastian Hernández Serrato

### Resumen del proyecto
Nuestro proyecto se basa en la medición de parámetros obtenidos de una técnica de separación de mezclas, llamada diferencias de punto de ebullición, empleada en una empresa de producción de fragancias y compuestos aromáticos sobre la cual existe una plataforma web en la que empleamos reportes generados por Azure.

![Diferencias de punto de Ebullición](/assets/prueba.gif "Diferencias de punto de Ebullición")

### Alcance

Obtener datos mediante sensores físicos y simulados para su procesamiento en la nube de Microsoft Azure. Con base en esa información, crear elementos como: dashboard en tiempo real y bases de datos, para la toma de desiciones y tratamiento de información. También, implementar acciones con las herramientas de PaaS para valores fuera de rangos definidos como el envío de correos de notificación. Finalmente, configurar una aplicación web para establecer comunicación de tipo C2D, de esta forma ejecutar funciones propias del dispositivo como abrir compuertas lógicas.

### Arquitectura de la solución

![Diagrama de la Solución](/assets/resume.png "Diagrama de la Solución")

#### Simulador de Java

El simulador de Java es un programa que incluye las librerias `com.microsoft.azure.sdk.iot:iot-device-client:1.0.21` para conectarse a la nube de Microsoft Azure.

#### Huzzah

El código incluye la configuración del Adafruit Huzzah que transmite datos a la nube de Microsoft Azure.

![Circuito en Protoboard](/assets/proto.png "Circuito en Protoboard")

#### IoT Hub

Es la puerta de entrada de información proveniente de los dispositivos (cosas).

#### Stream Analytics

Constituye parte fundamenta de nuestra solución ya que distribuye la información a las diferentes salidas configuradas para su posterior análisis. A continuación encuentra las consultas realizadas en el componente.

```sql
SELECT
    DeviceId,
    EventTime,
    MTemperature as TemperatureReading,
    Humidity as HumidityReading
INTO
    TemperatureTableStorage
FROM
    BeakerSensors
WHERE
    DeviceId is not null
    and EventTime is not null
    and tipo = 'BeakerA'


SELECT
    DeviceId,
    EventTime,
    MTemperature as TemperatureReading,
    Humidity as HumidityReading
INTO
    TemperatureAlertToEventHub
FROM
    BeakerSensors
WHERE
    MTemperature > 30
    and tipo = 'BeakerA'

SELECT
    encendido,
    llama,
    System.TimeStamp AS momento
INTO
    powerBIOutput
FROM
    BeakerSensors
WHERE
    tipo = 'Mechero'


SELECT
    encendido,
    llama,
    System.TimeStamp AS momento
INTO
    azureDocumentDBOutput
FROM
    BeakerSensors
WHERE
    tipo = 'Mechero'
```

#### Power BI

Gracias a la integración entre Azure y PowerBI podemos explotar sus versiones Web y de Escritorio.

##### Dashboard en tiempo Real (Versión Web)

![Dashboard Web](/assets/powerbi1.png "Dashboard Web")

##### Reporte en cliente de Escritorio

![Reporte Escritorio](/assets/powerbi2.png "Reporte Escritorio")

#### Document DB

Empleamos este componente para almacenar datos históricos y así poder generar reportes de analítica descriptiva usando clientes como Power BI.

#### Service Bus Queue

Este componente es una cola me mensajes que recibe datos cuándo los valores de temperatura generados por el mechero salen del límite establedico (30° C).

#### Table Storage

Acá almacenamos información de manera tabular desde los datos ingresados del Huzzah.

#### Event Hub

Gracias al Event Hub podemos disparar eventos que son procesados en la aplicación web y así controlamos la activación de puertas de paso en el sistema.

#### Web App

La aplicación web permite enviar mensajes al Huzzah y así controlar su comportamiento. Usando el modelo C2D.

![Aplicación Web](/assets/webpage.png "Aplicación Web")

#### Logic App

Esta aplicación basada en un mensaje de la cola de mensajería que definimos en el bus nos permite enviar conrreos con las alertas necesarias para tomar acciones en el laboratorio.

![Diagrama Aplicación Lógica](/assets/logicapp.png "Diagrama Aplicación Lógica")

![Correo Aplicación Lógica](/assets/correo.png "Correo Aplicación Lógica")
