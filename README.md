A web application for storing, importing, and viewing collections of flashcards.

# Used technologies
The application is packaged as a single OSGi bundle (a JAR file with extra manifest headers) 
and deployed on the Apache Karaf 4.4.8 application server.
All dependencies are on the server.

REST is used (JAX-RS specification, Jersey implementation) to serve HTML websites 
generated from Mustace templates.

Imported flashcard collections are stored in a PostgreSQL database.
(Other databases might work but have not been tested.)
JDBC is used for database access.

The application is developed with Java 21.
In addition, Java 21 must also be installed on the system to run Karaf.
Higher Java versions (e.g. Java 25) are not compatible with Karaf due to the deprecation of the SecurityManger.

Gradle and the bnd plugin are used to build the OSGi bundle.


# How to run (for developers)
To build and run the application locally, follow these steps:

1. Install Java 21. Required to run Karaf.
Higher Java versions are not supported by Karaf atm.

2. Setup PostgreSQL  
   - Install PostgreSQL:
   https://www.postgresql.org/download
   - Create roles.
   You can use the admin role or create another one.
   Make sure you can log in using a password, this is required for the DataSource (see below).
   - Create database and tables.  
   Log in to the role that should own the database and execute the script in /src/main/resources/init.sql  

3. Install Karaf:
https://karaf.apache.org/get-started.html

4. Install dependencies on Karaf.  
Navigate to ./bin and invoke ./karaf to open the Karaf console. Type:
   - feature:repo-add mvn:no.priv.bang.karaf/jersey/LATEST/xml/features
   - feature:install jersey-karaf-feature
   - feature:install jdbc
   - bundle:install mvn:org.glassfish.jersey.media/jersey-media-multipart/2.47
   - bundle:install mvn:org.glassfish.jersey.ext/jersey-mvc/2.47
   - bundle:install mvn:org.glassfish.jersey.ext/jersey-mvc-mustache/2.47
   - bundle:install wrap:mvn:com.github.spullara.mustache.java/compiler/0.9.14
   - bundle:install mvn:org.jvnet.mimepull/mimepull/1.9.15
   - bundle:install mvn:de.siegmar/fastcsv/4.4.0
   - bundle:install mvn:com.univocity/univocity-parsers/2.9.1
   - feature:install war
   - feature:install http
   - feature:install jndi
   - feature:install pax-jdbc-sqlite  
   This is not used to store flashcards, but to inspect the database inside APKG files.
   - feature:install pax-jdbc-postgresql  
   This feature is needed to connect to PostgreSQL. 
   For a different database, install the corresponding feature. See:
   https://karaf.apache.org/manual/latest/#_datasources_jdbc

5. Create a DataSource (still in the Karaf console).  
   - E.g., given these credentials:
      - username = postgres
      - password = pg
      - url = jdbc:postgresql://localhost:5432/collections
      - datasource name = collections 
   - The resulting command is:  
   `jdbc:ds-create -dn "PostgreSQL JDBC Driver" -u postgres -p pg -url jdbc:postgresql://localhost:5432/collections -dc org.postgresql.Driver collections`  
   Change those values to match your database.
   You can test if the connection works with `jdbc:ds-info collections`.  
   More info under
   https://karaf.apache.org/manual/latest/#_datasources_jdbc

6. Deploy this project.  
Clone this project.  
Open build.gradle to modify the "deploy_to_Karaf task", make sure to point to your Karaf's /deploy folder.
Execute `./gradlew deploy_to_Karaf`
in the root directory.
This builds the project and copies the resulting JAR directly into the /deploy folder.

7. Run the application.  
Open your browser at:
http://localhost:8181/flashcards/collections  
View your flashcard collections or import new ones from CSV and APKG files.  
You can find APKG files at: 
https://ankiweb.net/shared/decks

## Troubleshooting
If hot deploying does not work, try restarting Karaf: Ctrl-D and then $KARAF_HOME/bin/start.

### In the Karaf console
`log:display`  
is the most useful command to see errors.

### Via web console
`feature:install webconsole`  
Open
http://localhost:8181/system/console
(user=karaf, password=karaf)
in the browser to see all the bundles and if they are all resolved correctly.
              
## Bundles + features list of a working application
If your bundles fail to resolve, compare your lists of bundles and features on your Karaf server with the following.  

`feature:list`  

