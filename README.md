# calvinsCreations
## By Calvin Krist

This is a reposity to contain many of the smaller personal projects of mine, especially the miscellaneous ones I created before coming to UVA. These projects are often experimental, poorly designed and documented, or just unfinished. Many of these projects require Java 8 or higher due to the use of lamba expressions.

### DwarfGame
DwarfGame is a small platformer built using gradle and LibGDX. The graphics and sounds are all creative commons. The game currently only has a launcher for Windows (called DestopLauncher).

#### Known Glitches:
* The hitboxes of the final boss and his weapon do not align properly with the images.
* Occasionaly a mouse click is lost. 

### Pente
Pente is a Java recreation of the board game. It should be noted: the game uses the Impact font. I do not know if the text will show properly if the user's computer does not have this font installed.

#### Known Glitches:
* Player 2's captured pieces do not show up in the bottom right of the screen.
* After Player 2 wins, Player 1 can still place one more piece.

### Blackjack
This project implenents a multiplayer specific version of Blackjack. Using the Java Socker library, the server acts as the dealer and users, running a client version of the game, join as players. 
Furthermore, each player has an account stored on the server which, in the future, will store their money for betting. Due to this, a login is required to play.

#### Known Glitches:
* When the players and the server are connecting through different WiFi networks, the server's port must be forwarded. This is difficult to achieve at universities.
