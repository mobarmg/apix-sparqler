Api-X Sparqler
===================================


### Image on Docker Hub
`pandorasystems/exts-sparqler`
-----------------
[![](https://images.microbadger.com/badges/image/pandorasystems/exts-sparqler.svg)](https://microbadger.com/images/pandorasystems/exts-sparqler "pandorasystems/exts-sparqler")[![](https://images.microbadger.com/badges/version/pandorasystems/exts-sparqler.svg)](https://microbadger.com/images/pandorasystems/exts-sparqler "pandorasystems/exts-sparqler")

This contains a SPARQL result serializer.
This is an OSGi service that extends the functionality of a [Fedora4](https://wiki.duraspace.org/display/FF/Fedora+Repository+Home) repository.

Extensions
----------
* [`exts-sparqler`](exts-sparqler): Queries a triplestore and serializes a JSON-LD compacted response.

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