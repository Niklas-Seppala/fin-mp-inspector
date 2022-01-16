# Native Android application for following finnish MPs.

### NOTE: before building the project
At the root of the project, add `apikeys.properties` file with following content:
```properties
CONSUMER_KEY="TWITTER_API_KEY"
```

## Product Requirements
___The target of the project is to design and implement an application that allows user to browse information about members of the
parliament. It should also be possible to add comments about the MP and give them grades about their performance.___
- Kotlin
- Android Jetpack Architecture
- Image chaching
- LiveData
- MVVM
- RecyclerView

## Additional Features
### Twitter API integration
User can subscribe to each MP's twitter account, if it exists. Twitter Feed view displays 10 of the latest (unread) tweets by each subscribed MP.
Tweets can be marked as read by swiping right. `#` and `@` are highlighted. Hyperlinks open in browser.
Tweets can also be opened in browser.

### Add MP to favorites
User can add MP from his/hers favorites. This way, MPs of interest can be easily looked up in Favorites View.

## Used Components
### Android Jetpack
- Room Database
- Live Data
- Task scheduling with WorkManager
- Navigation with safeargs
### 3rd Party
- Retrofit
- Moshi JSON converter

## Screenshots
<img src="https://github.com/Niklas-Seppala/fin-mp-inspector/blob/master/screenshot/ui-demo.png" alt="alt text" width="800">
