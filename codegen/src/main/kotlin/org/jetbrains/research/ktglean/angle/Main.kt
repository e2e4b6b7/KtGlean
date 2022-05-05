package org.jetbrains.research.ktglean.angle

import kotlinx.cli.*
import java.nio.file.Path

fun main(args: Array<String>) {
    parseArgs(args).generate()
}

fun parseArgs(args: Array<String>): Command {
    val parser = ArgParser("")
    val basePackage by parser.option(
        ArgType.String,
        fullName = "basePackage",
        shortName = "p",
        description = "Base package for generated data classes"
    ).default("")
    val out by parser.option(
        ArgType.String,
        fullName = "output",
        shortName = "o",
        description = "Root directory for generated files"
    ).required()
    val srcRoot by parser.argument(
        ArgType.String,
        description = "Root directory of schema files"
    )
    parser.parse(args)
    val sources = Path.of(srcRoot).toFile().walk()
        .filter { it.isFile }
        .map { it.toPath() }
        .toList()
    return Command(sources, Path.of(out), basePackage)
}
