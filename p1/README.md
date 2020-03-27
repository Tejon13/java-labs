# Práctica 1: Implementación de un Servidor Web

Práctica realizada por Íker García Calviño <<iker.gcalvino@udc.es>>.

## Resumen del archivo .properties

```bash
# Puerto en el que escucha el servidor.
PORT=5000

# Nombre del fichero a buscar por defecto en caso de recibir una petición a un directorio.
DIRECTORY_INDEX=p1/resources/archives/index.html

# Directorio raíz del servidor web, en donde se ubicarán las páginas web.
DIRECTORY=p1/resources

# Directiva que se aplicará cuando se reciba una petición a un directorio.
ALLOW=TRUE
```

## Uso

- No hay que pasar ningún parámetro a WebServer.java.

- El directorio inicial debe de ser la carpeta /java-labs.

```java
System.getProperty("user.dir"); //NetBeans IDE 8.2's default directory.
```

## Correción

Están implementadas las 2 iteraciones.