# ShareCare
App de solicitud/oferta de servicio de cuidados a domicilio de niños y ancianos. API spring-boot y app Android en Kotlin.

Proyecto realizado en el marco de la Fase de Prácticas del CFGS "Desarrollo de Aplicaciones Multiplataforma" con la normativa aplicada por el COVID-19.

El Api se encuentra desplegada en heroku.

## Usuarios
    * username = usuario
    * password = usuario

Hay toda una batería de usuarios creados para poder interactuar entre ellos.

EN EL CASO DE PROBAR LA APLICACIÓN EN LOCAL HAY QUE TENER EN CUENTA QUE LOS DATOS DE PRUEBA USAN IMÁGENES PREVIAMENTE SUBIDAS A MI CUENTA DE IMGUR Y, LÓGICAMENTE, NO ESTARÁN DISPONIBLES.

## ¿Cuáles son las funcionalidades de la aplicación?

* Un usuario, al registrarse, indicará si ofrece el servicio de cuidado de personas (niños, ancianos, etc) a domicilio y el precio que cobra por hora. En el caso de no proporcionar esos datos (no ofrece servicio), no aparecerá en la lista de personas que ofrecen servicios.

* Una vez logueado podremos:

    * Ver un listado con todas las personas que ofrecen servicios y buscar por localidad.
    * Ver el detalle de los datos de una persona de la que se quiera requerir sus servicios y, mediante un botón en el detalle de esa       persona, enviar una solicitud de servicios requeridos con un comentario explicando lo que se desee.
    * Ver un listado de todas las solicitudes de servicios que hemos enviado y teniendo la posibilidad de modificar el comentario que se adjuntó o de eliminar la solicitud.
    * En el caso de que ofrezcamos el servicio de cuidados a domicilio, podremos también acceder a un listado de las solicitudes recibidas, con la posibilidad de eliminar las que no nos interesen.
    
* Un usuario podrá editar sus datos de perfil, pudiendo dejar de ofrecer los servicios en cualquier momento (o empezar a hacerlo).

* Cualquier usuario, ofrezca o no el servicio, podrá requerir el servicio de otra persona.

adjunto una [colección de peticiones de postman](https://github.com/jallamas/ShareCare/blob/master/ShareCare.postman_collection.json) con las que poder probar el API.

## postgreSQL

* En el Api:
   
   * Se incluye un fichero docker-compose para levantar un contenedor de postgresql (docker-compose up -d)
   
   *En el fichero aplication.properties se incluyen las instrucciones necesarias. La url de la base de datos contiene la IP con la que está configurado el Docker Toolbox.
  
 * En la aplicación Android:
   
   * Se incluye en las constantes las url local y de heroku (con una comentada)
