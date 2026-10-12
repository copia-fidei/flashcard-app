A web application where you can import and view collections of flashcards.
The imported collections are stored inside a PostgreSQL database.
The application runs on a Karaf application server.

Supported file formats that can be imported:
- CSV
- APKG (file format from the flashcards application Anki)

Used technologies:
- OSGi: application comprises OSGi bundles
- Gradle + bnd: build an OSGi application bundle
- Karaf application server (Version 4.4.8): where the application runs
- REST-API (JAX-RS with the Jersey implementation): serves websites
- Mustache: HTML templates
- PostgreSQL database: store the imported collections
    - (Other databases might work but have not been tested.)
- JDBC
    - for SQLite databases inside the imported APKG files
    - for PostgreSQL
- Java 21
    - required for the application
    - Karaf needs to run on the same version as the application
    - Karaf cannot run on Java 25 as the SecurityManger has been deprecated


# How to run (for developers)
If you want to clone and run this application, follow these steps:

## Install Java 21
Higher Java versions are not supported by Karaf atm.

## Install Karaf
https://karaf.apache.org/get-started.html
## Setup PostgreSQL
Install PostgreSQL:
https://www.postgresql.org/download
Create roles:
You can use the admin role or create another one. 
Make sure that you can log in via password.
Role and password will be used for the DataSource below.

# Create database and tables
Log in to that role and execute the script in /src/main/resources/init.sql

In theory other databases can work too, but this has not been tested.
## Install Karaf
https://karaf.apache.org/get-started.html
## Setup Karaf
Navigate to ./bin and invoke ./karaf. This opens the Karaf console. Type:

feature:repo-add mvn:no.priv.bang.karaf/jersey/LATEST/xml/features
feature:install jersey-karaf-feature
feature:install jdbc
bundle:install mvn:org.glassfish.jersey.media/jersey-media-multipart/2.47
bundle:install mvn:org.glassfish.jersey.ext/jersey-mvc/2.47
bundle:install mvn:org.glassfish.jersey.ext/jersey-mvc-mustache/2.47
bundle:install wrap:mvn:com.github.spullara.mustache.java/compiler/0.9.14
bundle:install mvn:org.jvnet.mimepull/mimepull/1.9.15
feature:install war
feature:install http
feature:install jndi
feature:install pax-jdbc-sqlite

feature:install pax-jdbc-postgresql
This feature is needed to connect to PostgreSQL. 
For a different database, install the corresponding feature. See:
https://karaf.apache.org/manual/latest/#_datasources_jdbc

Create a DataSource (still in the Karaf console):

E.g., given these PostgresSQL credentials:
username = postgres
password = pg
url = jdbc:postgresql://localhost:5432/collections
datasource name = collections 

The command would look like:
jdbc:ds-create -dn "PostgreSQL JDBC Driver" -u postgres -p pg -url jdbc:postgresql://localhost:5432/collections -dc org.postgresql.Driver collections

Change those values to match your database.
You can test if the connection works with jdbc:ds-info collections.

More info under
https://karaf.apache.org/manual/latest/#_datasources_jdbc
## Deploy this project
Clone this project.
Open build.gradle and change the karafDeployPath to point to your Karaf's /deploy folder.
Open a console in the root folder of this project that you just cloned.
Execute `./gradlew deploy_to_Karaf`
This will build this project and copy the resulting JAR into the /deploy folder.

## Run the application
Open your browser at
http://localhost:8181/flashcards/collections

You can open collections that contain flashcards from the main page and import CSV and APKG files.
Here you can download decks to upload into your application:
https://ankiweb.net/shared/decks

Enjoy!


## Troubleshooting
If hot deploying does not work try restarting Karaf: Ctrl-D and then $KARAF_HOME/bin/start.

log:display
is the most useful command to see errors.

### Via web console
`feature:install webconsole`
Open
http://localhost:8181/system/console
(user=karaf, password=karaf)
in the browser to see all the bundles and if they are all resolved correctly.
If there are still resolve conflicts create an issue on Github. Some bundles or features might still be missing.

