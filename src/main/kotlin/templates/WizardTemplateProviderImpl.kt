package templates

import com.android.tools.idea.wizard.template.Template
import com.android.tools.idea.wizard.template.WizardTemplateProvider
import templates.fragmentviewmodelfactory.fragmentViewModelFactoryTemplate

class WizardTemplateProviderImpl : WizardTemplateProvider() {
    override fun getTemplates(): List<Template> =
        listOf(
            fragmentViewModelFactoryTemplate,
        )
}
