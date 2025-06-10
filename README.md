# testLogin
EndoPoint

http://localhost:8080/api/users/sign-up (POST)

raw / json
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "a2asfGfdfdf4",
    "phones": [
        {
            "number": 12345678,
            "citycode": 1,
            "contrycode": "57"
        }
    ]
}

http://localhost:8080/api/users/login (POST)
en la pestaña de Authorization en el desplegable de Auth type se debe seleccionar Bearer Token y se debe copiar el tocken de la consulta anterior para consultar

El token anterior le di un tiempo elevado de expiracion, para ser mas facil las consultas y validar que el token cambia segun el uso asi solo se le da a consultar y copiar el token nueco vada ves
