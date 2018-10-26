package info.armado.ausleihe.client.model;
/**
  *
  * Klasse zur analyse des gescannten Barcodes auf die Pr�fz�ffer
  *
  * @version 1.0 vom 24.02.2011
  * @author Marc Arndt
  */

public class BarcodeChars {

  private static BarcodeChars instance;
  
  public static BarcodeChars getInstance() {
	  if (instance == null)
		  instance = new BarcodeChars();
	  return instance;
  }

  /**
   * Methode returnt die Pr�fziffer f�r den Code-Wert aller sich im Text-String befindlichen chars.
   * @param String str (7 Zeichen lang)
   */
  public String getPruefziffer(String str) {
    if (str == null || str.length() != 7)
       return "*";
    int a1 = str.charAt(0) - 48;
    int a2 = str.charAt(1) - 48;
    int a3 = str.charAt(2) - 48;
    int a4 = str.charAt(3) - 48;
    int a5 = str.charAt(4) - 48;
    int a6 = str.charAt(5) - 48;
    int a7 = str.charAt(6) - 48;

    int prz = a1 + a3 + a5 + a7;
    int r3 = 2 * a2;
    prz += r3 % 10 + r3 / 10;
    r3 = 2 * a4;
    prz += r3 % 10 + r3 / 10;
    r3 = 2 * a6;
    prz += r3 % 10 + r3 / 10;

    prz = prz % 10;
    
    if (prz < 0) { //negative Pr�fsumme = schlecht
      prz = prz * (-1);
    }

    return "" + prz;
  }

  public boolean isBarcodeValid(String barcode) {
	  String firstPart = barcode.substring(0,  7);
	  String checksum = barcode.substring(7);
	  
	  return isBarcodeOk(firstPart, checksum);
  }
  
  /**
   * Methode �berpr�ft ob der Barcode zahlendreher oder �hnliche Fehler hat.
   * @param String barcode, String pruefziffer
   * @return boolean true = passt / false = passt nicht.
   */
  public boolean isBarcodeOk(String barcode, String pz) {
    String prueffziffer = getPruefziffer(barcode);
    if (prueffziffer.equals(pz)) {//Die Pr�fziffer passt
      return true;
    } else {    //Die Pr�fziffer passt nicht.
      return false;
    }
  }
  
  /**
   * Methode trennt einen �bergebenen gescannten Barcode in den eigentlichen Barcode und die Pr�fziffer
   * @param String text
   * @return String[2] 0 = Barcode 1 = Pr�fziffer
   */
  public String[] splitBarcode(String barcode) {
    String[] toreturn = new String[2]; //0 = Barcode / 1 = Pr�fziffer
    if (barcode.length() == 0) {
      toreturn = new String[] {
        "", ""
      };
      return toreturn;
    }
    toreturn[0] = barcode.substring(0, barcode.length()-1);
    toreturn[1] = barcode.substring(barcode.length()-1);
    return toreturn;
  }
  
  /**
   * Nimmt einen 7 Stelligen String engegen und h�ngt die dazugeh�rige Pr�fziffer hinten dran
   * @param text	7 Stelliger Code/Text
   * @return		8 Stelliger Code mit Pr�fziffer
   */
  public String getBarcode(String text) {
	  return text + this.getPruefziffer(text);
  }
}
