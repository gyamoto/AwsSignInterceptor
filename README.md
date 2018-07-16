# AwsSignInterceptor
Intercept [OkHttp](http://square.github.io/okhttp/) request and sign [AWS v4 signature](https://docs.aws.amazon.com/general/latest/gr/signature-version-4.html)

## Download

Add project root _build.gradle_

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

and add package _build.gradle_

```gradle
dependencies {
    ...
    implementation 'com.github.gyamoto:AwsSignInterceptor:master'
}
```

## Usage

```kt
val credentialProvider = CognitoCredentialsProvider("identity pool id", Regions.AP_NORTHEAST_1)

val authInterceptor = AwsSignInterceptor(credentialProvider, AP_NORTHEAST_1)

val client = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .build()
```
