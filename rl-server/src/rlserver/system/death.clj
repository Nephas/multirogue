(ns rlserver.system.death
  (:require [rlserver.generate.object :refer [generate-corpse]]
            [rlserver.entity.entity :refer [destroy-entity]]
            [rlserver.entity.state :refer [apply-seq]]))

(defn check-death [state id]
  (let [hp (get-in state [:hp id 0])
        pos (get-in state [:pos id])]
    (if (<= hp 0)
      (-> state
          (destroy-entity id)
          (generate-corpse pos))
      state)))

(defn update-death [game]
  (let [stats (keys (:hp game))]
    (apply-seq game stats check-death)))