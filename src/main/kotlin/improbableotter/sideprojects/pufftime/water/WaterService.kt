package improbableotter.sideprojects.pufftime.water

import improbableotter.sideprojects.pufftime.grow.GrowRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class WaterService(private val growRepository: GrowRepository, private val wateringHistoryRepo: WateringHistoryRepo) {


    /**
     * This will bring back fullcalendar events, which we intend to use on the frontend
     */
    fun findWateringHistoryBetween(growId: Long, start: Date, end: Date): List<WateringHistoryEvent> {
        val grow = growRepository.findByIdOrNull(growId)!!
        val historyOfWater =
            wateringHistoryRepo.findAllByGrowAndWateringDateBetween(grow, start, end)
        return historyOfWater.map {
           WateringHistoryEvent(id=it.id!!, start=it.wateringDate?:Date(), title = it.title())
        }
    }

    fun findGrowWateringHistory(growId:Long):List<WateringHistoryEvent>{
       return growRepository.findByIdOrNull(growId)!!
           .wateringHistory
           .map{ WateringHistoryEvent(id=it.id!!, start=it.wateringDate!!, title=it.title() ) }
    }

//    fun findWateringHistoryBefore(growId: Long, end:Date):List<WateringHistoryEvent>{
//
//    }
}