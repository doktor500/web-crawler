appender("stdout", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} %-5level %logger{5} - %msg%n"
    }
}

root(INFO, ["stdout"])