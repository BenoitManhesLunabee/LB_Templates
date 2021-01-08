package templates.fragmentviewmodelfactory.src.app_package

import com.android.tools.idea.wizard.template.ProjectTemplateData

fun fragmentClass(
    packageName: String,
    fragmentClassName: String,
    viewModelClassName: String,
    projectData: ProjectTemplateData,
) = """package $packageName

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class $fragmentClassName : Fragment() {

    private val viewModel: $viewModelClassName by viewModels {
        ${viewModelClassName}Factory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
    // TODO: Observe viewModel's LiveData
    }

}
"""