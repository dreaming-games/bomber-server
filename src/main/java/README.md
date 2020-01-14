## The basics
Basically, there is 2 parts: the actual game implementation, which is in the ```bomber``` package.
And the server part, which includes all communication with clients and stuff, unsurprisingly in the ```server``` package.
The game implementation has no clue there is a server, and basically just has a game tick method. This method
is called by the server ( ```PlayHandler``` to be specific ) and returns events that happened in the game that tick.
The server then sends those events to its clients and repeat.

Also, the ```general``` package is just that, super general (and barely anything probably).


## About the server and Handlers
The server is run by the ```server.main.Server``` class, which does nothing except open a server socket,
accept clients and give the ```JoinHandler``` to them. A handler is basically the thing that handles communication
with a client. All clients are given the ```JoinHandler``` at the start. After they join or say they wanna spectate
a game, the clients will use the ```PlayHandler``` which handles playing a game. For this reason there is also always
one single ```JoinHandler``` instance while there might be a bunch of ```PlayHandler``` instances.

Since there is only one ```JoinHandler``` this handler keeps a list of all ```PlayHandlers``` so that we can
query the general server state from this etc.