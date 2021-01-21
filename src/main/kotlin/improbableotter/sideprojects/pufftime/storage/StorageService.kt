package improbableotter.sideprojects.pufftime.storage

import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


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
        File(uploadDir).mkdir()
        try {
            val path = Paths.get(uploadDir + fileName)
            Files.copy(file.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
            return path.toString()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
            throw RuntimeException(ioe)
        }
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
