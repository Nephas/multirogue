(ns rlserver.system.npc
  (:require [rlserver.reducer.move :refer [moveable? move-entity move]]))



(defn move-npc-rand [game id]
  (let [dir (rand-nth [:up :down :left :right])]
    (if (moveable? game dir id)
      (move-entity game id dir)
      game)))

(defn update-npcs [game]
  (loop [remaining (:npc game)
         next-game game]
    (if (empty? remaining) next-game
                           (recur (rest remaining)
                                  (move-npc-rand next-game (first remaining))))))
