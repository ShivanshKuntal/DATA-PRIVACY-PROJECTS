

# Cryptography Algorithms Collection

This repository contains Java implementations of several classical and modern cryptographic algorithms. The goal of this project is to provide clean, modular, and educational examples of encryption, decryption, and signature schemes used across different eras of cryptography.

The collection includes symmetric-key block ciphers, classical substitution ciphers, and a public-key digital signature scheme. Each implementation is structured for clarity and can be used for academic learning, demonstrations, or experimentation.

---

## 📌 **Algorithms Included**

### 1. **AES Encryption**

A modern symmetric-key block cipher widely used in secure communication systems.

* Supports 128-bit block operations
* SubBytes, ShiftRows, MixColumns, AddRoundKey transformations
* Resistant to differential and linear cryptanalysis

---

### 2. **DES Encryption**

The classic 56-bit key block cipher.

* Uses 16-round Feistel network
* Includes initial and final permutations
* Educational value in understanding symmetric encryption internals

---

### 3. **Triple DES (3DES)**

A strengthened version of DES applying:

```
Encrypt → Decrypt → Encrypt (EDE)
```

* Improves security over standard DES
* Still used in legacy banking systems

---

### 4. **Modified DES (64-bit Enhanced Version)**

A custom modification of DES supporting:

* 64-bit plaintext block processing
* Similar Feistel structure
* Intended for academic demonstration of cipher modifications

*(Useful for understanding how small changes in key or round structure affect cipher behavior.)*

---

### 5. **Hill Cipher**

A classical cipher based on linear algebra.

* Uses matrix multiplication over mod 26
* Demonstrates key concepts: invertible matrices, vector encoding
* Educational for understanding algebraic cryptanalysis

---

### 6. **Playfair Cipher**

A digraph substitution cipher.

* Uses 5×5 key matrix
* Encrypts letter pairs instead of single letters
* Stronger than Caesar/Vigenère but still a classical cipher

---

### 7. **ElGamal Digital Signature**

A public-key cryptographic signature scheme based on the difficulty of the discrete logarithm problem.

* Key generation (p, g, x, y)
* Signature generation & verification
* Ideal for demonstrating asymmetric crypto fundamentals

---

## 📂 **Project Structure**

```
├── AES ENCRYPTION/
├── DES ENCRYPTION/
├── TripleDes.java
├── ModifiedDES64.java
├── HillCipher.java
├── PlayfairCipher.java
├── ElGamalSignatureTable.java
├── .gitattributes
├── README.md
```

* Each algorithm folder or file contains a self-contained implementation.
* Code is written in Java for portability and academic clarity.

---

## 🚀 **Usage**

Compile and run using:

```bash
javac FileName.java
java FileName
```

Modify the main functions inside each file to test:

* Encryption
* Decryption
* Key generation
* Signature verification

---

## 🎯 **Learning Objectives**

This repository helps students understand:

* Difference between classical and modern cryptography
* Symmetric vs. asymmetric encryption
* Block vs. stream ciphers
* Feistel networks
* Algebraic ciphers
* Public-key signature schemes

Perfect for B.Tech, Cybersecurity, Cryptography, and Information Security coursework.

---

## ⚠️ **Disclaimer**

These implementations are for **educational purposes only**.
Do not use them in production systems or anywhere requiring real-world security.


Just tell me.

