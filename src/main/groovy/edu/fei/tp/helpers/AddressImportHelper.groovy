package edu.fei.tp.helpers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AddressImportHelper {
    public static final Logger log = LoggerFactory.getLogger(AddressImportHelper.class)

    static Map getAddresses(String splitter, String pathToAddressCsv){
        def csv = CSVHelper.loadCSV(pathToAddressCsv)
        def addresses = [:]
        csv.eachWithIndex { s, idx ->
            if(idx == 0)
                return
            def addressArr = s.split(splitter)
            def street = addressArr[0]
            def psc = addressArr[1]
            def city = addressArr[2]

            if(street == null || street == "" ||
                    psc == null || psc == "" ||
                    city == null || city == "" ) {
                return
            }

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
}
