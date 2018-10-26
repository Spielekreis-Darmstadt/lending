import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Envelope} from '../interfaces/server/envelope.interface';
import {AddEnvelopesResponse} from '../interfaces/server/add-envelopes-response.interface';
import {VerifyEnvelopesResponse} from '../interfaces/server/verify-envelopes-response.interface';

/**
 * A service used to do envelope related interaction with the server
 *
 * @author Marc Arndt
 */
@Injectable()
export class EnvelopeService {

  /**
   * Constructor
   * @param {HttpClient} http A http client
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Tries to add a given envelope `envelope` on the server.
   * Afterwards the given callback `resultCallback` is called.
   * If the envelope addition succeeded the callback is called with `true`, otherwise with `false`.
   * @param {Envelope} envelope The envelope to be added
   * @param {(AddIdentityCardsResponse) => void} resultCallback The callback to be called afterwards
   */
  addEnvelope(envelope: Envelope, resultCallback: (result: AddEnvelopesResponse) => void): void {
    this.http
      .put('/lending-admin-backend/rest/envelopes/add', [ envelope ])
      .subscribe(
        (data: AddEnvelopesResponse) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback(null)
      );
  }

  addEnvelopes(envelopes: Array<Envelope>, resultCallback: (result: AddEnvelopesResponse) => void): void {
    this.http
      .put('/lending-admin-backend/rest/envelopes/add', envelopes)
      .subscribe(
        (data: AddEnvelopesResponse) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback(null)
      );
  }

  selectEnvelopes(resultCallback: (identityCards: Array<Envelope>) => void): void {
    this.http
      .get('/lending-admin-backend/rest/envelopes/all')
      .subscribe(
        (data: Array<Envelope>) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback([])
      );
  }

  verifyEnvelopes(envelopes: Array<Envelope>, resultCallback: (valid: VerifyEnvelopesResponse) => void): void {
    this.http
      .put('/lending-admin-backend/rest/envelopes/verify', envelopes)
      .subscribe(
        (data: VerifyEnvelopesResponse) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback(null)
      );
  }
}
