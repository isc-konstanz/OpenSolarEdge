![emonmuc header](docs/img/emonmuc-logo.png)

This project implements a communication protocol as part of [emonmuc](https://github.com/isc-konstanz/emonmuc/) (**e**nergy **mon**itoring **m**ulti **u**tility **c**ommunication), based on the open-source project [OpenMUC](https://www.openmuc.org/), a software framework based on Java and OSGi, that simplifies the development of customized *monitoring, logging and control* systems.


----------

# OpenSolarEdge

*This section is a placeholder and will be filled with a project description.*


## 1 Installation

To setup this protocol driver, [emonmuc](https://github.com/isc-konstanz/emonmuc/) needs to be installed. To do so, a comprehensive guide is provided on the projects GitHub page.

With emonmuc being installed, the driver may be enabled

~~~
emonmuc install solaredge
~~~

To disable the driver, use

~~~
emonmuc remove solaredge
~~~

This shell command will set up the driver, as instructed in the [setup script](setup.sh).  
If there is the need to manually install the driver, the separate [installation guide](docs/LinuxInstall.md) may be followed.


## 2 Development

To provide an entrypoint for further developments, a generated [javadoc](https://isc-konstanz.github.io/OpenSolarEdge/javadoc/) can be visited.  
For other questions or initiatives please don't hesitate to file an issue or get into contact directly.


----------

# Contact

This project is maintained by:

![ISC logo](docs/img/isc-logo.png)

- **[ISC Konstanz](http://isc-konstanz.de/)** (International Solar Energy Research Center)
- **Adrian Minde**: adrian.minde@isc-konstanz.de
