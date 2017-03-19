SELECT
    DeviceId,
    EventTime,
    MTemperature as TemperatureReading
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
    MTemperature as TemperatureReading
INTO
    TemperatureAlertToEventHub
FROM
    BeakerSensors
WHERE
    MTemperature > 25
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
    documentDBOutput
FROM
    BeakerSensors
WHERE
    tipo = 'Mechero'