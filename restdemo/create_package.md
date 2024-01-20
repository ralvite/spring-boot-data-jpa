
### modificar el pom.xml

```
<packaging>war</packaging>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>


<build>
        <finalName>${artifactId}</finalName>


```

### modificar la clase principal
Se modifica RestdemoApplication.java con extends

`public class RestdemoApplication extends SpringBootServletInitializer`


### crear paquete war
```
cd my_project
.\mvnw clean package
```


### copiar war a carpeta de Tomcat
Copiar el war a la carpeta webapps

### arrancar tomcat desde Powershell
`PS C:\apache-tomcat-10.1.17\bin> .\catalina.bat start`