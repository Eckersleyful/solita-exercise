# Solita Dev Academy Pre-requisite task 2023
A full-stack program that implements a back-end with Spring Boot and front through React.
The goal was to read data from .csv files into a database and create a frontend for displaying it.




Technical requirements:

MySQL server 8.0+ (Was tested as working on 8.3.2)
(Can also run other relational database solutions but have to figure out the Java connector dependency yourself)

Java SDK 17

Maven

NPM 

#About MySQL

If you are running the default installation of MySQL you should know that the default
values for memory usage parameters are very conservative. 

As we are inserting over 3 million rows in a bulk, I would highly suggest you to navigate
to your my.ini file of your MySQL server and changing following attributes:

myisam_sort_buffer_size=127M

key_buffer_size=2G

read_buffer_size=128K

read_rnd_buffer_size=256K

innodb_log_buffer_size=32M

innodb_buffer_pool_size=4G

join_buffer_size=512K

max_allowed_packet=256M

sort_buffer_size=256K

NOTE: These are for 16GB RAM system, double/halve according to your own system specs or try even
greedier parameters.

#Backend

1: 

  Clone the repository and navigate to backend\src\main\resources and open application.properties
  Change spring.datasource.url to where ever your MySQL is running on, in my case it is:
  jdbc:mysql://localhost:3306/solita
  
  Where solita is the name of your connection.
  Keep the rest of the url parameters in their place.

2: 

  Still inside the application.properties, change spring.datasource.username and -.password to your own. 
  Make sure that user has create and write permissions. Currently just generic admin/admin.

3: 

  Navigate to the project root where the Maven pom.xml resides and in a terminal of your choice run:

  ```
  mvn package

  java -jar target/solita-backend-1.0-SNAPSHOT.jar solita.citybike.JourneyBackend

  ```
  
4: 

  Server should now be running on localhost port 8080. If the port 8080 is taken, add server.port = 8081 or whichever to the application.properties file.

  Make a post request to localhost:8080/journeys/populate to read the CSV data into the DB. 

  This process will unzip the .csv files and insert them into the DB. I suggest you grab a cup of coffee at this point because
  I have benchmarked it to run just under 10 minutes.


#Frontend

1:

  Navigate to the project root and then go to frontend/citybike and run:
  
  ```
  npm install
  
  npm start
  ```
  If the port 3001 is taken, open .env file in citybike and change the port attribute to your desire and run npm start again.
  
2:

  The frontend is now running on localhost:your_port so open it in your browser and you should see the web application.
  
