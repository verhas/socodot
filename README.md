socodot
=======

Source Code Documentation Tool to preprocess documentation using Velocity

Source documentation should resice close to the source and should use the source itself as much possible. Ideally
there would not be need for any tool as the source code is the best documentation if created perfectly. Practice is however not perfect and many times there is need to create separate documentation.

You can do that in word, LibreOffice, Confluence or by many means, but in my opinion the best approach is to use plain text as the source for the documentation and process it to some nice format. For the markup my preference these days is markdown. The markdown itself however does not solve problems, like

* include fragment from the source code ex example
* eliminate copy/paste in documentation where the repetition of some text is desirable in the output document
* include text from the source specially formatted to ensure the up-to-date-ness of the documentation
* check consistency during the compilation of the documentation

Socodot provides a framework for these. This tool alone does not do any of the above but lets you use Velocity in your documentation and call configured plugins that can be provided as java POJOs or as scripts (JavaScript or any other script language, which is available and configured when socodot is executed).

The goal is to provide ready made plugins for the above purposes, while remaining totally open for further plugins as need arise.
