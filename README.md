## Introduction
Le programme permet de visualiser les flux entre deux machines via leurs traces, et donne les détails des flux dans un fichier choisi en sortie.

## Fichiers principaux
src : où se trouve le code

sequences : où se trouveront les fichiers de sortie

data : données pour tester et utiliser le programme

bin : fichiers compilés

### Src : fichiers principaux
3 fichiers principaux dans visualisateur/ :
- Visualisateur.java : le fichier principal pour le lancement du programme.
- sequence : fichier d'analyse de la trame importée
- readwrite : fichier de lecture en entrée et d'écriture en fichier choisi de sortie.

## Sortie visualisateur
- On peut voir le flux du traffic de chaque machine à l'aide de "flèches" allant de l'adresse IP source à la destination.
- Un filtre peut être appliqué sur ces adresses IP
- au dessous de chaque envoie, on trouve plusieurs informations sur : 
  - le protocol
  - les ports source et destination
  - le window
  - la longueur
  - le ACK brute (donc le ACK original du flux, un ack relatif peut se calculer à partir de ce dernier)
  - le SEQ brute (donc le SEQ orinial du fluux, un SEQ relatif peut se calculer à partir de ce dernier).
- Un filtre pour choisir HTTP, TCP ou les deux est fournis.
- Le protocol est mis en couleur verte pour faciliter la visualisation du traffic
- Les informations et détails en dessous du traffic sont mis en différente couleur pour se séparer du traffic.

## Type de fichiers en entrée
- Chaque octet est codé par deux chiffres hexadécimaux.
- Chaque octet est délimité par un espace.
- Chaque ligne commence par l’offset du premier octet situé à la suite sur la même
ligne. L’offset décrit la positon de cet octet dans la trace.
- Chaque nouvelle trame commence avec un offset de 0 et l’offset est séparé de
trois espaces des octets capturés situés à la suite.
- L’offset est codé sur quatre chiffres hexadécimaux.
- Les caractères hexadécimaux peuvent être des majuscules ou minuscules.

### Fichiers des trames 
- segment : analyse des couches de la trame Ethernet
- segments supportés pour le visualisateur : TCP et HTTP.
- autres segments pour fichier de sortie (détails des ségements) : ARP, DNS, ICMP, UDP.
- flagsIP, flagsTCP, flagsDNS : utilisent l'interface FlagsInterface.java, flags spécifiques à chaque protocol.
- Les champs sont distribués sur plusieurs dossiers : info, lenght, qandanum, options, nextsegment, portandAddress.
- Ils utilisent tous l'interface ChampsInterface.java

## Comment fonctionne le programme
- Le programme a besoin de 2 arguments pour fonctionner : un argument avec le fichier d'entrée, et un argument pour celui de sortie.
- Dans le cas de plus d'arguments ou de moins d'arguments le programme va envoyer un message d'erreur.
- Le makefile vous indique comment mettre ces arguments.
- Après le lancement, le programme vous demande 2 filtres indépendants, mais pouvant être appliqués en même temps. Le premier filtre est celui du protocole (filtretype), le deuxième est le filtre d'adresse IP.
- Le programme peut être lancé sans filtre, en appuyant entrée.

### Quelques ressources
-  http://www.networksorcery.com/enp/topic/ipsuite.htm
-  https://www.wireshark.org/docs/wsdg_html_chunked/
-  https://gitlab.com/wireshark/wireshark/-/wikis/SampleCaptures
-  https://overapi.com/java
