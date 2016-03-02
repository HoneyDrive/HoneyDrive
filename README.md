# HoneyDrive
The ultimate repo!

## Prerequisites
You're going to need Maven with the `JSON.simple` dependency.

### Using IntelliJ
Press *File* and then *Project Structure*.
Click on *Libraries*, and then *New Project Library* (the small green plus symbol).
Choose *From Maven* and type
`com.googlecode.json-simple:json-simple:1.1`
into the textbox and click *OK*.

### General / Other IDEs
Download Maven. In the `pom.xml` file, add the `JSON.simple` dependency by adding
```
<dependencies>
	<dependency>
		<groupId>com.googlecode.json-simple</groupId>
		<artifactId>json-simple</artifactId>
		<version>1.1</version>
	</dependency>
</dependencies>
```
inside the `<project></project>` tag.