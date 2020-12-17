# Even the Gods are Gone

### Design Goals:

1. Singleplayer & 2-Player Coop
2. Sparse but tactical Combat
3. Exploration
4. Somber Atmosphere

## Coop Mechanics

* [X] 2 player entities per playthrough
* [X] Both player entities are always present in the world
* [X] both entities have to be close to transit between areas
* [ ] uncommanded entity will be present as draugr
	* following other player
	* new player can join into draugr body
* [ ] killed player entity
	* will rise again and follow living player as unkillable ai
	* can be resouled at magic location
* [ ] game ends if both players die

## Sparse but Tactical Combat

* [X] Realtime action point system
	* players have action point pool
	* replenishes small amount each tic
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
* [ ] Prevenable combat via stealth system
	* light-shadow system
	* distraction actions
	* invisibility items
	
## Exploration

* [ ] multiple Biomes
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

>The World has ended - for how long you do not know. Life is a burden and Death a treasure. But True Death does not come easily in this place beyond time, where the living eat the dead, and the dead eat the living.

>But the lonely Godi has told you so often - there must be a place, where you can leave it behind. A place where the Raven will not find you, and drag your rotting body back into the eternal circle.
A place - to find peace.

### Runestone

[Random Texts of ancient Norse Tragedy]

### Player Revive

[Text about putting your beloved body onto the altar of the Raven]

### 2-Player Death

[Text and Image about a Giant Raven fetching your corpses back to the last city]

### Goal

[The players reach the Gates of Valhalla - the Raven cannot reach this place, so death will be final. Player choice, who kills whom.]

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
	- Wolve
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
