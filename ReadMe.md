
# Log Analyser

## 

## Run and Test
- Clone the repository
- Run all commands from the root directory of the repository

### Compile
```
./mvnw compile
```

### Test
```
./mvnw test
```

### Run
```
./mvnw exec:java -DPROPERTY_TYPE=prod
```

- To pick a particular configuration (test/prod) you can pass as JVM system property or set environment variable
`PROPERTY_TYPE=test`

## Configurations
```
log_size_threshold=100

# Events will be alerted if the duration is more that below threshold in ms
event_alert_thresold=4

execution_mode=sequential
# execution_mode=parallel

driver=org.hsqldb.jdbc.JDBCDriver
url=jdbc:hsqldb:file:eventdb
user=
password=
```
