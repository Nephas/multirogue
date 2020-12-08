(ns rlserver.system.update
  (:require [rlserver.entity.state :refer [serialize-game game-store]]
            [rlserver.system.npc :refer [update-npcs]]
            [rlserver.system.effect :refer [clean-effects]]
            [rlserver.system.death :refer [update-death]]))

(defn update-time [game]
  (update game :tic inc))

(defn update-game [state id]
  (-> state
      (update id clean-effects)
      (update id update-time)
      (update id update-death)
      (update id update-npcs)))

(defn update [id broadcaster]
  (do (swap! game-store update-game id)
      (broadcaster id (serialize-game id))))