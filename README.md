# TrakrJ
#### A high-throughput, low-latency, statistic tracking framework for Java applications. 
A simple API provides the ability to dispatch statistics to underlying trackers. These trackers aggregate this information and their state is periodically logged in a human-readable format. The goal of this project is to do this in such a manner that minimizes the observer effect while providing periodic, near-realtime information to the application's log. 


## Quick Start

#### Last Value Quick Start
Run using `-Dtrakrj.enabled`:
```
// Create an ID for our Histogram Tracker
TrackerId id = TrackerId.generate("My_Value");

// Register our Histogram Tracker with the TrakrJ subsystem. Log every 5 seconds, Never Reset.
TrakrJ.stats().register(id, new LastLongTracker(), Intervals.seconds(5), Intervals.never());

// Send a value to TrakrJ, and watch the output
TrakrJ.stats().record(id, 123L);

// Keep the JVM from exiting while you watch
Thread.sleep(TimeUnit.HOURS.toMillis(1));
```

Output:
```
Sun Jan 27 15:32:05 CST 2019 - TrakrJ - My_Value - 123
Sun Jan 27 15:32:10 CST 2019 - TrakrJ - My_Value - 123
```

#### Histogram Quick Start
Run using `-Dtrakrj.enabled`:
```
// Create an ID for our Histogram Tracker
TrackerId id = TrackerId.generate("My_Histogram");

// Register our Histogram Tracker with the TrakrJ subsystem. Log every 5 seconds, Reset every 1 minute.
TrakrJ.stats().register(id, new HistogramTracker(), Intervals.seconds(5), Intervals.minutes(1));

// Send random numbers to TrakrJ, and watch the output
Random r = new Random();
while(true) {
  Thread.sleep(50);
  TrakrJ.stats().record(id, r.nextInt(1000));
}
```

Output:
```
Sun Jan 27 12:58:30 CST 2019 - TrakrJ - My_Histogram - [ 0=14 50=422 90=843 99=992 99.9=992 100=992 ] count=32
Sun Jan 27 12:58:35 CST 2019 - TrakrJ - My_Histogram - [ 0=36 50=560 90=906 99=997 99.9=997 100=997 ] count=94
Sun Jan 27 12:58:40 CST 2019 - TrakrJ - My_Histogram - [ 0=16 50=505 90=901 99=984 99.9=997 100=997 ] count=189
```

## Stats API
#### Register a Tracker
```
TrakrJ.stats().register(TrackerId id, Tracker tracker, Interval logInterval, Interval resetInterval)
```

#### Reset a Tracker On-Demand
```
TrakrJ.stats().reset(TrackerId id)
```

#### Record a Statistic
 ```
 public static void record(TrackerId id, double/long/Object value)
 public static void record(TrackerId id, double/long/Object key, double/long/Object value)
 ```


## Enabling TrakrJ

#### Method 1: System Property
Setting `-Dtrakrj.enabled` or `-Dtrakrj.enabled=true` will enable TrakrJ with sensible defaults that will log to stderr

#### Method 2: trakrj.properties
Add trakrj.properties to the classpath or working directory. The following sample is identical to defaults used in an empty config. 
```
conductor.impl=default          # default, disabled, or a fully-qualified custom Conductor class
conductor.ringbuffer.size=4096  # the number of statistics that can be queued before stats are dropped due to back-pressure
logger.impl=stderr              # stderr, stdout, slf4j, or a fully-qualified custom Conductor class
logger.name=TrakrJ              # name to be used by the underlying logger implementation
config.print=false              # setting to true will write this config to stderr on startup
```
You may use a custom config location by setting the following property:
```
-Dtrakrj.config=path/to/my.properties
``` 

#### Method 3: Code
You may instantiate a Stats instance yourself:
```
Stats stats = Stats.create(new Slf4jStatLogger()));
```

## Logging
### Provided Implementations
The following implementations are provided:
```
logger.impl=stderr
logger.impl=stdout
logger.impl=slf4j
logger.impl=multi
logger.impl=statsd
```
You may create a custom logger by implementing `io.thill.trakrj.logger.StatLogger` and setting the fully qualified path for `logger.impl`

### Using Multiple StatLoggers
You may log to multiple stat loggers using the `MultiStatLogger`, which will fan-out logger callbacks to multiple logger implementations. Here's a config example:
```
logger.impl=multi
logger.slf4j.impl=slf4j
logger.statsd.impl=statsd
logger.custom.impl=my.custom.StatLoggerImpl
logger.custom.mykey1=hello
logger.custom.mykey2=world
```


## Trackers
The following trackers are provided in the `io.thill.trakrj.trackers` package.

### Histogram
`HistogramTracker` - tracks long values using an HDR histogram

### Last Value
`LastDoubleTracker` - tracks the last double value
 
`LastLongTracker` - tracks the last long value
 
`LastObjectTracker` - tracks the last Object value 


### Aggregate
`AggregateDoubleTracker` - tracks the aggregate of double values
 
`AggregateLongTracker` - tracks the aggregate of long values
 

### Averaging
`AverageDoubleTracker` - tracks the average of double values
 
`AverageLongTracker` - tracks teh average of long values
 

### Arrays
`DoubleArrayTracker` - tracks an array of doubles
 
`LongArrayTracker` - tracks an array of longs

`ObjectArrayTracker` - tracks an array of objects


### Maps
`DoubleDoubleMapTracker` - tracks a double:double map 
 
`DoubleLongMapTracker` - tracks a double:long map
 
`DoubleObjectMapTracker` - tracks a double:Object map

`LongDoubleMapTracker` - tracks a long:double map
 
`LongLongMapTracker` - tracks a long:long map
 
`LongObjectMapTracker` - tracks a long:Object map

`ObjectDoubleMapTracker` - tracks a Object:double map
 
`ObjectLongMapTracker` - tracks a Object:long map

`ObjectObjectMapTracker` - tracks a Object:double map
 
### Custom
Custom trackers can be creating by implementing `io.thill.trakrj.Tracker`
 
## Considerations
#### Objects
In order to minimize the observer effect, stat tracking and logging is done asynchronously. As such, any tracked objects should be effecitvely immutable. If immutability is not possible, it is recommended that they are lock-free and thread-safe.

#### Managing Tracker IDs
To organize many Tracker IDs, it is recommended to use an enum that implements TrackerId.  Here's an example:
```
private enum ID implements TrackerId {
  SEC5(1, "5s Histogram"),
  MIN(2, "1m Histogram"),
  AGG(3, "Aggregated");

  private final int uid;
  private final String display;
  private ID(int uid, String display) {
    this.uid = uid;
    this.display = display;
  }
  public int uid() {
    return uid;
  }
  public String display() {
    return display;
  }
}
``` 

## Instantiating Your Own Instances
This library can be used without the TrakrJ singleton. Reasons you may consider this:
- You want to use different `StatLogger` implementations in different parts of your codebase
- You don't want to manage a `trakrj.properties` config
- You consider singleton an anti-pattern

Here's an example using `Slf4jStatLogger`:
```
Stats stats = Stats.create(new Slf4jStatLogger()));
stats.register(trackerId, new HistogramTracker(), Intervals.seconds(5), Intervals.seconds(5));
``` 

Here's an example using `MultiStatLogger`:
```
Stats stats = Stats.create(new MultiStatLogger(
                  new Slf4jStatLogger(), 
                  new StdoutStatLogger()
              ));
stats.register(trackerId, new HistogramTracker(), Intervals.seconds(5), Intervals.seconds(5));
``` 