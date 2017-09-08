# MilsMessage
<b>Prototype</b> of a translation module between MILS and DLMS formats

Currently, only the interactive portion and file parse operations of the prototype is implemented. Therefore, <i>no message can be built to/from a file yet</i>

Run the MILS translation prototype using the following syntax:

<code>java Mils </parse|/build|/parseFile> <format file> <MILSMessage|Type|inputFile> [MessageField|outputFile]</code>
  
  <b>/parse | /build |/parseFile </b> - Required functional operator.
  
  <b>format file</b> - Required for all operations. Path to the file containing MILS message definitions. Each line of the file must contain a single definition. The definitions must be in the following format: <b>Type, (startIndex, endIndex, FieldName), ...</b>
  
  <b>MILSMessage | Type | inputFile</b> - Required. For parse operations, a valid 80-character MILS message must be supplied as a parameter. For build operations, a 1-3 character definition type must be supplied. For parseFile operations, this may be a valid 80-character MILS message or a file containing one or more valid 80-character MILS messages.
  
  <b>MessageField | outputFile</b> - Optional. In parse operations, supply the name of a message field to retrieve. This will skip the user interactivity and return the requested field. In parseFile operations, this will write parse results to the file specified by this parameter.
