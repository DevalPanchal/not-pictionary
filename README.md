| Group Member      | Username      | SID       |
| :---------------- | :-----------: | --------: |
| Justin Katerberg  | jkaterberg    | 100750264 |
| Daniel Earley     | daniel-earley |           |
| Deval Panchal     | DevalPanchal  | 100744653 |
| Justin Wong       | SJustinWong   |           |

# Not-Pictionary
A simple multiplayer pictionary game (despite the name). Follows the standard pictionary game loop, one player is selected to draw an image from a random word. The image is streamed to all other players, who are able to make guesses about the original word. Once the word is guessed, the next player is given a word to draw and the loop repeats.

## Running
Tested with Gradle 6.8.3, Java 15.0.2 and JavaFX 15

- **Clone the repository:** `git clone https://github.com/jkaterberg/not-pictionary.git`
- **Enter the repository Directory:** `cd not-pictionary`
- **Start the Server:** `gradle server`
- **Start the Client:** `gradle run`
