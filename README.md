# DAI Practical work 2

## Authors
[Polien Lionel](https://github.com/polionel) and [Menétrey Arthur](https://github.com/xenogix)

## User guide
This program is an online game of 4 in a row. You can use this program to host a game or to join a server and play.

You can use the following commands :

| Command | Purpose                                      |
|:--------|:---------------------------------------------|
| server  | Start a server that will host a game         |
| client  | Start the client to join the server and play |
| help    | Shows the list of commands                   |

In order to run the program you can download [the compiled .jar file](build/FourInARow_20231128_release.jar) or clone this repo and build it yourself using maven.

Then you can run the following commands in a command prompt taking care of filling the placeholders.

To run the server use :

```
java -jar [file path of the compiled .jar] server -p [port]
```

To run the client use :

```
java -jar [file path of the compiled .jar] client -a [address] -p [port]
```

The port is optional for both client and server and the server will be hosted by default on the 5187 port.

The address is mandatory for the client and should correspond to the address of the game server

If the server was started successfully you should see a message similar to this :

```
[Information @ 13:55 02.470 | GameServer | Starting server on port 5187]
```

If an error occurs, an explicit error message will be written. 
In this example we try to run the server twice on the same port :

```
[Information @ 13:56 00.742 | GameServer | Starting server on port 5187]
[Error @ 13:56 00.750 | GameServer | Handled error : Address already in use: bind]
[Information @ 13:56 00.751 | GameServer | Shutting down server]
```

If the client was started successfully you should see a message similar to these :

```
Waiting for other player to join
```

<sup>If we are the first player to connect</sup>


```
| | | | | | | |
| | | | | | | |
| | | | | | | |
| | | | | | | |
| | | | | | | |
| | | | | | | |
 1 2 3 4 5 6 7
It's the other player turn
```

<sup>If we are the second player to connect and the game started</sup>


If an error occurs, an explicit error message will be written.
In this example we try to run the client but the server has not started :

```
An error occured : Connection refused: connect
```

## Commands parameters

Server parameters :

| Name |   FullName    | Purpose                                                   |
|:-----|:-------------:|:----------------------------------------------------------|
| -p   |    --port     | Port on which the server will be hosted                   |
| -h   |    --help     | Shows a list of the parameters and how to use the command |
| -V   |   --version   | Print the current version of the command                  |


Client parameters :

| Name | FullName  | Purpose                                                   |
|:-----|:---------:|:----------------------------------------------------------|
| -p   |  --port   | Port on which the server to join is hosted                |
| -a   | --address | Address at which the server is hosted                     |
| -h   |  --help   | Shows a list of the parameters and how to use the command |
| -V   | --version | Print the current version of the command                  |

The port is optional for both client and server and the server will be hosted by default on the 5187 port.

## Build the project using Maven

In order to build this project we use maven wrapper and so the manual installation of maven is not needed.

Note that a JAR containing only the classes and code of the project is built, whose name begins with "original".
In order to run the CLI you need to use the "executable" version and not the "original" one.

In order to build you can execute the following command in a command prompt at the root of the project.

```
mvn dependency:resolve clean compile package
```

<sup>Note that you need to be in the project's root folder in order for it to work</sup>
<sup>(you can use the command "cd [path]" to select a location on most common OS</sup>

If the process was successfull, you will find the compiled JAR file in the project "target" directory.

## Depedencies

This project was built and tested using the following depedencies. Feel free to use other versions of the depedencies
but keep in mind this could cause errors when building / running the program. You can edit the depedencies in the [pom.xml](pom.xml) file

| Depedency              |   URL                                                                                      | Version |
|:-----------------------|:-------------------------------------------------------------------------------------------|:-------:|
| picocli                | https://mvnrepository.com/artifact/info.picocli/picocli                                    | 4.7.5   |


## Protocole implementation

This protocole use an object-oriented approach and will transmit a command object between the client and the server.
To guarantee communication reliability this protocol uses TCP.

Please find the following UML diagram that describes communication between client and server :

![Overview](http://www.plantuml.com/plantuml/png/SoWkIImgAStDuIe0qfd9cGM9UIKAmQb5PQb52ed52iLW5bTYSab-aK8ea6S84Yq5CP04ZM22HbnSO3a5NJkeLWK5AmMl_CmyBWWNA07H3H9tJ4vEBIZX0ciagCC8eGEh9G4vYScfs6m9BKX9B46NCKAIWV0n3LF0gGUVFGwfUId0y0G0)

<sup>Overview</sup>

![Quit or error handling](http://www.plantuml.com/plantuml/png/XP2n3W8X38RtFaL77OoHyGPphauQSmz0UXr8Ji1UyVa5WWanHYVQ_-FZJnXNjQBfla3CfoCNhLUyiJnPqAwurWaiQH2SydOeQIf35vftQ00gmzsngOW3deFpqDOCwesIduWuAbJnhs4wevLqpe2NnH9_QvdYRFMJc8wqlN_icfpYXaM2O21AmlEU5iTUyP7HzaSFZEpd_A4l)

<sup>Error and quit handling</sup>

![Too many player handling](http://www.plantuml.com/plantuml/png/SoWkIImgAStDuIe0qfd9cGM9UIKAmQb5PQb52ed52iLW5bTYSab-aK8ea6S84Yq5CP04ZM22HaY2nZ11OowkKh1E1Lqxg84ALWfU-PbvN10UI1oY0YZkc9oSMb02rc54K8SHcWvZWZgCGJg2KjE0R2ukXzIy5A3V0G00)

<sup>Too many player example</sup>

The client and server can exchange the following commands :

| Command        | Purpose                                               | Sender |
|:---------------|:------------------------------------------------------|--------|
| InvalidCommand | Send information about an error that occurred         | Server |
| Join           | Ask to join the game                                  | Client |
| Accept         | Accept the client join request                        | Server |
| Refuse         | Refuse the client command with information about why  | Server |
| Place          | Ask to place a pawn                                   | Client |
| FF             | Inform that we want to forfeit the game               | Client |
| Quit           | Inform that we will quit the game                     | Client |
| Update         | Sends information about the current state of the game | Server |