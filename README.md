# MultiPickerLib
MultiPickerLib

## Add MultiPickerLib Library with dependency:

- Add it in your `build.gradle` (root) at the end of repositories:

```gradle
  allprojects {
    repositories {
      ...
      maven { url "https://jitpack.io" }
    }
  }
```

- Add the dependency in your `build.gradle` project

```gradle
  dependencies {
          implementation 'com.github.AlissonBolsoni:MultiPickerLib:1.0.1'
  }
```

## After that you can use like this

#config
```kotlin
FilePicker
    .Builder(this)
    .maxFiles(1)
    .showFiles(false)
    .showImages(true)
    .showAudios(false)
    .showVideos(false)
    .saveAudios(false)
    .saveImages(true)
    .saveVideos(false)
    .setProvider(BuildConfig.APPLICATION_ID + ".provider")
    .build()
```

#onActivityResult
```kotlin
if (REQUEST_CODE_TO_RESULT == requestCode && Activity.RESULT_OK == resultCode && data != null) {
       val paths = data.getSerializableExtra(PARAM_RESULT_ITEMS_PATHS) as ArrayList<String>
       for (path in paths) {
   
       }
   }
   ```
