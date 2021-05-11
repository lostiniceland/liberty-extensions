plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
    id("java-gradle-plugin")
}


// require
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly( gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.32")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.4.32")
    testCompileOnly(gradleTestKit())
    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.6.2")
}

gradlePlugin {
    plugins {
        register("package-feature") {
            id = "package-feature"
            implementationClass = "default.PackageSubsystemPlugin"
        }
    }
}