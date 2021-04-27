package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.water.WaterService
import improbableotter.sideprojects.pufftime.water.WateringHistoryEvent
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/wateringhistory")
class WateringHistoryKontroller(val growRepo: GrowRepository, val waterService: WaterService) {
    @GetMapping("/{growId}")
    fun getGrowWateringHistoryList(@PathVariable("growId") growId: Long,
                                   @RequestParam(required = false) start:String?,
                                   @RequestParam(required = false) end:String?): List<WateringHistoryEvent> {
        //format dates baby and we is away!
        //TODO we find out if they exist and if they don't we don't use them but first let's just get stuff working
        val startDate:Date = start?.let{ SimpleDateFormat("yyyy-MM-dd").parse(it) }?: Date()
        val endDate:Date= end?.let{ SimpleDateFormat("yyyy-MM-dd").parse(it) }?: Date()

        return waterService.findGrowWateringHistory(growId)

    }
}