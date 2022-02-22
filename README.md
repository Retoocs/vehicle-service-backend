## Installation

This project can be used as a base to your NAE application. Before you start coding please consider doing following
steps to personalize project:

- Rename root java package - **done**
- Edit maven project attributes in pom.xml, mainly groupId and artifactId - **done**
- Generate security certificates for token encryption

#### Generate certificates

You should generate own certificates to encrypt token used by NAE.

```shell
$ cd src/main/resources/certificates && openssl genrsa -out keypair.pem 4096 && openssl rsa -in keypair.pem -pubout -out public.crt && openssl pkcs8 -topk8 -inform PEM -outform DER -nocrypt -in keypair.pem -out private.der && cd ../../../..
```
