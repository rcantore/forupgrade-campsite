# For Upgrade - API Campsite Booking

## About the proyect

The proyect is IDE agnostic, it can be imported in any modern IDE that has support for Gradle builds and configurations. This is a Spring Boot application, currently only one profile is provided; a local profile with some sample data to have the app up and running for demonstration purposes.

Also, some unit tests are provided to prove the correct API functionality.

### Running the proyect

It can be run inside the IDE of your choise or using the gradle command line.

Whe using the command line option, it's advised to use the included gradle wrapper. Type on the root dir of the proyect:

```bash
./gradlew bootRun
```

## About the API

This is a REST API, not fully restful, and definitaly not HATEOAS.
It has two resources entry point, one for Campsites and one for Bookings
For simplicity purposes, the base url will be the one provided by the local default configuration: *http://localhost:8080*
Also there's a simple swagger installiont that can be found on <http://localhost:8080/api/swagger-ui.html>

### Campsites resource

#### Getting all the campsites available:
```
GET http://localhost:8080/api/campsites
```

Will return
```json
[
    {
        "id": 1,
        "name": "New Volcano Island",
        "location": "Pacific Ocean",
        "bookings": [
            {
                "id": 2,
                "fullName": "Edgard Camper",
                "email": "somemail@somedomain.com",
                "campsite": 1,
                "checkInDateTime": "2019-01-10T12:00:00",
                "checkoutDateTime": "2019-01-13T12:00:00"
            },
            {
                "id": 3,
                "fullName": "Jane Dove",
                "email": "jdove@anotherdomain.com",
                "campsite": 1,
                "checkInDateTime": "2019-01-30T12:00:00",
                "checkoutDateTime": "2019-01-31T12:00:00"
            }
        ]
    }
]
```

A Json Array with the campsites available, and if they have any reservations, they will be included in the response

#### Getting the bookings for a campsite

```
GET http://localhost:8080/api/campsites/1/bookings
```

Will return something like this:
```json
[
    {
        "id": 2,
        "fullName": "Edgard Camper",
        "email": "somemail@somedomain.com",
        "campsite": 1,
        "checkInDateTime": "2019-01-10T12:00:00",
        "checkoutDateTime": "2019-01-13T12:00:00"
    },
    {
        "id": 3,
        "fullName": "Jane Dove",
        "email": "jdove@anotherdomain.com",
        "campsite": 1,
        "checkInDateTime": "2019-01-30T12:00:00",
        "checkoutDateTime": "2019-01-31T12:00:00"
    }
]
```
A Json array with the bookings available for the campsite with id = 1

If a non existing id is used, then the API will return an HTTP 404 (Not Found) error

### Bookings resource

#### Getting all the bookings
```
GET http://localhost:8080/api/bookings
```

Will return all the bookings available

#### Creating bookings
Since the bookings must be added to a certain campsite, they are actually created on the campsites resource...

```
POST http://localhost:8080/api/campsites/{campsite_id}/bookings
```
Where {campsite_id} is the id of the campsite where the booking should be created

Example body:
```json
{
        "fullName": "Another One",
        "email": "dusty@somedomain.com",
        "checkInDateTime": "2019-02-22T12:00:00",
        "checkoutDateTime": "2019-02-24T12:00:00"
}
```

A variety of results is returned depending on the outcame

```
Error code 500
```
```json
["Booking should be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance"]
```
```json
["Bookings can't be for more than 3 days"]
```
```json
["The date you provided is not available for booking"]
```

On a sucessfull result (so the booking is confirmed), an HTTP 200 ok result is returned and in the body of the response the booking id which will be used for managing porpouses later.

#### Modifying bookings
```
PUT http://localhost:8080/api/campsites/1/bookings/{id}
```
Where *{id}* is the id provided on the sucessful response when creating the booking

Example body:
```json
{
        "fullName": "Another modified One",
        "email": "dusty@somedomain.com",
        "checkInDateTime": "2019-02-28T12:00:00",
        "checkoutDateTime": "2019-03-02T12:00:00"
}
```
#### Deleting bookings
```
DELETE http://localhost:8080/api/campsites/1/bookings/{id}
```
Where *{id}* is the id provided on the sucessful response when creating the booking

On suscessful DELETE, a 200 (ok) Status Code will be returned, on Error a 500 Internal server Error and finally, if the indicated id doesn't exist, a 404 Not found will be returned.


