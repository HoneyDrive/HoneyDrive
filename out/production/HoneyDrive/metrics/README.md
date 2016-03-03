# Metrics
The *metrics* package will provide a way to get sensor data from the car.

## Prerequisites
You're going to need Maven with the `JSON.simple` dependency.

### Using IntelliJ
Press *File* and then *Project Structure*.
Click on *Libraries*, and then *New Project Library* (the small green plus symbol).
Choose *From Maven* and type
`com.googlecode.json-simple:json-simple:1.1`
into the textbox and click *OK*.

### General / Other IDEs
Download Maven. In the `pom.xml` file, add the `JSON.simple` dependency by adding
```
<dependencies>
	<dependency>
		<groupId>com.googlecode.json-simple</groupId>
		<artifactId>json-simple</artifactId>
		<version>1.1</version>
	</dependency>
</dependencies>
```
inside the `<project></project>` tag.


## Usage
There are currently two ways to read the car data.

1. You can choose to read a `json` file direcly from disk without a simulated delay between the received data.
2. Or you can choose to read a `json` file directly from disk with an added delay between the received data, corresponding to the delay between the `timestamp` attribute in the `json` file.

Always remember to add `import metrics.*;` to the top of your file.

### 1. No Delay

#### Full example code

##### Using a single filter

```java
import metrics.*;

public class Example
{
    public void start()
    {
        CarAction.addCreatedListener(this::newAction, CarActionsFilter.all);
        IDataReader reader = new ReadFromOpenXCFileReader("src/metrics/TestData/data1.json");
        reader.startReading();
    }

    public void newAction(CarAction action)
    {
        System.out.println(action.getName() + ": " + action.getValue());
    }
}
```

##### Using multiple filters

```java
import metrics.*;

public class Example
{
    public void start()
    {
        CarAction.addCreatedListener(this::newAction, EnumSet.of(CarActionsFilter.latitude,CarActionsFilter.longitude));
        IDataReader reader = new ReadFromOpenXCFile("src/metrics/TestData/data3.json");
        reader.startReading();

    }

    public void newAction(CarAction action)
    {
        System.out.println(action.getName() + ": " + action.getValue());
    }
}
```

#### Explanation

Add a method to handle the data, for example `public void newAction(CarAction action)`. This is the method that will receive the car data.

The supplied `CarAction` class will expose four properties through `getType()`, `getName()`, `getValue()` and `getTimestamp()`.
`getName()` is a String equivalent to enum `getType()`.

To get `CarAction` objects, you have to listen/subscribe to the types of data you want. Add yourself as a listener by
adding
```java
CarAction.addCreatedListener(this::newAction, CarActionsFilter.all);
```
somewhere in your code. The first parameter decides what method that will process the data. The second parameter decides what data you will get. You can choose between 24 different filters.

