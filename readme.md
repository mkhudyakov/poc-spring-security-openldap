### Spring Security + LDAP

### 1. Create an environment
```
docker-compose up -d
```

### Import data to OpenLDAP server
```
docker cp ./users.ldif poc-spring-openldap_open-ldap_1:/users.ldif
docker exec poc-spring-openldap_open-ldap_1 ldapadd -x -H ldap://localhost -D "cn=admin,dc=openldap,dc=spring,dc=poc,dc=com" -f /users.ldif -w admin
docker exec poc-spring-openldap_open-ldap_1 ldapsearch -x -H ldap://localhost -b dc=openldap,dc=spring,dc=poc,dc=com -D "cn=admin,dc=openldap,dc=spring,dc=poc,dc=com" -w admin
```

### Run
```
mvn clean jetty:run
``` 

Once started go to `http://localhost:8080/restricted` and use `developer/developer` credentials. 

### Delete an environment
```
docker-compose down
```






```
mvn jetty:run
```