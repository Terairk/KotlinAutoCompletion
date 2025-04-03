import java.io.File
import java.util.jar.JarFile

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please provide a search term as an argument")
        return
    }

    val searchTerm = args[0]
    findSuggestions(searchTerm)
}

fun findSuggestions(searchTerm: String) {
    // Get the Kotlin runtime JAR path
    val kotlinJars = findKotlinJars()
    if (kotlinJars.isEmpty()) {
        println("Could not locate Kotlin runtime JARs")
        return
    }

    val suggestions = mutableListOf<String>()

    // Scan each Kotlin JAR file for matching classes
    for (jarPath in kotlinJars) {
        try {
            JarFile(jarPath).use { jar ->
                jar.entries().asSequence()
                    .filter { it.name.endsWith(".class") }
                    .map { it.name.replace('/', '.').removeSuffix(".class") }
                    .filter { it.startsWith("kotlin.") && !it.contains('$') } // Exclude inner classes
                    .filter {
                        val simpleName = it.substringAfterLast(".")
                        simpleName.contains(searchTerm, ignoreCase = true)
                    }
                    .forEach { suggestions.add(it) }
            }
        } catch (e: Exception) {
            System.err.println("Error scanning JAR file $jarPath: ${e.message}")
        }
    }

    // Sort and print suggestions
    suggestions.sorted().forEach { println(it) }

    if (suggestions.isEmpty()) {
        println("No suggestions found for '$searchTerm'")
    }
}

fun findKotlinJars(): List<String> {
    // Try to find Kotlin JARs in the classpath
    val classpath = System.getProperty("java.class.path")
    val classpathEntries = classpath.split(File.pathSeparator)

    val kotlinJars = classpathEntries.filter { path ->
        val filename = File(path).name.lowercase()
        filename.contains("kotlin") && filename.endsWith(".jar")
    }

    // If not found in classpath, try common locations
    if (kotlinJars.isEmpty()) {
        val kotlinHome = System.getenv("KOTLIN_HOME")
        if (kotlinHome != null) {
            File(kotlinHome, "lib").listFiles()?.forEach { file ->
                if (file.name.startsWith("kotlin-stdlib") && file.name.endsWith(".jar")) {
                    return listOf(file.absolutePath)
                }
            }
        }
    }

    return kotlinJars
}