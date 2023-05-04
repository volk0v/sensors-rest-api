# sensors-rest-api

## Description

This project is a REST API for system of sensors, which will send their measurements to the server by the API.

### API provide _3 endpoints_:

### POST `/sensors/registration/`

Registering new sensor in the system. The sensor's name should be unique and between 3 and 30 characters. If sensor with
the name already exists, server will return JSON with error.

Example of the request body:

```json
{
  "name": "House sensor"
}
```

Possible errors:

```json
{
  "errors": [
    {
      "fieldName": "name",
      "error": "Sensor with the name already exists"
    },
    {
      "fieldName": "name",
      "error": "Name should be between 3 and 30 characters"
    }
  ],
  "timestamp": 0
}
```

### POST `/measurements/add/`

Adding new measurement from the sensor. Has fields:

* value - temperature (float, -100.0 to 100.0)
* raining - is it raining (boolean)
* sensor.name - the name of the sensor, which sends the measurement.

Example of the request body:

```json
{
  "value": 24.7,
  "raining": false,
  "sensor": {
    "name": "House sensor"
  }
}
```

Possible errors:

```json
{
  "errors": [
    {
      "fieldName": "value",
      "error": "Value should be between -100 and 100"
    },
    {
      "fieldName": "value",
      "error": "Value field can't be null"
    },
    {
      "fieldName": "raining",
      "error": "Raining field can't be null"
    },
    {
      "fieldName": "sensor",
      "error": "Sensor can't be null"
    },
    {
      "fieldName": "sensor",
      "error": "Sensor with the name doesn't exist!"
    }
  ],
  "timestamp": 0
}
```

### GET `/measurements/`

Returns all measurements from database.

Example of the response:

```json
[
  {
    "value": 24.7,
    "raining": true,
    "sensor": {
      "name": "House"
    }
  },
  {
    "value": -24.7,
    "raining": false,
    "sensor": {
      "name": "Street"
    }
  },
  {
    "value": 0.0,
    "raining": false,
    "sensor": {
      "name": "Underground"
    }
  }
]
```

### GET `/measurements/rainy-days-amount/`

Returns amount of rainy days based on measurements in the database.

Example of the response:

```json
5
```
