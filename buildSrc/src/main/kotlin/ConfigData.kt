object ConfigData {
    const val compileSdkVersion = 33
    const val buildToolsVersion = "33.0.0"
    const val minSdkVersion = 21
    const val targetSdkVersion = 33

    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 3
    private const val versionBuild = 2
    const val versionCode = 1000 * (1000 * versionMajor + 100 * versionMinor + versionPatch) + versionBuild
    const val versionName = "${versionMajor}.${versionMinor}.${versionPatch}.${versionBuild}"
}

