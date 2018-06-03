## Dependencies

  Java 8
  sbt
  npm

## Execute the project with sbt

    sbt "run https://www.thoughtworks.com"

## Execute the tests with sbt

    sbt test

## Assemble the project and execute it

    sbt assemble
    java -jar ./target/scala-2.12/crawler.jar https://www.thoughtworks.com

# Visualize the site map graph

    Execute the app and then run `npm install -g local-server && ws`, then navigate to `http://localhost:8000` 

### Assumptions

- Url normalization has not been taken into account, that means urls such as `http://www.thoughtworks.com`, `https://www.thoughtworks.com`, `https://www.thoughtworks.com:80` will be treated as different urls
- It is OK to filter all the URLs that contain a '#' or ':'
- Validation is not done for input parameters
