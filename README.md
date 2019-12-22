# bomber-server

## General
#### Compile and run?
```mvn clean compile exec:java```

#### Are you Martin?
```
docker build -t ubuntu_bomber .
docker run -v $(pwd):/bomber -it ubuntu_bomber
```
In the container move to /bomber and run the compile and run command above

## Server - Client messages

#### General
- Most messages will use ascii encoding for ease of integration and debugging
- Most messages will use a ```\n``` as message termination character
- Sending a message does not mean the server accepts it. For example just because you sent move left does
not mean the server allows it, so wait for the server to update game state

#### Pre and post game messages
- To join a game, the message ```join NAME``` can be sent by a client, where ```NAME``` is the name
other people will receive, the server will respond with ```joined X``` where X is the player number
you will be during the upcoming game.
After this the server will periodically (most likely when a new player joins) send ```joining A/B```
where A is the number of players that already joined the upcoming game, and B is the players needed
to start the game. This means that the message ```joining A/A``` means that the game will start any second.
The ```joining A/B``` message can be forced by sending the message ```update``` to the server.
The game can start when not enough players joined yet (if the server decides this).
- Before a game, clients can also query the names of other players in the game with the ```names```
message, to which the server will respond with N messages of the format ```named I NAME``` for all
N players, where I is the player number, name NAME is the player name for that player number.
- When a game has finished you will receive the message ```idle``` (as well as game over
messages described later) to signify that you are in the position to join another game.
This ```idle``` message may also be received when first establishing a connection to the server.

#### Start game messages
- The message ```loading W/H``` will be sent by the server to mean that the map is being
broadcast to every player of this game, and the map has width W and height H.
- After the loading message you will receive H messages of length W in a row
which contains the map to be played in this game. In these messages the symbols are
used that are described in the Symbols section.
- On receiving the whole map you should reply with the message ```loaded M``` where
M is a hash of the map received created by ```M = 0; M += ascii( C ); M <<= 1; for every received character C```.
The server to this will respond with ```loaded``` or ```reload``` based on if your hash was correct.
If it was, do nothing, if it was not, the server will resend
the ```loading W/H``` message and start retransmitting the map.
- The message ```starting X/Y Z``` means that the game will begin in Z seconds,
this message will be sent every second (or more) until (including) Z is 0.
When Z reaches 0 the game has begun. In this message X is again your player
number for this game, and Y is the amount of players.
This also means all player numbers in this game are from 0 until Y.

#### In game messages
During the game, the server will send the following messages:
- ```map X/Y Z``` meaning the object on the map at X/Y changed to Z where Z is a map symbol.
- ```p P X/Y D``` meaning that player P is now at X/Y facing direction D.
- ```hurt P``` meaning that player P got damage.
- ```died P``` meaning that player P died.
- ```bomb P X/Y``` 