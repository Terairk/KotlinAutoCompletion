## My implementation of the task
Used the KOTLIN_HOME env variable to find all the relevant 
jars that make up the kotlin-std library. 
So there's a chance my code may not work on your machine.
I did have to do some work to ensure it runs as a gradlew executable
because I did have issues where it couldn't find any jars at all.

## To Compile/Run
NOTE: This code may only work on UNIX based systems since it does rely on
the existence of the KOTLIN_HOME env variable but I'm not sure
### Method 1
Use IntelliJ to just run the file in which you can set the 
command line arguments through editing configurations.
This is probably the easiest way to do this.

### Method 2 
Build using 
```bash
./gradlew build 
```
Then run them like so. 
```bash
./gradlew -q run --args="Array"
./gradlew -q run --args="AbstractCol"
./gradlew -q run --args="List"
./gradlew -q run --args="Mutable"
```

## Some Example Usages

![Abstract Col](./demoImages/AbstractCol.png)
![Mutable](./demoImages/Mutable.png)
![MutableMap](./demoImages/MutableMap.png)
