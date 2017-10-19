export interface VerifyGamesResponse {
  valid: boolean,
  alreadyExistingBarcodes: Array<string>,
  emptyTitleBarcodes: Array<string>
}
