# Indra Plugins

Indra implements a plugin-based extensibility mechanism built on the top of the *Java Service API*
which allows including new *compositional methods*, *score functions*, *threshold functions* and *filters* without
recompiling Indra's code.

#### Implementation

Implementing new functions involts the extension of two classes. The target function type
([VectorComposer](src/main/java/org/lambda3/indra/entity/composition/VectorComposer),
[ScoreFunction](src/main/java/org/lambda3/indra/entity/relatedness/ScoreFunction),
[Threshold](src/main/java/org/lambda3/indra/entity/composition/VectorComposer)) or
[Filter](src/main/java/org/lambda3/indra/entity/filter/Filter)) and its respective factory,
which should implement the [IndraFactory](src/main/java/org/lambda3/indra/factory/IndraFactory) interface.

Following the specification of the *Java Service API*, the factory should be registered by
putting the configuration file in the META-INF directory ([check the official Java totural for more information](https://docs.oracle.com/javase/tutorial/ext/basics/spi.html)).
To avoid this manual work, the configuration file can be generated automatically by adding the following annotation
in the new implmemented factory class:

```java
@AutoService(IndraFactory.class)
public class MyNewFactory implements IndraFactory {
//implementation goes here
}
```


#### Installation

To install the new extension, it is required to pack the new functions' implementations in a JAR file and place it in
the Indra's classpath.

#### Usage


Natively, Indra offers one or more implementation of each function, which can be accessed
by the **buildin** factory.








