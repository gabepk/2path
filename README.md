# 2Path Interface

## Introduction
2Path is a biological graph database for terpenoids, whose data are taken from [KEGG](http://www.kegg.jp/).

The 2Path interface allows users to search for either an enzyme or a pathway between two metabolites on a selected organism from the database. If there is enzyme or pathway, it shows a graph where the nodes can be an enzyme, a reaction or a compound.

![enzyme_graph](https://github.com/gabepk/2path/blob/master/src/main/webapp/resources/images/enzyme.png)

## Implementation
The _back-end_ is in Java and the data are accessed remotely with the Neo4j API.

The _front-end_ is in XHTML + Primefaces and the js's library D3 is used for the design of the graph.
