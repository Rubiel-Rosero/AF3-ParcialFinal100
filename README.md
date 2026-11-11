# Sistema de Gestion Academica y Planificacion de Rutas de Aprendizaje

Proyecto Final - Programacion III (Estructuras de Datos)

## Requisitos
- JDK 17 o superior

## Estructura
```
src/
  Main.java        Menu principal
  modelo/          Clases del dominio (Persona, Estudiante, Materia, Aula...)
  estructuras/     Estructuras propias (ListaEnlazada, Pila, Cola)
  excepciones/     Excepciones personalizadas
  util/            Utilidades (lectura de consola, CSV)
  pruebas/         Pruebas por bloque
datos/             Archivos CSV de ejemplo
docs/              UML, manual de usuario, justificacion de estructuras
```

## Compilar y ejecutar (desde la raiz del proyecto)
```
javac -d out $(find src -name "*.java")
java -cp out Main
```
En Windows (PowerShell):
```
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out Main
```
