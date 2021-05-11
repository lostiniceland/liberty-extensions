plugins {
    id ("io.openliberty.tools.gradle.Liberty") version "3.1.2"
    //`package-feature`
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    //libertyFeature(files("${buildDir}/liberty-extension-repo/liberty.logback.feature_1.0.0.esa"))
}


tasks {
//    register("test"){
//        println("FOOBAR")
//    }
//    withType<Wrapper>(){
//
//    }
//    withType<InitBuild>(){
//        finalizedBy("test")
//        finalizedBy(io.openliberty.tools.gradle.tasks.InstallLibertyTask.TASK_ACTION)
//    }
//    withType<JavaCompile>(){
//        dependsOn("test")
//    }
}

liberty {
    baseDir = "${projectDir}/cnf/liberty"
}