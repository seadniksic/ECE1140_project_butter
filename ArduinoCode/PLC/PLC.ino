
const int numBytes = 64;
byte receivedBytes[numBytes];
byte receivedAttribute[numBytes];

byte numReceived = 0;
byte adxReceived = 0;

boolean newData = false;
String programmedLine = "";

int programmedInputStartBlock = 0;
int programmedInputEndBlock = 0;
boolean InputIncreasing = false;

int programmedNCStartBlock = 0;
int programmedNCEndBlock = 0;
boolean NCIncreasing = false;

int programmedNOStartBlock = 0;
int programmedNOEndBlock = 0;
boolean NOIncreasing = false;

int programmedAnum[99];
int programmedAatt[99];
int attributes = 0;
int q = 0;

boolean flip = false;
int lightdisableDelay = 0;
boolean SwitchState = true;

void setup() {
  Serial.begin(9600);
  pinMode(D1, OUTPUT);//Red
  pinMode(D2, OUTPUT);//Yellow
  pinMode(D3, OUTPUT);//Green
  pinMode(D6, INPUT_PULLUP);
}

void loop() {
  recieveMessage();

  decodeMessage();

  actonMessage();

  showNewData();
}

void recieveMessage() {
  static boolean recvInProgress = false;
  static byte ndx = 0;
  static byte adx = 0;
  byte startMarker = 0x3C; //Start marker is <
  byte endMarker = 0x3E;  //End marker is >
  byte delimiter = 0x3B; //Delimiter is ;
  byte rb;
  static boolean delimiterInProgress = false;

  while (Serial.available() > 0 && newData == false) {
    rb = Serial.read(); //read the first byte to readbyte

    if (recvInProgress == true && delimiterInProgress == false) {
      if (rb == delimiter) { //If next character is the delimiter, end the message
        delimiterInProgress = true;
        receivedBytes[ndx] = '\0'; // terminate the string
        numReceived = ndx;  // save the number for use when printing
        ndx = 0;
      }
      else if (rb == endMarker) { //End the message
        receivedBytes[ndx] = '\0'; // terminate the string
        recvInProgress = false;
        numReceived = ndx;  // save the number for use when printing
        ndx = 0;
        newData = true;
      }
      else {  //Normal code
        receivedBytes[ndx] = rb;
        ndx++;
        if (ndx >= numBytes) {
          ndx = numBytes - 1;
        }
      }
    }
    else if (recvInProgress == true && delimiterInProgress == true) {
      if (rb == endMarker) {
        adxReceived = adx;
        adx = 0;
        recvInProgress = false;
        newData = true;
        delimiterInProgress = false;
      }
      else {
        receivedAttribute[adx] = rb;
        adx++;
        if (adx >= numBytes) {
          adx = numBytes - 1;
        }
      }
    }
    else if (rb == startMarker) {
      recvInProgress = true;
    }
  }
}

void decodeMessage() {
  if (newData == true) {
    String Function = "";
    //Parse the message
    //First dtermine the fucntion requested by translating the first 4 bytes
    for (byte n = 0; n < 4; n++) {
      Function += (char)receivedBytes[n];
    }

    if (Function == "0001") {
      Reprogram();
    }
    else if (Function == "0010") {
      Occupancy();
    }
    else if (Function == "0011") {
      ManualSwitch();
    }
    else if (Function == "0100"){
      BlockFailure();
    }
  }
}

void BlockFailure(){
  
}


void ManualSwitch() {
  String state = "";

  for (byte n = 4; n < 5; n++) {
    state += (char)receivedBytes[n];
  }
  Serial.println(state);

  if (state == "1") {
    SwitchState = true;
  }
  else if (state == "0") {
    SwitchState = false;
  }

  digitalWrite(D2, SwitchState);

  Serial.print("<0011");
  Serial.print(SwitchState);
  Serial.println(">");
}

