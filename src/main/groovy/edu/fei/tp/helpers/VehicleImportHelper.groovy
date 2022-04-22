package edu.fei.tp.helpers

import com.netgrif.application.engine.petrinet.domain.dataset.EnumerationMapField
import com.netgrif.application.engine.petrinet.domain.dataset.FileField
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class VehicleImportHelper {
    public static final Logger log = LoggerFactory.getLogger(VehicleImportHelper.class)

//    static private String pathToCarsCsv = "src/main/resources/csv/cars.csv"
//    static private String csvSplitter = ","


    static List validateCarInput(String file, String splitter){
        // . csv, header row, 2 stlpce
        if (!file.endsWith(".csv")) {
            return []
        }

        def csv = CSVHelper.loadCSV(file)

        String headerRow = csv.get(0).toLowerCase()
        String[] values;
        values = headerRow.split(',');

        if (!(values.length == 2)) {
            return []
        }
        if (!values[0].equals('manufacturer') || !values[1].equals('model')) {
            return []
        }

        for (s in csv) {
            if (!(s =~ '.+'+ splitter +'.+')) {
                return []
            }
        }
        return csv
    }


    static Map getCars(String splitter, List csv) {
        // def csv = CSVHelper.loadCSV(file)
        //def csv = CSVHelper.loadCSV(file)
        def cars = [:]
        csv.eachWithIndex { s, idx ->
            if(idx == 0)
                return

            def vehiclesArr = s.split(splitter)
            def manufacturer = vehiclesArr[0].replaceAll("\\.", "-");
            def model = vehiclesArr[1].replaceAll("\\.", "-");

            if(cars.containsKey(manufacturer)){
                if(!cars[manufacturer].contains(model)){
                    cars[manufacturer].add(model)
                }
            }
            else{
                cars[manufacturer] = []
                cars[manufacturer].add(model)
            }
        }
        return cars
    }
}
