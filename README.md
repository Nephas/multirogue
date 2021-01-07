# Even the Gods are Gone

<p align="left">
  <img src="https://raw.githubusercontent.com/Nephas/multirogue/main/img/raven_framed.png" width="512"/>
</p>

### Design Goals:

1. Singleplayer & 2-Player Coop
2. Sparse but tactical Combat
3. Exploration
4. Somber Atmosphere

## Coop Mechanics

* [X] 2 player entities per playthrough
* [X] Both player entities are always present in the world
* [X] both entities have to be close to transit between areas
* [X] uncommanded entity will be present as draugr
	* following other player
	* new player can join into draugr body
* [ ] killed player entity
	* will rise again and follow living player as unkillable ai
	* can be resouled at magic location
* [ ] game ends if both players die

## Sparse but Tactical Combat

* [X] turn based action point system
	* players have common action point pool
	* npcs will have simplified turn-based actions
* [ ] AP can be spent to
	* attack
	* move
	* use powers
	* block incoming damage (automatic)
* [ ] ranged weapons
	* target closest enemy by default
* [ ] NPC factions
	* not all NPCs are hostile
	* NPCs might be agressive towards each other
* [ ] Preventable combat via stealth system
	* light-shadow system
	* distraction actions
	* invisibility items
	
## Exploration

* [x] multiple Biomes
* [ ] level challenges - keys to progress to next level
    * three keys distributed across level
    * miniboss: steal or fight for key
    * godi: sacrifice stats for key
* [ ] killing is not rewarded - XP are gained for
	* reaching a new area
	* finding specific Items (runestones)
* [ ] levels generate with
	* rectangular Rooms
	* natural caverns
	* vegetation
	* lakes of water, swamp, frost, magma
	* uncrossable abysses

## Somber Atmosphere

* [ ] Beautiful Pixel-Art loading Screens
* [ ] melancholic music
* [ ] cryptic snippets of flavor text
* [ ] rain, snow, ash animation overlay
* [ ] majestic locations
	* gaping abysses
	* majestic ruins
	* collapsing world
* [ ] nordic/dark soulish setting with
	* Wolves
	* Ravens
	* Draugr
	* Giant Snakes
	* Old Godis 
	* travelling Bards
	* Runestones
	
# Player Journey

## Flavor Texts

### Intro

>The World has ended - for how long you do not know. Life is a burden and Death a treasure. Yet True Death does not come easily in this place beyond time, where the living eat the dead, and the dead eat the living.
>
>But the lonely Godi has told you so often - there must be a place, where you can leave it behind. A place where the Raven will not find you, and drag your rotting body back into the eternal circle.
A place - to find peace.

### Runestone

[Random Texts of ancient Norse Tragedy]

### Player Revive

[Text about putting your beloved body onto the altar of the Raven]

### 2-Player Death

[Text and Image about a Giant Raven fetching your corpses back to the last city]

### Goal

>This is the place they told you about. The Raven is far away and will not find you when you exhale your last breath. 
>You Close your eyes, the face of your beloved {other-player} in your mind and icy steel in your hand.
>
>One last thrust into your scarred body and a cold pain - the last pain you ever feel
>
>Valhalla awaits

## Levels

* **General NPCs:**
	- Godi
	- Bard

### City of the Dead

* **Tileset:** Castle
* **Generation:** BSP
* **Assets:**
	- Abyss
	- Flammable Oil
* **Effects:**
	- Fire
* **NPCs:**
	- Draugr
	- Rat
	- Raven

### The Wilderness

* **Tileset:** Ruins
* **Generation:** Cellular, expansive
* **Assets:**
	- Grass
	- Lakes
	- Trees & Bushes
* **Effects:**
	- Rain
* **NPCs:**
	- Snake
	- Goblin
	- Wolf
	- Kraken
	
### Pass to the Edge

* **Tileset:** Snowy
* **Generation:** Cellular & BSP, 
* **Effects:**
	- Snow
* **Assets:**
	- Frost
	- Magma
	- Snow
* **NPCs**
	- Spider
	- Norn (Frost Witch)
