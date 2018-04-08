# Quiz-Generator


## Run Quiz-Generator API

### Run within Intellij Idea
- To run in Intellij Idea, create a new run configuration by going to `Run > Edit Configurations > Add New Configuration > Application`.
- Enter the following under `Configuration` tab:
    * Main class: `com.gmat.quiz.generator.QuizGeneratorApplication`
    * Working directory: `/path/to/quiz-generator`
- Before launch run Maven goal:
    * Working directory: `/path/to/quiz-generator`
    * Command line: `clean install`
- Save the configuration. Now you can run it using `Run > Run` in the menu. You can check the project running on your local server on port 8888 by default.

- (http://localhost:8888/)
- By hitting above URL it will ask to upload quiz csv file
- Please look on Note section

### Run using mvn
```
mvn spring-boot:run
```

### Run as jar
- Create a jar by running: `mvn clean install`
- Run the generated jar file using `java -jar target/quiz-generator-x.x.x-SNAPSHOT.jar `


#### Note:
- It requires CSV file to upload which contains Quiz list with pipe('|') separated as delimiter. 
- Any Tag other than Tag1/Tag2/Tag3/Tag4/Tag5/Tag6 will be considered as Tag1
- Any Difficulty level other than EASY/MEDIUM/HARD will be considered as EASY
- In CSV file first line should be Header eg.
```
Question ID | Tag | Difficulty
```
- order can be change but header value should be same
- For csv sample data please find data.csv in resource directory