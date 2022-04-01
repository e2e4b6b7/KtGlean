package org.jetbrains.research.ktglean

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.*
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(CommandLineProcessor::class)
class KtGleanCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = pluginID

    override val pluginOptions: Collection<CliOption> = listOf(
        CliOption(
            optionName = outputDirectoryCLIKey,
            valueDescription = "Path",
            description = "Path to output directory for resulting JSON files"
        )
    )

    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration
    ) {
        return when (option.optionName) {
            outputDirectoryCLIKey -> configuration.put(KtGleanComponentRegistrar.OUTPUT_DIRECTORY_KEY, value)
            else -> error("Unexpected config option ${option.optionName}")
        }
    }
}
