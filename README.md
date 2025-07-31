# winproc-tools

A Spring Boot–friendly toolkit for native Windows process control, built on JNA.

Clean, user-friendly APIs for everything from input emulation to low-level memory access - all designed for seamless integration in Java applications.

### Input emulation advantages over `java.awt.Robot`

- **Background windows**. `Robot` only affects the foreground window. With JNA, you can send key/mouse events to any window, even if it's not in focus
- **No screen interference**. `Robot` can mess with what the user is doing (moves mouse, types something). 
JNA lets you automate in the background - the user won't even notice
- **Fine-grained control over messages**. Using JNA, you can specify exactly which window gets what input.
- **More performance for mass input**. If you're automating many windows or processes, JNA avoids constant context switching and focus stealing. 
You can dispatch messages in parallel

## How to use

The project isn't published to Maven central yet, so would need to publish to Maven local in order to use it.

```sh
$ ./gradlew publishToMavenLocal
```

Link in another project:

```xml
<dependency>
    <groupId>com.jnafamily.winproctools</groupId>
    <artifactId>winproc-tools</artifactId>
    <version>${VERSION}</version>
</dependency>
```