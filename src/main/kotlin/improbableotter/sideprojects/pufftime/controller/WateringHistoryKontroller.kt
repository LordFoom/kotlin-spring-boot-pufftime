package improbableotter.sideprojects.pufftime.controller

import improbableotter.sideprojects.pufftime.event.Event
import improbableotter.sideprojects.pufftime.grow.GrowRepository
import improbableotter.sideprojects.pufftime.water.WaterService
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/wateringhistory")
class WateringHistoryKontroller(val growRepo: GrowRepository, val waterService: WaterService) {
    @GetMapping("/{growId}")
    fun getGrowWateringHistoryList(@PathVariable("growId") growId: Long,
                                   @RequestParam(required = false) start:String?,
                                   @RequestParam(required = false) end:String?): List<Event> {
        //format dates baby and we is away!
        //TODO we find out if they exist and if they don't we don't use them but first let's just get stuff working
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDate:Date = start?.let{ dateFormat.parse(it) }?: Date()
        val endDate:Date= end?.let{ dateFormat.parse(it) }?: Date()

        return waterService.findGrowWateringHistory(growId)

    }
}