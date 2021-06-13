package improbableotter.sideprojects.pufftime.storage

import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate
import javax.imageio.ImageIO
import kotlin.math.roundToInt


@Service
class StorageService {

    val uploadDir = "./uploads/"

    /**
     * @return filepath/thumbnail filepath where the file is stored
     */
    fun store(file: MultipartFile): Pair<String, String> {
        val fileName = file.originalFilename?.let { StringUtils.cleanPath(it) }
        val dirPath = createUploadDirIfNeeded()
        try {
            val path = Paths.get("$dirPath/$fileName")
            Files.copy(file.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
            val filePath = path.toString().removePrefix(".")
            val thumbnailPath = storeThumbNail(path).removePrefix(".")
            return Pair(filePath, thumbnailPath)
        } catch (ioe: IOException) {
            ioe.printStackTrace()
            throw RuntimeException(ioe)
        }
    }

    /**
     * Will create a thumbnail of the image at path
     * @param path the path to be scaled down
     */
    fun storeThumbNail(path: Path):String{
        //find the image
        return storeThumbNail(path.toFile())

    }

    fun storeThumbNail(path: String):String{
        return storeThumbNail(File(path))
    }

    private fun storeThumbNail(originalImgFile: File): String {
        val image = ImageIO.read(originalImgFile)

        //reduce it in size and save it
        //make it 20% size?
        val dWidth = (image.width * 0.2).roundToInt()
        val dHeight = (image.height * 0.2).roundToInt()
        var scaledImg = BufferedImage(dWidth, dHeight, image.type)
        val g2d = scaledImg.createGraphics()
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.drawImage(image, 0, 0, dWidth, dHeight, null)
        //write it to the filesystem

        val newFileName = "${originalImgFile.nameWithoutExtension}_thumb.${originalImgFile.extension}"
        val newFilePath = "${originalImgFile.parentFile.path}/${newFileName}"
        ImageIO.write(scaledImg, originalImgFile.extension, File(newFilePath))

        //get the filename and return it
        return newFilePath
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
