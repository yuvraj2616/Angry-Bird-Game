# Angry Birds Game

This project is a simplified version of the Angry Birds game built in Java using the libGDX framework. It includes various bird and block types, each with unique characteristics. Birds inherit from a base `Bird` class, blocks inherit from a base `Block` class, and pigs are represented by the `Pig` class.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [Code Structure](#code-structure)
  - [Bird Classes](#bird-classes)
  - [Block Classes](#block-classes)
  - [Level Classes](#level-classes)
  - [Slingshot Class](#slingshot-class)
  - [Pig Classes](#pig-classes)
- [Screens](#screens)
  - [Home Screen](#home-screen)
  - [Level Screens](#level-screens)
  - [Level Selection Screen](#level-selection-screen)
  - [Win Screen](#win-screen)
  - [Lose Screen](#lose-screen)
  - [Pause Screen](#pause-screen)
- [Core Class](#core-class)
- [Gameplay Instructions](#gameplay-instructions)

## Installation

1. **Prerequisites**:
   - Install [libGDX](https://libgdx.com/) and configure it in your development environment.
   - Use a Java IDE like IntelliJ IDEA or Eclipse.

2. **Clone the Repository**:
   - Clone the repository and open it in your IDE.

3. **Dependencies**:
   - Ensure that the libGDX libraries are included in your project setup.

## Usage

- Run the game within your IDE to start the Angry Birds-style gameplay.
- Experience different types of birds and blocks, each with distinct functionalities and behaviors.

## Features

- **Variety of Bird Types**: Each bird type offers unique abilities, such as:
  - **BlueBird**: Splits into three smaller birds, increasing the chances of hitting multiple targets.
  - **YellowBird**: Gains a speed boost, enabling rapid movement and greater impact.
  - **RedBird**: A balanced bird without special abilities, suitable for straightforward play.

- **Multiple Block Types**: Different materials impact gameplay mechanics:
  - **Glass Blocks**: Fragile, easily destroyed, providing strategic options for bird choice.
  - **Wood Blocks**: Moderate durability, needing more force or precision to break.
  - **Stone Blocks**: Highly durable, requiring strong birds or careful strategy.

- **Challenging Levels**: Levels increase in difficulty, with varying block placements, bird types, and pig configurations to test player skills.

- **Physics-Driven Gameplay**: Utilizes libGDX’s physics engine for realistic launching and collision effects, enhancing game immersion.

- **Level Selection Menu**: Players can choose levels from a dedicated menu, with locked levels shown to encourage progression.

- **Pause and Resume**: Players can pause the game at any time to access a menu with options to resume or return to the home screen.

- **Win and Lose Screens**: Displays player performance at the end of a level, offering options to retry or move forward.

## Code Structure

### Bird Classes

Each bird type extends the base `Bird` class, which provides shared properties such as texture, position, and velocity.

#### 1. Bird.java

The `Bird` class serves as the base class for all bird types, handling:
- **Texture**: Bird’s visual representation.
- **Position**: Bird’s screen location.
- **Velocity**: Bird’s speed and direction.

##### Main Methods:
- `update(float delta)`: Updates the bird’s position based on velocity.
- `draw(SpriteBatch batch)`: Renders the bird at its position.
- `dispose()`: Frees texture memory.
- `setVelocity(Vector2 velocity)`: Sets bird velocity.
- `getPosition()` and `getVelocity()`: Retrieve position and velocity.

#### 2. BlueBird.java

The `BlueBird` class extends `Bird`, adding a `split` ability.
- **split()**: Function representing bird splitting into smaller entities.

#### 3. RedBird.java

The `RedBird` class extends `Bird` without additional abilities.

#### 4. YellowBird.java

The `YellowBird` class extends `Bird`, featuring a `speedBoost` ability.
- **speedBoost()**: Increases the bird’s speed for a short duration.

### Block Classes

Each block type inherits from the base `Block` class, which provides properties like texture and position.

#### 1. Block.java

`Block` serves as the base class for all block types, defining:
- **Texture**: Block’s visual representation.
- **Position**: Block’s screen location.

##### Main Methods:
- `draw(SpriteBatch batch)`: Renders the block.
- `dispose()`: Releases resources.
- `setPosition(float x, float y)`: Sets block position.
- `getPosition()`: Retrieves position.

#### 2. GlassBlock.java

The `GlassBlock` class represents a glass-type block, inheriting from `Block`.
- Texture: `glass_block.png`

#### 3. StoneBlock.java

The `StoneBlock` class represents a stone-type block.
- Texture: `steel_block.png`

#### 4. WoodBlock.java

The `WoodBlock` class represents a wood-type block.
- Texture: `WoodBlock.png`

### Level Classes

Each level is implemented as a separate class, inheriting from an abstract base class.

#### 1. Level.java

The `Level` base class defines common attributes and methods for initializing birds, pigs, and blocks.

##### Main Methods:
- `initializeBirds()`: Abstract method for setting up birds.
- `initializePigs()`: Abstract method for setting up pigs.
- `initializeBlocks()`: Abstract method for setting up blocks.
- `show()`: Displays the level.

#### Level1.java, Level2.java, Level3.java

Each class defines specific configurations of birds, pigs, and blocks for progressively challenging gameplay.

### Slingshot Class

The `Slingshot` class manages bird launching mechanics.

##### Main Methods:
- `pullBack(float distance)`: Simulates pulling the slingshot.
- `release()`: Launches the bird toward targets.

### Pig Classes

Each pig type inherits from a base `Pig` class, which defines shared properties and methods for drawing and taking damage.

#### Pig.java

Defines common pig properties such as health and position.

##### Main Methods:
- `draw(SpriteBatch batch)`: Renders the pig.
- `takeDamage(int damage)`: Reduces health; the pig is destroyed if health reaches zero.
- `dispose()`: Releases resources.
- `getPosition()` and `getHealth()`: Retrieve position and health.

#### SmallPig, MediumPig, LargePig

Each pig type differs in health, size, and appearance, impacting the strategy required to defeat them.

## Screens

### Home Screen

- Displays the game title with options for **New Game**, **Load Game**, and **Exit**.
- "New Game" starts gameplay.

### Level Screens

#### Level 1 Screen
- First level setup with specific birds, pigs, and blocks.
- Includes a "Pause" button.

#### Level 2 & Level 3 Screens
- Additional levels with more complex configurations of birds, pigs, and blocks.

### Level Selection Screen

- Displays available levels; players can start any unlocked level or return to the home screen.

### Win Screen

- Appears after successfully completing a level.
- Displays the score and offers options to proceed, retry, or return to the level selection screen.

### Lose Screen

- Appears when the player runs out of birds or fails to complete the level.
- Displays the score and options to retry or return to the level selection screen.

### Pause Screen

- Displays upon pausing the game.
- Shows the current score and the following options:
  - **Resume**: Continue playing.
  - **Return to Level Selection**: Go back to level selection.
  - **Restart**: Restart the level.
  - **Exit**: Return to the home screen.

## Core Class

The `Core` class initializes and manages the game, starting with the `HomeScreen`. It uses a `SpriteBatch` for rendering, and its main methods (`create`, `render`, and `dispose`) handle game resources and screen transitions.

## Gameplay Instructions

- **Set Angle**: Adjust the launch angle with the **Angle** slider.
- **Set Power**: Adjust the launch strength with the **Power** slider.
- **Launch**: Press the **Launch** button to fire the bird.
- **Objective**: Destroy all pigs in a level to progress to the next one.
