package edu.fei.tp.helpers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AddressImportHelper {
    public static final Logger log = LoggerFactory.getLogger(AddressImportHelper.class)

    static List validateAddressesInput(String file, String splitter){
        if (!file.endsWith(".csv")) {
            return []
        }
        def csv = CSVHelper.loadCSV(file)

        String headerRow = csv.get(0).toLowerCase()
        String[] values;
        values = headerRow.split(',');

        if (!(values.length == 3)) {
            return []
        }
        if (!values[0].equals('ulica') || !values[1].equals('psc') || !values[2].equals('obec')) {
            return []
        }

        for (s in csv) {
            if (!(s =~ '.+'+ splitter +'.+')) {
                return []
            }
        }
        return csv

    }

    static Map getAddresses(String splitter, List csv){
//        def csv = CSVHelper.loadCSV(pathToAddressCsv)
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
