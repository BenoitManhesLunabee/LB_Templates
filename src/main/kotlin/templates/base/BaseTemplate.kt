package templates.base

import com.android.tools.idea.wizard.template.*

val baseTemplate    //TODO: To change & put in WizardTemplateProviderImpl
    get() = template {
        revision = 1
        // Name appearing in the templates list TODO: Define Template Name
        name = ""
        // Description TODO: Define Template Description
        description = ""
        minApi = 21 //TODO: Define minApi
        minBuildApi = 21    //TODO: Define minBuildApi
        // Type of template (Activity, Fragment, ...) TODO: Define type
        category = Category.Fragment
        formFactor = FormFactor.Mobile
        // Where the template is available
        screens = listOf(
            WizardUiContext.FragmentGallery, WizardUiContext.MenuEntry,
            WizardUiContext.NewProject, WizardUiContext.NewModule,
        )

        val packageNameParam = defaultPackageNameParameter

        // Input for a Class name TODO: List all input needed (className, layoutName, subPackage, ...)
        val exempleClassName = stringParameter {
            name = "ExempleClass"
            default = "MyFragment"
            help = "The name of the fragment class to create and use in Activity"
            constraints = listOf(Constraint.NONEMPTY)
        }

        val exempleLayoutName = stringParameter {
            name = "Layout Name"
            default = "my_act"
            help = "The name of the layout to create for the activity"
            constraints = listOf(Constraint.LAYOUT, Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { "${activityToLayout(exempleClassName.value.toLowerCase())}s" }
        }

        // Widget used for input TODO: Add widgets corresponding to the inputs
        widgets(
            TextFieldWidget(exempleClassName),
            PackageNameWidget(packageNameParam),
        )

        // Setup recipe: generate and open files TODO: List all values return by the inputs
        recipe = { data: TemplateData ->
            // defined in Recipe.kt
            recipeSetup(
                data as ModuleTemplateData,
                packageNameParam.value,
                exempleClassName.value,
                exempleLayoutName.value,
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