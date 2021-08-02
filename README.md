# JarDemo

###### Coding assignment for Jar:
```
You have to create a sample app with following:

A launcher screen with 2 buttons

On tap of Button 1, Screen 1 (Task 1):

Open a blank screen with two shapes, circle and square on bottom and an undo icon. If user taps circle, add a circle on the screen anywhere randomly. 
If user taps on the square, add a square anywhere on the screen randomly. Undo icon will remove the last added shape from the screen. 
Bonus: if you can avoid collisions of the shapes.

On tap of Button 2, Screen 2 (Task 2):

Open a screen and show a grid view of popular images from up splash using their public API. Bonus: if you use proper architecture, pagination and caching.

Reference https://unsplash.com/developers

Both of these tasks are designed to help us understand your knowledge of the framework. The first task is to understand your familiarity with UI layer and 
2nd task it to understand your familiarity with architecture, latest components, threading and api integration.

Note: You are free to use any library you want which you will use in the production code.

```

#### The app has been implemented based on above requirements using MVVM, databinding, Kotlin coroutines, Retrofit for api call, Glide for image loading and caching. Implemented bonus points also for both tasks.

App has following UI classes.

##### [MainActivity](https://github.com/dhavalrupapara14/JarDemo/blob/main/app/src/main/java/com/jar/demo/ui/activity/MainActivity.kt)
Launcher and host activity for fragments.

##### [HomeFragment](https://github.com/dhavalrupapara14/JarDemo/blob/main/app/src/main/java/com/jar/demo/ui/fragment/home/HomeFragment.kt)
Home screen containing 2 buttons to open task 1 and task 2.

##### [Task1Fragment](https://github.com/dhavalrupapara14/JarDemo/blob/main/app/src/main/java/com/jar/demo/ui/fragment/task1/Task1Fragment.kt)
Task 1 screen showing shape task

##### [Task2Fragment](https://github.com/dhavalrupapara14/JarDemo/blob/main/app/src/main/java/com/jar/demo/ui/fragment/task2/Task2Fragment.kt)
Task 2 screen showing images list in grid with pagination and caching. Popular images will be loaded from Unsplash public api. 
###### You need to add your own access key to run as API has a limit on number of API requests. Add it in NetworkModule class (check TODO).
###### Note: Ignore any other fragment present in the code as it was added for testing purpose.

#### Sample screenshots and video are in the [screenshots](https://github.com/dhavalrupapara14/JarDemo/tree/main/screenshots) folder.
