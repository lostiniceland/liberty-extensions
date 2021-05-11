package default

import org.gradle.api.Plugin
import org.gradle.api.Project

class PackageSubsystemPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project){
            tasks.create("package-feature", PackageSubsystemTask::class.java)
        }
    }


}