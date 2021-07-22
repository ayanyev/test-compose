### Requirements

The Project needs to compile and be executable on a state of the art Android Phone. Use SDK Level 21
and above.
Try and keep the project as vanilla as possible. However, the following 3rd party libraries are allowed:
Retrofit / Volley, Rx, Jetpack libraries, Dagger 2 / Hilt.
Please add a readme with the assignment to explain your development approach and/or things that you
would do differently.

###### Task 1 - Connect to the Github API

Connect to the Github API to retrieve the list of public repositories in your Github Account. Alternatively,
use this account: 'https://api.github.com/users/<user>/repos'
This results in a list of public Repositories. Visualize the results in a list. You are free to choose any
meaningful subset of data to show in each row.

###### Task 2 - Create a detail page for the repository
Upon clicking an item on the repository list, redirect the user to a detail page.
Retrieve information about all the commits in the selected repository. Then render any relevant data into
a simple detail page. This can be done with the following call:
'https://api.github.com/repos/<user>/<repository>/commits'
Feel free to choose any meaningful subset of data to display.

###### Task 3 - Create a custom view to display commits in a month

On the detail page, a custom view with the following requirements should be shown.
- At the base of the view should be a text denoting the month.
- It should contain a rectangle, akin to the bar in a bar chart. The height of the bar should be in
proportion to the number of commits in the given month vs the maximum commits in a month
for that repository.
- Another text, aligned with the top of the bar should show the number of commits in that month.
It should look similar to this. Feel free to choose your own color scheme and make your own design
choices.

###### Task 4 - Cycle through all the months and trigger updates on the custom view.

While the user is on the detail page, the custom view needs to update continuously and should be cycling
through all the months.
Create a mechanism that can update the custom view at an interval of 1.5 seconds.
At each new interval, the height of the bar, the month name text and the number of commits text should
be updated.
The user should be free to navigate back to the repository list and open up a different repository’s detail page.

###### Bonus

Animate the height changes in the bar with each new update.

#### Implementation description

This work is based on other project, so initial and final implementation
(first and last commits) are completely different and can be examined separately

###### Used libraries
* RxJava 3 + RxAndroid + RxKotlin
* coroutines
* Retrofit 2 + OkHttp + Moshi
* Hilt / Koin
* Jetpack libraries (viewModel, fragment-ktx, constraintLayout, compose)
###### Used solutions
* clean architecture (domain/data/ui separation)
* MVVM
* dependency injection (Hilt -> Koin)
* threading (Rx -> coroutines)
* reactive (Rx -> flows)
* ui (databinding -> Compose)
* custom chart view and list creation was done with the help of databinding
###### Not done
* proper exception handling
* proper pagination
* proper dependency management
* MVI
* testing
###### Hint for testers
1. Be sure that project is running at least on Android Studio Arctic Fox RC1
2. In order to avoid GitHub API restriction for non-authorized requests, please
add your GitHub API token into :app build.gradle file
```groovy
buildConfigField("String", "TOKEN", "\"<token goes here>\"")
```