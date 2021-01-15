# LB FILE TEMPLATES PLUGIN
The purpose of this plugin is to generate prefill file to avoid writing all the boilerplate code.

## Import
#### 🚨 Required 🚨
- Kotlin plugin 1.4.2+
- Android studio 4.1+

lb_templates.jar

<kbd>Preferences</kbd> -> <kbd>plugin</kbd> -> <kbd>⚙️</kbd> -> <kbd>install plugin from disk</kbd>

## Templates available
- #### Fragment-ViewModel with factory
  A fragment without layout and a view model with factory.
  
## Use
Identical use to a basic android template

<kbd>some package</kbd> -> <kbd>New</kbd>

## Create
#### Plugin template structure

A generated IntelliJ Platform Plugin Template repository contains the following content structure:

```
src                                             Plugin sources
└── main
    ├── kotlin/                                 Kotlin source files
        ├── com.github.lunabee.lbtemplate/      Handle project
        └── templates                           Templates files
            ├── base/                           Base to create a new template
            ├── ...                             all templates
            └── WizardTemplatePoviderImpl.kt    File to implemente the templates
    ├── java/                                   Java source files
    └── resources/                              Resources - plugin.xml, icons, messages
```

### Template Structure
To create a new template, create a new folder in `src/main/kotlin/templates/`

To go faster, it' possbile to copy `base/` and change all `//TODO` lines in the folder and templates files.

```
somePlugin
    ├── res/                                    res template files
    ├── src/                                    src template files
    ├── Recipe.kt
    └── Template.kt
```
### Templates Files
Templates files contains function to generate each file. To put in respective folder `res/layout` , `src/app_package`
```kotlin
import com.android.tools.idea.wizard.template.ProjectTemplateData
import com.android.tools.idea.wizard.template.extractLetters

fun someActivity(
        packageName: String,
        entityName: String,
        layoutName: String,
        projectData: ProjectTemplateData
) = """
package $packageName

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import ${projectData.applicationPackage}.R;

class ${entityName}sActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.${extractLetters(layoutName.toLowerCase())})
    }
}
"""
fun someActivityLayout(
        packageName: String,
        entityName: String) = """
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="${packageName}.${entityName}sActivity">

</androidx.constraintlayout.widget.ConstraintLayout>
"""
```
### Template.kt
It defines the template’s name, description, and parameters that can be inputted by the user with widgets. It has the following structure:
```kotlin
import com.android.tools.idea.wizard.template.*

val myTemplate
    get() = template {
        revision = 1
        name = ""
        description = ""
        minApi = 21 
        minBuildApi = 21    
        category = Category.Fragment
        formFactor = FormFactor.Mobile
        screens = listOf(
            WizardUiContext.FragmentGallery, WizardUiContext.MenuEntry,
            WizardUiContext.NewProject, WizardUiContext.NewModule,
        )

        val packageNameParam = defaultPackageNameParameter

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

        widgets(
            TextFieldWidget(exempleClassName),
            PackageNameWidget(packageNameParam),
        )

        recipe = { data: TemplateData ->
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
```
- **template :**
Element defining the file group template.

- **name :**
name of the template, visible to the user in the new file list

- **description :**
template description, visible at the top of the new file group dialog

- **category :**
category in which the template would show in, it creates a new category in the file picker if there was no such category.

All category in the `enum class Category`

- **formFactor :**
Type of app device (Mobile, Tv, ...). All category in the `enum class FormFactor`

- **screens :**
Where the template is available

- **packageNameParam**
Define the app package with the methode `defaultPackageNameParameter` bellow

- **recipe :**
Use the `recipe methode` define in the `Recipe.kt` to create files. Take all parameters needed for file creation

- **widgets :**
List of widget used to enter user inputs (class name, package name, check box, ...)

#### Parameters
A parameter can be define by user with input enter in the widgets. It's used for exemple a class name or package name. To define an inputs
parameter
- **name :** parameter name visible to the user
- **default :** default value of the parameter
- **constraints :**  constraints of the input, can take following values: layout, unique, nonempty, package;
- **suggest :** value parameter suggestion, can reference other parameters, e.g. we could use this feature to generate an activity’s layout name based on a feature name :
`activity_${featureName}`
- **help :** — optional parameter that defines a hint that appears at the bottom of the new component dialog when the user is editing a particular parameter’s value.


