## Execute the project with sbt

    sbt "run http://www.kenfos.co.uk"
    
## Execute the tests with sbt

    sbt test
    
## Assemble the project and execute it

    sbt assemble
    java -jar ./target/scala-2.12/crawler.jar http://www.kenfos.co.uk
    
### Assumptions

- Url normalization has not been taken into account, that means urls such as `http://www.kenfos.co.uk`, `https://www.kenfos.co.uk`, `http://www.kenfos.co.uk:80` will be treated as different urls
- It is OK to filter all the URLs that contain a '#' or ':'
- Input parameters validation is not need it