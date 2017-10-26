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


### Fight Bots
This project was my dive into genetic programming after reading "A Field Guide to Genetic Programming". It simulates bots (cirles) that fight each other (shoot smaller circles at eachother). The bots are supposed to learn over time to fight each other better, but this doesn't really work. Even when left for 700+ generations, the bots just seem to behave randomly.
This is likely do to a poorly designed fitness function, but it may also be due to the fact that there is no standard to which the bots are held: each generation, the best is chosen from the population and everyone fights against it.
Perhaps if I were to program an AI by hand and use that, the project would go better. It is also possible that the necesarry operators to enable bots to progessively improve are not included in the AI construction process.

The AI are represented by binary trees, which themselves are represented by Strings so crossover and mutation can be easily applied.

##### Perhaps in the future, this process could be improved by:
* Fixing the issues with genetic programming: ensure that the bots actually improve over the generations
* Simulating the bots in a distributed manner. A server could assign each computer a set of AI to evaluate the fitness for in order to make the process faster (also I love network programming and distributed systems).
* Instead of simulating bots, I could test my knowledge of genetic programming in a system where the programming is simpler and there are less options the bot can make: a Tic-Tac-Toe game. My concepts can be tested here, and the system could even be made distributable. After I know what works, I could go back to the bot idea.

### Encryption 2.0
This is a GUI that allows users to interact with the Java Base64 encoding system. It used custom programmed buttons as well as a small logo at the top that I am (stupidly) proud of, which is strange considering how easy it was to program. I had wanted to make this GUI a cover for my own encryption mechanisms, but eventually decided against that in favor of using already existing libraries.

#### Known Glitches:
* The Base64 encryption messes up when there are characters it doesn't recognize. This means that many file types, such as png and docx, cannot be encoded safely using this application.