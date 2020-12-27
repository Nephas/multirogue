(ns rlserver.system.update
  (:require [rlserver.entity.state :refer [serialize-diff game-store]]
            [rlserver.system.ai.ai :refer [update-ai]]
            [rlserver.system.effect :refer [clean-timers]]
            [rlserver.system.transition :refer [check-transitions]]
            [rlserver.system.consumable :refer [update-consumables]]
            [rlserver.system.death :refer [update-death]]))

(defn update-tic
  ([state]
   (-> state
       (update :tic inc)
       (clean-timers)
       (update-death)
       (update-consumables)
       (check-transitions))))

(defn maybe-update-ai-turn [state]
  (let [[ap ap-max] (:pap state)]
    (if (> ap 0) state
                 (-> state
                     (update-ai)
                     (update :turn inc)
                     (assoc :pap [ap-max ap-max])))))