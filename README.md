#CrawlerZwei (CZwei)

![neko](http://cdn-ak.f.st-hatena.com/images/fotolife/t/thk/20130915/20130915193049.jpg)


## Overview


**CZwei** Crawler for www.2ch.net 

*The Base Framework 2ch Crawler System*

![sysmap](http://cdn-ak.f.st-hatena.com/images/fotolife/t/thk/20130916/20130916054626.png)


##Use Software Library

- Apache Commons Software

- Java Mail

- Mysql and Mysql JDBC driver


##Environment
JVM support OS (Windows/Mac OSX/Linux)


##Setting Software
 1. Install Mysql Server
 2. Start Mysql Server
 3. Write czwei setting.xml


##Boot Command:

Standard read setting.xml

	java -jar czwei.jar

JVM decide memory

	java -Xmx96m -jar czwei.jar

Decide setting.xml file

	java -Xmx96m -jar czwei.jar -F settingNplus.xml
	
View GC (Use Java Option)
	
	java -verbose:gc -jar czwei.jar -F setting_vip.xml

##Compile
###Use Eclipse
Main Class = jp.co.omega11.webcrawler.w2fj.MainBoot

###Use Ant
	ant build.xml

##Licence
*GPL v2*

<http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>


## Author
*touyou hacker (thk)*


##Contact
**mail** : <itimonzi@gmail.com>

**twitter** : *@touyou_hk* <https://twitter.com/touyou_hk>

##Site
Blog : <http://d.hatena.ne.jp/thk/>


##History
- Ver 1.0 2013/09/XX
 
 
##Powored Java
![duke](http://cdn-ak.f.st-hatena.com/images/fotolife/t/thk/20130916/20130916012105.gif)
