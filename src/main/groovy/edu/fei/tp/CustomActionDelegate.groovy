package edu.fei.tp
import com.netgrif.application.engine.petrinet.domain.dataset.EnumerationMapField
import com.netgrif.application.engine.petrinet.domain.dataset.logic.action.ActionDelegate
import edu.fei.tp.helpers.AddressImportHelper
import edu.fei.tp.helpers.CSVHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class CustomActionDelegate extends ActionDelegate {
    public static final Logger log = LoggerFactory.getLogger(CustomActionDelegate.class)

    static private String pathToCarsCsv = "src/main/resources/csv/cars.csv"
    static private String pathToAddressCsv = "src/main/resources/csv/address.csv"
    static private String csvSplitter = ","

    @Autowired
    private AddressImportHelper addressImportHelper

    void setAddresses(EnumerationMapField system_cities, EnumerationMapField system_streets){
        def addresses = addressImportHelper.getAddresses(csvSplitter, pathToAddressCsv)
        def i = 0;
        def tmpCities = [:]
        def tmpStreets = [:]
        for (entry in addresses) {
            def addressArr = entry.getKey().split("-");
            def psc = addressArr[0]
            def city = addressArr[1]
            tmpCities = tmpCities + [(psc) : city + " - " + psc]
            for(street in entry.getValue()){
                tmpStreets = tmpStreets + [(i + "-" + psc) : street]
                i++;
            }
        }
        change system_cities options {   tmpCities  }
        change system_streets options {  tmpStreets  }
        log.info("Succesfully loaded " + tmpCities.size() + " cities into " + useCase.petriNet.title)
        log.info("Succesfully loaded " + tmpStreets.size() + " streets into " + useCase.petriNet.title)
    }

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
