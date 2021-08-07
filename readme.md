### Introduction

This work is based on other project, so initial (first commit) and final implementation are
completely different and can be examined separately.

### Hint for testers

1. Be sure that project is running at least on Android Studio Arctic Fox RC1
2. In order to avoid GitHub API restriction for non-authorized requests, please add your GitHub API
   token into `:feature-repository:build.gradle` file

```groovy
def githubToken = localProperties.getProperty('github_token') ?: "<token goes here>"
```

### Requirements

###### Task 1 - Connect to the Github API

Connect to the Github API to retrieve the list of public repositories in <user> Github Account:
<https://api.github.com/users/<user>/repos>
This results in a list of public Repositories. Visualize the results in a list. You are free to
choose any meaningful subset of data to show in each row.

###### Task 2 - Create a detail page for the repository

Upon clicking an item on the repository list, redirect the user to a detail page. Retrieve
information about all the commits in the selected repository. Then render any relevant data into a
simple detail page. This can be done with the following call:
<https://api.github.com/repos/<user>/<repository>/commits>
Feel free to choose any meaningful subset of data to display.

###### Task 3 - Create a custom view to display commits in a month

On the detail page, a custom view with the following requirements should be shown.

- At the base of the view should be a text denoting the month.
- It should contain a rectangle, akin to the bar in a bar chart. The height of the bar should be in
  proportion to the number of commits in the given month vs the maximum commits in a month for that
  repository.
- Another text, aligned with the top of the bar should show the number of commits in that month. It
  should look similar to this. Feel free to choose your own color scheme and make your own design
  choices.

###### Task 4 - Cycle through all the months and trigger updates on the custom view.

While the user is on the detail page, the custom view needs to update continuously and should be
cycling through all the months. Create a mechanism that can update the custom view at an interval of
1.5 seconds. At each new interval, the height of the bar, the month name text and the number of
commits text should be updated. The user should be free to navigate back to the repository list and
open up a different repository’s detail page.

###### Bonus

Animate the height changes in the bar with each new update.

### Implementation description

###### Used libraries

* Koin
* SqlDelight + coroutine & flows support
* Retrofit2 + coroutine support / OkHttp / Moshi
* Jetpack libraries (viewModel, compose, navigation-compose)

###### Used solutions

* MVVM
    * view models inherit BaseViewModel which provides:
        - enhanced coroutine scope with SupervisorJob (scope is not cancelled if exception happens)
          and exceptions handler. Error handling can be changed by overriding
          BaseViewModel.handleError()
        - access to ActivityDelegate which is basically a relay for message, navigation, loading
          status events. Those are triggered in view models and consumed in Activity
        - navigation Screen events can take route arguments as well as parcelable arguments
        - view models request data and handles results in its coroutine scope
        - if exception occurs it's is gently handled and message is shown to the user
* application is modularized feature-wise
    * each feature module has domain/data/ui packages
    * common module for reusable code
    * dependency management done with the help
      of [gradle platform module](https://docs.gradle.org/current/userguide/java_platform_plugin.html)
* dependency injection with Koin which is created with Kotlin in mind
* threading done with coroutines
* persistence implemented with SqlDelight (just to give it a try)
* reactive ui built with Compose and Kotlin flows

###### Trade-offs

* repositories owner is hardcoded - JakeWharton
* due to simple business an ui logic data flow layers reduced
    * from: network -> data source -> repository -> use case -> view model -> ui
    * to: network -> repository -> view model -> ui
* MVI suits better for Compose

###### Left out

* proper pagination
