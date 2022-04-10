package edu.fei.tp
import com.netgrif.application.engine.petrinet.domain.dataset.EnumerationMapField
import com.netgrif.application.engine.petrinet.domain.dataset.logic.action.ActionDelegate
import edu.fei.tp.helpers.CSVHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AddressImportHelper extends ActionDelegate {
    public static final Logger log = LoggerFactory.getLogger(AddressImportHelper.class)
    static private String pathToAddressCsv = "src/main/resources/csv/address.csv"
    static private String csvSplitter = ";"


    private static Map getAddresses(String splitter){
        def csv = CSVHelper.loadCSV(pathToAddressCsv)
        def addresses = [:]
        csv.eachWithIndex { s, idx ->
            if(idx == 0)
                return
            def addressArr = s.split(splitter)
            def street = addressArr[0]
            def psc = addressArr[1]
            def city = addressArr[2]

            if(addresses.containsKey(psc+"-"+city)){
                if(!addresses[psc+"-"+city].contains(street)){
                    addresses[psc+"-"+city].add(street)
                }
            }
            else{
                addresses[psc+"-"+city] = []
                addresses[psc+"-"+city].add(street)
            }
        }
        return addresses
    }

    void setAddresses(EnumerationMapField system_cities, EnumerationMapField system_streets){
        def addresses = getAddresses(csvSplittr)
        def i = 0;
        for (entry in addresses) {
            def addressArr = entry.getKey().split("-");
            def psc = addressArr[0]
            def city = addressArr[1]
            change system_cities options {   system_cities.options + [(psc):city];  }
            for(street in entry.getValue()){
                change system_streets options {  system_streets.options + [(i+"-"+psc):street];  }
                i++;
            }
        }

        log.info("Succesfully loaded " + system_streets.options?.size() + " vehicles into " + useCase.petriNet.title)
    }
}
