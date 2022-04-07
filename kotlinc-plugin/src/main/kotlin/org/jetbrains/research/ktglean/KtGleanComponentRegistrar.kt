package org.jetbrains.research.ktglean

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import java.nio.file.Path
import kotlin.io.path.exists


@AutoService(ComponentRegistrar::class)
class KtGleanComponentRegistrar : ComponentRegistrar, KoinComponent {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
        startKoin {
            modules(builders)
        }

        val outputPath = buildOutputPath(configuration)
        val indexersRegistrar = buildIndexersRegistrar(project, outputPath)
        FirExtensionRegistrar.registerExtension(project, indexersRegistrar)
    }

    private fun buildOutputPath(configuration: CompilerConfiguration): Path {
        val rootPath = configuration[OUTPUT_DIRECTORY_KEY] ?: error("Output directory is required")
        val moduleName = configuration[CommonConfigurationKeys.MODULE_NAME] ?: error("Module name expected")
        val path = Path.of(rootPath).resolve(moduleName)
        if (path.exists()) return path.addFreeIndex()
        return path
    }

    private fun buildIndexersRegistrar(project: MockProject, path: Path): FirExtensionRegistrar {
        val storage = storage(path)
        Disposer.register(project, storage)

        val indexers = indexers.map { it(storage).singletonExtension() }

        return object : FirExtensionRegistrar() {
            override fun ExtensionRegistrarContext.configurePlugin() = indexers.forEach { +it }
        }
    }

    companion object {
        val OUTPUT_DIRECTORY_KEY = CompilerConfigurationKey.create<String>("$pluginID.$outputDirectoryCLIKey")
    }
}
