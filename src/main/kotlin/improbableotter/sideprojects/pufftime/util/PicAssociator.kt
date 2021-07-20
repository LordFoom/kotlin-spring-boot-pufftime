package improbableotter.sideprojects.pufftime.util

import java.sql.DriverManager
import java.util.*

/**
 * Created to refactor the picture model:
 * <ul>
 *     <li>Take the column id from the table</li>
 *     <li>Use it to create the new table</li>
 *     <li>Put the id of the original entity from the eg grow_id column in, along with the picture id</li>
 *     <li>Do for both grow and picture</li>
 *     <li>Drop the columns</li>
*  </ul>
 */
class PicAssociator {

    fun reassociate(colName:String){
        //connect to the database
        val props = Properties()
        props["user"] = "root"
        props["password"] = "qwerty"

        DriverManager.getConnection("jdbc:mysql://localhost:3306/pufftimedb", props)
            .use{ conn ->
                conn.createStatement().use{ stmt ->
                    //create the new table if it does not exist
                    val tableName  = "${colName.split("_")[0]}_picture"
                    stmt.executeUpdate("create table if not exists $tableName($colName bigint, picture_id bigint)" )
                    val preparedStatement = conn.prepareStatement("insert into $tableName ($colName, picture_id) VALUES (?,?)")
                    //now we we want to create our new entries - could totally do this with a straight sql select
                    stmt.executeQuery("select id, $colName from picture where $colName>0")
                        .use{rs ->
                            while (rs.next()) {
                                preparedStatement.setLong(1, rs.getLong(colName))
                                preparedStatement.setLong(2, rs.getLong("id"))
                                preparedStatement.executeUpdate()
                            }
                        }
                }
            }
    }

}
fun main(){
    val pa = PicAssociator()
    pa.reassociate("grow_id")
    pa.reassociate("plant_id")
}
