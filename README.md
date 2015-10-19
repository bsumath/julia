Julia Fractal Drawing Program
=============================
This program is a specialized tool for the study of various dynamical systems. It was developed by Rich Stankewitz of the Ball State University (BSU) Mathematical Sciences department.

Julia is designed to draw the following types of sets in the complex plane:

1. Julia sets of a function.
2. Julia sets of rational semigroups.
3. Attractor sets of an Iterated Function System (IFS).
4. Iterates of a seed value under any of the allowable map types.
5. Postcritical sets (of finite order) of any function or semigroup of the allowable map types.
6. Forward and Inverse images (using any of the allowable map types) of the above sets or any sets imported into Julia. 


Requirements
------------
Julia will run on systems with the following:

* Java Runtime Environment version 6 or greater (JRE 8 recommended)
* An OpenGL graphics card.
* Windows, Mac OS X, Linux, or Solaris operating systems
* To draw large sets, at least 2gb of RAM is needed.


Installation and Use
--------------------
Julia can be launched as a Java Web Start Application:

* [Julia - v2.2.0 (stable)](https://bsumath.github.io/jnlp/julia.jnlp)

### Help
Help files will be brought here soon, but in the meantime please see the help files
located at http://rstankewitz.iweb.bsu.edu/JuliaHelp2.0/Julia.html

Development
-----------
Development requirements:

* Java SE Development Kit 8 or higher
* Groovy 2.4.4 or higher
* Gradle 2.6 or higher
* IntelliJ IDEA (recommended IDE)

### Build
To build Julia run:

    gradle build
  
### Release
To release a new version of Julia, in addition to building the project you need to sign the jar files with a certificate. Rich Stankewitz keeps the BSU Math department's signing key. 

Create a file in this repo named `keystore.properties` with the following:

    storepass=the key store password goes here
    storetype=pkcs12
    keystore=D:\\path\\to\\the\\cert.p12
    alias=the correct certificate alias
    tsaurl=http://the.real.timestamp.authority.example.com
    
Rich Stankewitz has the correct values for the `keystore.properties` file. Once that file is created you can sign the jars and generate the jnlp content with:

    gradle createWebstart

Then the `build/jnlp` directory will contain

* `julia.jnlp` - the file to launch Julia via Java Web Start
* `lib/*.jar` -  the signed jar files for Julia and its dependencies

These files should then be committed on the [bsumath/julia@gh-pages](https://github.com/bsumath/julia/tree/gh-pages) branch.

Contributors
------------
Initial development of Julia was done by Team Motivity as a Software Engineering project for the BSU Computer Science department. Team Motivity was:

* Rich Stankewitz
* Wendy Conatser
* Trey Butz
* Yun Li
* Kristopher Hart

Further development of Julia has been done by:

* Ben Dean
* Wendy Conatser
* Sida Qiu


License
-------
Copyright © 2007-2015 Ball State University

Julia is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Julia is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Julia.  If not, see <http://www.gnu.org/licenses/>.