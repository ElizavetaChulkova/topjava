GET http://localhost:8080/topjava/rest/meals
[
{
"id": 100009,
"dateTime": "2020-01-31T20:00:00",
"description": "Ужин",
"calories": 510,
"excess": true
},
{
"id": 100008,
"dateTime": "2020-01-31T13:00:00",
"description": "Обед",
"calories": 1000,
"excess": true
},
{
"id": 100007,
"dateTime": "2020-01-31T10:00:00",
"description": "Завтрак",
"calories": 500,
"excess": true
},
{
"id": 100006,
"dateTime": "2020-01-31T00:00:00",
"description": "Еда на граничное значение",
"calories": 100,
"excess": true
},
{
"id": 100005,
"dateTime": "2020-01-30T20:00:00",
"description": "Ужин",
"calories": 500,
"excess": false
},
{
"id": 100004,
"dateTime": "2020-01-30T13:00:00",
"description": "Обед",
"calories": 1000,
"excess": false
},
{
"id": 100003,
"dateTime": "2020-01-30T10:00:00",
"description": "Завтрак",
"calories": 500,
"excess": false
}
]

GET http://localhost:8080/topjava/rest/meals/100003
{
"id": 100003,
"dateTime": "2020-01-30T10:00:00",
"description": "Завтрак",
"calories": 500,
"user": null
}

POST http://localhost:8080/topjava/rest/meals/100003
{
"id":100003,
"dateTime": "2020-01-30T10:00:00",
"description": "updated meal",
"calories": 500,
"user": null
}

POST http://localhost:8080/topjava/rest/meals
{
"dateTime": "2022-03-25T10:00:00",
"description": "created meal",
"calories": 333,
"user": null
}

DELETE http://localhost:8080/topjava/rest/meals/100003

GET http://localhost:8080/topjava/rest/meals/
    filter?startDate=2020-01-31&endDate=2020-01-31&startTime=07:00&endTime=21:00
[
{
"id": 100009,
"dateTime": "2020-01-31T20:00:00",
"description": "Ужин",
"calories": 510,
"excess": true
},
{
"id": 100008,
"dateTime": "2020-01-31T13:00:00",
"description": "Обед",
"calories": 1000,
"excess": true
},
{
"id": 100007,
"dateTime": "2020-01-31T10:00:00",
"description": "Завтрак",
"calories": 500,
"excess": true
}
]

GET http://localhost:8080/topjava/rest/admin/users/100000/with-meals
{
"id": 100000,
"name": "User",
"email": "user@yandex.ru",
"password": "password",
"enabled": true,
"registered": "2023-03-25T11:44:49.782+00:00",
"roles": [
"USER"
],
"caloriesPerDay": 2000,
"meals": [
{
"id": 100009,
"dateTime": "2020-01-31T20:00:00",
"description": "Ужин",
"calories": 510
},
{
"id": 100008,
"dateTime": "2020-01-31T13:00:00",
"description": "Обед",
"calories": 1000
},
{
"id": 100007,
"dateTime": "2020-01-31T10:00:00",
"description": "Завтрак",
"calories": 500
},
{
"id": 100006,
"dateTime": "2020-01-31T00:00:00",
"description": "Еда на граничное значение",
"calories": 100
},
{
"id": 100005,
"dateTime": "2020-01-30T20:00:00",
"description": "Ужин",
"calories": 500
},
{
"id": 100004,
"dateTime": "2020-01-30T13:00:00",
"description": "Обед",
"calories": 1000
},
{
"id": 100003,
"dateTime": "2020-01-30T10:00:00",
"description": "Завтрак",
"calories": 500
}
]
}

GET http://localhost:8080/topjava/rest/profile/with-meals
{
"id": 100000,
"name": "User",
"email": "user@yandex.ru",
"password": "password",
"enabled": true,
"registered": "2023-03-25T11:44:49.782+00:00",
"roles": [
"USER"
],
"caloriesPerDay": 2000,
"meals": [
{
"id": 100009,
"dateTime": "2020-01-31T20:00:00",
"description": "Ужин",
"calories": 510
},
{
"id": 100008,
"dateTime": "2020-01-31T13:00:00",
"description": "Обед",
"calories": 1000
},
{
"id": 100007,
"dateTime": "2020-01-31T10:00:00",
"description": "Завтрак",
"calories": 500
},
{
"id": 100006,
"dateTime": "2020-01-31T00:00:00",
"description": "Еда на граничное значение",
"calories": 100
},
{
"id": 100005,
"dateTime": "2020-01-30T20:00:00",
"description": "Ужин",
"calories": 500
},
{
"id": 100004,
"dateTime": "2020-01-30T13:00:00",
"description": "Обед",
"calories": 1000
},
{
"id": 100003,
"dateTime": "2020-01-30T10:00:00",
"description": "Завтрак",
"calories": 500
}
]
}