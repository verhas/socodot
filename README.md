socodot
=======

Source Code Documentation Tool to preprocess documentation using Velocity.

Socodot suggest that you document your program along the source code using some markup. The documentation should rely on the source code being Java, C, whatever, JSON or any other resource file that play role in the application during execution, build, testing or just any other phase of the build process. The main goal is

> You should NOT copy any code fragment, statement, value, sentence to the documentation, which is available in the source code.

The source code may change and the documentation should automatically update when assembled.

Socodot says that the assembly of the documentation should be two separate steps:

1. Gathering the information for the documentation from the documentation source code and the program source code.
2. Format the gathered documentation from the markup of your choice to some prety printed format.

Socodot is a framework to perform the first step. This first step can perform tasks like:

* include fragment from the source code as example
* help to eliminate copy paste text when the final documentation should, for didactical reasons, contain the same text many times
* import some part of the documentation from source code comments to help keeping the documentation as close to the source code as possible
* check the consistency of the documentation to ensure QA process

Socodot employs Velocity. It runs all your documentation source code through the velocity engine as a very basic feature. Velocity alone however is not the best tool to perform the actions listed above. It can act as a framework, a macro format that lets the documentation to reference (using the well known `$object...` notation) objects available in the velocity engine context. Many tools that employ Velocity are limited by the available objects in the context. Socodot is different in this very detail.

Socodot gives you the total freedom to have just any Java object or any script referenced in your documentation. As an example: to include a part of a file from surce code is not easy using Velocity macros. On the other hand this is just a breeze employing groovy (or JavaScript, or Jython, or Java or whatever you want). To do that you can write a script that does that the way your documentation needs fit and then you can configure the document transformation in `socodot.config` to include the script into the context of the Velocity engine for the documentation source processing.

The beauty of Socodot is that the file `socodot.config` is nothing else than a text file processed using Velocity. When this file is processed you have Velocity objects, like `$register` that you can use to register a singleton, or script for the context of the documentation processing. For example the `socodot.config`

```
$directory.source("src/wiki")
$directory.target("target/wiki")
$directory.exclude("macro")
$file.exclude("*.js")
$register.singleton("com.javax0.socodot.SocodotTest$LoggerPlugin").as("log")
$register.script("logging.js").as("jslog")
$register.script("resourcelog.js").as("rslog")
```

declares that the documentation files are in the source directory `src/wiki`, the compiled documentation is to be put into `target/wiki` and the directory `src/wiki/macro` contains macro files that are not to be directly processed. The files `*.js` are also to be exluded from the documentation since they contain JavaScript and not documentation. The last three lines register a singleton and two scripts with the names `log`, `jslog` and `rslog` so the documentation files may refer to them as `$log`, `$jslog` and `$rslog`.


For more information read the documentation of the project.


