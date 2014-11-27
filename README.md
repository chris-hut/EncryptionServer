EncyrptionServer
================

Project 2 for ECE 422: Reliable & Secure Systems Design

Using a client-server architecture a secure fileshare has been created allowing clients to download files from the server.

Encryption is provided via the Tiny Encryption Algorithm (TEA) cipher, which encrypts and decrypts all communication between client and server.
* Encryption is performed in C for performance reasons, and is called via JNI
* Drivers for both a server and a client are provided allowing for easy setup and destruction of clients and servers
* Server is multithreaded allowing for scalability with multiple clients
