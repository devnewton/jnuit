# jnuit

Java Newton User Interface Toolkit is a simple lib to create UI for games.

It use [lwjgl](http://lwjgl.org) rendering and a [playn](https://code.google.com/p/playn/) implementation is in development.

[![demo](http://devnewton.bci.im/media/jnuit/jnuit_demo.png)](http://devnewton.bci.im/media/jnuit/jnuit_demo.webm)

## Features

- simple components: button, select, toggle.
- specialized components for games: control settings, video mode selector, audio volume configurator, texts and voices language selector, dialogues...
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

## Use jnuit

### For a new game project

Use [maven](https://maven.apache.org/) archetype plugin to create a project using jnuit:

```bash
mvn archetype:generate -DarchetypeGroupId=im.bci -DarchetypeArtifactId=jnuit-archetype -DarchetypeVersion=LATEST -DgroupId=com.mycompagny -DartifactId=superbaryo2 -Dversion=1.0-SNAPSHOT
```

This project archetype contains everything you need to do a nice game:

- a full GUI with every settings players expects: video, audio, inputs, languages, bonus...
- dependency injection with [guice](https://code.google.com/p/google-guice/).
- entity system with [artemis](http://gamadu.com/artemis/).
- [tiled](http://www.mapeditor.org/) level loading.
- basic rendering and main character moving in isometric and orthogonal levels.

### With an existing lwjgl based project

Add jnuit to your maven dependency list:

```xml
<dependency>
    <groupdId>im.bci</groupId>
    <artifactId>jnuit-lwjgl</artifactId>
    <version>${jnuit.version}</version>
</dependency>
```

## Learn API

jnuit is classic GUI API with a simple widget and layout hierarchy. There is only a few concepts:

- widget: basic GUI element. Every widget has a background, borders and can react to input events.
- layout: widget made to contains other widget.
- cursor: a cursor focus the current selected widget. Cursor can be moved using keyboard, mouse or gamepad.
- input events: moving cursor, ok and cancel.

The best way to learn jnuit API is to look at samples:

- [How to init jnuit and integrate with an lwjgl event loop](samples/src/main/java/im/bci/jnuit/samples/AbstractSample.java): all sample are based on this one.
- [Mandatory Hello world sample](samples/src/main/java/im/bci/jnuit/samples/HelloWorld.java).
- [How to layout widgets freely](samples/src/main/java/im/bci/jnuit/samples/ContainerLayoutSample.java).
- [How to layout widgets in table](samples/src/main/java/im/bci/jnuit/samples/TableLayoutSample.java).
- [How to create a simple form](samples/src/main/java/im/bci/jnuit/samples/FormSample.java).
