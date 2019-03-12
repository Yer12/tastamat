import { Component } from '@angular/core';
import { NavController, NavParams, Platform} from 'ionic-angular';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { File, FileEntry } from "@ionic-native/file";
import { HttpClient } from "@angular/common/http";
import { Storage } from "@ionic/storage";
import { LoaderProvider } from "../../providers/loader.provider";
import { VerifyService } from "../../services/verify.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SignInPage} from "../signIn/signIn";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'verification-page',
  templateUrl: 'verification_step1.html'
})
export class Verification_step1Page {
  firstName: string;
  lastName: string;
  birthDate: any;
  docType: string;
  docNumber: string;
  docExpDate: any;
  step1: FormGroup;
  submitAttempt: boolean = false;

  constructor(
    public navCtrl: NavController,
    public platform: Platform,
    private storage: Storage,
    public formBuilder: FormBuilder
  ) {
    this.platform.registerBackButtonAction(() => {},1);
    this.step1 = formBuilder.group({
      firstName: ['', Validators.compose([Validators.maxLength(30), Validators.pattern('[a-zA-Zа-яА-Я]*'), Validators.required])],
      lastName: ['', Validators.compose([Validators.maxLength(30), Validators.pattern('[a-zA-Zа-яА-Я]*'), Validators.required])],
      birthDate: ['', Validators.required],
      docType: ['', Validators.required],
      docNumber: ['', Validators.required],
      docExpDate: ['', Validators.required]
    });
  }

  async signOut() {
    await this.storage.remove('token');
    this.navCtrl.push(SignInPage);
  }

  verify() {
    this.submitAttempt = true;
    if (this.step1.valid) {
      const data = {
        firstname: this.firstName,
        lastname: this.lastName,
        birthDate: new Date(this.birthDate).getTime(),
        docType: this.docType,
        docNumber: this.docNumber,
        docExpDate: new Date(this.docExpDate).getTime(),
      };
      this.navCtrl.push(Verification_step2Page, {form: data});
    }
  }
}

// --------------------------------------------STEP 2----------------------------------------------------------------

@Component({
  selector: 'verification-page',
  templateUrl: 'verification_step2.html'
})

export class Verification_step2Page {
  form: any;
  user: any = {};
  userFiles: any[];

  constructor(
    public navCtrl: NavController,
    private navParams: NavParams,
    public platform: Platform,
    private camera: Camera,
    public http: HttpClient,
    public file: File,
    private storage: Storage,
    private loader: LoaderProvider,
    private verifyService: VerifyService,
    private authService: AuthService
  ) {
    this.form = this.navParams.get('form');
    console.log(this.form);
    this.platform.registerBackButtonAction(() => this.navCtrl.push(Verification_step1Page),1);
  }

  ionViewDidLoad() {
    this.updateUser();
  }

  updateUser() {
    this.authService.getAccount().subscribe(
      res => {
        this.user = res;
        this.storage.set('user', res);
        try { this.userFiles = res.profile.files; } catch (e) {}
      },
      err => {
        this.storage.get('user').then(user => this.user = user);
      }
    );
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
      this.uploadImage(formData);
    };
    reader.readAsArrayBuffer(file);
  }

  private uploadImage(formData: FormData) {
    this.verifyService.uploadPhoto(this.user.id, formData).subscribe(
      response => this.userFiles.push(response),
      err => console.log(err));
  }

  deletePhoto(id) {
    this.verifyService.deletePhoto(id).subscribe(
      res => this.updateUser(),
      err => console.log(err)
    );
  }

  goBackTo() {
    this.navCtrl.push(Verification_step1Page)
  }

  send() {
    this.verifyService.sendForm(this.user.id, this.form).subscribe(
      response => this.navCtrl.push(Verification_step3Page),
      err => console.log(err));
  }
}

// --------------------------------------------STEP 3----------------------------------------------------------------

@Component({
  selector: 'verification-page',
  templateUrl: 'verification_step3.html'
})

export class Verification_step3Page {
  requestStatus: string;

  constructor(private storage: Storage) {
    this.storage.get('user').then(user => {
      if (user.request)
        this.requestStatus = user.request.status;
      else
        this.requestStatus = 'APPROVED'
    });
  };
}
