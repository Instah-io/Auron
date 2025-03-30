import com.github.ajalt.mordant.rendering.TextColors.brightRed
import com.github.ajalt.mordant.rendering.TextStyles.bold
import io.instah.auron.gradlePlugin.android.AndroidTargetLogic
import io.instah.auron.gradlePlugin.config.AuronConfigScope
import io.instah.auron.gradlePlugin.desktop.DesktopTargetLogic
import io.instah.auron.gradlePlugin.util.introducePropertyIfNotPresent
import io.instah.auron.gradlePlugin.util.updateAdditionalProperties
import io.instah.auron.gradlePlugin.web.WebTargetLogic
import korlibs.io.file.std.localVfs
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

//TODO: Add support for minify
//TODO: Split the code into functions
fun Project.auron(
    block: AuronConfigScope.() -> Unit = {}
) {
    if ((group as? String)?.isNotBlank() == false) throw Exception("You must set a group")

    val scope = AuronConfigScope(this)
    scope.block()

    updateAdditionalProperties(scope)

    introducePropertyIfNotPresent(
        file = localVfs(project.rootProject.projectDir.absolutePath)["gradle.properties"],
        property = "kotlin.mpp.applyDefaultHierarchyTemplate", value = "false"
    )

    if (project.extensions.findByType(KotlinMultiplatformExtension::class.java) == null) {
        project.logger.warn(bold(
            brightRed(
                "Auron: Gradle refresh required to apply required gradle plugins for the project: ${project.name}"
            )
        ))
        return
    }

    val targetLogics = scope.targets.map {
        when (it) {
            AuronTarget.Web -> WebTargetLogic()
            AuronTarget.Android -> AndroidTargetLogic()
            AuronTarget.Desktop -> DesktopTargetLogic()
        }
    }

    targetLogics.forEach { logic ->
        logic.config = scope
        logic.executeInProject(project)
    }

    project.extensions.configure(KotlinMultiplatformExtension::class.java) { extension ->
        extension.jvmToolchain(scope.jvmToolchain)

        val sourceSets = targetLogics.associate { logic ->
            logic to logic.executeInKotlinBlock(extension)
        }

        val commonMain = extension.sourceSets.getByName("commonMain")
        sourceSets.forEach { sourceSet ->
            sourceSet.value.dependsOn(commonMain)
        }

        targetLogics.mapNotNull { it.belongsToSourceSetGroup }.toSet().forEach { sourceSetGroup ->
            val sourceSetGroupSourceSet = extension.sourceSets.create(sourceSetGroup)
            targetLogics.filter { it.belongsToSourceSetGroup == sourceSetGroup }.forEach {
                sourceSets[it]!!.dependsOn(sourceSetGroupSourceSet)
            }
        }

        commonMain.dependencies {
            implementation(auron.sdk)
            if (!scope.isLibrary) {
                implementation(auron.appSdk)
            }
        }

        extension.sourceSets.run {
            scope.sourceSetBlocks.forEach {
                it()
            }
        }
    }
}