# PixelWarfare
A project for the course [TDA367](http://www.cse.chalmers.se/edu/year/2017/course/tda367/#news)

## Who are we?
**We are Group 26, the Codetroopers**:

Abdullatif alShriaf (latiif) [llusx](https://github.com/llusx)

Hugo Ekelund [hugormen](https://github.com/Hugormen)

Lovisa Landgren [lumoIT](https://github.com/LumoIT)

 
## Getting started
This project consists of two parts, the Server-side and the Client-side.

To run the server, you can just use the commands

`mvn package`

 Then head to the folder *target* and run

`java -jar PixelWarfare-Server-1.0.jar`

**NOTE** If you're running the project outisde of an IDE, don't run it with JRE9, otherwise it is guaranteed to run on Jre8 and 7.


As for the Client-side, it goes best to run it inside Android Studio ecosystem, by importing the Gradle project.
Sync the Gradle Project, and make sure the API v25 is installed, and support for Constraint Layout is present. Normally, Android Studio will over guidance as to what to do.

## Getting it up and running

This project, starts the server on localhost listenting to port `3000`, to facilitate debugging and testing.

As of the date of this file, Android Studio's Emulator uses the address ```10.0.2.2``` to access the host's localhost address.

##
If you want to login, use the the existing id "Pixel", or just create your own. 

**Note** that you'll first be spawned on coordinates  (0,0), since the emulator has no predefined coordinates.

To simulate the movement in the eumlator we use the built in controller for the emulator:
![Extended controls for Android Emulator](http://i.imgur.com/MZW7jpY.png)

One can simulate moving to a specific coordinate to simulate the location change for the emulator.

Other option is loading a GPX file with predefined coordinates to simulate walking on a path.

A precompiled list of GPX files with coordinates around Johannaberg campus is available in the folder GPX-files
## 
