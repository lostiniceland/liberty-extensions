pluginManagement {
  plugins {
    id("biz.aQute.bnd.workspace") version "5.3.0"
  }
  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
  }
}
plugins {
  id ("biz.aQute.bnd.workspace")
}
//buildscript {
//  repositories {
//    maven {
//      setUrl("https://bndtools.jfrog.io/bndtools/libs-snapshot")
//    }
//  }
//  dependencies {
//    classpath("biz.aQute.bnd:biz.aQute.bnd.gradle:+")
//  }
//}
//
//apply<aQute.bnd.gradle.BndWorkspacePlugin>()

