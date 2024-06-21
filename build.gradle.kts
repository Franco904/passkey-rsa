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
    implementation("org.bouncycastle:bcpkix-fips:1.0.7")

    // Encoding
    implementation("commons-codec:commons-codec:1.16.1")

    // Storage
    implementation("org.xerial:sqlite-jdbc:3.45.3.0")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("org.slf4j:slf4j-log4j12:2.0.13")

    // Fake data generation
    implementation("io.github.serpro69:kotlin-faker:1.16.0")
}
