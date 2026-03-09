
Project Structure
```
logger-library/
└── src/
├── main/
│   ├── java/
│   │   └── com/logger/  # All the core library code lives here
│   │       ├── config/    # Configuration parsing logic
│   │       ├── sinks/     # All Sink implementations (Console, File, etc.)
│   │       └── ...      # Logger, LogManager, and other core classes
```
Core Features
```
Here's what it can do:
Multiple Log Destinations (Sinks): Log messages to the Console or to a File. And the best part? It's extensible, so you can easily add your own sinks (like a database).
Asynchronous Logging: Don't let logging slow down your application. You can configure any sink to run in ASYNC mode, which pushes log messages to a background queue to be processed without blocking your main threads.
Configurable Threading: For ASYNC sinks, you can choose between a SINGLE background thread (which guarantees log order) or a MULTI-threaded model for higher throughput.
Automatic Log Rotation & Compression: FileSink won't let your log files grow forever. It automatically rotates them when they hit a size limit and compresses the old ones into .gz archives to save space.
Automatic Context Injection: Automatically adds hostName and a trackingId to every log message, which is perfect for tracing requests in modern applications.

```

