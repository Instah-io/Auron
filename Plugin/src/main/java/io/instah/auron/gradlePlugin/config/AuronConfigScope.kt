package io.instah.auron.gradlePlugin.config

import AuronTarget
import io.instah.auron.gradlePlugin.util.addDependencies
import io.instah.auron.permissions.Permission
import korlibs.io.serialization.xml.Xml
import korlibs.io.util.UUID
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

//TODO: Allow multiple blocks of the same type for whole auron
class AuronConfigScope(
    private val project: Project
) {
    internal var jvmToolchain = 21

    internal var targets = setOf(
        AuronTarget.Android, AuronTarget.Web, AuronTarget.Desktop
    )

    internal val customAndroidPermissions = mutableSetOf<String>()
    internal val customAndroidFeatures = mutableSetOf<String>()

    internal val configUUID = UUID.randomUUID().toString()
    internal var isLibrary = false
    internal var applicationName: String = project.rootProject.name
    internal var applicationId: String = createAppId(applicationName)
        set(value) {
            field = createAppId(value)
        }
    internal var permissions: MutableSet<Permission> = mutableSetOf()
    internal var useCompose: Boolean = true
    internal var manifestConfigureScope: ManifestConfigureScope = ManifestConfigureScope()
    internal var minify = true
    internal var sourceSetBlocks = mutableListOf<NamedDomainObjectContainer<KotlinSourceSet>.() -> Unit>()
    internal var useExperimentalWebOptimizations = false

    internal fun createAppId(appName: String): String {
        return project.group.toString() + "." + appName.lowercase().replace(" ", "_")
            .filter {
                (('0'..'9') + ('a'..'z') + '_').contains(it)
            }
    }

    fun setTargets(
        vararg targets: AuronTarget
    ) {
        if (targets.isEmpty()) throw Exception("Auron cannot be configured with no targets")
        this.targets = targets.toSet()
    }

    fun library() {
        isLibrary = true
    }

    fun disableCompose() {
        useCompose = false
    }

    enum class DependencyType {
        Implementation, Api, CompileOnly
    }

    fun addCustomAndroidPermission(permission: String) {
        customAndroidPermissions.add(permission)
    }

    fun addCustomAndroidFeature(feature: String) {
        customAndroidFeatures.add(feature)
    }

    open inner class AuronDependencyHandler(
        private val dependenciesSet: MutableSet<Pair<DependencyType, Any>>
    ) {
        fun implementation(dependency: Any) {
            dependenciesSet.add(
                DependencyType.Implementation to dependency
            )
        }

        fun api(dependency: Any) {
            dependenciesSet.add(
                DependencyType.Api to dependency
            )
        }

        fun compileOnly(dependency: Any) {
            dependenciesSet.add(
                DependencyType.CompileOnly to dependency
            )
        }
    }

    inner class AuronDependenciesBlock(
        commonMainDependencyList: MutableSet<Pair<DependencyType, Any>>,
        private val sourceSetDependenciesMap: MutableMap<String, MutableSet<Pair<DependencyType, Any>>>
    ) : AuronDependencyHandler(commonMainDependencyList) {
        val desktop = "desktopMain"
        val web = "webMain"
        val mobile = "mobileMain"
        val android = "androidMain"

        operator fun String.invoke(block: AuronDependencyHandler.() -> Unit) {
            val dependenciesSet = mutableSetOf<Pair<DependencyType, Any>>()
            block(AuronDependencyHandler(dependenciesSet))
            if (sourceSetDependenciesMap.contains(this)) {
                sourceSetDependenciesMap[this]!!.addAll(dependenciesSet)
            } else {
                sourceSetDependenciesMap[this] = dependenciesSet
            }
        }
    }

    fun dependencies(
        block: AuronDependenciesBlock.() -> Unit
    ) {
        val commonMainDependencyList = mutableSetOf<Pair<DependencyType, Any>>()
        val sourceSetDependenciesMap = mutableMapOf<String, MutableSet<Pair<DependencyType, Any>>>()

        block(AuronDependenciesBlock(commonMainDependencyList, sourceSetDependenciesMap))

        sourceSetBlocks.add {
            getByName("commonMain").dependencies {
                addDependencies(commonMainDependencyList)
            }

            sourceSetDependenciesMap.forEach { pair ->
                getByName(pair.key).dependencies {
                    addDependencies(pair.value)
                }
            }
        }
    }
    
    fun useExperimentalWebOptimizations() {
        useExperimentalWebOptimizations = true
    }

    fun application(
        name: String,
        configure: ApplicationConfigureScope.() -> Unit = {}
    ) {
        val scope = ApplicationConfigureScope()
        applicationName = name
        isLibrary = false
        configure(scope)
        val appConfig = scope.build()
        if (appConfig.appId != null) {
            applicationId = appConfig.appId
        }
    }

    fun disableMinify() {
        minify = false
    }

    fun jvmToolchain(jdkVersion: Int) {
        jvmToolchain = jdkVersion
    }

    fun manifest(
        configure: ManifestConfigureScope.() -> Unit
    ) {
        configure(manifestConfigureScope)
    }

    data class ManifestConfigureScopeResult(
        val applicationSectionAdditions: List<String>
    )

    class ManifestConfigureScope() {
        internal var processes = mutableListOf<(Xml) -> Xml>()
            private set
        private var applicationSectionAdditions = mutableListOf<Xml>()

        fun addToApplicationSection(
            text: String
        ) {
            processes.add { xml ->
                Xml(
                    name = "manifest",
                    type = xml.type,
                    attributes = xml.attributes,
                    allChildren = xml.allChildren.map {
                        if (it.name == "application") {
                            Xml(
                                name = "application",
                                allChildren = it.allChildren + Xml.Raw(text),
                                attributes = it.attributes,
                                content = it.content,
                                type = it.type
                            )
                        } else it
                    }, content = ""
                )
            }
        }

        //TODO: Make the API easier
        fun addXmlProcess(
            process: (Xml) -> Xml
        ) {
            processes.add(process)
        }
    }

    class ApplicationConfigureScope() {
        private var appId: String? = null

        fun appId(id: String) {
            appId = id
        }

        class ApplicationConfiguration(
            val appId: String?
        )

        fun build(): ApplicationConfiguration {
            return ApplicationConfiguration(appId)
        }
    }

    operator fun Permission.unaryPlus() {
        permissions.add(this)
    }
}