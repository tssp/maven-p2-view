# Maven-P2-View
A library to provide a true P2 repository view on Maven repositories

This project started to fill the gap between Tycho-based Eclipse RCP builds and the need for P2 repositories in a high-frequent read and write environment.

In such an environment one typically builds and deploys Maven artifacts but also OSGi artifacts to a central - commonly internal - artifact repository. There are tools that create P2 repositories out of the common Maven repositories, so that they can be consumed by Eclipse RCP developers.

Anyway in an environment with thousands of reads and writes - potentially concurrent - to such P2 repositories, lead to different challenges that must be tackled:

* Provide sophisticated locking mechanisms due to concurrent read and write access
* Treat multiple writes (deployments) of logically grouped features as atomic operations
* Guarantee consistency even for partially deployed artifacts
* Avoid blocking to not slow down read and write access

# Code Climate

[![Build Status](https://travis-ci.org/coding-me/maven-p2-view.svg?branch=master)](https://travis-ci.org/coding-me/maven-p2-view)

# Internals

## The Scala API


```scala
class XXX {
  
}
```
# Actor System

![Actor System](http://g.gravizo.com/g?
  digraph G {
    node [ fontname=Helvetica, fontsize=16];
    router[shape=box, label="Repository Router"];
    rr1[label="Repository\\nReceptionist\\n(Repo 1)"];
    rr2[label="Repository\\nReceptionist\\n(Repo n)"];
    router -> {rr1, rr2};
    mg[label="Metadata Generator"];
    mgu[label="Metadata Updater"];
    mgr[label="Metadata Rebuilder"];
    mg -> {mgu, mgr};
    ic[label="Artifact Collector"];
    ici[label="Insert Artifact Collector"];
    icd[label="Delete Artifact Collector"];
    rr1 -> {ic, mg};
    ic -> {ici, icd};
    aa1[label="Artifact\\nAnalyzer", fontsize=12];
    aa2[label="Artifact\\nAnalyzer", fontsize=12];
    aa3[label="Artifact\\nAnalyzer", fontsize=12];
    aa4[label="Artifact\\nAnalyzer", fontsize=12];
    aa5[label="Artifact\\nAnalyzer", fontsize=12];
    aa6[label="Artifact\\nAnalyzer", fontsize=12];
    ici -> {aa1, aa2, aa3};
    icd -> {aa4, aa5, aa6};
  }
)

# Acknowledgments

* Klaus: for discussing actor systems on a whiteboard
* Stephan: for discussing about reactive systems in general
* Stefan (another one): for his fundamental knowledge about Eclipse RCP
* Juergen: for the deep knowledge about Eclipse and build tools
* Rainer: for the deep review discussions and the 5 why's
