# Auron
### Making android project development easier

**Auron allows you to:**
- have a main function
- have some parts of the android api simplified
- have an easy api for permissions

1. Add the auron plugin by using `id("io.instah.Auron-Gradle") version "1.1.0"` *for the newest features append
`-SNAPSHOT` to the version
2. Download the android sdk version `36`

You should be now able to easily configure your project with auron!

\*simplification of the process is planned

*add a way for developers to directly interact with the MainActivity

TODO:
- permission API on desktop
- implement KSP
- support widgets
- support rotation
- light sensor API
- proximity sensor API
- accelerometer API
- location API
- automatically download the android SDK
- automatically install android plugins
- support projects that use newer kotlin versions
- automatic svg -> xml conversion
- setting the app icon
- minification support
- stop using android.application and android.library plugins
- common native API composables (like camera view)
- support for translations that don't use XML
- support for apple targets
- support for translation AI models
- support for localized formats for ex. dates
- compose for android native