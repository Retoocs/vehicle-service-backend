package edu.fei.tp.helpers

import java.util.stream.Collectors

enum NetEnum {
    CEA(HELPERS_FOLDER + "child_enum_addresses.xml", "child_enum_addresses", "Child enum addresses", "CEA"),
    CEV(HELPERS_FOLDER + "child_enum_vehicles.xml", "child_enum_vehicles", "Child vehicle enumeration", "CEC"),
    MEA(HELPERS_FOLDER + "main_enum_addresses.xml", "main_enum_addresses", "Main enumeration addresses", "MEA"),
    MEV(HELPERS_FOLDER + "main_enum_vehicles.xml", "main_enum_vehicles", "Main vehicle enumeration", "MEV"),
    RTW(HELPERS_FOLDER + "ri_to_wi.xml", "ri_to_wi", "RI to WI", "RTW"),
    WTR(HELPERS_FOLDER + "wi_to_ri.xml", "wi_to_ri", "WI to RI", "WTR"),
    CUS("customer.xml", "customer", "Customer", "CUS"),
    NTF("notification.xml", "notification", "Notification", "NTF"),
    RPR("repair.xml", "repair", "Repair", "RPR"),
    RI("repair_item.xml", "repair_item", "Repair Item", "RI"),
    VEH("vehicle.xml", "vehicle", "Vehicle", "VEH"),
    WH("warehouse.xml", "warehouse", "Warehouse", "WH"),
    WI("warehouse_item.xml", "warehouse_item", "Warehouse Item", "WI"),

    public static final String HELPERS_FOLDER = "helpers/"
    public static final String ENGINE_FOLDER = "engine/"

    final String netFile
    final String netIdentifier
    final String netName
    final String netInitials

    NetEnum(String fileName, String identifier, String name, String initials) {
        this.netFile = fileName
        this.netIdentifier = identifier
        this.netName = name
        this.netInitials = initials
    }

    static List<String> getAllNetIdentifiers() {
        return values().toList().stream()
                .map({ it.netIdentifier })
                .collect(Collectors.toList())
    }
}
