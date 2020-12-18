(ns rlserver.generate.level.frost
  (:require [rlserver.generate.object :refer [generate-runestone]]
            [rllib.board :refer [midpoint]]
            [rlserver.generate.level.room :refer [rand-field]]
            [rlserver.entity.state :refer [apply-seq]]
            [rlserver.generate.npc :refer [generate-wolf]]
            [rllib.rand :refer [rand-coll rand-bool]]))

(defn generate-wolves [state tier-defs]
  (let [rooms (last tier-defs)]
    (apply-seq state rooms (fn [s r] (if (rand-bool) (generate-wolf s (rand-field r)) s)))))

(defn generate-frost-level [state tier-defs corridor-defs]
  (-> state
      (generate-runestone (rand-field (rand-coll (last tier-defs))))
      (generate-runestone (rand-field (rand-coll (last tier-defs))))
      (generate-runestone (rand-field (rand-coll (last tier-defs))))
      (generate-wolves tier-defs)))