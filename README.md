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

![Communication diagram](https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/communication_diagram.png)

*Fig.1: Communication diagram*

## What technologies were used?

| Component | Technology | 
| ------------- |:-------------:| 
| Server | Apache Tomcat + Jersey + SQLite3 | 
| Client | Java |  
| Terminal | Java |  

## Main Functionalities

1. Registration

The registration process goes through a thorough input validation as well as a process of formatting text.

In the input validation, by making use of the Android EditTexts' `inputType` capabilities, we make sure that the user when entering his/hers details, inputs the correct format of information. For example, when the user is asked to insert the credit card credentials, upon click, the keyboard only accepts numbers as input, enforcing a security policy against potential attacks to the application and providing a better user interface. 

Throughout the text fields the user has to fill in order to complete his registration in the app, there are multiple hints to guide the user through this process. Also, it is important to note that exists real-time warning messages about specific requirements of a field

![Registration screenshot 1](https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/register.jpg)

*Fig.2: Registration's personal information details*

![Registration screenshot 2](https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/creditcardkeyboard.png)

*Fig.3: Registration's credit card input type*

2. Log In

The login process also goes through an input validation. The user should insert his username and password to be able to enter the application. This creates an APIRequest to evaluate the information and check if that data is already registered inthe server.

![Login screenshot 2](https://github.com/francismaria/Supermarket_Android_App/blob/master/docs/login.jpg)


## Development
