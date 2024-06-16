plugins {
    kotlin("jvm") version "2.0.0"
}

version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Security
    implementation("org.bouncycastle:bc-fips:1.0.2.5")

    // Encoding
    implementation("commons-codec:commons-codec:1.16.1")
}
