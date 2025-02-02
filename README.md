# simdjson-java

A Java version of [simdjson](https://github.com/simdjson/simdjson) - a JSON parser using SIMD instructions,
based on the paper [Parsing Gigabytes of JSON per Second](https://arxiv.org/abs/1902.08318) 
by Geoff Langdale and Daniel Lemire.

This implementation is still missing several features available in simdsjon. For example:

* Support for Unicode characters
* UTF-8 validation
* Full support for parsing floats
* Support for 512-bit vectors

## Code Sample

```java
byte[] json = loadTwitterJson();

SimdJsonParser parser = new SimdJsonParser();
JsonValue jsonValue = simdJsonParser.parse(json, json.length);
Iterator<JsonValue> tweets = jsonValue.get("statuses").arrayIterator();
while (tweets.hasNext()) {
    JsonValue tweet = tweets.next();
    JsonValue user = tweet.get("user");
    if (user.get("default_profile").asBoolean()) {
        System.out.println(user.get("screen_name").asString());
    }
}
```

## Benchmarks

To run the JMH benchmarks, execute the following command:

```./gradlew jmh```

## Tests

To run the tests, execute the following command:

```./gradlew test```

## Performance

This section presents a performance comparison of different JSON parsers available as Java libraries. The benchmark used 
the [twitter.json](src/jmh/resources/twitter.json) dataset, and its goal was to measure the throughput (ops/s) of parsing 
and finding all unique users with a default profile.

**Note that simdjson-java is still missing several features (mentioned in the introduction), so the following results
may not reflect its real performance.**

Environment:
* CPU: Intel(R) Core(TM) i5-4590 CPU @ 3.30GHz
* OS: Ubuntu 23.04, kernel 6.2.0-23-generic
* Java: OpenJDK 64-Bit Server VM Temurin-20.0.1+9

 Library                                           | Version | Throughput (ops/s) 
---------------------------------------------------|---------|--------------------
 simdjson-java                                     | -       | 1450.951           
 simdjson-java (padded)                            | -       | 1505.227           
 [jackson](https://github.com/FasterXML/jackson)   | 2.15.2  | 504.562            
 [fastjson2](https://github.com/alibaba/fastjson)  | 2.0.35  | 590.743            
 [jsoniter](https://github.com/json-iterator/java) | 0.9.23  | 384.664            

To reproduce the benchmark results, execute the following command:

```./gradlew jmh -Pjmh.includes='.*ParseAndSelectBenchmark.*'```
