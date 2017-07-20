# Introduction

The LinShare upload proposition application developed with drop wizard.

# Running The Application

To test the example application run the following commands.

* To package the example run.

        mvn package

* To run the server run.

        java -jar target/linshare-upload-proposition-<version>.jar server config.yml

* To hit the ok message.

	http://localhost:9080/uploadpropositions
	
* For health check (for admin)

	http://localhost:9081/ping
	http://localhost:9081/healthcheck
