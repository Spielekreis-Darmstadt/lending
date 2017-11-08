export interface Envelope {
  barcode: string,
  activated?: boolean
}

export class EnvelopeInstance implements Envelope {
  public barcode: string;
  public activated: boolean;
}
