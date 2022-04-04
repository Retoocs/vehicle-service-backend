package edu.fei.tp
import com.netgrif.application.engine.petrinet.domain.dataset.EnumerationMapField
import com.netgrif.application.engine.petrinet.domain.dataset.logic.action.ActionDelegate
import org.springframework.stereotype.Component


@Component
class CustomActionDelegate extends ActionDelegate {
    String path = "cars.csv";
    String splitter = ",";

    public Map getCars(String splitter) {
        CSVHelper helper = new CSVHelper()
        def csv= helper.loadCSV(path)
        def cars = [:]
        csv.each {s ->
            def vehiclesArr = s.split(splitter)
            def manufacturer=vehiclesArr[0].replaceAll("\\.", "-");
            def model=vehiclesArr[1].replaceAll("\\.", "-");

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
        return cars;
    }


    public void setCars(EnumerationMapField make,EnumerationMapField model){
        def cars = getCars(splitter)
        for (entry in cars) {
            change make options {   make.options + [(entry.getKey()):entry.getKey()];  }
            for(mod in entry.getValue()){
                change model options {  model.options + [(entry.getKey()+"-"+mod):mod];  }
            }
        }
    }
}
