Paginated Records
=

Instalación
- 

1. Descargar el repositorio.
2. Modificar el archivo settings.xml, agregar las credenciales del servidor Tomcat 8.5 donde se desea desplegar.
3. Modificar la sección project / build / plugins / plugin / configuration / url del plugin tomcat7-maven-plugin y colocar el URL del servidor Tomcat 8.5 donde se desea desplegar.
4. Cambiar a la carpeta donde se encuentra el archivo settings.xml (raíz del repositorio recién descargado).
5. Ejecutar el comando <code>mvn --settings settings.xml clean install tomcat7:deploy</code>

El proyecto estará accesible en la dirección suministrada, bajo el contexto <code>paginated-records</code>
