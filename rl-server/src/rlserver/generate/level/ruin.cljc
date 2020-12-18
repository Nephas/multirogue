(ns rlserver.generate.level.ruin
  (:require [rlserver.generate.object :refer [generate-runestone]]
            [rllib.board :refer [midpoint]]
            [rlserver.generate.level.room :refer [rand-field]]
            [rlserver.entity.state :refer [apply-seq]]
            [rlserver.generate.npc :refer [generate-snake]]
            [rllib.rand :refer [rand-coll rand-bool]]))

(defn generate-snakes [state tier-defs]
  (let [rooms (last tier-defs)]
    (apply-seq state rooms (fn [s r] (if (rand-bool) (generate-snake s (rand-field r)) s)))))

(defn generate-ruin-level [state tier-defs corridor-defs]
  (-> state
      (generate-runestone (rand-field (rand-coll (last tier-defs))))
      (generate-runestone (rand-field (rand-coll (last tier-defs))))
      (generate-snakes tier-defs)))