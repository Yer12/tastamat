# Tastamat Mobile Application for iOS and Android

Is is used to

## Development Environment

### NodeJS, NPM & NVM

Use [NVM](http://nvm.sh) to manage node version & use node version 8.

### Ionic

- https://ionicframework.com/docs/installation/cli

```
$ npm install -g ionic
```

- https://ionicframework.com/docs/installation/ios

```
$ npm install -g ios-sim
$ npm install -g ios-deploy
```

Use Cordova 8:

```
$ npm i -g cordova@8
```

## Build

```
$ ionic cordova prepare ios
$ ionic cordova prepare android
```

If you face this error:
```
ERROR: Could not find method leftShift() for arguments [build_1o7oa8h4zgt9598snalt19rye$_run_closure6@5dbcd76e] on task ':app:cdvPrintProps' of type org.gradle.api.DefaultTask.
```

here is the solution:

https://stackoverflow.com/questions/55793095/could-not-find-method-leftshift-for-arguments-after-updating-studio-3-4


- https://ionicframework.com/docs/building/ios