void Occupancy() {

  //String Line = "";
  String Block = "";
  String state = "";
  boolean occupancy = false;
  String str = "";

  //  //Read the next 8 bits which are Line Color
  //  for (byte n = 4; n < 12; n++) {
  //    if (receivedBytes[n] != 0){ //If it recieved a 0, discard it
  //      Line += receivedBytes[n];
  //    }
  //  }
  //
  //  If (Line == programmedLine){

  //Read the next 4 bits which in the case of occupancy is the block int
  for (byte n = 4; n < 8; n++) {
    if (receivedBytes[n] != '-') { //If it recieved a -, discard it
      //Serial.println(receivedBytes[n]);
      Block += (char)receivedBytes[n];
    }
  }

  //Read the next block which in the case of occupancy is block status
  for (byte n = 8; n < 9; n++) {
    str = (char)receivedBytes[n];
    occupancy = str == "1";
  }

  //If the block is in the input section of the track
  if (InputIncreasing == true) {
    if (Block.toInt() >= programmedInputStartBlock && Block.toInt() <= programmedInputEndBlock) {
      if (occupancy) {
        if (Block.toInt() == programmedInputStartBlock) {
          SwitchState = false; //switch track to connect NC
          digitalWrite(D2, LOW);
        }
        else {
          //If within the range but not at the start block
        }
      }
      else {
        //If you want to do anything upon removing occupancy
        //SwitchState = false; //switch track to connect NC
        //digitalWrite(D2, HIGH);
      }
    }

  }
  else {
    if (Block.toInt() <= programmedInputStartBlock && Block.toInt() >= programmedInputEndBlock) {
      if (occupancy) {
        if (Block.toInt() == programmedInputStartBlock) {
          SwitchState = false; //switch track to connect NC
          digitalWrite(D2, LOW);
        }
        else { //If within the range but not at the start block
        }
      }
      else {
        //If you want to do anything upon removing occupancy
        //SwitchState = false; //switch track to connect NC
        //digitalWrite(D2, HIGH);
      }
    }
  }

  //If the block is in the NC section of the track
  if (NCIncreasing == true) {
    if (Block.toInt() >= programmedNCStartBlock && Block.toInt() <= programmedNCEndBlock) {
      if (occupancy) {
        if (Block.toInt() == programmedNCStartBlock) {
          SwitchState = false; //switch track to connect NC
          digitalWrite(D2, LOW);
        }
        else {
          //If within the range but not at the start block
        }
      }
      else {
        //If you want to do anything upon removing occupancy
        //SwitchState = false; //switch track to connect NC
        //digitalWrite(D2, HIGH);
      }
    }
  }
  else {
    if (Block.toInt() <= programmedNCStartBlock && Block.toInt() >= programmedNCEndBlock) {
      if (occupancy) {
        if (Block.toInt() == programmedNCStartBlock) {
          SwitchState = false; //switch track to connect NC
          digitalWrite(D2, LOW);
        }
        else { //If within the range but not at the start block
        }
      }
      else {
        //If you want to do anything upon removing occupancy
        //SwitchState = false; //switch track to connect NC
        //digitalWrite(D2, HIGH);
      }
    }
  }

  //If the block is in the NO section of the track
  Serial.println(NOIncreasing);
  if (NOIncreasing == true) {
    if (Block.toInt() >= programmedNOStartBlock && Block.toInt() <= programmedNOEndBlock) {
      if (occupancy) {
        if (Block.toInt() == programmedNOStartBlock) {
          SwitchState = true; //switch track to connect NO
          digitalWrite(D2, HIGH);
        }
        else {
          //If within the range but not at the start block
        }
      }
      else {
        //If you want to do anything upon removing occupancy
        //SwitchState = false; //switch track to connect NC
        //digitalWrite(D2, HIGH);
      }
    }
  }
  else {
    if (Block.toInt() <= programmedNOStartBlock && Block.toInt() >= programmedNOEndBlock) {
      if (occupancy) {
        if (Block.toInt() == programmedNOStartBlock) {
          SwitchState = true; //switch track to connect NC
          digitalWrite(D2, HIGH);
        }
        else { //If within the range but not at the start block
        }
      }
      else {
        //If you want to do anything upon removing occupancy
        //SwitchState = false; //switch track to connect NC
        //digitalWrite(D2, HIGH);
      }
    }
  }
  
  //Now what we can do, is if there is an attribute in that block then trigger lights because there will be lights at each attribute
  for (int j = 0; j < 99; j++){
    if (Block.toInt() == programmedAnum[j]){
      digitalWrite(D3, HIGH);
      lightdisableDelay = 100000;
    }
  }

int programmedAnum[99];
int programmedAatt[99];


  //Need to add the reply here maybe?>
  Serial.print('<');
  Serial.print(SwitchState);
  Serial.println('>');

  //}
}

