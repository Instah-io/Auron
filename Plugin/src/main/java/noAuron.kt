import korlibs.io.file.std.localVfs
import kotlinx.coroutines.runBlocking
import org.gradle.api.Project

fun Project.noAuron() {
    runBlocking {
        localVfs(project.projectDir.absolutePath)[".noAuron"].writeBytes(byteArrayOf(0))
    }
}