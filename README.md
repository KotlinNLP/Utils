# Utils [![GitHub version](https://badge.fury.io/gh/KotlinNLP%2FUtils.svg)](https://badge.fury.io/gh/KotlinNLP%2FUtils) [![Build Status](https://travis-ci.org/KotlinNLP/Utils.svg?branch=master)](https://travis-ci.org/KotlinNLP/Utils)

Utils is a Kotlin package containing utilities for the KotlinNLP library.

Utils is part of [KotlinNLP](http://kotlinnlp.com/ "KotlinNLP").


## Getting Started

### Import with Maven

```xml
<dependency>
    <groupId>com.kotlinnlp</groupId>
    <artifactId>utils</artifactId>
    <version>2.0.0</version>
</dependency>
```

### ProgressIndicator

ProgressIndicator implements different indicators to track a progress: 
* Bar ```|███████████ | 95%```
* Icon ```/``` ```-``` ```\ ``` ```|```
* Percentage ```[60%]```

#### Example

Simply declare a ProgressIndicator passing it the total amount of steps to track and the call the `tick()` method to 
fo forward of one single step. Is it possible to pass it the amount of steps setting the `amount` parameter.
```kotlin
import com.kotlinnlp.utils.progressindicator.ProgressIndicatorBar

val progress = ProgressIndicatorBar(1000)

(0 until 1000).forEach {
  progress.tick()
  Thread.sleep(10)
}
```
Try some examples running the files in the `examples/progressindicator` folder.


## License

This software is released under the terms of the 
[Mozilla Public License, v. 2.0](https://mozilla.org/MPL/2.0/ "Mozilla Public License, v. 2.0")


## Contributions

We greatly appreciate any bug reports and contributions, which can be made by filing an issue or making a pull 
request through the [github page](https://github.com/kotlinnlp/Utils "Utils on GitHub").
