package improbableotter.sideprojects.pufftime.storage

import java.sql.DriverManager
import java.util.*

/**
 * Intended to create thumbnails from fullsize files stored in the db
 */
class ThumbnailCreator {


    fun createThumbnails() {
        //connect to the database
        val props = Properties()
        props["user"] = "root"
        props["password"] = "zxcvbnm"

        val storageService = StorageService()
        val pathPrefix = "/home/lordfoom/IdeaProjects/kotlin-spring-boot-pufftime"

        DriverManager
            .getConnection("jdbc:mysql://localhost:3306/pufftimedb", props)
            .use { conn ->
                //get list of existing pictures
                conn.createStatement().use{ stmt ->
                    stmt.executeQuery("select id,file_path from picture").use{ rs->
                        while (rs.next()) {
                            val path = pathPrefix+rs.getString("file_path")
                            //thumbail those pics
                            println("Reading: ${path}")
                            val thumbPath = storageService.storeThumbNail(path)
                            //we need to remove the directory again
                            val strippedThumbPath = thumbPath.replace(pathPrefix, "")
                            println("Stored ${thumbPath}, updating db...")
                            conn.createStatement().use {
                                it.executeUpdate("update picture set small_file_path = '$strippedThumbPath' where id = ${rs.getLong(1)}")
                            }
                            println("DB Updated with $strippedThumbPath" )
                        }
                    }
                }
            }
    }
}

fun main() {
    println("Starting thumbnailing...")
    ThumbnailCreator().createThumbnails()
}