For reference these features are installed on a Karaf instance where the application works:
Name                 │ Version          │ Required 
─────────────────────┼──────────────────┼──────────
jndi                 │ 4.4.8            │ x        
jdbc                 │ 4.4.8            │ x        
jersey-karaf-feature │ 1.9.11           │ x        
jersey-hk2           │ 1.9.11           │          
hk2-karaf-feature    │ 1.9.11           │          
pax-web-core         │ 8.0.33           │          
pax-web-websockets   │ 8.0.33           │          
pax-web-jetty        │ 9.4.57.v20241219 │          
pax-web-http-jetty   │ 8.0.33           │          
pax-web-jsp          │ 8.0.33           │          
pax-web-war          │ 8.0.33           │          
aries-proxy          │ 4.4.8            │          
feature              │ 4.4.8            │ x        
shell                │ 4.4.8            │ x        
deployer             │ 4.4.8            │ x        
bundle               │ 4.4.8            │ x        
config               │ 4.4.8            │ x        
diagnostic           │ 4.4.8            │ x        
instance             │ 4.4.8            │ x        
jaas                 │ 4.4.8            │ x        
log                  │ 4.4.8            │ x        
package              │ 4.4.8            │ x        
service              │ 4.4.8            │ x        
system               │ 4.4.8            │ x        
pax-web-http         │ 8.0.33           │          
http                 │ 8.0.33           │ x        
pax-web-http-war     │ 4.4.8            │          
war                  │ 4.4.8            │ x        
kar                  │ 4.4.8            │ x        
ssh                  │ 4.4.8            │ x        
management           │ 4.4.8            │ x        
eventadmin           │ 4.4.8            │ x        
pax-url-wrap         │ 2.6.17           │          
wrap                 │ 2.6.17           │ x        
pax-jdbc-spec        │ 1.5.7            │          
pax-jdbc             │ 1.5.7            │          
pax-jdbc-config      │ 1.5.7            │          
pax-jdbc-postgresql  │ 1.5.7            │ x        
pax-jdbc-sqlite      │ 1.5.7            │ x        

And these are the bundles:


Version            │ Name
───────────────────┼───────────────────────────────────────────────────────
4.4.8              │ Apache Karaf :: OSGi Services :: Event
1.3.5              │ Jakarta Annotations API
2.1.6              │ jakarta.ws.rs-api
3.25.0.GA          │ Javassist
2.6.1              │ HK2 API module
2.6.1              │ aopalliance version 1.0 repackaged as a module
2.6.1              │ javax.inject:1 as OSGi bundle
2.6.1              │ ServiceLocator Default Implementation
1.0.3              │ OSGi resource locator
2.6.1              │ HK2 Implementation Utilities
2.47.0             │ jersey-core-common
2.47.0             │ jersey-inject-hk2
2.19.1             │ Jackson-annotations
2.19.1             │ Jackson-core
2.19.1             │ jackson-databind
2.19.1             │ Jackson module: Old JAXB Annotations (javax.xml.bind)
1.2.1              │ JavaBeans Activation Framework
1.2.1              │ JavaBeans Activation Framework API jar
2.0.2              │ Jakarta Bean Validation API
2.3.2              │ jakarta.xml.bind-api
2.47.0             │ jersey-container-servlet
2.47.0             │ jersey-container-servlet-core
2.47.0             │ jersey-core-client
2.47.0             │ jersey-core-server
2.47.0             │ jersey-ext-entity-filtering
2.47.0             │ jersey-media-jaxb
2.47.0             │ jersey-media-json-jackson
2.0.0.SP1          │ CDI APIs
1.2.2              │ javax.interceptor API
1.2                │ javax.transaction API
1.9.3.1            │ Apache ServiceMix :: Bundles :: jasypt
1.5.7              │ OPS4J Pax JDBC Generic Driver Extender
1.5.7              │ OPS4J Pax JDBC Config
1.5.7              │ OPS4J Pax JDBC Pooling Support Base
1.1.0.202212101352 │ org.osgi:org.osgi.service.jdbc
4.4.8              │ Apache Karaf :: JDBC :: Core
1.5.7              │ OPS4J Pax JDBC SQLite Driver Adapter
3.45.2.0           │ SQLite JDBC
2.47.0             │ jersey-media-multipart
2.47.0             │ jersey-ext-mvc
2.47.0             │ jersey-ext-mvc-mustache
0                  │ wrap_mvn_com.github.spullara.mustache.java_compiler_0.9.14
1.9.15             │ MIME streaming extension
42.7.3             │ PostgreSQL JDBC Driver
1.0.0              │ flashcard-app