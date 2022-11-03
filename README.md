# hackernews-app
An application that downloads stories from the Hacker News API: https://github.com/HackerNews/API.<br/>For each of the top 30 stories, the output contains:
- The story title
- The top 10 commenters of that story.<br/>

For each commenter, the output contains:
- The number of comments they made on the story.
- The total number of comments they made among all the top 30 stories.

### Software Version Used:
- openjdk 18.0.1.1 2022-04-22
- OpenJDK Runtime Environment (build 18.0.1.1+2-6)
- OpenJDK 64-Bit Server VM (build 18.0.1.1+2-6, mixed mode, sharing)
- Apache Maven 3.8.6 

### Build:
To build the application, run the following command in a terminal from the root directory of the project
```
mvn clean compile assembly:single
```

### Execute:
To execute the build, run the following command in a terminal from the root directory of the project
```
java -cp target/hackernews-app-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.App
```

### Test:
To run tests, run the following command in a terminal from the root directory of the project
```
mvn test
```
