(ns rlserver.system.death
  (:require [rlserver.generate.object :refer [generate-corpse]]
            [rlserver.entity.entity :refer [destroy-entity]]
            [rlserver.entity.state :refer [apply-seq]]))



(defn player-die [state id]
  (-> state
      (update :animated dissoc id)
      (assoc-in [:ap id 0] 0)
      (assoc-in [:sprite id] [8 8])))

(defn check-death [state id]
  (let [hp (get-in state [:hp id 0])
        pos (get-in state [:pos id])]
    (cond
      (and (<= hp 0) (contains? (set (:pc state)) id))
      (player-die state id)

      (<= hp 0)
      (-> state
          (destroy-entity id)
          (generate-corpse pos))

      true state)))

(defn update-death [game]
  (let [stats (keys (:hp game))]
    (apply-seq game stats check-death)))