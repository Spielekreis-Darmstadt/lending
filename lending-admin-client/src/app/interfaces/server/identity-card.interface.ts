export interface IdentityCard {
  barcode: string,
  activated?: boolean
}

export class IdentityCardInstance implements IdentityCard {
  public barcode: string;
  public activated: boolean;
}
