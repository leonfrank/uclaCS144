This example contains a simple utility class to simplify opening database
connections in Java applications, such as the one you will write to build
your Lucene index. 

To build and run the sample code, use the "run" ant target inside
the directory with build.xml by typing "ant run".

for lucene index, we choose (StringField) Itemid, Name , and (TextField)content( a combination of itemid, name ,category and Description ) as our lucene index.