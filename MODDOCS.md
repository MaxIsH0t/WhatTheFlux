## Crafting Generators ##
To craft a generator, you need a generator item, two gears of the same type, a dynamo, a capacitor, a cathode lead, and an anode lead. Arrange the items as such:

![](http://i.imgur.com/AdM4hNg.png)

The generator item determines the type of generator you're crafting:

Item             | Type
---------------- | ----
Heat Furnace     | Furnace
Heat Sink        | Geothermal
Wind Turbine     | Wind
Water Turbine    | Hydroelectric
Fission Reactor  | Nuclear
Parabolic Trough | Solar

The gear type determines the melting point and maximum dynamo velocity:

Gear     | Melting Point | Maximum Velocity
-------- | ------------- | ----------------
Tin      | 449           | 12
Invar    | 1427          | 34
Platinum | 3215          | 32
Enderium | 3422          | 60
Bronze   | 950           | 24
Copper   | 1085          | 20
Electrum | 1001          | 20
Gold     | 1063          | 20
Iron     | 1593          | 36
Lead     | 328           | 15
Lumium   | 512           | 15
Nickel   | 1453          | 32
Signalum | 1084          | 26
Silver   | 961           | 20
Uranium  | 1132          | 27
Zinc     | 420           | 20

The capacitor determines the RF buffer size and output rate:

Capacitor       | Buffer Size | Output Rate (RF/t)
--------------- | ----------- | ------------------
Ceramic         | 24000       | 128
Film            | 80000       | 256
Electrolytic    | 50000       | 384
x2 Ceramic      | 48000       | 256
x2 Film         | 160000      | 512
x2 Electrolytic | 100000      | 768
Ni-MH Cell      | 48000       | Infinite

The dynamo determines the power generation multiplier. More coils = more power per work.

## Using Generators ##
A generator generates electricity when its rotor is rotated. Higher angular velocity results in greater power. However, care must be taken not to exceed the generator's velocity cap; failure to do so will result in a big boom. Similarly, generators that produce heat must be kept below their melting points.

### Furnace Generators ###
Generates electricity by burning furnace fuel. Fuel with longer burn times yields quadratically more heat than fuel with shorter burn times. Heat is then converted to rotor velocity, which generates energy.

![](http://i.imgur.com/wOmsHRD.png)

### Geothermal Generators ###
Generates electricty using the heat of its surroundings. Covering the device with hot fluids (e.g. lava or pyrotheum) increases its internal heat significantly. To drive the rotor and generate electricity, the tank must be filled with water, which will be consumed as the device operates.

![](http://i.imgur.com/XnK0vvb.png)

### Wind Turbines ###
Generates electricity using the flow of air. The higher up this device is placed, the faster the rotor spins. Having solid blocks near the machine also inhibits its ability to do work. Functions better in a storm.

![](http://i.imgur.com/pZhCbs5.png)

### Water Turbines ###
Generates electricity using the flow of water. The left tank can be filled with water, or piped into from any face other than the bottom. Water will slowly be drawn from the left tank into the right tank, driving the rotor in the process. Water in the right tank will be deposited in block form beneath the machine if possible, but can also be piped out from the bottom face.

![](http://i.imgur.com/z0JOw19.png)

### Nuclear Reactors ###
Generates electricity using nuclear fission.

#### Injecting Fuel ####
To inject fuel into the reactor, a neutron howitzer must be placed in the top-left slot, and uranium ingots must be placed in the second slot from the right. The howitzer will lose some durability and the fuel will be stored in the reactor's fuel buffer.

#### Performing Nuclear Fission ####
To begin a reaction, a control rod must be placed in the slot below the neutron howitzer. When a reaction takes place, some fuel will be converted to waste and heat will be generated. The higher the reactor's temperature is, the more fuel will be consumed per reaction. Additionally, the control rod will lose some durability in the process. If the waste buffer becomes too full, irradiated barium will be output automatically into the waste slot.

#### Coolant ####
To cool the reactor, water can (and should) be inserted into the reactor's coolant tank through the bottom-left slots on the interface. If the reactor is heated, coolant will be consumed to lower the reactor's temperature, driving the rotor in the process. Additionally, the reactor can be covered in a cold fluid to further cool it, although this doesn't convert the heat to rotor velocity.

![](http://i.imgur.com/WIwKRwL.png)

### Solar Boilers ###
Generates electricity using sunlight. When light strikes the parabolic mirror in this device, the light is focused upon water in a tank, which is vapourized in order to drive the rotor.

![](http://i.imgur.com/i0qipwX.png)

## Sensor Blocks ##
To monitor the status of a generator, sensor blocks can be used. A sensor block measures a property of a generator and outputs a redstone signal if that property exceeds a set threshold.

![](http://i.imgur.com/oBoZXsr.png)

Three sensor blocks exist:

Sensor      | Property
----------- | --------
Thermometer | Temperature
Voltmeter   | Energy buffer
Dynamometer | Rotor velocity

![](http://i.imgur.com/lPeaeDB.jpg)

## Other Notes ##
### Redstone Control ###
Some generators support limited redstone control capability. When these generators receive a redstone signal, they will cease all function until the signal terminates.

* Furnace Generator
* Hydroelectric Generator
* Nuclear Reactor

### ComputerCraft Integration ###
All generators function as ComputerCraft peripherals if need be. To retrieve a list of available functions, `peripheral.getMethods(side)` can be used.