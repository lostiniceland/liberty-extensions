package default

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class PackageSubsystemTask : DefaultTask() {

    init {
        group = "export"
        description = "Exports a OSGI Subsystem to a Enterprise-Subsystem-Archive (ESA)"
    }

    @TaskAction
    fun doSomething(){
        println("Hello from Task")
    }
}