[ ![Download](https://api.bintray.com/packages/pulimet/utils/gps/images/download.svg) ](https://bintray.com/pulimet/utils/gps/_latestVersion)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-GpsDetector-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4916)

# Gps Detector - Android Library

When GPS disabled shows a dialog and on accept enable it without a need to open android settings

# Installation

- Add the dependency from jCenter to your app's (not project) build.gradle file:

```sh
repositories {
    jcenter()
}

dependencies {
    compile 'net.alexandroid.utils:gps:1.6'
}
```

If you experiencing version conflicts with play services libraries use exclude as shown below:
```sh
compile ('net.alexandroid.utils:gps:1.6') {
    exclude group: 'com.google.android.gms', module: 'play-services-location'
    exclude group: 'com.google.android.gms', module: 'play-services-gcm'
}
```    

# How to use it

- Implement GpsStatusDetectorCallBack interface and it methods
```sh
public class MainActivity extends AppCompatActivity 
        implements GpsStatusDetector.GpsStatusDetectorCallBack { 
```
<br>

If boolean is true gps is enabled and vice versa.
```sh
@Override
public void onGpsSettingStatus(boolean enabled) {
    // Your code
}
```
<br>

If GPS disabled and dialog that offers to enable GPS shown. If User refuse this method invoked. 
Please note that onGpsSettingStatus(false) also invoked.
```sh    
 @Override
public void onGpsAlertCanceledByUser() {
  // Your code
}  
```
<br>



- Create GpsStatusDetector instance, invoke checkGpsStatus() method where you need.
```sh
private GpsStatusDetector mGpsStatusDetector;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mGpsStatusDetector = new GpsStatusDetector(this);
    mGpsStatusDetector.checkGpsStatus();
}
```

- Override onActivityResult and add checkOnActivityResult() method as shown below:
```sh
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mGpsStatusDetector.checkOnActivityResult(requestCode, resultCode);
}  
```

 Thats it. You are ready to go!
 
 
# Fragment support
 
 
 Add this in your activity class:
 
 ```sh
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     for (Fragment fragment : getSupportFragmentManager().getFragments()) {
         fragment.onActivityResult(requestCode, resultCode, data);
     }
}
 ```


# License

```
Copyright 2016 Alexey Korolev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
