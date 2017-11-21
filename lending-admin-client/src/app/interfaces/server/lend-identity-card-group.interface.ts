export interface LendGame {
  barcode: string,
  lendTime: Date
}

export interface LendIdentityCardGroup {
  barcode: string,
  lendGames: Array<LendGame>
}
