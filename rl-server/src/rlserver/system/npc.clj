(ns rlserver.system.npc
  (:require [rlserver.reducer.move :refer [moveable? entity-move move]]
            [rlserver.entity.state :refer [apply-seq]]))



(defn move-npc-rand [game id]
  (let [dir (rand-nth [:up :down :left :right])]
    (if (moveable? game dir id)
      (entity-move game id dir)
      game)))

(defn update-npcs [game]
  (apply-seq game (:npc game) move-npc-rand))
