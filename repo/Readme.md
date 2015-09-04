Getting started
==================================
 
Start the server:
------
  1. Start the server with Maven with "mvn clean package cargo:run"
  2. Open your browser http://localhost:8080/infomodelrepository/  

REST API:
----------------
1. Full-text search of models
HTTP GET: /rest/model/query=<search expression>
HTTP Response: List of ModelResources

2. Get a single model: 
HTTP GET: /rest/model/<namespace>/<name>/<version>
HTTP Response: ModelResource

3. Download model content
HTTP GET: rest/model/file/<namespace>/<name>/<version>
HTTP Response : file as application/octet-stream

4. Upload of model (for validation)
HTTP POST: multipart/form-data file
HTTP Response : UploadResult which contains handleId needed for checkin
  
5. Check-in of model
HTTP PUT: rest/model/<handleId>
HTTP Response : 200
