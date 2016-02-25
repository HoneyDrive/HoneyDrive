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

```java
import metrics.*;

public class Example
{
    public void start()
    {
        CarAction.addCreatedListener(this::newAction, CarActionsFilter.all);
        IDataReader reader = new ReadFromOpenXCFileReader("src/metrics/data1.json");
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
somewhere in your code. The first parameter decides what method that will process the data. The second parameter decides what data you will get. You can choose between 21 different filters.


`CarActionsFilter.all` will give you all data that is recorded.

`CarActionsFilter.other` will give you unrecognized data.

The 19 remaining categories are

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

```java
import metrics.*;

public class Example
{
    public void start()
    {
        IDataStreamer streamer = new DataStreamSimulator("src/metrics/data2.json", CarActionsFilter.all);
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
IDataStreamer streamer = new DataStreamSimulator("src/metrics/data2.json", CarActionsFilter.all);
streamer.addStreamListener(this::newAction);
```
somewhere in your code. The second parameter of `DataStreamSimulator` decides what data you will get. You can choose between 21 different filters.

See the explanation for "*1. No Delay*" above to read about the different filters available.

Start to read the data by running
```java
streamer.startStreaming();
```

That's it! You will get all the information in the supplied `.json` file, with delays corresponding to the time difference given by `timestamp` in the `json` file.

## Code maintainance

TODO: Lag forenklet klassediagram. Forklar de forskjellige delene