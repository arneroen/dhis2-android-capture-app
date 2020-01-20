# README #

To get started, clone the project. Navigate to the repo directory, and make sure you are on branch `2.0.0_beta`. Then, run the following commands in the repo directory:
```
git submodule init
rm -rf dhis2-rule-engine/
rm -rf dhis2-android-sdk/
git submodule update
```

Then, change the configuration in `/app/src/main/assets/videoServer.properties` to reflect your setup.

Then, create a file called `build.gradle` in the `dhis2-rule-engine/`-folder, and add the following to it: 
```
apply plugin: 'java'

dependencies {

    testImplementation 'junit:junit:4.12'
    testCompile group: 'org.assertj', name: 'assertj-core', version: '3.11.1'
    testImplementation group: 'org.mockito', name: 'mockito-all', version: '1.8.4'
    testCompile "com.google.truth:truth:0.44"
    testCompile group: 'nl.jqno.equalsverifier', name: 'equalsverifier', version: '3.1.9'

    implementation 'com.google.code.findbugs:jsr305:3.0.2'
    implementation 'com.google.guava:guava:24.1-android'
    compileOnly "com.google.auto.value:auto-value:1.5.2"
    annotationProcessor "com.google.auto.value:auto-value:1.5.2"
    implementation group: 'org.apache.commons', name: 'commons-lang3', version: '3.8.1'
    implementation group: 'commons-logging', name: 'commons-logging', version: '1.2'
    implementation group: 'org.apache.commons', name: 'commons-jexl', version: '2.1.1'
}
```
Then, make sure the build variant of the `app`-module is set to `dhisDebug`, and `debug` for all other modules. 


Check the [Wiki](https://github.com/dhis2/dhis2-android-capture-app/wiki) for information about how to build the project and its architecture **(WIP)**

### What is this repository for? ###

DHIS2 Android application.
