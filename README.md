# ComponentCraft
### Implement
``` kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/FerdiStro/ComponentCraft")
        credentials {
            username = "GITHUB_USERNAME" // Your Username
            password = "GITHUB_TOKEN"    // Your Access-token
        }
    }
}
dependencies {
    implementation 'com.FerdiStro:componentcraft:VERSION'
}
```