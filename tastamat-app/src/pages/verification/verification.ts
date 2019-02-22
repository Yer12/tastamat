import { Component } from '@angular/core';
import { NavController, Platform} from 'ionic-angular';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { File, FileEntry } from "@ionic-native/file";
import { HttpClient } from "@angular/common/http";
import { Storage } from "@ionic/storage";
import { LoaderProvider } from "../../providers/loader.provider";
import { VerifyService } from "../../services/verify.service";

@Component({
  selector: 'verification-page',
  templateUrl: 'verification_step1.html'
})
export class Verification_step1Page {
  name: string;
  surname: string;
  birthDate: any;
  docType: string;
  docNumber: string;
  docExpDate: any;
  constructor(
    public navCtrl: NavController,
    public platform: Platform,
    private verifyService: VerifyService,
    private storage: Storage
  ) {
    this.platform.registerBackButtonAction(() => {},1);
  }

  verify() {
    const data = {
      firstname: this.name,
      lastname: this.surname,
      birthDate: new Date(this.birthDate).getTime(),
      docType: this.docType,
      docNumber: this.docNumber,
      docExpDate: new Date(this.docExpDate).getTime(),
    };
    this.storage.get('user').then((user) => {
      this.verifyService.sendForm(user.id, data).subscribe(
        response => {
          console.log(response);
          this.navCtrl.push(Verification_step2Page);
        },
        err => {
          console.log(err);
        });
    });
  }
}

// --------------------------------------------STEP 2----------------------------------------------------------------

@Component({
  selector: 'verification-page',
  templateUrl: 'verification_step2.html'
})

export class Verification_step2Page {
  photo: any;

  constructor(
    public navCtrl: NavController,
    public platform: Platform,
    private camera: Camera,
    public http: HttpClient,
    public file: File,
    private storage: Storage,
    private loader: LoaderProvider,
    private verifyService: VerifyService,
  ) {
    this.platform.registerBackButtonAction(() => this.navCtrl.push(Verification_step1Page),1);
  }

  takePhoto(type) {
    const options: CameraOptions = {
      quality: 100,
      sourceType: type === 'camera'
        ? this.camera.PictureSourceType.CAMERA
        : this.camera.PictureSourceType.PHOTOLIBRARY,
      destinationType: this.camera.DestinationType.FILE_URI,
      encodingType: this.camera.EncodingType.JPEG,
      mediaType: this.camera.MediaType.PICTURE
    };

    this.camera.getPicture(options).then((imageData) => {
      console.log(imageData);
      this.uploadPhoto(imageData);
    }, (err) => {});
  }

  private uploadPhoto(imageFileUri: any): void {
    this.loader.show();
    this.file.resolveLocalFilesystemUrl(imageFileUri)
      .then(entry => (<FileEntry>entry).file(file => this.readFile(file)))
      .catch(err => console.log(err));
  }

  private readFile(file: any) {
    const reader = new FileReader();
    reader.onloadend = () => {
      const formData = new FormData();
      const imgBlob = new Blob([reader.result], {type: file.type});
      formData.append('file', imgBlob, file.name);
      this.postData(formData);
    };
    reader.readAsArrayBuffer(file);
  }

  private postData(formData: FormData) {
    this.uploadImage(formData).then(
      result => this.loader.hide(),
      err => this.loader.hide()
    );
  }

  private uploadImage(formData) {
    return new Promise((resolve, reject) => {
      this.storage.get('user').then((user) => {
        this.verifyService.uploadPhoto(user.id, formData).subscribe(
          response => {
            // this.photo = `https://testtasta.tastamat.com/api/rest/a/files/${response.id}/view`;
            // this.verifyService.getPhoto(response.id).subscribe(
            //   response => console.log(response),
            //   err => console.log(err)
            // );
            resolve(response)
          },
          err => {
            console.log(err);
            reject(err);
          });
      })
    })
  }

  goBackTo() {
    this.navCtrl.push(Verification_step1Page)
  }

  send() {
    console.log('send');
  }

}