| Name                 | Version          | Required |
| -------------------- | ---------------- | -------- |
| jndi                 | 4.4.8            | x        |
| jdbc                 | 4.4.8            | x        |
| jersey-karaf-feature | 1.9.11           | x        |
| jersey-hk2           | 1.9.11           |          |
| hk2-karaf-feature    | 1.9.11           |          |
| pax-web-core         | 8.0.33           |          |
| pax-web-websockets   | 8.0.33           |          |
| pax-web-jetty        | 9.4.57.v20241219 |          |
| pax-web-http-jetty   | 8.0.33           |          |
| pax-web-jsp          | 8.0.33           |          |
| pax-web-war          | 8.0.33           |          |
| aries-proxy          | 4.4.8            |          |
| feature              | 4.4.8            | x        |
| shell                | 4.4.8            | x        |
| deployer             | 4.4.8            | x        |
| bundle               | 4.4.8            | x        |
| config               | 4.4.8            | x        |
| diagnostic           | 4.4.8            | x        |
| instance             | 4.4.8            | x        |
| jaas                 | 4.4.8            | x        |
| log                  | 4.4.8            | x        |
| package              | 4.4.8            | x        |
| service              | 4.4.8            | x        |
| system               | 4.4.8            | x        |
| pax-web-http         | 8.0.33           |          |
| http                 | 8.0.33           | x        |
| pax-web-http-war     | 4.4.8            |          |
| war                  | 4.4.8            | x        |
| kar                  | 4.4.8            | x        |
| ssh                  | 4.4.8            | x        |
| management           | 4.4.8            | x        |
| eventadmin           | 4.4.8            | x        |
| pax-url-wrap         | 2.6.17           |          |
| wrap                 | 2.6.17           | x        |
| pax-jdbc-spec        | 1.5.7            |          |
| pax-jdbc             | 1.5.7            |          |
| pax-jdbc-config      | 1.5.7            |          |
| pax-jdbc-postgresql  | 1.5.7            | x        |
| pax-jdbc-sqlite      | 1.5.7            | x        |

`bundle:list`  


| Name                                                       | Version            |
| ---------------------------------------------------------- | ------------------ |
| Apache Karaf :: OSGi Services :: Event                     | 4.4.8              |
| Jakarta Annotations API                                    | 1.3.5              |
| jakarta.ws.rs-api                                          | 2.1.6              |
| Javassist                                                  | 3.25.0.GA          |
| HK2 API module                                             | 2.6.1              |
| aopalliance version 1.0 repackaged as a module             | 2.6.1              |
| javax.inject:1 as OSGi bundle                              | 2.6.1              |
| ServiceLocator Default Implementation                      | 2.6.1              |
| OSGi resource locator                                      | 1.0.3              |
| HK2 Implementation Utilities                               | 2.6.1              |
| jersey-core-common                                         | 2.47.0             |
| jersey-inject-hk2                                          | 2.47.0             |
| Jackson-annotations                                        | 2.19.1             |
| Jackson-core                                               | 2.19.1             |
| jackson-databind                                           | 2.19.1             |
| Jackson module: Old JAXB Annotations (javax.xml.bind)      | 2.19.1             |
| JavaBeans Activation Framework                             | 1.2.1              |
| JavaBeans Activation Framework API jar                     | 1.2.1              |
| Jakarta Bean Validation API                                | 2.0.2              |
| jakarta.xml.bind-api                                       | 2.3.2              |
| jersey-container-servlet                                   | 2.47.0             |
| jersey-container-servlet-core                              | 2.47.0             |
| jersey-core-client                                         | 2.47.0             |
| jersey-core-server                                         | 2.47.0             |
| jersey-ext-entity-filtering                                | 2.47.0             |
| jersey-media-jaxb                                          | 2.47.0             |
| jersey-media-json-jackson                                  | 2.47.0             |
| CDI APIs                                                   | 2.0.0.SP1          |
| javax.interceptor API                                      | 1.2.2              |
| javax.transaction API                                      | 1.2                |
| Apache ServiceMix :: Bundles :: jasypt                     | 1.9.3.1            |
| OPS4J Pax JDBC Generic Driver Extender                     | 1.5.7              |
| OPS4J Pax JDBC Config                                      | 1.5.7              |
| OPS4J Pax JDBC Pooling Support Base                        | 1.5.7              |
| org.osgi:org.osgi.service.jdbc                             | 1.1.0.202212101352 |
| Apache Karaf :: JDBC :: Core                               | 4.4.8              |
| OPS4J Pax JDBC SQLite Driver Adapter                       | 1.5.7              |
| SQLite JDBC                                                | 3.45.2.0           |
| jersey-media-multipart                                     | 2.47.0             |
| jersey-ext-mvc                                             | 2.47.0             |
| jersey-ext-mvc-mustache                                    | 2.47.0             |
| wrap_mvn_com.github.spullara.mustache.java_compiler_0.9.14 | 0                  |
| MIME streaming extension                                   | 1.9.15             |
| PostgreSQL JDBC Driver                                     | 42.7.3             |
| flashcard-app                                              | 1.0.0              |
| de.siegmar.fastcsv                                         | 4.4.0              |
| univocity-parsers                                          | 2.9.1              |
