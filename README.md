# Goal
REST service providing information about places in specific location

# Api

/{country}/{city}/{description_of_place}

  type: application/json
  
  array of separate cities  | City -> array of Places | Place -> {name, latitude, longitude}
 
  Because country can have multiple cities with the same name, different cities (recognized by city_id from Facebook Graph API) are divided into separate arrays so final result structure is: array of array of Place
  
# Configuration
For app to work correctly you need to provide AppId and AppSecret (to resources/application.properties file), so app can connect to Facebook Graph API, otherwise app will shutdown

You can create both at https://developers.facebook.com/
