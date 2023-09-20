# CityPoi

It is an app to view points of interest (POI) developed in Kotlin.

Originally it connected to the citymap endpoints (https://cityme-services.prepro.site/app_dev.php/api/districts/), since they are no longer available, a Json has been developed so that it can be tested.

This app allows you to do the following actions for a POI: See its location on maps, play its audio, save it in a local database and show its image.

## How to start the project

In maps_api_key.xml (app/src/main/res/values/maps_api_key.xml) enter your google maps api key.

<img width="1149" alt="imagen" src="https://github.com/antoniomy82/CityPoi/assets/25392687/40b027f0-92fd-4eba-b6a5-d81220c6f2cb">


## Used libraries/components

  - Dagger hilt.
  - Glide.
  - MediaPlayer.
  - Classic fragment replace.
  - Room.
  - Data Binding.
  - View Binding.
  - Google Play Services Maps.
  - UnitTest (JUnit,mockk).
  - Coroutines.
  - LiveData , Flow.
  - Retrofit, Gson.
  - ViewModel
  - Clean architecture. (Data, Domain, App<Framework + presentation> )