### Recipe.kt
Recipe files contain instructions for the processor pertaining to file creation and modification. The template file functions are called here. It has the following structure:
```kotlin
import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.github.lunabee.lbtemplates.listeners.MyProjectManagerListener.Companion.projectInstance
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import org.jetbrains.kotlin.idea.KotlinLanguage
import templates.base.res.layout.someActivityLayout
import templates.base.src.app_package.someActivity

fun recipeSetup(
    moduleData: ModuleTemplateData,
    packageName: String,
    entityName: String,
    layoutName: String,
) {
    val (projectData) = moduleData
    val project = projectInstance ?: return

    val virtualFiles = ProjectRootManager.getInstance(project).contentSourceRoots
    val virtSrc = virtualFiles.first { it.path.contains("src") }
    val virtRes = virtualFiles.first { it.path.contains("res") }
    val directorySrc = PsiManager.getInstance(project).findDirectory(virtSrc)!!
    val directoryRes = PsiManager.getInstance(project).findDirectory(virtRes)!!

    someActivity(packageName, entityName, layoutName, projectData)
        .save(directorySrc, packageName, "${entityName}sActivity.kt")

    someActivityLayout(packageName, entityName)
        .save(directoryRes, "layout", "${layoutName}.xml")
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
```

#### Conversions methods
Beside standard Freemarker built-in methods you can also use conversion methods added by JetBrains. There’s no official reference for those, but all of them can be seen in the source code of their FreemarkerUtil::createParameterMap method:
```java
public static Map<String, Object> createParameterMap(@NotNull Map<String, Object> args) {
    // Create the data model.
    final Map<String, Object> paramMap = new HashMap<>();

    // Builtin conversion methods
    paramMap.put("activityToLayout", new FmActivityToLayoutMethod());
    paramMap.put("camelCaseToUnderscore", new FmCamelCaseToUnderscoreMethod());
    paramMap.put("classToResource", new FmClassNameToResourceMethod());
    paramMap.put("compareVersions", new FmCompareVersionsMethod());
    paramMap.put("compareVersionsIgnoringQualifiers", new FmCompareVersionsIgnoringQualifiersMethod());
    paramMap.put("escapePropertyValue", new FmEscapePropertyValueMethod());
    paramMap.put("escapeXmlAttribute", new FmEscapeXmlAttributeMethod());
    paramMap.put("escapeXmlString", new FmEscapeXmlStringMethod());
    paramMap.put("escapeXmlText", new FmEscapeXmlStringMethod());
    paramMap.put("extractLetters", new FmExtractLettersMethod());
    paramMap.put("getAppManifestDir", new FmGetAppManifestDirMethod(paramMap));
    paramMap.put("getApplicationTheme", new FmGetApplicationThemeMethod(paramMap));
    paramMap.put("isAndroidxEnabled", new FmIsAndroidxEnabledMethod(paramMap));
    paramMap.put("getConfigurationName", new FmGetConfigurationNameMethod(paramMap));
    paramMap.put("getMaterialComponentName", new FmGetMaterialComponentNameMethod());
    paramMap.put("hasDependency", new FmHasDependencyMethod(paramMap));
    paramMap.put("isGradleComponentPluginUsed", new FmIsGradleComponentPluginUsed(paramMap));
    paramMap.put("layoutToActivity", new FmLayoutToActivityMethod());
    paramMap.put("slashedPackageName", new FmSlashedPackageNameMethod());
    paramMap.put("truncate", new FmTruncateStringMethod());
    paramMap.put("underscoreToCamelCase", new FmUnderscoreToCamelCaseMethod());
    paramMap.put("escapeKotlinIdentifiers", new FmEscapeKotlinIdentifierMethod());

    // Dependencies multimap. Doesn't store duplicates, preserves insertion order.
    paramMap.put(TemplateMetadata.ATTR_DEPENDENCIES_MULTIMAP, LinkedHashMultimap.create());

    // Parameters supplied by user
    paramMap.putAll(args);

    return paramMap;
  }
```

## Useful links
[Guide used for this plugin](https://steewsc.medium.com/template-plugin-for-android-studio-4-1-92dcbc689d39)

[Old template version guide](https://medium.com/codequest/file-group-templates-in-android-studio-unofficial-guide-85dfa0a0c1ec)
Here are listed all conversions methods
