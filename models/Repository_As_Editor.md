# Using the Vorto Repository as local model editor
- Clone the Github repository: `git clone https://github.com/eclipse/vorto.git`
- Navigate to docker folder `cd vorto/docker/`
- Install docker-compose `sudo apt install docker-compose`
- Edit vorto-variables.env file
  - Set your Github user as admin: `ADMIN_USER=<github username>`
  - Set your Github OAuth client ID: `GITHUB_CLIENT_ID=<github oauth2 client id>`
  - Set your Github OAuth client secret: `GITHUB_CLIENT_SECRET=<github oauth2 client secret>`
- Start the docker containers: `docker-compose up`
- Open your browser and go to http://localhost:8080/#/
- Login with Github
- Create your namespace
- Create or import your model
- Edit your model
- Release, approve and publish the model
- With any REST client you can retrieve:
  - JSON representation: `GET http://localhost:8080/api/v1/models/<Model ID>/content`
  - Vortolang: `GET http://localhost:8080/api/v1/models/com.bosch.test:Testmodel:1.0.0/file`
  - Links: `GET http://localhost:8080/api/v1/attachments/com.bosch.test:Testmodel:1.0.0/links`
  - Images and other attachments: `GET http://localhost:8080/api/v1/attachments/com.bosch.test:Testmodel:1.0.0/`
- Stop the docker containers    

## Troubleshooting

If you receive the following error message when trying to start the repository:

`[ERROR] [Entrypoint]: MYSQL_USER="root", MYSQL_USER and MYSQL_PASSWORD are for configuring a regular user and cannot be used for the root user `

comment or remove the line ``MYSQL_USER`` in `vorto-variables.env`
