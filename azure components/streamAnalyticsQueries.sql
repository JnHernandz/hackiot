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