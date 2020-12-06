(ns rlserver.system.update
  (:require [rlserver.state :refer [init-state serialize-game store]]
            [rlserver.system.npc :refer [update-npcs]]))

(defn update-time [game]
  (update game :tic inc))

(defn update-game [state id]
  (-> state
      (update id update-time)
      (update id update-npcs)))

(defn update [id broadcaster]
  (do (swap! store update-game id)
      (broadcaster id (serialize-game id))))