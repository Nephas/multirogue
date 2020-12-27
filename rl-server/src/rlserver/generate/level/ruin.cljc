(ns rlserver.generate.level.ruin
  (:require [rlserver.generate.object :refer [generate-potion]]
            [rlserver.generate.runes :refer [generate-blood-circle]]
            [rllib.board :refer [midpoint]]
            [rlserver.generate.level.room :refer [rand-field]]
            [rlserver.entity.state :refer [apply-seq]]
            [rlserver.generate.npc :refer [generate-snake]]
            [rllib.rand :refer [rand-coll rand-bool]]))

(defn generate-snakes [state tier-defs]
  (let [rooms (last tier-defs)]
    (apply-seq state rooms (fn [s r] (if (rand-bool) (generate-snake s (rand-field r)) s)))))

(defn generate-potions [state tier-defs]
  (let [rooms (last tier-defs)]
    (apply-seq state rooms (fn [s r] (if (zero? (rand-int 4)) (generate-potion s (rand-field r)) s)))))

(defn generate-ruin-level [state tier-defs corridor-defs]
  (-> state
      (generate-blood-circle (rand-field (rand-coll (last tier-defs)) 3))
      (generate-potions tier-defs)
      (generate-snakes tier-defs)))