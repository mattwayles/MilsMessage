package Mils;

import java.util.LinkedHashMap;

/**
 * Name: MILSMsg.java
 * Description: A MILS Message with a hashmap containing retrievable fields accessed by the main.Mils.MilsAgent class
 * Author: Matthew Blough-Wayles
 * Last Edited: 08/25/2017
 */
class MilsMsg {
  private LinkedHashMap<String, String> pairs;

  /**
   * Construct a new main.Mils.MilsMsg Instance
   */
  MilsMsg() {
    setData(new LinkedHashMap<>());
  }

  /**
   * Add values to this instance's hashmap
   *
   * @param key   The message field name
   * @param value The associated message field value
   */
  void add(String key, String value) {
    this.getData().put(key, value);
  }

  /**
   * Retrieve values from this instance's hashmap
   *
   * @param name The name of the field whose value is to be retrieve
   * @return The value corresponding to the provided field
   */
  String get(String name) {
    return this.getData().get(name);
  }

  /**
   * Provide encapsulation by hiding the state of the LinkedHashMap variable
   * @return  The LinkedHashMap contained message information
   */
  LinkedHashMap<String,String> getData() {
    return pairs;
  }

  /**
   * Create a LinkedHashMap object to store message data
   * @param hashMap The LinkedHashMap associated with this main.Mils.MilsMsg object
   */
  void setData(LinkedHashMap<String, String> hashMap) {
    pairs = hashMap;
  }
}
