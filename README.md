open-android
============
# PG Prerequisites
* You need to enroll with Citrus as a merchant.
* Have an HMAC generator installed on your server - (Sample - sign.php - replace your secret key and access key.)
* Make sure that you have following parameters from Citrus
	1) Secret Key (Admin panel --> Checkout Page Settings)
	2) Access Key (Admin panel --> Checkout Page Settings)

Following can be obtained from our support team. Do write a mail to tech.support@citruspay.com or call on +91-87-677-099-00 Extn: 2 (Technical Support)
	3) SignIn Key 
	4) SignIn Secret
	5) SignUp Key
	6) SignUp Secret
  	

# SDK Prerequisites

You need to have installed and configured:
* Java JDK version 1.6 or greater
* Android SDK
* A `git` client
* All Citrus PG Prerequisites.

In case you do not have these details, do not proceed.


# Installation
## From source code
Get the latest source code from github.com:
```bash
$ git clone https://github.com/citruspay/open-android.git
```

## IDE integration
### Eclipse

For Eclipse, import the NativeSDK and CitrusLibrary folders into, by using Import -> General -> Existing Projects into Workspace, and browsing to the open-android folder. Make sure that you add CitrusLibrary as a dependency to the NativeSDK Project.

### Android Studio

_For Android Studio, choose Import Project from the "Welcome to Android Studio" screen. Choose the build.gradle file at the top of the open-android repository. A sample implementation can be found in NativeSDK._

