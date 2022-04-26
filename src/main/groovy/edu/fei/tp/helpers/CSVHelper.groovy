package edu.fei.tp.helpers

class CSVHelper {
    static List loadCSV(String path) {
        def input = []
        new File(path).each {fields ->
            input.add(fields)
        }
        return input
    }

}