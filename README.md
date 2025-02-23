| Group Member      | Username      | 
| :---------------- | :-----------: | 
| Justin Katerberg  | jkaterberg    | 
| Daniel Earley     | daniel-earley | 
| Deval Panchal     | DevalPanchal  | 
| Justin Wong       | SJustinWong   | 

# Not-Pictionary
A simple multiplayer pictionary game (despite the name). Follows the standard pictionary game loop, one player is selected to draw an image from a random word. The image is streamed to all other players, who are able to make guesses about the original word. Once the word is guessed, the next player is given a word to draw and the loop repeats.

## Implementation
The frontend application was developed using Java, FXML, and CSS which produced a visually appealling application during run-time. During the initial phase of this project a 
very simple wireframe was produced to grasp a better understanding of the UI and where everything would fall into place. A sample of the wireframe produced looked like:
![Mockup](mockup.PNG)
The backend portion of this application was developed using Java with sockets and multithreading. The backend was in charge of the communication between multiple clients effectively making a multiplayer game.

## Application Preview
This is an example output of the application running on the drawer's side:
![Drawer Example](drawer.PNG)
This is an example output of the application running on the guesser's side:
![Guesser Example](Guesser.PNG)

The drawings and chat menu are broadcasted to each user in real-time.

A video of the running application can be found on [youtube](https://youtu.be/aKIN6Qy5PPY).

## Installation
Tested with Gradle 6.8.3, Java 15.0.2 and JavaFX 15

- **Clone the repository:** `git clone https://github.com/jkaterberg/not-pictionary.git`
- **Enter the repository Directory:** `cd not-pictionary`
- **Start the Server:** `gradle server`
- **Start the Client:** `gradle run`

## Software Used

- Java
- JavaFX
- Gradle
- FXML
- CSS
