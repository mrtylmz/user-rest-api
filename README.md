# user-rest-api
Sample basic security rest services with spring boot.

In presentation layer: Rest Controllers, Request Models,DTOs
In service layer: Business Logic, DTOs
In data layer: Entities, Jpa, Mysql, DTOs


All layers protected by spring boot security.
Security Token handlers with JSON web token.

Object transfers handle with ModelMapper.http://modelmapper.org/
Web security handle with Spring Boot Security.
Data input/output format accepts XML and Json.
Build with Maven.
Web tokens handle with Json Web Tokens.(io.jsonwebtoken)

You can CRUD operationals between two relational tables(users and addresses)
You have to change application.properties file for your specific configuration(database, port, log level
First create user, then go to login in users/login page. Take your token.
Then you can make crud operations.

