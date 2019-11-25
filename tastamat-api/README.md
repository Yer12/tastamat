# Tastamat Mobile Application Backend

This application serves Tastamat App.

## Build

Run this command in the project root:

```
$ ./gradlew build
```

This will create `./build/libs/tastamat-api-fat.jar` and `./build/libs/tastamat-api-fat.jar` files to be run as project backend.

## Run

Run this command to launch the application locally:

```
$ java -jar tastamat-api-fat.jar run kz.zx.api.app.AppVerticle --launcher-class=kz.toolpar.bootstrap.AppLauncher --conf=conf/app.json
```

## Docker

To build an image use:


```
$ docker build -t registry.gitlab.com/toolpar/tastamat/tastamat-api:dev .
```
