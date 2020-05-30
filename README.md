# ShareCare
App de solicitud/oferta de servicio de cuidados a domicilio de niños y ancianos. API spring-boot y app Android en Kotlin

## Usuarios
    * username = usuario
    * password = usuario

Hay toda una batería de usuarios creados para poder interactuar entre ellos.

## ¿Cuáles son las funcionalidades de la aplicación?

* Un usuario, al registrarse, indicará si ofrece el servicio de cuidado de personas (niños, ancianos, etc) a domicilio y el precio que cobra por hora. En el caso de no proporcionar esos datos (no ofrece servicio), no aparecerá en la lista de personas que ofrecen servicios.

* Una vez logueado podremos:

    * Ver un listado con todas las personas que ofrecen servicios y buscar por localidad.
    * Ver el detalle de los datos de una persona de la que se quiera requerir sus servicios y, mediante un botón en el detalle de esa       persona, enviar una solicitud de servicios requeridos con un comentario explicando lo que se desee.
    * Ver un listado de todas las solicitudes de servicios que hemos enviado y teniendo la posibilidad de modificar el comentario que se adjuntó o de eliminar la solicitud.
    * En el caso de que ofrezcamos el servicio de cuidados a domicilio, podremos también acceder a un listado de las solicitudes recibidas, con la posibilidad de eliminar las que no nos interesen.
    
* Un usuario podrá editar sus datos de perfil, pudiendo dejar de ofrecer los servicios en cualquier momento (o empezar a hacerlo).

* Cualquier usuario, ofrezca o no el servicio, podrá requerir el servicio de otra persona.

adjunto una [colección de peticiones de postman]() con las que poder probar el API.
