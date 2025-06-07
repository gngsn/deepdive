plugins {
    kotlin("jvm") version "2.0.21"
}

group = "com.gngsn"
version = "1.0-SNAPSHOT"

val kotlinxHtmlVersion = "0.11.0"

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:${kotlinxHtmlVersion}")
//    implementation("org.jetbrains.kotlinx:kotlinx-html-js:${kotlinxHtmlVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinxHtmlVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}