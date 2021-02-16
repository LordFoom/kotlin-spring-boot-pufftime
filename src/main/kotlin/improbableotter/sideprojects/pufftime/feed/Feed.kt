package improbableotter.sideprojects.pufftime.feed

import improbableotter.sideprojects.pufftime.grow.Grow
import improbableotter.sideprojects.pufftime.water.WateringHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
@Table(name="feed")
class Feed(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @OneToOne
    @JoinColumn(name="watering_history_id")
    var wateringHistory: WateringHistory,
    var notes: String? = null,
    @OneToMany(mappedBy = "feed", cascade = [CascadeType.ALL])
    var nutes: List<Nute> = ArrayList(),
    val createDate: Date = Date(),
    var lastUpdate: Date = Date(),
)

//go via wateringhistopry
//interface FeedRepository : JpaRepository<Feed, Long> {
//    fun findAllBy(grow: Grow?)
//}

/**
 * Intended to be loosy goosy  - maybe it has a nuteType, maybe it doesn't
 */
@Entity
@Table(name="nute")
class Nute(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "nute_type_id")
    var type: NuteType? = null,
    var notes: String? = null,
    var pH: Double? = 7.0,
    /**
     * milliliter per Liter - 1 ml/l is 1000 ppm (parts per million)
     */
    var mlPerL: Double? = 1.0,
    @ManyToOne
    @JoinColumn(name = "feed_id")
    var feed: Feed? = null,
)

@Entity
@Table(name="nute_type")
class NuteType(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    @OneToMany(mappedBy = "type", cascade = [CascadeType.ALL])
    var nutes: List<Nute>? = ArrayList(),
)


