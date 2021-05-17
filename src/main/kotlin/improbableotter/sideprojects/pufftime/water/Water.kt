package improbableotter.sideprojects.pufftime.water

import improbableotter.sideprojects.pufftime.nute.Feed
import improbableotter.sideprojects.pufftime.grow.Grow
import org.springframework.data.jpa.repository.JpaRepository
import java.text.SimpleDateFormat
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
                return "Watered ${literAmount}"
        }

        @Transient
        val nuteDateFmt = SimpleDateFormat("yyyy-MM-dd")

        @Transient
        val nonNoteDateFmt = SimpleDateFormat("yyyy-MM-dd'T12:00'")
        /**
         * This will make nute waterings stand out from non-nute waterings
         */
        fun getAppropriateDate():String{
            if(nutes == NuteStatus.NUTES)
                    return nuteDateFmt.format(wateringDate?:createDate)

            return nonNoteDateFmt.format(wateringDate?:createDate)

        }
}

enum class NuteStatus{
        NONE, NUTES
}



interface WateringHistoryRepo: JpaRepository<WateringHistory, Long>{
        fun findByGrowOrderByWateringDateDesc(grow: Grow):List<WateringHistory>
        fun findByGrowAndWateringDateBetween(grow:Grow, start:Date, end:Date):List<WateringHistory>
        fun findAllByGrowAndWateringDateGreaterThan(grow:Grow, start:Date):List<WateringHistory>
        fun findByGrowAndWateringDateLessThan(grow:Grow, end:Date):List<WateringHistory>
}

