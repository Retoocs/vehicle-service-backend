package edu.fei.tp
import com.netgrif.application.engine.petrinet.domain.dataset.EnumerationMapField
import com.netgrif.application.engine.petrinet.domain.dataset.Field
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

}
