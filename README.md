# Simple Blog application on Spring

## WIP moving to gradle and spring boot

This is simple blog application I created for spring framework practice.
Nothing special, just practice. You can add/delete/edit posts, add comments, like or dislike it.

There is no any users and security, don't expect it.

DB is H2 for less complexity of project.

### Requirements

- Java 21
- Any server with servlet support

### How to use

1. ```git clone https://github.com/AligoruM/SpringProjectBlog.git```
2. Optional, check ```application.properties``` for few options
3. Run ```mvnw clean package``` to create WAR package or use own mvn
4. Deploy ```blog.war``` to servlet container (for example, webapps folder for Tomcat)
5. Start server
6. Open ```HOST_ADDRESS:POST/blog/posts```

### Tests

1. Run ```mvnw test```
2. Should bre green :)