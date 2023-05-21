## Solita Dev Academy Pre-requisite task 2023
A full-stack program that implements a back-end with Spring Boot and front through React.
The goal was to read data from .csv files into a database and create a frontend for displaying it.


Technical requirements:

MySQL server 8.0+ (Was tested as working on 8.3.2)
(Can also run other relational database solutions but have to figure out the Java connector dependency yourself)

Java SDK 17

Maven

NPM 

# About MySQL

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

## Backend

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

  Navigate to the backend project root where the Maven pom.xml resides and in a terminal of your choice run:

  ```bash
  mvn package

  java -jar target/solita-backend-1.0-SNAPSHOT.jar solita.citybike.JourneyBackend

  ```
  
4: 

  Server should now be running on localhost port 8080. If the port 8080 is taken, add server.port = 8081 or whichever to the application.properties file.

  Make a post request to localhost:8080/journeys/populate to read the CSV data into the DB. 

  This process will unzip the .csv files and insert them into the DB. I suggest you grab a cup of coffee at this point because
  I have benchmarked it to run just under 10 minutes.


## Frontend

1:

  Navigate to the project root and then go to frontend/citybike and run:
  
  ```bash
  npm install
  
  npm start
  ```
  If the port 3001 is taken, open .env file in citybike and change the port attribute to your desire and run npm start again.
  
2:

  The frontend is now running on localhost:your_port, so open it in your browser and you should see the web application.
  
  
If you have any issues or questions about the software, please contact me through the details
left in the Academy application.

## Some thoughts

The first design decision I had to do was regarding the large sizes of the given .csv files.
The issue was that GitHub doesn't allow files over 50MB's, which the .csv's were exceeding.
So I had 3 choices:

1:
  Upload the files to another host and then fetch them inside the application. I wasn't fond of this idea because it felt
  like I was just overcomplicating the issue and relying on another external service, and also possibly slowing down the data
  initialization.
  
2:
  Read the files locally to a DB and create a SQL dump file from the inserted data. I also didn't like this approach because I
  wasn't sure if the large .csv files were a "test" to see how would you go about inserting large amounts of data.
    
3:
  Zip the data, allowing it to be hosted on the GitHub repo. Unzipping is a cheap computational process and only took about ten seconds
  per file.
  
**About bulk inserting the data:**
When I first started the data insertion, it took nearly 45 minutes which is unacceptable.
So I took some steps to increase the insertion efficiency:

  - First, insert in batches so we reduce total amount of queries massively. Testing different batch sizes,
  I found the best performance at 25 000. There might be better parameters, but this worked well enough.
  
  - Secondly, caching a needed entity that was being queried for each saved object.
    Everytime we wanted to save Object A we needed to query for Object B to embed it, resulting
    in a wild amount of DB lookups. Caching eliminated this completely while not avoiding performance, as the
    cached table was rather small (400 rows).
    
  - Third, using a sequence table for generating the ID for the object being saved. Normally with GENERATIONTYPE.IDENTITY you run into an issue
    with batch statements that Spring is not aware of the saveable objects ID before saving. This means that bulk inserts can become as slow as single inserts.
    We combat this with a sequence table that holds the last known ID of an inserted record. This will be incremented once after a bulk insert.
    So if the ID in the table is 12500, Spring will assign the entities 125001, 125002 and so on. Then it will just once update the sequence table.
    After for example inserting 500 records, the sequence value would be 13000. 
    
  - Fourth, very simply, just allowing MySQL to use more resources. I am pretty sure the default settings of MySQL on installation are for systems from 2008.
    The memory usage allowances are very pitiful and by increasing these, I saw performance increasing.
    
    
# About the frontend
 
If just by looking at the frontend it wasn't clear enough, I am quite bad at designing anything that looks like it could be used by humans.
Following a design is very much preferred as I can just leave the artistic side to more creative people.
Also, this project really motivated me to want to get better with React and I want to be able to create more responsive and modular solutions.

# About tests

I rarely get frustrated when coding but spending a whole day trying to figure out why my test class Spring Bean injections
were not working really tested me. I originally wanted to run the tests through Surefire whenever the project was built, and well,
Surefire worked but my test classes didn't. I could not for the life of me get JUnit to pick up my Repo/Service beans and ultimately,
I had to give up to save my last slivers of sanity.


# There is code that could be done in a better way

The one thing that I constantly run into in my own projects is the following:
I need to do X. I can achieve it by doing Y, but doing Y is probably very bad due to Z.
And I try to browse stack threads and ask from different communities which is the "correct" way
of achieving it but more often than not, I am left without a clear answer.
Which leads to, as always, this project probably having some subpar solutions, from which I would
love to hear feedback from.

All in all, I really enjoyed this project and got to do different kinds of stuff, and got better at doing that stuff.

  
