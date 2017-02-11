# Hueflix
This is a small app that provides a service to automatically start a Philips Hue Scene whenever you start a Netflix Movie on your Smart-TV or Chromecast, and start another scene when pausing.
It uses the fact that the Netflix Notification gets updated 5 times when pressing play and 3 times when pressing Pause.

MIN-SDK Version is 21 because of the [MediaSession](https://developer.android.com/reference/android/media/session/MediaSession.html) that Netflix uses. While there is no code that uses AOI level 21 (despite the theming), it probably wouldn't work below it.

# Warning
I haven't tested this app so far on any other device than my OnePlus 3T. **It might not work on your device**, because it might be slower or Netflix doesn't update the notification on other devices in the same count as on mine.

# To-Do
* I know that there are apps on the Play-Store (for example [Sleep Timer](https://play.google.com/store/apps/details?id=ch.pboos.android.SleepTimer&hl=de) that directly read the notifications, so it probably is possible. I haven't figured out a way to do that so far, any help on that would be greatly appreciated.
* The app has no logo yet because I suck at Design, soooo...