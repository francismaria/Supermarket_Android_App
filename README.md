# Acme Supermarket: An Android App

This repo hosts the code for an android application created for the CMOV (Mobile Computing) course at FEUP (2019/2020). 

## Table of Contents

- [What is it?](#what-is-it)
- [What technologies were used?](#what-technologies-were-used)
- [Main functionalities](#main-functionalities)
- [Development](#development)

## What is it?

The project consisted in creating an Android application to simulate a supermarket app where the users could shop with their smartphones only. Below is the transcript of the project's requirements.

*"A supermarket – call it the Acme Electronic Supermarket – intends to implement a more efficient shopping and payment system supplying an Android app to its customers.*

*The idea asks for the customers to register the products in the app, when they put them in the shop basket. When they are ready to leave, they check out the products in the basket passing or showing the phone in a terminal. The payment is made (using the associated credit or debit card) and the gate doors are opened.*

*Using the app, the customers should first make a registration (only once when they use the app for the first time) in the supermarket remote service, supplying personal data and payment card data for payments. Also, an asymmetric cryptographic key pair is generated, being the private key securely stored in the device.*

*At any time, the app should be capable of consulting past transactions’ information, and retrieve available emitted loyalty vouchers, that can concede a discount in some future buy. A loyalty voucher is offered whenever the customer accumulated payments surpassed a multiple of some value."*

<p align="middle">
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/communication_diagram.png" hspace="20">
</p>
*Fig.1: Communication diagram*

## What technologies were used?

| Component | Technology | 
| ------------- |:-------------:| 
| Server | Apache Tomcat + Jersey + SQLite3 | 
| Client | Java |  
| Terminal | Java |  

## Main Functionalities

#### 1. Registration

The registration process goes through a thorough input validation as well as a process of formatting text.

In the input validation, by making use of the Android EditTexts' `inputType` capabilities, we make sure that the user when entering his/hers details, inputs the correct format of information. For example, when the user is asked to insert the credit card credentials, upon click, the keyboard only accepts numbers as input, enforcing a security policy against potential attacks to the application and providing a better user interface. 

Throughout the text fields the user has to fill in order to complete his registration in the app, there are multiple hints to guide the user through this process. Also, it is important to note that exists real-time warning messages about specific requirements of a field

*Fig.2: Registration's personal information details*


*Fig.3: Registration's credit card input type*

<p align="middle">
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/register.jpg" height="500" hspace="20"/>
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/creditcardkeyboard.png" height="500" hspace="20"/> 
</p>

#### 2. Log In

The login process also goes through an input validation. The user should insert his username and password to be able to enter the application. This creates an APIRequest to evaluate the information and check if that data is already registered inthe server.

<p align="middle">
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/login.jpg" height="500"/>
</p>

*Fig.4: Log in page*

#### 3. Buying

The Acme Supermarket tries to offer an efficient and instinctive way to the client realize his shopping. The user enters the application and can immediately add products to his shopping cart with a QR Code reader. The products in this demonstration are represented by its corresponding QR codes, as you can see in the image below.

<p align="middle">
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/nivea.png" height="500"/>
</p>

*Fig.5: QR code for a product*

In the end of the shopping or if the user adds a product that he doesn't wants anymore, he can view his full basket and remove the products that he wants. Finally he can perform the checkout which generates also a QR Code that will be read from the Acme Supermarket terminal and with that, the shopping process will end.

The user is able to read these QR Codes in just one click after he enters the application on the activity displayed on. The listener of the button opens a QR Code reader that will be able to interpret the encoded message of each product.

<p align="middle">
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/home_screen.jpg" height="500"/>
</p>

*Fig: 6 - Acme's Home Screen*

Finally the user is ready to finalize his shopping. On the top right corner on every screen of the application the user has a cart logo available to click that redirects the application for the cart screen that is shown in [Fig. 8]. This section allows the user add or remove the products and select the amount of vouchers that wants to use for a final discount (if available).

<p align="middle">
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/cart.jpg" height="500" hspace="20"/>
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/vouchers.jpg" height="500" hspace="20"/> 
</p>

*Fig: 7 - Acme's Checkout Pages*

#### 4. Consulting Past Transactions

A user can check on his past transactions at any time. In the navigation menu, there exists the "History" option which leads to a Fragment containing a RecyclerView where, upon receiving the server response for the current logged user past transactions, it is updated with all the transactions/orders the user has done. Moreover, the user can click in any of these elements of the RecyclerView, in order to check the details of the transaction (its date, the products bought, if a voucher was used, the total cost of the transaction, etc.). 

<p align="middle">
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/history.jpg" height="500" hspace="20"/>
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/order.jpg" height="500" hspace="20"/> 
</p>

*Fig. 8 - Past History Screens*

#### 5. Profile

Acme Supermarket' application also provides an independent screen for the user to check his information. In this screen the user can confirm his username, email or even the credit cart that he has introduced when registering.

<p align="middle">
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/profile.jpg" height="500" hspace="20"/>
</p>

*Fig. 9 - Profile Screen*

#### 6. Contact

The feedback that each costumer can provide for a company is key for its success. Knowing this fact we have also implemented a Contact functionality that allows the user to directly contact the company. In the application just needs to write the message and click the *Send Button* and it'll be sent to the Acme Supermarket team (server) which can then access it in a file which keeps every message with the following format : \[date - email] : msg.

<p align="middle">
  <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/empty_message.jpg" height="500" hspace="20"/>
    <img src="https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/message_sent.jpg" height="500" hspace="20"/>
</p>

*Fig. 10 - Contact Screen*

#### 7. Security

As it was requested, our application uses \textit{digital signatures} to sign a message from a user upon he *checked-out* a purchase. In order to do this, to each client, upon registering himself in the application, is generated a keypair (private-public) which allows to sign these kind of messages.

This keypair was generated by entering the following commands:

    1st command: keytool -genkeypair -alias acme -keyalg RSA -keysize 2048 -dname "CN=AcmeServer" -validity 365 -storetype PKCS12 -keystore acme-server-keystore.p12 -storepass password
    
    
    2nd command: keytool -exportcert -alias acme -storetype PKCS12 -keystore acme-server-keystore.p12 -file acme_certificate.cer -rfc -storepass password
    
After the generation of the keys, the client will send to the server his public key, so that when the user signs its message, the server can be sure of its integrity, non-repudiation and authenticity by verifying the signature with the now kept public key.

The supermarket products are also encrypted with the supermarket's private key. The user, upon registering, receives the server's public key so he can later decrypt the QR code tag message. 

As an extra feature, we decided to hash the passwords entered by the user, in the login and the registration of the app. This way, ensures that the password is not *easy-read* for hipothetical attackers. 

## Development

This project is no longer under development.
