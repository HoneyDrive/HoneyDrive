# Metrics
The *metrics* package will provide a way to get sensor data from the car.

## Prerequisites
You're going to need Maven with the Google JSON dependency.

### Using IntelliJ
Press *File* and then *Project Structure*.
Click on *Libraries*, and then *New Project Library* (the small green plus symbol).
Choose *From Maven* and type
`com.googlecode.json-simple:json-simple:1.1`
into the textbox and click *OK*.

### General / Other IDEs
Download Maven. In the `pom.xml` file, add the `json-simple` dependency by adding
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

1. You can choose to read a `json` file direcly from disk without a smimulated delay between the received data.
2. Or you can choose to read a `json` file directly from disk with an added delay between the received data, corresponding to the delay between the `timestamp` attribute in the `json` file.

Always remember to add `import metrics.*;` to the top of your file.

### 1. No Delay

#### Full example code
```java
import metrics.*;

public class Example implements IActionListener
{
    public void start()
    {
        CarAction.addCreatedListener(this, CarActionsFilter.all);
        ProcessCarData proc = new ProcessCarData(new ReadFromOpenXCFileReader("src/metrics/data1.json"));
    }

    public void newCarAction(CarAction action)
    {
        System.out.println(action);
    }
}
```

#### Explanation

Add `implements IActionListener` to your class. Add the required `public void newCarAction(CarAction action)` method. This is the method that will receive the car data.

The supplied `CarAction` class will expose four properties through `getType()`, `getName()`, `getValue()` and `getTimestamp()`.
`getName()` is a String equivalent to enum `getType()`.

To get `CarAction` objects, you have to listen/subscribe to the types of data you want. Add yourself as a listener by
adding
```CarAction.addCreatedListener(this, CarActionsFilter.all);```
somewhere in your code. The second parameter decides which data you will get. You can choose between 21 different filters.

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

Finally, to start reading in the data, create a new instance of `ProcessCarData` by adding
```java
ProcessCarData proc = new ProcessCarData(new ReadFromOpenXCFileReader("src/metrics/data1.json"));
```
to your code.


### 2. With Simulated Delay

#### Full example code

TODO

#### Explanation

TODO

## Code maintainance

TODO: Lag forenklet klassediagram. Forklar de forskjellige delene