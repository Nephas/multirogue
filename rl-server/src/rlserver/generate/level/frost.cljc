(ns rlserver.generate.level.frost
  (:require [rlserver.generate.runes :refer [generate-blood-circle]]
            [rllib.board :refer [midpoint]]
            [rlserver.generate.level.room :refer [rand-field]]
            [rllib.state :refer [apply-seq]]
            [rlserver.generate.npc :refer [generate-wolf]]
            [rllib.rand :refer [rand-coll rand-bool rand-n]]))

(defn generate-wolves [state tier-defs]
  (let [rooms (last tier-defs)]
    (apply-seq state rooms (fn [s r] (if (rand-bool) (generate-wolf s (rand-field r)) s)))))

(defn generate-frost-level [state tier-defs corridor-defs]
  (-> state
      (generate-blood-circle (rand-field (rand-coll (last tier-defs)) 3))
      (generate-wolves tier-defs)))