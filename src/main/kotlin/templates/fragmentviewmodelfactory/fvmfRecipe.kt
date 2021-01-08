package templates.fragmentviewmodelfactory

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.github.lunabee.lbtemplates.listeners.MyProjectManagerListener.Companion.projectInstance
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import org.jetbrains.kotlin.idea.KotlinLanguage
import templates.fragmentviewmodelfactory.src.app_package.fragmentClass
import templates.fragmentviewmodelfactory.src.app_package.viewModelClass

fun fvmfSetup(
    moduleData: ModuleTemplateData,
    packageName: String,
    fragmentName: String,
    viewModelName: String
) {
    val (projectData) = moduleData
    val project = projectInstance ?: return

    val virtualFiles = ProjectRootManager.getInstance(project).contentSourceRoots
    val virtSrc = virtualFiles.first { it.path.contains("src") }
    val directorySrc = PsiManager.getInstance(project).findDirectory(virtSrc)!!

    fragmentClass(packageName, fragmentName, viewModelName, projectData)
        .save(directorySrc, packageName, "${fragmentName}.kt")

    viewModelClass(packageName, viewModelName, projectData)
        .save(directorySrc, packageName, "${viewModelName}.kt")
}

fun String.save(srcDir: PsiDirectory, subDirPath: String, fileName: String) {
    try {
        val destDir = subDirPath.split(".").toDir(srcDir)
        val psiFile = PsiFileFactory
            .getInstance(srcDir.project)
            .createFileFromText(fileName, KotlinLanguage.INSTANCE, this)
        destDir.add(psiFile)
    } catch (exc: Exception) {
        exc.printStackTrace()
    }
}

fun List<String>.toDir(srcDir: PsiDirectory): PsiDirectory {
    var result = srcDir
    forEach {
        result = result.findSubdirectory(it) ?: result.createSubdirectory(it)
    }
    return result
}