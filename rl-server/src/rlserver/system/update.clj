(ns rlserver.system.update
  (:require [rlserver.entity.state :refer [serialize-game game-store]]
            [rlserver.system.ai :refer [update-ai]]
            [rlserver.system.effect :refer [clean-effects]]
            [rlserver.system.transition :refer [check-transitions]]
            [rlserver.system.consumable :refer [update-consumables]]
            [rlserver.system.action :refer [replenish-actions]]
            [rlserver.system.death :refer [update-death]]))

(defn update-time [game]
  (update game :tic inc))

(defn update-game [state id]
  (-> state
      (update id clean-effects)
      (update id update-time)
      (update id update-death)
      (update id update-ai)
      (update id update-consumables)
      (update id check-transitions)
      (update id replenish-actions)))

(defn update [id broadcaster]
  (do (swap! game-store update-game id)
      (broadcaster id (serialize-game id))))