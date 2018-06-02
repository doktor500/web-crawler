## Execute the project with sbt

    sbt "run http://www.kenfos.co.uk"
    
## Execute the tests with sbt

    sbt test
    
## Assemble the project and execute it

    sbt assemble
    java -jar ./target/scala-2.12/crawler.jar http://www.kenfos.co.uk