package edu.fei.tp.helpers

import com.netgrif.application.engine.petrinet.domain.dataset.EnumerationMapField
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class VehicleImportHelper {
    public static final Logger log = LoggerFactory.getLogger(VehicleImportHelper.class)

//    static private String pathToCarsCsv = "src/main/resources/csv/cars.csv"
//    static private String csvSplitter = ","


    static boolean validateCarInput(String file, String splitter){
        def csv = CSVHelper.loadCSV(file)
        csv.each { s ->
            if (!(s =~ '.+,.+')) {
                return false
            }
        }
        return true
    }
}
