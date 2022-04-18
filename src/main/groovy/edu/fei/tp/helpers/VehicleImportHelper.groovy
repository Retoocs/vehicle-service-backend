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

    static boolean validateCarInput(File file, String splitter){
        def csv = CSVHelper.loadCSV(file)
        csv.each { s ->
            if (!(s =~ '.+,.+')) {
                return false
            }
        }
        return true
    }

    private static Map getCars(String splitter, File file) {
        // def csv = CSVHelper.loadCSV(file)
        def csv = file
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

    void setCars(EnumerationMapField manufacturers, EnumerationMapField models, File file, String splitter){
        def cars = getCars(splitter, file)
        def tmpManufacturers = [:]
        def tmpModels = [:]

        for (entry in cars) {
            tmpManufacturers = tmpManufacturers + [(entry.getKey()):entry.getKey()]
            for(mod in entry.getValue()){
                tmpModels = tmpModels + [(entry.getKey()+"-"+mod):mod]
            }
        }
        change manufacturers options {   tmpManufacturers  }
        change models options {  tmpModels  }

        log.info("Succesfully loaded " + tmpModels.size() + " vehicles into " + useCase.petriNet.title)
    }
}
