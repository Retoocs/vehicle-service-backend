package edu.fei.tp

class CSVHelper {

    public String[] loadCSV(String path) {
        def input = []
        new File(path).each {fields ->
            input.add(fields)
        }
        return input;
    }

}
