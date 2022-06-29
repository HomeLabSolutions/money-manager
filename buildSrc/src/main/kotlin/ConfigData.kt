object ConfigData {
    const val compileSdkVersion = 32
    const val buildToolsVersion = "32.0.0"
    const val minSdkVersion = 21
    const val targetSdkVersion = 32

    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 2
    private const val versionBuild = 5
    const val versionCode = 1000 * (1000 * versionMajor + 100 * versionMinor + versionPatch) + versionBuild
    const val versionName = "${versionMajor}.${versionMinor}.${versionPatch}.${versionBuild}"
}
