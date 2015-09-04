Getting started
==================================
 
Start the server:
------
  1. Start the server with Maven with "mvn clean package cargo:run"
  2. Open your browser http://localhost:8080/infomodelrepository/  

REST API:
----------------
####Full-text search of models

HTTP GET: /rest/model/query=\<search expression\>

HTTP Response: List of ModelResources

####Get a single model 

HTTP GET: /rest/model/\<namespace\>/\<name\>/\<version\>

HTTP Response: ModelResource

####Download model content

HTTP GET: rest/model/file/\<namespace\>/\<name\>/\<version\>

HTTP Response : file as application/octet-stream

####Upload of model (for validation)

HTTP POST: multipart/form-data file

HTTP Response : UploadResult which contains handleId needed for checkin
  
####Check-in of model

HTTP PUT: rest/model/\<handleId\>

HTTP Response : 200

