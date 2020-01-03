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

## Code changes

#### Game / Server separation
The game code and server code are separated so that the game is 100% independent of the server
i.e. the server includes game packages, but the game never includes the server packages. Try to keep
it this way and keep this in mind to put the changes in the place they belong.
Other than that, change whatever you want bois, the classes probably speak for themselves.
## Server - Client messages

#### General
- Most (if not all) messages will use ascii encoding for ease of integration and debugging
- Most (if not all) messages will use a ```\n``` as message termination character
- Sending a message does not mean the server accepts it. For example just because you sent move left does
not mean the server allows it (this holds for all message including pre/post game), so wait for the server to update game state

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
These named message will also be send to everybody just before a game starts.
- When a game has finished you will receive the message ```idle``` (as well as game over
messages described later) to signify that you are in the position to join another game.
This ```idle``` message may also be received when first establishing a connection to the server.

#### Start game messages
- The message ```loading W/H``` will be sent by the server to mean that the map is being
broadcast to every player of this game, and the map has width W and height H.
- After the loading message you will receive H messages of length W in a row
which contains the map to be played in this game. In these messages the symbols are
used that are described in the Symbols section..
- The message ```starting Y Z``` means that the game will begin in Z seconds,
this message will be sent every second (or more) until (including) Z is 0.
When Z reaches 0 the game has begun. In this message Y is the amount of players.
This also means all player numbers in this game are from 0 until Y.

#### In game messages
During the game, the server will send the following messages:
- ```change X/Y Z``` meaning the object on the map at X/Y changed to Z where Z is a map symbol.
- ```p P X/Y D``` meaning that player P is now at X/Y facing direction D.
- ```hurt P L``` meaning that player P got damaged, and has L lives left now.
- ```died P``` meaning that player P died.
- ```drop X Y P``` meaning that player P dropped a bomb at location X Y.
- ```boom X Y``` meaning that the bomb at X Y exploded.
- ```fire X Y W H``` meaning that there is hurtful fire (explosion, etc) at X Y with size W H.
Where W is width and H is height of course. This fire can be from a just exploded
bomb or some fuse or something.
- ```over P``` meaning that the game is over and P is the id of the winning player,
P may also not me there (the message will then be just ```over```) if this game did
not have a winner (maybe a practice game or single player test map, etc)

Clients can send two different messages during a game:
- ```move D``` to move in direction D where D is one of ```{ N, NE, E, SE, S, SW, W, NW }``` for
the direction to move in based on wind direction annotations. This message will either move
in direction D once (in the upcoming tick) or keep moving in that direction, in which case another move
or the stop message should be send to stop moving, this behaviour is based on a server setting,
see the stop message below.
- ```stop``` to stand still (stop moving). If this message is necessary depends on the
```resetWalkOnTick``` setting. See the file 'resources/settings' for more information about this.
- ```drop B``` to drop a bomb, where B is the bomb type id (in case we implement different bombs)
sending ```bomb``` will be the same as ```bomb 0``` aka the default cross explosion bombs.

## Changes
Something you want differently about the server, messages etc. Or did you find
a bug/error or wrong message format being sent, let me (Koen) know or change it yourself.