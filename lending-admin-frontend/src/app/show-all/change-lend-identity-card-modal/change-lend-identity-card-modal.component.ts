import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {LendIdentityCard} from "../../interfaces/server/lend-identity-card.interface";
import {LendIdentityCardService} from "../../core/lend-identity-card.service";

export interface DialogData {
  lendIdentityCard: LendIdentityCard;
}

@Component({
  selector: 'lending-change-lend-identity-card-modal',
  templateUrl: './change-lend-identity-card-modal.component.html',
  styleUrls: ['./change-lend-identity-card-modal.component.css']
})
export class ChangeLendIdentityCardModalComponent implements OnInit {
  /**
   * The entered owner by the user
   */
  public owner: string;

  /**
   * The shown lend identity card
   */
  public get lendIdentityCard(): LendIdentityCard {
    return this.data.lendIdentityCard;
  }

  constructor(@Inject(MAT_DIALOG_DATA) private data: DialogData,
              private dialogRef: MatDialogRef<ChangeLendIdentityCardModalComponent>,
              private lendIdentityCardService: LendIdentityCardService) {
    this.owner = this.lendIdentityCard.owner;
  }

  ngOnInit() {
  }

  /**
   * Tries to change the owner of the shown identity card.
   * If this is not possible nothing happens.
   * In either case the dialog is closed
   */
  changeOwner(): void {
    if (this.owner) {
      this.lendIdentityCardService.updateOwner(
        this.lendIdentityCard.identityCardBarcode,
        this.owner,
        result => {
          if (result) {
            this.lendIdentityCard.owner = this.owner;
          }

          this.dialogRef.close()
        }
      );
    } else {
      this.lendIdentityCardService.clearOwner(
        this.lendIdentityCard.identityCardBarcode,
        result => {
          if (result) {
            this.lendIdentityCard.owner = this.owner;
          }

          this.dialogRef.close()
        }
      );
    }
  }
}
