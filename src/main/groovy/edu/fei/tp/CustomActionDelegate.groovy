package edu.fei.tp
import com.netgrif.application.engine.petrinet.domain.dataset.EnumerationMapField
import com.netgrif.application.engine.petrinet.domain.dataset.logic.action.ActionDelegate
import edu.fei.tp.helpers.CSVHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class CustomActionDelegate extends ActionDelegate {
    public static final Logger log = LoggerFactory.getLogger(CustomActionDelegate.class)

    static private String pathToCarsCsv = "src/main/resources/csv/cars.csv"
    static private String csvSplitter = ","

    private static Map getCars(String splitter) {
        def csv = CSVHelper.loadCSV(pathToCarsCsv)
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


    void setCars(EnumerationMapField manufacturers, EnumerationMapField models){
        def cars = getCars(csvSplitter)
        for (entry in cars) {
            change manufacturers options {   manufacturers.options + [(entry.getKey()):entry.getKey()];  }
            for(mod in entry.getValue()){
                change models options {  models.options + [(entry.getKey()+"-"+mod):mod];  }
            }
        }

        log.info("Succesfully loaded " + models.options?.size() + " vehicles into " + useCase.petriNet.title)
    }
}
