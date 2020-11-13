# number-profiler

## Requirements

Deliver an AWS Lambda that outputs the mean, median, and mode of a series of numbers.

## Approach

The requirements provided above are intentionally vague. The only real requirements provided are:

- it is an AWS Lambda
- it returns the mean, median, and mode for a series of numbers.

There are no requirements for:

- the input or output format (_I've chosen JSON._)
- the anticipated size of the number series that will be profiled. (_I will support the [maximum number of values that can be represented in a Java ArrayList](https://stackoverflow.com/questions/3767979/how-much-data-can-a-list-can-hold-at-the-maximum)._)
- how the lambda will be triggered or how the result should be delivered. (_The lambda will be triggered manually._)
- how to handle error conditions (_I will return these in the JSON response._)
- are the numbers integers, floats, big decimal, etc. (_I will assume integer.  If a JSON array of integers is not supplied, the lambda will fail before it ever reaches this code._)
- there seems to be differing definitions for mode.  I used [the definition from PurpleMath](https://www.purplemath.com/modules/meanmode.htm).

One provided hint was that the solution should highlight concurrency, threading, and error handling. The fact that an AWS Lambda was specified as a requirement poses some unique challenges in this regard. Namely:

- The process to profile the number series is entirely CPU bound (once the number series is submitted, there are no calls to outside services). So while a lambda can have up to [1024 threads](https://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-limits.html), a lambda VM will have [1 or 2 core processors](https://stackoverflow.com/questions/34135359/whats-the-maximum-number-of-virtual-processor-cores-available-in-aws-lambda#:~:text=One%20Lambda%20function%20has%20only,infrastructure%20(and%20your%20wallet)) to service these threads. I would guess that any speed benefit would only happen with very large sets of numbers. Since we don't know if that is a likely scenario, I thought it best to hold off on adding this complexity now. There is nothing to prevent us from circling back on this later once we know more about the typical size of the number series.
- Another idea was to recursively call the lambda to sort and total the number set, then calculate median and mode once they all complete. AWS [discourages this approach](https://docs.aws.amazon.com/lambda/latest/dg/best-practices.html), citing higher costs and the potential to overrun the allocated lambda quota.
- A third idea would be to follow a microservices approach to get increased bandwidth, possibly utilizing an event bus. The would involve a lambda "listener" to accept requests. This lambda would in turn invoke separate lambda services to "sum" and "sort" the number set. The "sort" service would trigger services for "median" and "mode" calculations. A "response" service would listen for each service to complete (utilizing something like a correlation id) and respond to the client when complete. Additional logic would be needed to handle timeouts and other errors. Like the threading option, a performance gain would only be seen in very large number sets and would need to be balanced against the additional cost and complexity of this solution. Also like the threading option, this is something we can circle back on once we understand the data a bit more.

For these reasons, and the fact that no anticipated input size has been specified, I've opted not to overbuild this solution and provided a simple, brute force approach. The Java [List.sort](https://docs.oracle.com/javase/8/docs/api/java/util/List.html#sort-java.util.Comparator-) is efficient for most data sets and will serve well for an initial pass at this solution.

I've also decided not to include a dependency injection framework like Guice or Spring. This service is fairly straightforward, attached to no external services. There is so little benefit we would gain from a DI solution that it was excluded in favor of a faster lambda boot time.  I would certainly advocate for DI if the solution were more complicated than this.

## Sample Input
```
[3, 1, 2, 3]
```

## Sample Output
```
{
  "mean": 2.25,
  "median": 2.5,
  "mode": [
    3
  ]
}
```

## Build
```
mvn package
```

## Deployment & Execution
I manually uploaded the lambda jar produced by this build to AWS and used the test capability from the AWS lambda console to test it.  

The lambda configuration is a vanilla Java 8 lambda, with no special environment, network, or other settings.  The handler for the lambda is `com.blackwaterpragmatic.handler.NumberProfileHandler::handleRequest`.

## Notes
- The build runs a default checkstyle configuration and jacoco to verify adherence to coding standards and test coverage.

## TODO
- If this were a production lambda, it might be good to provide scripts to automate the build and deployment of this solution.
- I have not provided any integration tests for the solution.
- There is no support for gitflow in the maven POM.  this would be needed to ease production deployment and migration of a "real" app.
