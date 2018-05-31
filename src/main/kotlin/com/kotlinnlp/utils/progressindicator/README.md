# ProgressIndicator

ProgressIndicator implements different indicators to track a progress: 
* Bar ```|███████████ | 95%```
* Icon ```/``` ```-``` ```\ ``` ```|```
* Percentage ```[60%]```

## Getting Started

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

### Examples

Try some examples running the files in the `examples/progressindicator` folder.
