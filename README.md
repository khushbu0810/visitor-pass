Society where you are living and visitors comes to visit you.

-> Resident Microservice
1. register in app as resident of society (fill your details) --> signin basically
2. enter visitors details that is going to visit you.
3. generate qr code for visitor

features of resident :
-> can modify own details
-> can modify visitor details




1. delete all test cases from all microservices
2. add pom in parent module - visitorPass , which contains all microservices names
3. add this visitorPass in core module
4. add a docker-compose.yml in root folder 
5. if change something in code then reload all maven modules first before running docker compose file
    as it can use cache otherwise



Adding  authentication (Login / signup)
for google login (Backend):
--> add google in pom first.
1. add google config , which verifies idToken sent by angular
2. we are using google id token 


when user loged in by google then he will see like --> add your details 
and after adding details , can see own profile.

------------------->
done with resident and visitor service 
resident-> create visitor 
now generating a qr code for visitor as a pass


--> for generating qrCode , use ZXing dependency


USing RestClient client for passing visitor to pass service
while verifying pass , first read token from headdrs and verify valid visitor



<----------------------------SWAGGER PENDING------------------------------->



KAFKA ======>>>>
1. resident create the visitor --> publish pass created event
2. adding a separate file as producer that defines send method, and service use this method
   (we can use kafkaTemplate directly in service but as best practice we r creating separate file as visitorProducer)
3. visitor --> publish pas screated event and pass service consume this event
4. consumer consumes pass created event in handler folder using kafkaListener

If using kafka , then don't need zookeeper 

with zookeeper  
principal 
    |
teacher
    |
student 


without zookeeper 
teacher
    |
student
(NO NEED FOR PRINCIPAL THAT MANAGES OR LOOKS EVERYTHING IS WORKING FINE)

provided common network : kafka-network to each service 

--> tag each image separately (docker tag passmicroservice:latest khushbu0810/passmicroservice:latest)
--> deploy each image separately on render
--> using aiven for kafka online on cloud
(same we r using tidb for database)


docker compose id for local development/testing

KAFKA AIVEN ----->
1. first create topics in aiven that you r using in your project
        -> manage streams -> topic
2. Overview
  ↓
  Apache Kafka
and download these (in client certificate):
  CA Certificate
  Access Certificate
  Access Key
  (Download:
        ca.pem
        service.cert
        service.key)
3.
    1. Create the PKCS12 Keystore
       openssl pkcs12 -export -in service.cert -inkey service.key -out kafka-keystore.p12 -name kafka
       password : visitorpass123
   2. Create the Truststore
      keytool -import -alias CARoot -file ca.pem -keystore kafka-truststore.jks
      password : visitorpass123
   3. now folder have these files :
          ca.pem
          service.cert
          service.key
          kafka-keystore.p12
          kafka-truststore.jks
   4. copy these files in resources in spring app
4. upload these 2 files in render in secrets



OPTION 2: instead of certificates(SSL) use avien SASL -> require username and password only
        ----> first do mvn clean intall, again build the project so that these kafka files move to jar folder also.
        ----> copying kafka folder form resource which contain ssl secret files into docker container
            (COPY --from=builder /app/VisitorMicroservice/src/main/resources/kafka /app/kafka)


REMoved test cases if failed building