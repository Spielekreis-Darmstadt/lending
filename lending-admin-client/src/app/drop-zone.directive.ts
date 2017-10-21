import {Directive, EventEmitter, HostBinding, HostListener, Input, Output} from '@angular/core';

@Directive({
  selector: '[dropZone]'
})
export class DropZoneDirective {
  @Input()
  public allowedExtensions: Array<string> = [];

  @Output()
  public filesChange: EventEmitter<File[]> = new EventEmitter();
  @Output()
  public filesInvalid: EventEmitter<File[]> = new EventEmitter();

  constructor() {
  }

  @HostListener('dragover', ['$event'])
  public onDragOver(evt) {
    evt.preventDefault();
    evt.stopPropagation();
  }

  @HostListener('dragleave', ['$event'])
  public onDragLeave(evt) {
    evt.preventDefault();
    evt.stopPropagation();
  }

  @HostListener('drop', ['$event'])
  public onDrop(evt) {
    evt.preventDefault();
    evt.stopPropagation();

    let files = evt.dataTransfer.files;

    let validFiles: Array<File> = [];
    let invalidFiles: Array<File> = [];

    if (files.length > 0) {
      for (let index = 0; index < files.length; index++) {
        const file = files.item(index);

        if (this.allowedExtensions.some(allowedExtension => file.name.endsWith(allowedExtension))) {
          validFiles.push(file);
        } else {
          invalidFiles.push(file);
        }
      }

      this.filesChange.emit(validFiles);
      this.filesInvalid.emit(invalidFiles);
    }
  }
}
