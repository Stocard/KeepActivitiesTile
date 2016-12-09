# Keep Activities Quick Settings Tile

A quick settings tile to quickly toggle "Don't keep activities".

1. Pressing the tile toggles "Don't keep activities" on and off.

Note that this app requires you to **manually grant permissions** to allow the app to alter the "Dont't keep activity" setting. To do this, issue the following `adb` commands:

* to allow the tile to store the setting

`adb shell pm grant de.stocard.keepactivitiestile android.permission.WRITE_SECURE_SETTINGS`

* to allow the tile to inform the ActivityManager of the current setting 

`adb shell pm grant de.stocard.keepactivitiestile android.permission.SET_ALWAYS_FINISH`


**[Download APK](https://github.com/stocard/KeepActivitiesTile/releases)**

Quick Settings Tiles are support starting with Android 7.0.

### Kudos
* nickbutcher for [AnimatorDurationTile](https://github.com/nickbutcher/AnimatorDurationTile)
* (icons8)[https://icons8.com] for the (brain-logo)[https://icons8.com/web-app/2069/Brain]

### License
based on [AnimatorDurationTile](https://github.com/nickbutcher/AnimatorDurationTile)

```
Copyright 2016 Stocard GmbH.
Copyright 2016 Google Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements. See the NOTICE file distributed with this work for
additional information regarding copyright ownership. The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```


