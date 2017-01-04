# Indra REST Service

This is a REST Service that interfaces with the Java Indra Client.

* Run the `start` script in the `bin` directory of this distribution. This will start the service with the 
 default configurations that may not be suitable for you;
* The `start` script is not intended for production yet;
* Execute `start -h` to see the running options available;
* There is a sample configuration file in the `conf` directory;
* The service uses Logback as the logger. Look for the `logback.xml` in the `conf` dir.

# Sample Request using cURL

    curl -X POST -H "Accept: application/json" -H "Content-Type: application/json" -d '{
	"corpus": "wiki-2014",
	"model": "W2V",
	"language": "EN",
	"scoreFunction": "COSINE",
	"pairs": [{
		"t2": "car",
		"t1": "engine"
	}]
    }' "http://localhost:8916/indra/v1/relatedness"