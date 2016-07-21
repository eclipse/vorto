## Improvements to the Vorto DSL

### Description on operation Parameters:

Now you can add descriptions for each operation parameters. Example:

```
operations { 
    drive(leftWheelSpeed as float "Speed of left wheel", rightWheelSpeed as float "Speed of right wheel") returns boolean 
} 
```

### Constraint on operation parameters and return type:

Previously, you can only specify constraints on properties. Now you can also specify them on operation parameters and return types. Example:

```
operations {
    move(leftWheelSpeed as float <MIN 0, MAX 512>, rightWheelSpeed as float <MIN 0, MAX 512>) "Moves the wheels"
    currentMotorHeat() returns float <MIN -200, MAX 200> "Returns the current motor heawt"
}
```

### Function block can now inherit from another function block
Previously, you can only specify inheritance relationship on Datatypes. Now you can also specify it in Function blocks.
```
functionblock DifferentDriveWithSkidWheel extends com.mycompany.fb.DifferentialDrive {
```

### Measurement Unit, Readable, Writeable, Eventable

You can specify a measurement unit for any properties that you add. You can also specify if they are readable, writeable
or eventable.

Example:

```
status {
    mandatory motorHeat as float with { measurementUnit : Temp.Celsius, readable : true, writable : true, eventable : false }
}
```

### Breakable:

You can also specify if an operation in your function block can throw an error by specifying it as
breakable, like so:

```
operations {
	move(leftWheelSpeed as float,rightWheelSpeed as float)
	breakable forward()
	backward()
	breakable turn(angle as float)
}
```

### Dictionary Type:

One of the constant request is to add a Dictionary type to Vorto. We have finally added it.

```
using com.mycompany.type.Color ; 1.0.0

entity ColorLookupTable {
	mandatory redLookupTable as Dictionary [ string , int ]
	mandatory greenLookupTable as Dictionary [ string , int ]
	mandatory blueLookupTable as Dictionary [ string , int ]
	mandatory hueLookupTable as Dictionary
	mandatory property1 as Dictionary [ string , Color ]
	mandatory property2 as Dictionary [ string , Dictionary ]
	mandatory property3 as Dictionary [ string , Dictionary [ int, Color ] ]
}
```
