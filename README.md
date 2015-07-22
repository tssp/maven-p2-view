# Maven-P2-View
A library to provide a true P2 repository view on Maven repositories

This project started to fill the gap between Tycho-based Eclipse RCP builds and the need for P2 repositories in a high-frequent read and write environment.

In such an environment one typically builds and deploys Maven artifacts but also OSGi artifacts to a central - commonly internal - artifact repository. There are tools that create P2 repositories out of the common Maven repositories, so that they can be consumed by Eclipse RCP developers.

Anyway in an environment with thousands of reads and writes - potentially concurrent - to such P2 repositories, lead to different challenges that must be tackled:

* Provide sophisticated locking mechanisms due to concurrent read and write access
* Treat multiple writes (deployments) of logically grouped features as atomic operations
* Guarantee consistency even for partially deployed artifacts
* Avoid blocking to not slow down read and write access

# Acknowledgments

* Klaus: for discussing actor systems on a whiteboard
* Stephan: for discussing about reactive systems in general
* Stefan (another one): for his fundamental knowledge about Eclipse RCP
* Juergen: for the deep knowledge about Eclipse and build tools
* Rainer: for the deep review discussions and the 5 why's
