/**
 * Calculates the checksum of a given barcode string.
 * The string needs to have a length of 7 characters
 * @param {string} str The given barcode
 * @returns {string} The calculated checksum
 */
function calculateChecksum(str: string): string {
  var prz = 0;
  var r3 = 0;

  prz = parseInt(str[0], 10) + parseInt(str[2], 10) + parseInt(str[4], 10) + parseInt(str[6], 10);
  r3 = 2 * parseInt(str[1], 10);
  prz += r3 % 10 + Math.floor(r3 / 10);
  r3 = 2 * parseInt(str[3], 10);
  prz += r3 % 10 + Math.floor(r3 / 10);
  r3 = 2 * parseInt(str[5], 10);
  prz += r3 % 10 + Math.floor(r3 / 10);

  prz = prz % 10;

  // negative checksum => bad
  if (prz < 0) {
    prz = prz * (-1)
  }

  return prz.toString(10);
}

/**
 * Checks whether a given barcode is valid.
 * A barcode is valid, if it has a length of 8 and its checksum is correct
 * @param {string} barcode The to be checked barcode
 * @returns {boolean} True if the given barcode has the correct checksum, false otherwise
 */
export function isBarcodeValid(barcode: string): boolean {
  if (!barcode || barcode.length != 8) {
    return false;
  }

  return barcode[7] == calculateChecksum(barcode.substring(0, 7));
}

export function createBarcode(prefix: string, index: string): string {
   const numberOfZeroes = 5 - index.length;
   const firstPart = prefix + Array(numberOfZeroes).fill(0).join('') + index;

   return firstPart + calculateChecksum(firstPart);
}

