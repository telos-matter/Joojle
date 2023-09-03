# Joojle

A knockoff version of Hoogle but for Java.
<br>
**Heavily** inspired by <a href="https://www.twitch.tv/tsoding">Tsoding</a>, more
specifically from these <a href="https://youtube.com/playlist?list=PLpM-Dvs8t0VYhYLxY-i7OcvBbDsG4izam&si=aBJkIpS3pjflStvS">streams</a>, pretty much a translation
of what he did to Java, no new concept on my part

## How-to
### Usage:
Currently there is no user interface, planing on adding that later on, but atm it works like so:
```console
$ java joojle/Joojle 'path/to/the/jar/file.jar' 'query_of_the_signature_your_are_looking_for'
```
You could also additionally supply the maximum number of results to show as a 3rd argument, by default it is 50
### Query:
The query should be like so:
```
return_type(parameter_1,parameter_2,parameter_3<type_1,? extends type_2>,..)
```
Specify only the simple name of the class and not its full name, for example if you are looking for a
method that returns a String and takes no parameters you would write
```
String()
```
and not
```
java.lang.String()
```
Same thing applies to the &lt;types&gt;
<br>
As for arrays its:
```
int[]()
```
Constructors have a return type of the class and not void, for the void returning methods you simply write void
### Example:
To look for a method that returns a map like so Map&lt;String,T&gt; and takes a list of something that *supers* T and an array of Strings you would write:
```
Map<String,T>(List<? super T>,String[])
```

