package templates.fragmentviewmodelfactory.src.app_package

import com.android.tools.idea.wizard.template.ProjectTemplateData

fun viewModelClass(
    packageName: String,
    viewModelClassName: String,
    projectData: ProjectTemplateData,
    ) =
"""package $packageName

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class $viewModelClassName : ViewModel() {
    // TODO: Implement the ViewModel
}

class ${viewModelClassName}Factory(

) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ${viewModelClassName}(

        ) as T
    }
}"""