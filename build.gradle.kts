val kotlinVersion: String by project
val projectVersion: String by project
val jsoupVersion: String by project

plugins {
    kotlin("platform.jvm")
}

group = "ru.pearx.cheapcsv"
version = projectVersion

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("org.jsoup:jsoup:$jsoupVersion")
}