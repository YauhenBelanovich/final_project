# Online store/Course project
* Final project on the course _"Technologies for the development of interprise solutions in Java"_
    * [IT-Academy](https://www.it-academy.by/)
    * Сreated during April 2020

## Requirements
* Multi-module maven project (3-level three-module development architecture)
* Version Control System - _Git_
* Technologies:
   * _Java 8_
   * _Log4j2_
   * _Spring boot 2_
   * _Spring 5_
   * _Hibernate 5_
   * _MySQL 8_
* Unit and Integration application tests
* Using _GitHub Actions_ to Build a Project
* Using _Liquibase_ for Migration

## Getting started

### _application.properties_

  * add your database settings
```
spring.datasource.username=
spring.datasource.password=
```

  * This project is using Gmail SMTP server, tested with TLS (port 587) and SSL (port 465).
Add your gmail username and password
```
spring.mail.username=
spring.mail.password=
```
For Gmail SMTP, if your account is enabled the 2-Step verification, please create an “App Password”. Read more on this [JavaMail – Sending email via Gmail SMTP example](https://mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/)

### _database-changelog.xml_

* Add ADMINISTRATOR here
```
<changeSet id="5" author="yauhen2012@gmail.com">
        <insert tableName="user">
            <column name="email" value="...your email..."/>
            <column name="password" value="...your encrypted password..."/>
            <column name="role" value="ADMINISTRATOR"/>
        </insert>
    </changeSet>
    <changeSet id="6" author="yauhen2012@gmail.com">
        <insert tableName="user_details">
            <column name="user_id" value="1"/>
            <column name="last_name" value="...your last name..."/>
            <column name="first_name" value="...your first name..."/>
            <column name="patronymic" value="...your middle name or patronymic..."/>
        </insert>
    </changeSet>
```
You can encrypt your password here: [bcrypt-generator](https://bcrypt-generator.com/)
  
### _AdministratorConstant_
 Path: _\springbootmodule\constant\AdministratorConstant.java_
 
* Add your email here:
```
public interface AdministratorConstant {

    String ADMINISTRATOR_EMAIL = "...your email...";
}
```

## Authors

* **Yauheni Belanovich** - [YauhenBelanovich](https://github.com/YauhenBelanovich)

### Java trainer

* **Artemije Perevoznikov** - [ArtemijePerevoznikov](https://github.com/artemijeperevoznikov)
