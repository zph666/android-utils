pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 腾讯云 maven 镜像聚合了：central、jcenter、google、gradle-plugin
//        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
        google()
        maven(url = "https://jitpack.io")
        mavenCentral()
    }
}

rootProject.name = "android-utils"
include(":app")
include(":lib_utils")
