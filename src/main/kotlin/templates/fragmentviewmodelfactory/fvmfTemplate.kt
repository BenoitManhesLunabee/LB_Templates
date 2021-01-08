package templates.fragmentviewmodelfactory

import com.android.tools.idea.wizard.template.*

val fragmentViewModelFactoryTemplate
    get() = template {
        revision = 2
        name = "LB Fragment ViewModel Factory"
        description = "Creates a new fragment and a viewModel including a factory."
        minApi = 21
        minBuildApi = 21
        category = Category.Fragment
        formFactor = FormFactor.Mobile
        screens = listOf(
            WizardUiContext.FragmentGallery, WizardUiContext.MenuEntry,
            WizardUiContext.NewProject, WizardUiContext.NewModule,
        )

        val packageNameParam = defaultPackageNameParameter

        val fragmentClassName = stringParameter {
            name = "Fragment Name"
            default = "MyFragment"
            help = "The name of the fragment class to create and use in Activity"
            constraints = listOf(Constraint.NONEMPTY)
        }

        val viewModelClassName = stringParameter {
            name = "ViewModel Name"
            default = "MyFragmentViewModel"
            help = "The name of the viewModel to create for the fragment"
            constraints = listOf(Constraint.NONEMPTY)
            suggest = { "${fragmentClassName.value}ViewModel" }
        }

        widgets(
            TextFieldWidget(fragmentClassName),
            TextFieldWidget(viewModelClassName),
            PackageNameWidget(packageNameParam),
        )

        recipe = { data: TemplateData ->
            fvmfSetup(
                data as ModuleTemplateData,
                packageNameParam.value,
                fragmentClassName.value,
                viewModelClassName.value,
            )
        }
    }

val defaultPackageNameParameter
    get() = stringParameter {
        name = "Package name"
        visible = { !isNewModule }
        default = "com.mycompany.myapp"
        constraints = listOf(Constraint.PACKAGE)
        suggest = { packageName }
    }