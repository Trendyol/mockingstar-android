# Mocking Star 🌟

Mocking Star is a powerful request mocking tool designed to simplify the process of http request mocking, network debugging, and using UI tests for your mobile applications.  With just a single line of code, you can use Mocking Star in your project.

![](/img/demo.gif)

## Getting Started 🔥



## Installation

Library is distributed through JitPack.

**Add repository to the root build.gradle**

```gradle
repositories {
	maven { url("https://jitpack.io") }
}
```

**Add the library**

```gradle
implementation("com.trendyol.mockingstar:mockingstar:{latest-version}")
```

You can check the latest version from the Releases

## How to Use

Library consists of an [OkHttp](https://square.github.io/okhttp/) [Interceptor](https://square.github.io/okhttp/features/interceptors/) called `MockingStarInterceptor`. 

It can take two parameters: 
- `MockUrlParam`: class to customize the connection url should you decide to update.
- `header`: A string map, is used to supply any custom header pair you want to add to the ongoing request.

```kotlin
class MockingStarInterceptor(
	private val params: MockUrlParams = MockUrlParams(),
	private val header: Map<String, String> = emptyMap(),
) : Interceptor {

// Class contents

}
```

To use it, pass the `MockingStarInterceptor` to your `OkHttpClient`. After this, you can monitor the requests that are sent from your application on Mockingstar.

```kotlin
OkHttpClient.Builder()
  // configuration code
  .addInterceptor(MockingStarInterceptor())
  .build()
```

**Warning!**

Default address to communicate with `MockingStar` application is `10.0.2.2`. This type of network access is disabled by default in Android Applications. You must add 

```xml
android:usesCleartextTraffic="true"
```

to the `application` block in your `AndroidManifest.xml` to enable it.



