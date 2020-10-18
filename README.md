# Swf Map Loader
[![Build Status](https://scrutinizer-ci.com/g/Arakne/SwfLangLoader/badges/build.png?b=master)](https://scrutinizer-ci.com/g/Arakne/SwfLangLoader/build-status/master) [![Scrutinizer Code Quality](https://scrutinizer-ci.com/g/Arakne/SwfLangLoader/badges/quality-score.png?b=master)](https://scrutinizer-ci.com/g/Arakne/SwfLangLoader/?branch=master) [![Code Coverage](https://scrutinizer-ci.com/g/Arakne/SwfLangLoader/badges/coverage.png?b=master)](https://scrutinizer-ci.com/g/Arakne/SwfLangLoader/?branch=master) [![javadoc](https://javadoc.io/badge2/fr.arakne/swf-lang-loader/javadoc.svg)](https://javadoc.io/doc/fr.arakne/swf-lang-loader) [![Maven Central](https://img.shields.io/maven-central/v/fr.arakne/swf-lang-loader)](https://search.maven.org/artifact/fr.arakne/swf-lang-loader) 
 
Load and parse Dofus retro swf lang files.

## Installation

For installing using maven, add this dependency into the `pom.xml` :

```xml
<dependency>
    <groupId>fr.arakne</groupId>
    <artifactId>swf-lang-loader</artifactId>
    <version>0.1-alpha</version>
</dependency>
```

## Usage

### Simple usage

On this example, we find the nearest bank map for the given player :

```java
public class Main {
    public MapPosition findNearestBankMap(Player player){
        // Declare the loader 
        // The first parameter is the langs location, which contains versions_xx.txt files, and swf folder
        // The second parameter is the language. Can be "fr", "en", "de", "es", "it", "nl", "pt"
        LangLoader loader = new LangLoader("http://my-cdn.dofus-server.com/lang", "fr");

        // Load maps and hints swf files
        MapsFile maps = loader.maps();
        HintsFile hints = loader.hints();

        // Get map position
        MapPosition currentPosition = maps.position(player.map().id());

        // Get all banks hints, convert to map position, and get the nearest position
        return hints.byType(Hint.TYPE_BANK)
            .map(hint -> maps.position(hint.mapId()))
            .min(Comparator.comparingInt(currentPosition::distance))
        ;
    }
}
```

### Configure loader & cache system

The loader can be configurer to customize the version loader, and cache system.
If enabled (by default), the loader will keep decompiled action script files as cache.

```java
public class Main {
    public LangLoader getLoader() {
        return new LangLoader(
            "http://my-cdn.dofus-server.com/lang", // The lang CDN 
            "fr", // The language
            new TxtVersionsLoader(), // Define the version loading strategy. TxtVersionsLoader will parse versions_xx.txt file
            new SwfFileLoader(
                Paths.get("my/cache/directory"), // Define the cache directory
                true // Enable caching ? (i.e. keep AS files for further use)
            )
        );
    }
}
```

You can also clear the cache, and force reload all swf files by calling `LangLoader#clear()` method.

### Load a custom SWF structure

You can declare a custom SWF file and load it by using [SwfFileLoader](./src/main/java/fr/arakne/swflangloader/loader/SwfFileLoader.java).

```java
public class Main {
    // Define the structure class. Here extends BaseLangFile to handle default (i.e. undeclared) SWF variable
    class MyCustomSwf extends BaseLangFile {
        // Declare "FOO" swf variable as string
        // When an assignation `FOO = xxx;` will be parsed, the value "xxx" will be interpreted as String, and MyCustomSwf#foo will be set. 
        @SwfVariable("FOO")
        private String foo;
    
        // The variable name is optional if the java field name match with the SWF variable.
        // The parser handle JSON objects type
        @SwfVariable
        private MySubObject SUB;

        // Handle associative assignation (i.e. `OBJ["foo"] = {...};`)
        // Extract key and value type for declaration. Can handle any primitive value as key.
        @SwfVariable("OBJ")
        final private Map<String, MyOtherObject> objects = new HashMap<>();
    }

    public static void main(String[] args) {
        // Declare the swf loader
        SwfFileLoader loader = new SwfFileLoader();

        // Declare the hydrator by parsing class annotations
        MapperHydrator<MyCustomSwf> hydrator = MapperHydrator.parseAnnotations(MyCustomSwf.class);
        
        // Instantiate the SWF structure
        MyCustomSwf swf = new MyCustomSwf();
        // Load and parse SWF file. The URL can be a local file
        loader.load(new URL("http://my-cdn.dofus-server.com/lang/swf/custom_fr_123.swf"), swf, hydrator);

        // Here, you can use swf
        System.out.println(swf.foo);
    }
}
```

## Licence

This project is licensed under the LGPLv3 licence. See [COPYING](./COPYING) and [COPYING.LESSER](./COPYING.LESSER) files for details.

Use [FFDec Library](https://github.com/jindrapetrik/jpexs-decompiler) which is licensed with GNU LGPL v3, for parsing SWF files.