Remember to read the [The CarAction **value** property](#CarActionValue) below for an explanation about the different values.

`CarActionsFilter.all` will give you all data that is recorded.

`CarActionsFilter.other` will give you unrecognized data.

The 22 remaining categories are

<table border="0">
   <tbody>
       <tr>
           <td>steering_wheel_angle</td>
           <td>torque_at_transmission</td>
           <td>engine_speed</td>
       </tr>
       <tr>
           <td>vehicle_speed</td>
           <td>accelerator_pedal_position</td>
           <td>parking_brake_status</td>
       </tr>
       <tr>
           <td>brake_pedal_status</td>
           <td>transmission_gear_position</td>
           <td>gear_lever_position</td>
       </tr>
       <tr>
           <td>odometer</td>
           <td>ignition_status</td>
           <td>fuel_level</td>
       </tr>
       <tr>
           <td>fuel_consumed_since_restart</td>
           <td>door_status</td>
           <td>headlamp_status</td>
       </tr>
       <tr>
           <td>high_beam_status</td>
           <td>windshield_wiper_status</td>
           <td>latitude</td>
       </tr>
       <tr>
           <td>longitude</td>
           <td>button_state</td>
           <td>powertrain_torque</td>
       </tr>
       <tr>
            <td>fine_odometer_since_restart</td>
        </tr>
   </tbody>
</table>

Finally, to start reading the data, create a new instance of `ReadFromOpenXCFileReader` by adding
```java
IDataReader reader = new ReadFromOpenXCFileReader("src/metrics/data1.json");
```
to your code.

Start to read the data by running
```java
reader.startReading();
```

That's it! You will get all the information in the supplied `.json` file.

### 2. With Simulated Delay

#### Full example code

##### Using a single filter

```java
import metrics.*;

public class Example
{
    public void start()
    {
        IDataStreamer streamer = new DataStreamSimulator("src/metrics/TestData/data2.json", CarActionsFilter.all);
        streamer.addStreamListener(this::newAction);
        streamer.startStreaming();
    }

    public void newAction(CarAction action)
    {
        System.out.println(action.getName() + ": " + action.getValue());
    }
}
```

##### Using multiple filters

```java
import metrics.*;

public class Example
{
    public void start()
    {
        IDataStreamer streamer = new DataStreamSimulator("src/metrics/TestData/data3.json", EnumSet.of(CarActionsFilter.latitude,CarActionsFilter.longitude));
        streamer.addStreamListener(this::newAction);
        streamer.startStreaming();
    }

    public void newAction(CarAction action)
    {
        System.out.println(action.getName() + ": " + action.getValue());
    }
}
```

#### Explanation

Add a method to handle the data, for example `public void newAction(CarAction action)`. This is the method that will receive the car data.

The supplied `CarAction` class will expose four properties through `getType()`, `getName()`, `getValue()` and `getTimestamp()`.
`getName()` is a String equivalent to enum `getType()`.

To get `CarAction` objects, you have to listen/subscribe to the types of data you want. Add yourself as a listener by
adding
```java
IDataStreamer streamer = new DataStreamSimulator("src/metrics/TestData/data2.json", CarActionsFilter.all);
streamer.addStreamListener(this::newAction);
```
somewhere in your code. The second parameter of `DataStreamSimulator` decides what data you will get. You can choose between 24 different filters.

Remember to read the [The CarAction **value** property](#CarActionValue) below for an explanation about the different values.

`CarActionsFilter.all` will give you all data that is recorded.

`CarActionsFilter.other` will give you unrecognized data.

The 22 remaining categories are

<table border="0">
	<tbody>
		<tr>
			<td>steering_wheel_angle</td>
			<td>torque_at_transmission</td>
			<td>engine_speed</td>
		</tr>
		<tr>
			<td>vehicle_speed</td>
			<td>accelerator_pedal_position</td>
			<td>parking_brake_status</td>
		</tr>
		<tr>
			<td>brake_pedal_status</td>
			<td>transmission_gear_position</td>
			<td>gear_lever_position</td>
		</tr>
		<tr>
			<td>odometer</td>
			<td>ignition_status</td>
			<td>fuel_level</td>
		</tr>
		<tr>
			<td>fuel_consumed_since_restart</td>
			<td>door_status</td>
			<td>headlamp_status</td>
		</tr>
		<tr>
			<td>high_beam_status</td>
			<td>windshield_wiper_status</td>
			<td>latitude</td>
		</tr>
		<tr>
			<td>longitude</td>
			<td>button_state</td>
			<td>powertrain_torque</td>
		</tr>
		<tr>
            <td>fine_odometer_since_restart</td>
        </tr>
	</tbody>
</table>

Start to read the data by running
```java
streamer.startStreaming();
```

That's it! You will get all the information in the supplied `.json` file, with delays corresponding to the time difference given by `timestamp` in the `json` file.

### The CarAction **value** property<a name="CarActionValue"></a>

The `value` property in `CarAction` contains the value given by the car for the specific event. Since this value can be
multiple different types of data, it is set to be a Java `Object`.

To use the value, you need to cast it to the type you expect it to be.

Shown below is the information presented by the [OpenXC GitHub page](https://github.com/openxc/openxc-message-format):

#### steering_wheel_angle
numerical, -600 to +600 degrees
10Hz
#### torque_at_transmission
numerical, -500 to 1500 Nm
10Hz
#### engine_speed
numerical, 0 to 16382 RPM
10Hz
#### vehicle_speed
numerical, 0 to 655 km/h (this will be positive even if going in reverse as it's not a velocity, although you can use the gear status to figure out direction)
10Hz
#### accelerator_pedal_position
percentage
10Hz
#### parking_brake_status
boolean, (true == brake engaged)
1Hz, but sent immediately on change
#### brake_pedal_status
boolean (True == pedal pressed)
1Hz, but sent immediately on change
#### transmission_gear_position
states: first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, reverse, neutral
1Hz, but sent immediately on change
#### gear_lever_position
states: neutral, park, reverse, drive, sport, low, first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth
1Hz, but sent immediately on change
#### odometer
Numerical, km 0 to 16777214.000 km, with about .2m resolution
10Hz
#### ignition_status
states: off, accessory, run, start
1Hz, but sent immediately on change
#### fuel_level
percentage
2Hz
#### fuel_consumed_since_restart
numerical, 0 - 4294967295.0 L (this goes to 0 every time the vehicle restarts, like a trip meter)
10Hz
#### door_status
Value is State: driver, passenger, rear_left, rear_right.
Event is boolean: true == ajar
1Hz, but sent immediately on change
#### headlamp_status
boolean, true is on
1Hz, but sent immediately on change
#### high_beam_status
boolean, true is on
1Hz, but sent immediately on change
#### windshield_wiper_status
boolean, true is on
1Hz, but sent immediately on change
#### latitude
numerical, -89.0 to 89.0 degrees with standard GPS accuracy
1Hz
#### longitude
numerical, -179.0 to 179.0 degrees with standard GPS accuracy
1Hz

### Not presented by the OpenXC GitHub page
#### button_state
Seems to only occur when its value is "pressed".
#### powertrain_torque
Seems to contain a double value.
#### fine_odometer_since_restart
Seems to contain a double value.


## Code maintainance

![Class diagram](https://raw.githubusercontent.com/HoneyDrive/HoneyDrive/testing/DataReading/src/metrics/Images/classdiagram.png)

A class implementing `IDataReader` will read car data events from somewhere, and for each event it will create a `CarAction` object.
`ReadFromOpenXCFile` is an example of a class implementing `IDataReader`. It will read a `json` file from the disk.

Other classes can subscribe/listen to new `CarAction` objects being created. If you want to immediately read in all the
car data, you can listen to `CarAction`.

A class implementing `IDataStreamer` will somehow create a delayed `CarAction` object to match the car event timestamp property.

`DataStreamSimulator` is a class that implements `IDataStreamer`. It listens to
`CarAction`, and then relays the `CarAction` objects to match the car event timestamp property.
Be aware that the class spawns a new thread and operates in that thread for CPU intensive tasks.

If a class wants to listen to `CarAction`, the class must implement `IActionListener`, and register itself through `CarAction`s
`addCreatedListener` method.

If a class wants to listen to delayed `CarAction`s, the class must implement `IStreamListener`, and register itself through `IDataStreamer`s
`addStreamListener` method.

### CarAction

The `CarAction` class contains the data generated by the car. For each of the car events, a new `CarAction` is created.

Public methods:

Method | Description
-------|------------
addCreatedListener(IActionListener newListener, CarActionsFilter filter) | Adds a class that inherits `IActionListener` as a new listener. This class will then receive all car events that match the supplied `filter`.
getType() | Gets the type of car event represented in the current `CarAction`. This is given by the `CarActionFilter` enum. The same information, in text form, is given by the `getName()` method.
getName() | Gets the type of car event represented in the current `CarAction`, returned as a `String`.
getValue() | Gets the value of the car event represented in the current `CarAction`. This value may be many different types of data, and is therefore returned as an `Object`.
getTimestamp() | Gets the time of the car event represented in the current `CarAction`. The time is a value created by the car itself, and is set when the car itself created the data.

### CarActionFilter

The `CarActionFilter` enum contains all forms of data that the car can create. In addition, it contains two additional categories, `all` and `other`. `all` is used to receive all data, while `other'
is a category created in case a car should create unknown data.

### CarActionFilterTranslator

The `CarActionFilterTranslator` class contains code to translate a text string into a `CarActionFilter` enum value.

### IActionListener

The `IActionListener` interface is used when a class should be able to listen for new `CarAction`s. The class must then contain `implemets IActionListener`, and the
required `newCarAction(CarAction action)` method.

### IDataReader

The `IDataReader` interface is used when a class is supposed to provide a way to read car data. The class must then contain `implemets IDataReader`, and the
required `startReading()` method.

### ReadFromOpenXCFile

The `ReadFromOpenXCFile` class provides a way to read an *OpenXC* `json` file. It implements the `IDataReader` interface.

Its constructor takes the path to the *OpenXC* `json` file it is supposed to read. This path may be *absolute* or *relative*.

Public methods:

Method | Description
-------|------------
startReading() | Starts reading the *OpenXC* `json` file from disk. It will call the private method `createData(String line)` for every line of `json` code. This method is required because the class implements the `IDataReader` interface.

Private methods:

Method | Description
-------|------------
createData(String line) | Parses `json` code. In our provided files, every line in the files are individual `json` code. This method creates a new `CarAction` object. If no one is listening for that particular type of `CarAction`, Java's own *garbage collection* will remove the object from memory. If someone is listening for that type of `CarAction`, the `CarAction` object will present itself to those listeners.

### IDataStreamer

The `IDataStreamer` interface is used when a class is supposed to provide a way to stream car data with delays between the data corresponding to the difference give by the `timestamp` property
in the `json` files.

The class must then contain `implemets IDataStreamer`, and the required `addStreamListener(IStreamListener streamListener)` and `startStreaming()` methods.

### IStreamListener

The `IStreamListener` interface is used when a class should be able to listen for new `CarAction`s from a time delayed stream. The class must then contain `implemets IStreamListener`, and the
required `onNewAction(CarAction action)` method.

### DataStreamSimulator

The `DataStreamSimulator` class provides a way to stream car data with delays between the data corresponding to the difference give by the `timestamp` property in the `json` files.

The class spawns a new thread so that it can work in parallel with the rest of the program.

Its four constructors first takes the path to the *OpenXC* `json` file it is supposed to read. This path may be *absolute* or *relative*.

The second argument decides what kind of car data it is supposed to listen to. This is a `CarActionsFilter` enum value.

Public methods:

Method | Description
-------|------------
newCarAction(CarAction action) | A method that receives `CarAction` data. This is required because the class implements the `IActionListener` interface.
startStreaming() | Starts to read the *OpenXC* `json` data from disk, and transmit it to its `IStreamListener` listeners. This method spawns a new thread.
addStreamListener(IStreamListener streamListener) | Adds a class that inherits `IStreamListener` as a new listener. This class will then receive all car events that match the `filter` supplied in the constructor.

Private methods:

Method | Description
-------|------------
notifyListeners(CarAction action) | Transmits the new `CarAction` object to its listeners.
startStreamingLogic() | Contains the functionality to delay the CarAction objects. If not run in its own thread, it will probably lock the application.
