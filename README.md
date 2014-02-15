# jnuit

Java Newton User Interface Toolkit is a simple lib to create UI for games.

It use [lwjgl](http://lwjgl.org) rendering and a [playn](https://code.google.com/p/playn/) implementation is in development.

[![demo](http://devnewton.bci.im/media/jnuit/jnuit_demo.png)](http://devnewton.bci.im/media/jnuit/jnuit_demo.webm)

## Features

- simple components: button, select, toggle.
- specialized components for games: control settings, video mode selector, audio volume configurator, texts and voices language selector.
- navigable with only one input device: mouse, keyboard or gamepad.
- free or table based layout.

## Optional features

The lwjgl implementation also comes with a collection of utilities to make 2d game prototyping easy to do:

- assets management.
- [nanim](http://devnewton.bci.im/softwares/nanim) 2d animations loading.
- smpjpeg video playback.

## Games made with jnuit

- [Newton Adventure](http://devnewton.bci.im/games/newton_adventure)
- [Ned et les maki](http://devnewton.bci.im/games/nedetlesmaki)

# Getting started

## Use jnuit for a new game project

Use [maven](https://maven.apache.org/) archetype plugin to create a project using jnuit:

```bash
mvn archetype:generate -DarchetypeGroupId=im.bci -DarchetypeArtifactId=jnuit-archetype -DarchetypeVersion=LATEST -DgroupId=comycompagny.superbaryo2 -DartifactId=superbaryo2 -Dgame-name=superbaryo2 -Dgame-package=com.mycompagny.superbaryo2 -Dversion=1.0-SNAPSHOT
```

This project archetype contains everything you need to do a nice game:

- a full GUI with every settings players expects: video, audio, inputs, languages, bonus...
- dependency injection with [guice](https://code.google.com/p/google-guice/).
- entity system with [artemis](http://gamadu.com/artemis/).
- [tiled](http://www.mapeditor.org/) level loading.
- basic rendering and main character moving in isometric and orthogonal levels.

## Use jnuit with an existing lwjgl based project

Add jnuit to your maven dependency list:

```xml
<dependency>
    <groupdId>im.bci</groupId>
    <artifactId>jnuit-lwjgl</artifactId>
    <version>${jnuit.version}</version>
</dependency>
```
