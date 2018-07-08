# Beacon
Bluetooth Low Energy find and show results on map

![MainActivity](https://github.com/karpovichDima/Images/blob/master/listBeac.JPG)
![Map](https://github.com/karpovichDima/Images/blob/master/map.PNG)

Thanks to this application you will have an opportunity to understand more in detail the principles of Bluetooth, BluetootnLowEnergy, 
Firebase, Adapters, SWG, JSON, CustomView in the first implementation of the project also used Services, but decided that this is not 
rational, so I used usual AsyncTask for searching.

The application was originally conceived as a way of using Beacon technology, the user walks through the hypermarket's trading hall, 
moving from one Module to another, all his movements should be displayed on the smartphone, based on the signal strength of the 
radio module. But when working out I encountered a very big problem, Beacon is a very inaccurate technology, with the same distance 
between the radio module and the smartphone, the signal can change significantly, which greatly hinders the correct display of the 
user's location on the map.

Therefore, it is rather an educational application, it does not solve real problems to get acquainted with technologies.
