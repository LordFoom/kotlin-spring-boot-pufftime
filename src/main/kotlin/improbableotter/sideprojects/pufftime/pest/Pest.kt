package improbableotter.sideprojects.pufftime.pest

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "pest")
class Pest (
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
  val name: String="",
  val notes: String?=null,
  val createDate: Date = Date(),
  val lastUpdate: Date = Date(),
 )