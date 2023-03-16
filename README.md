# 3D Graphics Laboratory Materials (2023 Spring)

This repository contains initial codes for the laboratory sessions. 

These are the same codes as on Moodle.

During the semester, each initial code will be pushed to a different branch.
Under [Releases](https://github.com/bobarna/3dgr-labs/releases), you can easily
download each zip file as well as corresponding pdfs.

For convenience, you can also find pdf exports at
[https://cg.iit.bme.hu/~barney/3dgr/](https://cg.iit.bme.hu/~barney/3dgr/)

# Build Instructions 

- Install gradle7.x (not 8.x!), then `gradle build`
- Run a webserver, e.g. `python3 -m http.server 8000`
- Open `localhost:8000` in your browser, and navigate to `build/web`

## gradlew
Instead of installing gradle, you can also simply run the `gradlew[.bat]`
script, which will pull the correct version of gradle to the `gradle` folder.
*(For more details, see the [documentation of gradle
wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).)*

For actually building the project, you can use:
```
./gradlew[.bat] build
```

(The first build will take some time, but further builds will run much faster.)

# Running the Web Server
You can use any web server, as described in the slides of the 1st lab. 

E.g.
```
python -m http.server
```
