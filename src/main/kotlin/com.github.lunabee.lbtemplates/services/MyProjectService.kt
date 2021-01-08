package com.github.lunabee.lbtemplates.services

import com.intellij.openapi.project.Project
import com.github.lunabee.lbtemplates.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
