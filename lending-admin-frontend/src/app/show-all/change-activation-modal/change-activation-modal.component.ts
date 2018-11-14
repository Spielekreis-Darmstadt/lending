import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {EntityService} from "../../core/entity.service";
import {Lendable} from "../../interfaces/server/lendable.interface";

export interface DialogData {
  entityName: string;
  entity: Lendable;
}

@Component({
  selector: 'lending-change-activation-modal',
  templateUrl: './change-activation-modal.component.html',
  styleUrls: ['./change-activation-modal.component.css']
})
export class ChangeActivationModalComponent implements OnInit {

  /**
   * The entity name of the lendable entity, i.e. game, identity card or envelope
   */
  public get entityName(): string {
    return this.data.entityName;
  }

  /**
   * The shown lendable entity
   */
  public get entity(): Lendable {
    return this.data.entity;
  }

  constructor(@Inject(MAT_DIALOG_DATA) private data: DialogData,
              private dialogRef: MatDialogRef<ChangeActivationModalComponent>,
              private entityService: EntityService) {
  }

  ngOnInit() {
  }

  /**
   * Tries to change the activation state of the lendable entity
   * If this is not possible nothing happens.
   * In either case the dialog is closed
   */
  changeActivation(): void {
    if (this.entity.activated) {
      this.entityService.deactivateBarcodes([this.entity.barcode], result => {
        if (result) {
          this.entity.activated = false;
        }

        this.dialogRef.close();
      });
    } else {
      this.entityService.activateBarcodes([this.entity.barcode], result => {
        if (result) {
          this.entity.activated = true;
        }

        this.dialogRef.close();
      });
    }
  }
}
