(ns rlserver.generate.level.castle
  (:require [rlserver.generate.runes :refer [generate-blood-circle]]
            [rllib.board :refer [midpoint]]
            [rlserver.generate.level.room :refer [rand-field]]
            [rllib.state :refer [apply-seq]]
            [rlserver.generate.npc :refer [generate-raven generate-skeleton]]
            [rllib.rand :refer [rand-coll rand-bool]]))

(defn generate-skeletons [state tier-defs]
  (let [rooms (last tier-defs)]
    (apply-seq state rooms (fn [s r] (if (zero? (rand-int 4)) (generate-skeleton s (rand-field r)) s)))))

(defn generate-ravens [state tier-defs]
  (let [rooms (last tier-defs)]
    (apply-seq state rooms (fn [s r] (if (rand-bool) (generate-raven s (rand-field r)) s)))))

(defn generate-castle-level [state tier-defs corridor-defs]
  (-> state
      (generate-blood-circle (rand-field (rand-coll (last tier-defs)) 3))
      (generate-ravens tier-defs)
      (generate-skeletons tier-defs)))