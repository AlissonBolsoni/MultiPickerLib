# MultiPickerLib
MultiPickerLib

To use this Lib on your project. 
You must add on your app gradle
(implementation 'com.github.AlissonBolsoni:MultiPickerLib:1.0.1')
and
in your project gradle

```
(maven { url 'https://jitpack.io' })
```

After that you can use like this

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
