# JavaCraps
This project implements a terminal-based version of the classic dice game Craps using Java and the Model-View-Controller (MVC) architecture. It is a learning-focused, offline simulation intended for single-player use. The goal is to win by growing the player’s balance from $100 to $1000 using correct betting and dice roll outcomes per Craps rules.

* project specs and architecture: Software Architect GPT 
* coding: Cursor as an AI coding agent

## Run the application
- `mvn clean install`
- `cd main`
- `mvn exec:java -Dexec.mainClass="org.asarenski.JavaCraps.Main"`

## Game Win/Lose States
The player starts the game with $100. Minimum bet is $5. If the player reaches $0 they lose. If the player reaches $1000 they win.

## Craps Game Rules
### The "Come Out" Roll (First Roll):
The game begins with a come-out roll.

If the shooter rolls a:

7 or 11: You win ("natural")

2, 3, or 12: You lose ("craps")

4, 5, 6, 8, 9, or 10: That number becomes the point

### 🔁 Point Phase:
Once the point is established (e.g., shooter rolls a 6), the goal is for the shooter to roll that same number again before rolling a 7.

If they roll the point again: You win

If they roll a 7 first: You lose (this is called "seven-out")

### 💵 The Basic Bets:
1. Pass Line Bet (most common):
Bet before the come-out roll.

Wins on 7 or 11

Loses on 2, 3, or 12

If a point is set, wins if point is hit again before 7

2. Don’t Pass Line Bet (opposite of Pass Line):
Wins on 2 or 3 (12 is a push)

Loses on 7 or 11

After a point is set, you win if a 7 comes before the point