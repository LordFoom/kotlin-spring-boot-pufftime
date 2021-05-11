package improbableotter.sideprojects.pufftime.water

import improbableotter.sideprojects.pufftime.nute.Feed
import improbableotter.sideprojects.pufftime.grow.Grow
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "watering_history")
/**
 * This baby can fit a lot of h2woah
 */
class WateringHistory(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long? = null,
        var wateringDate: Date? = null,
        var literAmount:Double? = 0.0,
        /** optional feed, bruv*/
        @OneToOne
        @JoinColumn(name = "feed_id")
        var feed: Feed? = null,
        @ManyToOne
        @JoinColumn(name="grow_id")
        var grow: Grow? = null,
        var notes: String? = null,
        var pH: Double? = 7.0,
        var createDate: Date = Date(),
        var nutes: NuteStatus = NuteStatus.NONE,

        ){
        fun title():String{
                return "Watering"
        }
}

enum class NuteStatus{
        NONE, NUTES
}



interface WateringHistoryRepo: JpaRepository<WateringHistory, Long>{
        fun findAllByGrowOrderByCreateDateDesc(grow: Grow):List<WateringHistory>
        fun findAllByGrowAndWateringDateBetween(grow:Grow, start:Date, end:Date):List<WateringHistory>
}

