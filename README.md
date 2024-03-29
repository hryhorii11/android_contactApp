# Contact app

## Description
Contact App is a simple and user‑friendly mobile application that lets users create their profiles and manage their contacts with ease. It offers features like registration, login, auto‑login, profile creation and editing, contact list management, adding and deleting contacts, multiselect contact deletion, viewing user profiles, user directory, search.

## Tech stack
Android XML Views, Retrofit2, Hilt, Coroutines, Flow, Room

## Architecture
The App consists of two activities (Auth and Main) with fragments in them. Each activity uses its own nav graph to navigate between fragments.

## Authentication
The user has the option to register or log in to the account, if he is already registered, autologin is also implemented

<img src=https://i.imgur.com/wW015F2.gif width="200" height="400">



## User profile and his contacts
After authentication user can see his profile and list of contacts

<img src=https://imgur.com/4zJEMTr.gif width="200" height="400">


## Deleting contacts
the user can delete contacts by clicking on the button or by swiping,also he can return deleted contact within 5 seconds

<img src=https://imgur.com/tBrlvN0.gif width="200" height="400">

## Multiselect
 Also, the user can select several contacts and simultaneously delete them using multiselect

<img src=https://imgur.com/OSo8RYW.gif width="200" height="400">


## Contact profile
By clicking on the contact, the user can go to the profile of this person

<img src=https://imgur.com/AI66ekM.gif width="200" height="400">

## Adding contacts
To add contacts, the user must go to the list of all contacts and select the ones he needs

<img src=https://imgur.com/vLoxjIk.gif width="200" height="400">

## Searching contacts

<img src=https://imgur.com/mwLMlLV.gif width="200" height="400">
