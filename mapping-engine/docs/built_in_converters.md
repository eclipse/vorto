# Built-in Mapping Functions

This gives an overview of what mapping functions that are currently supported. You can use these functions from within mapping xpath rules and mapping conditions. 

> Important Note: If you pass an array directly on root level, you can access its array elements by ```array[index]```

## Condition Funtions


### xpath:eval()

Extracts a value by evaluating an xpath expression with a given context object:
```xpath:eval(String xpathExpression, this)```

#### Example

Extracts the english greeting from a list of all greetings and checks if the value matches 'hello':
```xpath:eval("greetings[@lang = 'english']",this) == "hello"```

### vorto_base64:decodeString()

Decodes a base64 String to a byte array

```vorto_base64:decodeString(String value)```

### vorto_base64:decodeByteArray()

Decodes a base64 byte array to a byte array

```vorto_base64:decodeByteArray(byte[] value)```

### empty(expression)

Checks if the given expression is null or empty string

#### Example

```empty(person.name)```

### not empty(expression)

Checks if the given expression is not null or not empty

#### Example

```empty(person.name)```

### More functions

For more functions, please visit [Apache JEXL Reference](http://commons.apache.org/proper/commons-jexl/reference/syntax.html)

## Mapping Functions

### vorto_string:substring(String value, int start)

Gets a substring from the specified String avoiding exceptions.

### vorto_string:substring(String value, int start, int end)

Gets a substring from the specified String avoiding exceptions.

#### Example Usage

Data: ```{"voltage : "2323mV"}```

Example Mapping that extracts ```2323```:

```vorto_string:substring(voltage,0, string:length(voltage)-2)```

### vorto_string:trim(String value)

Removes control characters (char <= 32) from both ends of this String

### More String functions

For more string functions, visit [Complete API Documentation](https://commons.apache.org/proper/commons-lang/javadocs/api-3.6/org/apache/commons/lang3/StringUtils.html)

### vorto_conversion1:byteArrayToInt()

Converts an array of byte into an int using the default (little endian, Lsb0) byte and bit ordering:

```vorto_conversion1:byteArrayToInt(byte[] src, int srcPos, int dstInit, int dstPos, int nBytes)```

#### Example Usage

Example data: ```{"data : [0, 0, 0, -48, 7, 0]}```

Example Mapping:	
```vorto_conversion:byteArrayToInt(/data, 3, 0, 0, 3)```


### More Conversion functions

For more conversion functions, visit [Complete API Documentation](https://commons.apache.org/proper/commons-lang/javadocs/api-3.6/org/apache/commons/lang3/Conversion.html)

### vorto_number:toFloat()

Converts a string value to a float:

```number:toFloat(String value)```

### More number functions

For more number functions, visit [Complete API Documentation](https://commons.apache.org/proper/commons-lang/javadocs/api-3.6/org/apache/commons/lang3/math/NumberUtils.html)

### vorto_date:format()

Formats the given long value to a date string of form 'yyyy-MM-dd HH:mm:ssZ'

```vorto_date:format(long value)```

Formats the given long value to a date string with the given format:

```vorto_date:format(long value, String pattern)```


### vorto_type:convertDouble()

Converts the given double value to a string:

```type:convertDouble(double value)```

### vorto_type:convertInt()

Converts the given integer value to a string

```type:convertInt(int value)```

### vorto_boolean:toBoolean()

Converts the given string to a boolean:

```boolean:toBoolean(String str)```

### More Boolean functions

For more boolean functions, see [Complete API Documentation](https://commons.apache.org/proper/commons-lang/javadocs/api-3.6/org/apache/commons/lang3/BooleanUtils.html)

### vorto_conversion2:parseHexBinary()

Converts the hex string argument into an array of bytes:

```vorto_conversion2:parseHexBinary(String str)```

### More converter2 functions

For more converter2 functions, visit [Complete API Documentation](https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/DatatypeConverter.html)

### vorto_endian:swapShort()

Converts a "short" value between endian systems

```vorto_endian:swapShort(short value)```

### vorto_endian:swapFloat()

Converts a "float" value between endian systems

```vorto_endian:swapFloat(float value)```

### More endian functions

For more endian functions, visit [Complete API Documentation](https://commons.apache.org/proper/commons-io/javadocs/api-2.4/org/apache/commons/io/EndianUtils.html)
