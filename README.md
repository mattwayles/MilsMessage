# MilsMessage
<b>Prototype</b> of a translation module between MILS and DLMS formats

Currently, only the interactive portion of the prototype is implemented. Therefore, <i>no files can be translated yet</i>

Run the MILS translation prototype using the following syntax:

<b>java Mils </parse|/build> <format file> <MILSMessage|Type> [MessageField]</br>
  
  <b>/parse | /build </b> - Required functional operator. Currently, only these two operations are supported.
  <b>format file</b> - Required for all operations. Path to the file containing MILS message definitions. Each line of the file must contain a single definition. The definitions must be in the following format: <b>Type, (startIndex, endIndex, FieldName), ...</b>
  <b>MILSMessage | Type</b> - Required. For parse operations, a valid 80-character MILS message must be supplied as a parameter. For build operations, a 1-3 character definition type must be supplied.
  <b>MessageField</b> - Optional. In parse operations, supply the name of a message field to retrieve. This will skip the user interactivity and return the requested field.
