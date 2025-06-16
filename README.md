# Simple Blog application on Spring

This is simple blog application I created for spring framework practice.
Nothing special, just practice. You can add/delete/edit posts, add comments, like or dislike it.

There is no any users and security, don't expect it.

DB is H2 for less complexity of project.

### Requirements

- Java 21

### How to use

1. ```git clone https://github.com/AligoruM/SpringProjectBlog.git```
2. Optional, check ```application.properties``` for few options
3. Run ```gradlew build``` or ```gradlew bootJar``` to create fat jar
4. Run ```java -jar <path to project fat jar in build/libs jar>```
5. Wait few seconds until server start
6. Open ```localhost:8080/posts```

### Tests

1. Run ```gradlew test```
2. Should bre green :)