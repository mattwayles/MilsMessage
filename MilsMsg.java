//region Import Statements
import java.util.HashMap;
//endregion

/**
 * Name: MILSMsg.java
 * Description: A MILS Message with a hashmap containing retrievable fields accessed by the ParseMils class
 * Author: Matthew Blough-Wayles
 * Last Edited: 08/07/2017
 */
public class MilsMsg {
    //region Global Variables
    HashMap<String, String> pairs;
    //endregion

    /**
     * Construct a new MilsMsg Instance
     */
    public MilsMsg() {
        pairs= new HashMap<>();
    }

    /**
     * Add values to this instance's hashmap
     * @param key   The message field name
     * @param value The associated message field value
     */
    void add(String key, String value) {
        pairs.put(key, value);
    }

    /**
     * Retrieve values from this instance's hashmap
     * @param name  The name of the field whose value is to be retrieve
     * @return
     */
    String get(String name) {
        return pairs.get(name);
    }
}
