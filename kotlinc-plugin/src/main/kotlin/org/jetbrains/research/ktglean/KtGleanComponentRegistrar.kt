package org.jetbrains.research.ktglean

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar
import java.nio.file.Path


@AutoService(ComponentRegistrar::class)
class KtGleanComponentRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
        val outputPath = buildOutputPath(configuration)
        val indexersRegistrar = buildIndexersRegistrar(project, outputPath)
        FirExtensionRegistrar.registerExtension(project, indexersRegistrar)
    }

    private fun buildOutputPath(configuration: CompilerConfiguration): Path {
        val rootPath = configuration[OUTPUT_DIRECTORY_KEY] ?: error("Output directory is required")
        val moduleName = configuration[CommonConfigurationKeys.MODULE_NAME] ?: error("Module name expected")
        return Path.of(rootPath).resolve(moduleName)
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
