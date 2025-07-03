# redis-netty-bridge

redis-netty-bridge is a library that can be used for building Redis proxies (servers that implement Redis (RESP protocol) and capable of delegating commands processing to external resources)

Use case:

System consists of the following components:
1. Redis cluster
2. Daemon that constantly updates stock data in Redis
3. HTTP Gateway microservice that provides stock data, stored in Redis, to clients

There is a need for new application that need stock data for its business - lets just call it stock-data-consumer
stock-data-consumer has very strict latency requirements, because of this fetching data through HTTP gateway is not an option because that would lead to higher latency.
stock-data-consumer will read data directly from Redis as well. 
Since development team doesn't have direct access to Redis, to test new version of stock-data-consumer locally development team is forced to start Redis locally, manually retrieve data for some products through gateway and populate local Redis with it, which takes significant amount of time.

Potential solutions:
1. Taking snapshot of Redis - doable if amount of data is not big, otherwise Redis would consume too much resources. In this particular case its not feasible
2. Fetching data from HTTP Gateway if development profile is activated - will require code changes in app, potential duplication if many services need stock data.
3. Creating Redis proxy that will delegate processing of commands to gateway - no code changes, redis proxy consumes minimal amount of resources. 

![img.png](usecase.png)