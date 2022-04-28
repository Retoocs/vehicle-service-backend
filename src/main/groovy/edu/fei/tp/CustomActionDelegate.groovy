package edu.fei.tp
import com.netgrif.application.engine.petrinet.domain.dataset.EnumerationMapField
import com.netgrif.application.engine.petrinet.domain.dataset.Field
import com.netgrif.application.engine.petrinet.domain.dataset.FileField
import com.netgrif.application.engine.petrinet.domain.dataset.logic.action.ActionDelegate
import edu.fei.tp.helpers.AddressImportHelper
import edu.fei.tp.helpers.CSVHelper
import edu.fei.tp.helpers.VehicleImportHelper
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

    boolean setAddresses(EnumerationMapField system_cities, EnumerationMapField system_streets,String file=pathToAddressCsv){
        csv = addressImportHelper.validateAddressesInput(file, ",")
        if (csv == []) {
            return false
        }

        def addresses = addressImportHelper.getAddresses(csvSplitter, csv)
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

        return true
    }

    private List<Map<String, Field>> getDataFromReferencedTasks(List<String> taskIds){
        def resultList = []
        for(taskId in taskIds){
            def atask = taskService.findOne(taskId)
            if(atask != null){
                resultList.add(getData(atask))
            }
        }
        return resultList
    }

    void getAndSetVehicleManufacturerAndModel(){
        def listOfTaskData = getDataFromReferencedTasks((List<String>) useCase.dataSet['enum_form'].value)
        if(listOfTaskData.isEmpty())
            return

        def data = listOfTaskData.get(0)

        String tmpManufacturer, tmpModel
        if(data['is_undefined_manufacturer'].value)
            tmpManufacturer = data['undefined_manufacturer'].value
        else
            tmpManufacturer = data['manufacturers'].options?.get(data['manufacturers'].value)?.defaultValue

        if(data['is_undefined_model'].value)
            tmpModel = data['undefined_model'].value
        else
            tmpModel = data['filtered_models'].options?.get(data['filtered_models'].value)?.defaultValue

        change useCase.getField('manufacturer') value { tmpManufacturer }
        change useCase.getField('model') value { tmpModel }
    }

    void getAndSetAddressFromEnum(String taskRefId){
        def listOfTaskData = getDataFromReferencedTasks((List<String>) useCase.dataSet[taskRefId].value)
        if(listOfTaskData.isEmpty())
            return

        def data = listOfTaskData.get(0)
        String tmpCity, tmpStreet

        if(data['is_undefined_city'].value)
            tmpCity = data['undefined_city'].value
        else
            tmpCity = data['cities'].options?.get(data['cities'].value)?.defaultValue

        if(data['is_undefined_street'].value)
            tmpStreet = data['undefined_street'].value
        else
            tmpStreet = data['filtered_streets'].options?.get(data['filtered_streets'].value)?.defaultValue

        change useCase.getField('postal_code') value { data['postal_code'].value }
        change useCase.getField('city') value { tmpCity }
        change useCase.getField('street') value { tmpStreet }
        change useCase.getField('house_number') value { data['house_number'].value }
    }

    boolean setCars(EnumerationMapField manufacturers, EnumerationMapField models, String file=pathToCarsCsv){
        csv = VehicleImportHelper.validateCarInput(file, ",")
        if (csv == []) {
            return false
        }
        def cars = VehicleImportHelper.getCars(",", csv)
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

        return true
    }

}
