package improbableotter.sideprojects.pufftime.storage

import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate


@Service
class StorageService {

    val uploadDir = "./uploads/"
    fun init() {

    }

    /**
     * @return filepath where the file is stored
     */
    fun store(file: MultipartFile): String {
        val fileName = file.originalFilename?.let { StringUtils.cleanPath(it) }
        val dirPath = createUploadDirIfNeeded()
        try {
            val path = Paths.get("$dirPath/$fileName")
            Files.copy(file.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
            return path.toString().removePrefix(".")
        } catch (ioe: IOException) {
            ioe.printStackTrace()
            throw RuntimeException(ioe)
        }
    }

    private fun createUploadDirIfNeeded():String {
        val now = LocalDate.now()
        val pathname = "$uploadDir${now.year}/${now.monthValue}/"
        val mkdir = File(pathname).mkdirs()
        //chop off the first . so we can correctly display it
        return pathname
    }

//    fun loadAll(): Stream<Path?>?{
//
//    }
//
//    fun load(filename: String?): Path?{
//
//    }
//
//    fun loadAsResource(filename: String?): Resource?{
//
//    }

    fun deleteAll() {

    }

}
