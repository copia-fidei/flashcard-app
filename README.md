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

## Install Karaf
https://karaf.apache.org/get-started.html
## Install Database
This project has only been tested with PostgreSQL
https://www.postgresql.org/download/
## Start Karaf in the console
https://karaf.apache.org/get-started.html
## Install these Karaf features

In Karaf console
feature:repo-add mvn:no.priv.bang.karaf/jersey/LATEST/xml/features
feature:install jersey-karaf-feature
feature:install jdbc
feature:install pax-jdbc-sqlite
bundle:install mvn:org.glassfish.jersey.media/jersey-media-multipart/2.47
bundle:install mvn:org.glassfish.jersey.ext/jersey-mvc/2.47
bundle:install mvn:org.glassfish.jersey.ext/jersey-mvc-mustache/2.47
bundle:install wrap:mvn:com.github.spullara.mustache.java/compiler/0.9.14
bundle:install mvn:org.glassfish.jersey.media/jersey-media-multipart/2.47
bundle:install mvn:org.jvnet.mimepull/mimepull/1.9.15

This feature is needed to read Anki's APKG file format:
feature:install pax-jdbc-postgresql
If you use a different database, install the corresponding feature. See:
https://karaf.apache.org/manual/latest/#_datasources_jdbc

feature:install war
feature:install http
feature:install jndi

Create the database and tables:
Run /src/main/resources/init.sql
Some commands inside init.sql are PostgreSQL specific.


Create a datasource (in Karaf console):
See documentation
https://karaf.apache.org/manual/latest/#_datasources_jdbc
under "jdbc:ds-create"

E.g., for these PostgresSQL credentials:
username = postgres
password = xxxc
url = jdbc:postgresql://localhost:5432/collections
datasource name = collections

The command would look like:
jdbc:ds-create -dn "PostgreSQL JDBC Driver" -u postgres -p xxx -url jdbc:postgresql://localhost:5432/collections -dc org.postgresql.Driver collections

## Deploy this project
Clone this project.
Open build.gradle and change the karafDeployPath to point to your Karaf installation.
Do not forget to point to the /deploy subfolder, not your Karaf installation's root folder.
The /deploy folder automatically deploys bundles that are inside.
Open a console in the root folder of this project that you just cloned.
Execute `./gradlew bundle`
This will build this project and put it and any necessary dependencies inside the /deploy folder.

## Run the application
Open your browser at
http://localhost:8181/flashcards/collections

You can open collections that contain flashcards from the main page and import CSV and APKG files.
Here you can download decks to upload into your application:
https://ankiweb.net/shared/decks

Enjoy!


## Troubleshooting
Sometimes live deploying does not work. Just restart Karaf. Ctrl-D and then $KARAF_HOME/bin/start again.

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
Name                             │ Version          │ Required
─────────────────────────────────┼──────────────────┼──────────
jndi                             │ 4.4.8            │ x
jdbc                             │ 4.4.8            │ x
jersey-karaf-feature             │ 1.9.11           │ x
karaf-servlet-example-annotation │ 4.5.0.SNAPSHOT   │ x
jersey-hk2                       │ 1.9.11           │
hk2-karaf-feature                │ 1.9.11           │
pax-web-core                     │ 8.0.33           │
pax-web-websockets               │ 8.0.33           │
pax-web-jetty                    │ 9.4.57.v20241219 │
pax-web-http-jetty               │ 8.0.33           │
pax-web-jsp                      │ 8.0.33           │
pax-web-whiteboard               │ 8.0.33           │
pax-web-war                      │ 8.0.33           │
pax-web-karaf                    │ 8.0.33           │ x
aries-proxy                      │ 4.4.8            │
feature                          │ 4.4.8            │ x
shell                            │ 4.4.8            │ x
deployer                         │ 4.4.8            │ x
bundle                           │ 4.4.8            │ x
config                           │ 4.4.8            │ x
diagnostic                       │ 4.4.8            │ x
instance                         │ 4.4.8            │ x
jaas                             │ 4.4.8            │ x
log                              │ 4.4.8            │ x
package                          │ 4.4.8            │ x
service                          │ 4.4.8            │ x
system                           │ 4.4.8            │ x
pax-web-http                     │ 8.0.33           │
http                             │ 8.0.33           │ x
pax-web-http-whiteboard          │ 8.0.33           │
http-whiteboard                  │ 8.0.33           │ x
pax-web-http-war                 │ 4.4.8            │
war                              │ 4.4.8            │ x
kar                              │ 4.4.8            │ x
webconsole                       │ 4.4.8            │ x
ssh                              │ 4.4.8            │ x
management                       │ 4.4.8            │ x
eventadmin                       │ 4.4.8            │ x
scr                              │ 4.4.8            │ x
pax-url-wrap                     │ 2.6.17           │
wrap                             │ 2.6.17           │ x
pax-jdbc-spec                    │ 1.5.7            │
pax-jdbc                         │ 1.5.7            │
pax-jdbc-config                  │ 1.5.7            │
pax-jdbc-postgresql              │ 1.5.7            │ x
pax-jdbc-sqlite                  │ 1.5.7            │ x

And these are the bundles:
Version             │ Name
────────────────────┼────────────────────────────────────────
 4.4.8              │ Apache Karaf :: OSGi Services :: Event
 0.0.0              │ scripts
 2.0.1.Final        │ Bean Validation API
 2.30.1             │ jersey-media-multipart
 1.9.11             │ MIME streaming extension
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
 0.9.14             │ compiler
 2.47.0             │ jersey-ext-mvc
 2.47.0             │ jersey-media-multipart
 2.47.0             │ jersey-ext-mvc-mustache
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
 42.7.3             │ PostgreSQL JDBC Driver
 0.0.0              │ flashcards