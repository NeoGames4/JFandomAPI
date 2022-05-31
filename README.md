# JFandomAPI
A Java library for Fandom.com.

## Features
1. Receive information about Fandom pages, articles, images, sections, posts and users.
2. Get information about a Fandom, as well as statistics.
3. Be notified whenever a recent change or a discussion post happens (via listeners).

## Getting started
1. Download the [latest release](https://github.com/NeoGames4/JFandomAPI/releases) (make sure to download the org.json-file, too) and add them to your project.


2. Create a new Fandom instance:

```java
Fandom fandom = new Fandom(id);
```

The ID should look like fandom-id.fandom.com[/lang] (for example '`disney.fandom.com`' or '`avatar.fandom.com/de`').

3. Do whatever you want to do (but make sure to follow WikiMedia's and Fandom's guidelines of course).
