
---

__USED DESIGN PATTERNS__

1. Singleton Pattern -> fused for the Admin class
2. Observer Pattern -> used in user package by implementing a Subeject interface
which is implemented by Artist class. Also, we add an Observer class,
implemented by the User class, so that we can implement the notification part of the
application. (Best for keeping an evidence for subscribers and send them notifications when needed, for example, when we add a new album or a new event for an artist)
3. Command Pattern -> used in pages package by implementing an interface, Command, which is implemented by BackCommand, ForwardCommand and ChangePage classes, that are used for those commands in the app. Also, we created a new class, Navigation History, so that we keep an evidence of the history navigation (the pages that an user have been on before). (Best for keeping a history for the page navigation, so that we can "undo" the previous page, as much as we want)

Continuing developing an app similar to spotify, I created new classes and methods to implements new functionalities, such as subscribe, getNotifications, or wrapped. Those methods are just added to the existing classes, only a few new ones being added. I will talk about where those methods could be found and how they work.

- __aboout wrapped__: from the very beginning of the program, we have to calculate for each user some statistics based on what they have listened to during the whole program. In the __simulatePlayer__ method from Player, after setting the next audio file, we update the wrapp for the corresponding user. 
In admin, we defined a method, called updateWrappedIndividually, which update the wrapped for that user and the corresponding content creator. After adding the current audio file into the user's wrapped, I keep those hash maps sorted, thus making easier to extract the information I will need later. That method will be called also in the load. 
When we firstly add a statistic into a user's profile, we set a hasStatistics flag to true, so that I can handle error cases easily. 

- __endProgram__: when we run the app, this will be the last command to be added to the output. This will show a statistics about the artists on the platform, like their total revenue from songs, from merch, the most profitable song, and a ranking, based on their total income. At the end of the program, the admin is the one to give this statistic and give the money to the artists based on user's type of subscription. Firstly, we iterate through the users list an check which users are premium. For those, we give the money to the artists based on a premium history for each user, and update each artist's statistics (total revenue, from songs and merch, and set the most profitable song). After gaving each artist the money, we recalculate the ranking, and after that, we add the results in the json file.

- __buyPremium__ and __cancelPremium__: those methods are for a normal user to buy/cancel a premium subscription. Thus, we they listen to amn artist's song, they will "pay" this artist at the end of the program or when they cancel this subscription. For calculating the premiumHistory, we added this feature into updateWrappedIndividually, we check if the user is a premium one, or not. For both cases, we count those songs.
After giving money to an artist after an user cancel it's premium subscription, we have to clear the lists, in case they want to rebuy it.

- __adBreak__: this method will insert an ad after the current audio file will be done. For making this, we set a shouldInsertAd flag to true. For each user that gets this command, we will have the price for that ad. When the current audio file is done, we check if the corresponding flag is set to true. If so, we make a copy of the current PlayerSource and insert the ad into the player (the ad is a predefined song in the library), and set a comeBack flag. Thus, I know that after the ad is done, I have to come back to the old source and continue. When I insert the ad in the player, we give the money to the artists based on what that user listened. 

- __buyMerch__: we check if the user is on an artist page, if the merch we want to buy exists, and if so, we add the merch to the user's merch list, and give the artist the money for that article.

- __seeMerch__: show the merch a user bought

- __subscribe__: an user can subscribe to an artist, thus, when an artist add a new event/album/merch, the subscribers will be notified. For this command, the user will be added or removed (if the user is already subscribed to that artist). 

- __getNotifications__: will show for the corresponding user all the notification he received during the program (after the moment he subscribed to an artist). After showing the notifications, I will clear the notifications list (like it happens to our phones, after we check new notifications, they disappear).

- __previousPage__: we go to the previous page of the current user, if it exists. 

- __nextPage__: we go to the next page of the current user, if it exists (the navigation history will be cleaned after we have a changePage command (we will delete everything that was after the current page)). Also, for those two commands, the changePage command add new options (artist and host).

- __updateRecommendations__: for this command, we check what kind of recommendations the user want to get. Firstly, we check if there are new recommendations, if not, we give an error message. If there are new recommendations, we calculate those recommendations and then we add those to the current user's page.

- __loadRecommendations__: for this command, we load the last recommendation in the user's player. (we keep an evidence of the last type in each user, by setting a string value `type` to what we need).

## Moreover ##

- this project could have been improved by organizing it better (for example, some methods in admin and user are a bit complicated, with a complex logic, but I didn't change them, because this was the only way they worked :D ). 

- the last design pattern could have been builder for wrapped (but this was the only way (for me) to make this part of the application working)

- some code is still duplicated (especially in **updateWrappedIndividually**), because the way I update the playlist, album and library are more or less, the same. I wanted to keep it like this because it is more clear that for each kind of audio we have to make the update, and if we want to modify the structure later, thus would be way easier.
