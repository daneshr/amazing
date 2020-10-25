# Amazing Company

We in Amazing Company need to model how our company is structured so we can do awesome stuff.


### Approach
There are different methods for persisting a tree, I assumed we need a relational solution, so by some searches I found out that we have different choices.

- [Adjacency list](https://en.wikipedia.org/wiki/Adjacency_list) : create a node relation with self reference to its parent. I thought this method isn't suitable for this problem because finding all descendants need recursive query.

- [Subsets](https://bitworks.software/en/2017-10-20-storing-trees-in-rdbms.html) : It keeps all the subtrees that each node belongs to, in another relation. With this approach we have to keep lots of redundant information.

- [Materialized paths](https://docu.ilias.de/goto.php?target=wiki_1357_Materialized_Path&lang=en) : Retain a path field with value of path to root for each node. This is not match, because changing a parent node would be a nightmare.

- [Nested sets](https://en.wikipedia.org/wiki/Nested_set_model) : keep left and right values for each node, these fields demonstrate boundaries for each set.

I selected the Nested Set approach, It needs O(n) for retrieving all descendants of a node, related to change parent, it is more complicated but still acceptable. As I assumed we need much more reads rather than changing parent, decided to use this one.



### Technologies

I used Java, Spring Boot,Spring web, Spring Data JPA, Postgresql, H2, Flyway, Docker, Lombok and Swagger for back-end development, I also developed a small UI with ReactJS. 

 
### How to Run

The following Maven command, builds the artifact:

```
mvn clean install
```

The following Docker compose command will build and run the application:

```
docker-compose up
```
The UI would be available at the following address:

```
http://localhost:8081/
```

If you want to open this project in any IDE, you need to have Lombok plug-in installed.

##### Rest API Documentation:


```
http://localhost:8081/swagger-ui/index.html#/
```


















