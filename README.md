# ForumsApp
An android application that allows users to create and view threads in forum style format.

## Description

The app utilizes a database to store information about threads and user accounts, allowing for easy retrieval and management of data. User authentication is implemented to ensure that only authorized users can create and interact with threads. Users can also edit and delete their own threads.

The app also includes a search function, which allows users to quickly find and read threads that match specific keywords or phrases. Additionally, users can filter threads by categories, and pagination is used to display a set number of threads per page.

The app also makes use of Push notifications to alert users of new replies in the thread they are following. The app also uses Firebase Cloud Messaging (FCM) to handle the notifications.

The source code is written in Java and makes use of several popular libraries such as Retrofit, Room, Glide and Firebase. The architecture of the app follows MVP pattern.