void Reprogram() {
  if (newData == true) {

    //First thing to do is reset all the old programmed stuff

    programmedLine = "";

    programmedInputStartBlock = 0;
    programmedInputEndBlock = 0;
    InputIncreasing = false;

    programmedNCStartBlock = 0;
    programmedNCEndBlock = 0;
    NCIncreasing = false;

    programmedNOStartBlock = 0;
    programmedNOEndBlock = 0;
    NOIncreasing = false;

    for (int i = 0; i < 99; i++) {
      programmedAnum[i] = 0;
      programmedAatt[i] = 0;
    }

    attributes = 0;
    q = 0;

    char* Ldelimiter = ";"; //Large Delimiter is ;
    char* Sdelimiter = ","; //Small Delimiter is ,

    String Line = "";
    String InputStartBlock = "";
    String InputEndBlock = "";
    String NCStartBlock = "";
    String NCEndBlock = "";
    String NOStartBlock = "";
    String NOEndBlock = "";
    String Attribute = "";
    char* blockWithAtt;

    String str = String((char *)receivedAttribute);
    char str_array[str.length() + 1];
    str.toCharArray(str_array, str.length() + 1);
    Serial.print("----------New Data!-----------: ");
    Serial.println(str);
    char str_array_cpy[str.length() + 1];
    str.toCharArray(str_array_cpy, str.length() + 1);

    char* sToken = strtok(str_array, " ");
    //Serial.print("Str array1 : ");
    //Serial.println(str_array);
    /* get the first token */
    int check = 0;
    int from = 0;


    while (check != -1) {
      check = str.indexOf(Ldelimiter, from); //First check if ; appears, if so save the position to the variable check
      from = check + 1;  //save where it's at and use that to repeat the search
      attributes++;
    }
    //Serial.print("Str array2 : ");
    //Serial.println(str_array);
    //Serial.println(attributes);

    blockWithAtt = strtok(str_array, Ldelimiter);
    //Serial.println(blockWithAtt);
    int counter = 0;
    //Serial.print("Str array3 : ");
    //Serial.println(str_array);
    /* walk through other tokens */
    //while (blockWithAtt != NULL) {
    while (blockWithAtt != NULL) {

      //This is where I find the Block Number of the attribute section, A
      sToken = strtok(blockWithAtt, Sdelimiter);
      //Serial.print("sToken : ");
      //Serial.println(sToken);
      String a = "";
      for (int n = 0; n < sizeof(sToken); n++) {
        if (sToken[n] != '-') {
          a += sToken[n];
          //Serial.print("A : ");
          //Serial.println(a);
        }
      }

      programmedAnum[q] = a.toInt();
      //Serial.print("Num ");
      //Serial.println(programmedAnum[q]);


      //Here is where I find the attribute number Num, B
      sToken = strtok(NULL, Sdelimiter);
      String b = "";
      for (int n = 0; n < sizeof(sToken); n++) {
        if (sToken[n] != '-') {
          b += sToken[n];
        }
      }
      //Serial.print("Str array5 : ");
      //Serial.println(str_array);
      programmedAatt[q] = b.toInt();
      //Serial.print("Att ");
      //Serial.println(programmedAatt[q]);
      counter++;

      //This is the problem right here
      str.toCharArray(str_array_cpy, str.length() + 1);
      blockWithAtt = strtok(str_array_cpy, Ldelimiter);

      //while (blockWithAtt != NULL) {
      for (int i = 0; i < counter ; i++) {
        //Serial.print("First block : ");
        //Serial.println(blockWithAtt);
        blockWithAtt = strtok(NULL, Ldelimiter);
        //Serial.print("Second block : ");
        //Serial.println(blockWithAtt);
      }
      //Serial.print("blockWithAtt 2 : ");
      //Serial.println(blockWithAtt);

      q++;
    }

    //Read the next 8 bits which are Data1; in the case of Reprogram they are Line COlor
    for (byte n = 4; n < 12; n++) {
      if (receivedBytes[n] != 0) { //If it recieved a 0, discard it
        Line += receivedBytes[n];
      }
    }
    programmedLine = Line;

    //INPUT//
    //Read the next 4 bits which are Data2; in the case of Reprogram are Input Start Block Number
    for (byte n = 12; n < 16; n++) {
      if (receivedBytes[n] != '-') { //If it recieved a 0, discard it
        InputStartBlock += (char)receivedBytes[n];
      }
    }
    programmedInputStartBlock = InputStartBlock.toInt();
    //Read the next 4 bits which are Data2; in the case of Reprogram are End Block Number
    for (byte n = 16; n < 20; n++) {
      if (receivedBytes[n] != '-') { //If it recieved a 0, discard it
        InputEndBlock += (char)receivedBytes[n];
      }
    }
    programmedInputEndBlock = InputEndBlock.toInt();
    if (programmedInputStartBlock < programmedInputEndBlock) {
      InputIncreasing = true;
    }

    //NORMALLY CLOSED//
    //Read the next 4 bits which are Data2; in the case of Reprogram are Input Start Block Number
    for (byte n = 20; n < 24; n++) {
      if (receivedBytes[n] != '-') { //If it recieved a 0, discard it
        NCStartBlock += (char)receivedBytes[n];
      }
    }
    programmedNCStartBlock = NCStartBlock.toInt();
    //Read the next 4 bits which are Data2; in the case of Reprogram are End Block Number
    for (byte n = 24; n < 28; n++) {
      if (receivedBytes[n] != '-') { //If it recieved a 0, discard it
        NCEndBlock += (char)receivedBytes[n];
      }
    }
    programmedNCEndBlock = NCEndBlock.toInt();
    if (programmedNCStartBlock < programmedNCEndBlock) {
      NCIncreasing = true;
    }

    //NORMALLY OPEN//
    for (byte n = 28; n < 32; n++) {
      if (receivedBytes[n] != '-') { //If it recieved a 0, discard it
        NOStartBlock += (char)receivedBytes[n];
      }
    }
    programmedNOStartBlock = NOStartBlock.toInt();
    for (byte n = 32; n < 36; n++) {
      if (receivedBytes[n] != '-') { //If it recieved a 0, discard it
        NOEndBlock += (char)receivedBytes[n];
      }
    }
    programmedNOEndBlock = NOEndBlock.toInt();
    if (programmedNOStartBlock < programmedNOEndBlock) {
      NOIncreasing = true;
    }

    Serial.print('<');
    for (byte n = 0; n < numReceived; n++) {
      Serial.print((char)receivedBytes[n]);
    }
    
//    Serial.print(" plus ");
//    for (byte n = 0; n < q; n++) {
//      Serial.print("AttBlkNum: ");
//      Serial.print(programmedAnum[n]);
//      Serial.print(" AttBlkAtt: ");
//      Serial.println(programmedAatt[n]);
//      //Serial.print("  ");
//    }

    Serial.print('>');
    Serial.println();

  }
}


void actonMessage() {
  if (newData == true) {
    if (flip == false) {
      digitalWrite(D1, HIGH);
    }
    else {
      digitalWrite(D1, LOW);
    }
    flip = !flip;
  }
}


void showNewData() {
  lightdisableDelay--;
  if (lightdisableDelay == 0){
    digitalWrite(D3, LOW);
  }
  if (newData == true) {

    //    Serial.print('<');
    //    for (byte n = 0; n < numReceived; n++) {
    //      Serial.print((char)receivedBytes[n]);
    //    }
    //    Serial.print(" plus ")
    //    for (byte n = 0; n < q; n++) {
    //      Serial.print(programmedAnum[n]);
    //      Serial.print(" + ");
    //      Serial.print(programmedAatt[n]);
    //      Serial.print(" + ");
    //    }
    //    Serial.print('>');
    //    Serial.println();

    newData = false;
  }
}
