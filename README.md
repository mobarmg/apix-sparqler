APIX-SPARQLER
===================================

An API-X extension for serializing SPARQL Results.

Building
--------
Create OSGI bundles
```sh 
    $ gradle build
```
Copy bundles from local Maven repository to Docker Build directory
```sh      
    $ gradle copyTask
```
Build Docker image
```sh 
    $ gradle docker
```
Execute Docker Compose
 ```sh     
    $ docker-compose up
  ```   
Creating Test Data
----------------- 
See [modeller](https://github.com/pan-dora/modeller)    

Check Sparqler Endpoint
-----------------

```sh    
    $ curl -sS 'http://localhost:9104/sparqler?type=manifest&node=http://localhost:8080/fcrepo/rest/collection/some/node/manifest'
```