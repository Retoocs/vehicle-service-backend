package edu.fei.tp.helpers

class CSVHelper {
    static String[] loadCSV(String path) {
        def input = []
        new File(path).each {fields ->
            input.add(fields)
        }
        return input
    }

}
