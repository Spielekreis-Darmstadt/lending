import * as Handsontable from "handsontable";
import {createBarcode} from "./barcode-utility";
import {Lendable} from "../interfaces/server/lendable.interface";

export function createToBarcodeConverter(prefix: string, getHot: (() => Handsontable), getLendables: (() => Array<Lendable>)):
  ((key: string, options: Handsontable.contextMenu.Options) => void) {
  
  return (key, options) => {
    const hot = getHot();

    const fromRow = hot.getSelectedLast()[0];
    const toRow = hot.getSelectedLast()[2] + 1;

    getLendables().slice(fromRow, toRow)
      .forEach(lendable => {
        // only convert barcode strings that are made up of only the index part
        if (lendable.barcode.length <= 5) {
          lendable.barcode = createBarcode(prefix, lendable.barcode);
        }
      });

    hot.render();
  }
}
